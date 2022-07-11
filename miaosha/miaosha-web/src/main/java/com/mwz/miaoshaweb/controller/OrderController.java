package com.mwz.miaoshaweb.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import com.mwz.miaoshaservice.service.OrderService;
import com.mwz.miaoshaservice.service.StockService;
import com.mwz.miaoshaservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Controller
public class OrderController {
    private static  final Logger LOGGER= LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private StockService stockService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    // Guava令牌桶:每秒放行10个请求
    RateLimiter rateLimiter=RateLimiter.create(10);

    // 延时时间:预估读数据库数据业务逻辑的好事,用来做缓存再删除
    private static final int DELAY_MILLSECONDS = 1000;

    // 延时双删
    private static ExecutorService cachedThreadPool = new ThreadPoolExecutor(0,Integer.MAX_VALUE,60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());


    /**
     * 最终方案
     * 1.用户进入商品界面
     * 2.前台通过getVerify接口得到验证值(隐藏接口）
     * 3.用户点击下单需携带md5字段等,后台进行验证(防刷接口)
     * 4.令牌桶限流+单用户验证(接口限流)
     * 5.有无库存，若有则使用消息队列RabbitMQ异步下单(防止数据库写表压力大)
     * 6.乐观锁更新库存(防止超卖)
     * 7.canal延时双删(数据库和缓存一致性)
     * 7.秒杀成功
     * @param sid
     * @param userId
     * @param verifyHash
     * @return
     */
    @RequestMapping(value = "/createOrder",method = RequestMethod.GET)
    @ResponseBody
    public  String createOrder(@RequestParam(value = "sid") Integer sid,
                               @RequestParam(value ="userId")Integer userId,
                               @RequestParam(value ="verifyHash")String verifyHash){
        // 阻塞式
        // LOGGER.info("等待时间"+rateLimiter.acquire());
        // 非阻塞式,没有拿到令牌直接返回失败
        if(!rateLimiter.tryAcquire(1000,TimeUnit.MILLISECONDS)){
            LOGGER.warn("你被限流了,真不幸");
            return "你被限流了,真不幸";
        }
        try {
            // 检查缓存中该用户是否已经下单过,或者用isbanned验证是否超过规定次数
            Boolean hasOrder=orderService.checkUserOrderInfoInCache(sid,userId);
            if(hasOrder!=null&&hasOrder){
                LOGGER.info("该用户已经抢购过了");
                return "你已经去抢购过了,不要太贪心..";
            }
            // int count =userService.addUserCount(userId);
            // LOGGER.info("用户截至该次的访问次数为:[{}]",count);
            // boolean isBanned=userService.getUserIsBanned(userId);
            // if(isBanned){
            //     return "购买失败,超过频率限制";
            // }
            // 没有下单,检查缓存中商品是否还有库存
            LOGGER.info("没有抢购过,检查缓存中商品是否还有库存");
            Integer count=stockService.getStockCount(sid);
            if(count==0){
                return "秒杀请求失败,库存不足..";
            }

            // 有库存,将用户id和商品id和hash封装为消息体传给消息队列处理
            // 注意这里的有库存和已经下单都是缓存中的结论,存在不可靠性,在消息队列中会查表再次验证
            LOGGER.info("有库存:[{}]",count);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("sid",sid);
            jsonObject.put("userId",userId);
            jsonObject.put("verifyHash",verifyHash);
            sendToOrderQueue(jsonObject.toJSONString());
            // 删除库存缓存
            // 延长指定时间再次删除缓存,假设失败
            // cachedThreadPool.execute(new delCacheByThread(sid));
            return "秒杀请求提交成功";
        }catch (Exception e){
            LOGGER.error("下单接口:异步处理订单异常:",e);
            return "秒杀请求失败,服务器正忙...";
        }

    }

    @RequestMapping("/createWrongOrder/{sid}")
    @ResponseBody
    public String createWrongOrder(@PathVariable int sid){
        int id=0;
        try{
            id=orderService.createWrongOrder(sid);
            LOGGER.info("创建订单:[{}]",id);
        }catch (Exception e){
            LOGGER.error("Exception",e);
        }
        return String.valueOf(id);
    }

    /**
     * 下单接口:乐观锁更新库存+令牌桶限流
     * @param sid
     * @return
     */
    @RequestMapping("/createOptimisticOrder/{sid}")
    @ResponseBody
    public  String createOptimisticOrder(@PathVariable int sid){
        // 阻塞式
        LOGGER.info("等待时间"+rateLimiter.acquire());
        // 非阻塞式,没有拿到令牌直接返回失败
        // if(!rateLimiter.tryAcquire(1000,TimeUnit.MILLISECONDS)){
        //     LOGGER.warn("你被限流了,真不幸");
        //     return "你被限流了,真不幸";
        // }
        int id;
        try{
            id=orderService.createOptimisticOrder(sid);
            LOGGER.info("购买成功.剩余库存为:[{}]",id);
        }catch (Exception e){
            LOGGER.error("购买失败:[{}]",e.getMessage());
            return "购买失败,库存不足";
        }
        return String.format("购买成功,剩余库存:%d",id);
    }

    /**
     * 下单接口:悲观锁更新库存 事务for update更新库存
     * @param sid
     * @return
     */
    @RequestMapping("/createPessimisticOrder/{sid}")
    @ResponseBody
    public  String createPessimisticOrder(@PathVariable int sid){
        int id;
        try{
            id=orderService.createPessimisticOrder(sid);
            LOGGER.info("购买成功,剩余库存为:[{}]",id);
        }catch (Exception e){
            LOGGER.error("购买失败:[{}]",e.getMessage());
            return "购买失败库存不足";
        }
        return String.format("购买成功,剩余库存为:%d",id);
    }

    /**
     * 验证接口:下单前用户获取验证值
     * @return
     */
    @RequestMapping(value ="/getVerifyHash",method = {RequestMethod.GET})
    @ResponseBody
    public String getVerifyHash(@RequestParam(value = "sid") Integer sid,
                                @RequestParam(value="userId") Integer userId){
        String hash;
        try{
            hash =userService.getVerifyHash(sid,userId);
        }catch(Exception e){
            LOGGER.error("获取验证hash失败,原因");
            return "获取验证hash失败";
        }
        return  String.format("请求抢购验证hash值为:%s",hash);
    }

    /**
     * 下单接口:验证用户的下单接口
     * @param sid
     * @param userId
     * @param verifyHah
     * @return
     */
    @RequestMapping(value="/createOrderWithVerifiedUrl",method = {RequestMethod.GET})
    @ResponseBody
    public  String createOrderWithVerifiedUrl(@RequestParam(value = "sid") Integer sid,
                                              @RequestParam(value ="userId")Integer userId,
                                              @RequestParam(value ="verifyHash")String verifyHah){
        int stockLeft;
        try{
            stockLeft=orderService.createVerifiedOrder(sid,userId,verifyHah);
            LOGGER.info("购买成功,剩余库存为:[{}]",stockLeft);
        }catch (Exception e){
            LOGGER.error("购买失败:[{}]",e.getMessage());
            return e.getMessage();
        }
        return String.format("购买成功,剩余库存为:%d",stockLeft);
    }

    /**
     * 下单接口:要求验证的抢购接口 + 单用户限制访问频率
     * @param sid
     * @param userId
     * @return
     */
    @RequestMapping(value = "/createOrderWithVerifiedUrlAndLimit",method = {RequestMethod.GET})
    @ResponseBody
    public String createOrderWithVerifiedUrlAndLimit(@RequestParam(value = "sid") Integer sid,
                                                     @RequestParam(value = "userId") Integer userId,
                                                     @RequestParam(value = "verifyHash") String verifyHash){
        int stockLeft;
        try{
            int count =userService.addUserCount(userId);
            LOGGER.info("用户截至该次的访问次数为:[{}]",count);
            boolean isBanned=userService.getUserIsBanned(userId);
            if(isBanned){
                return "购买失败,超过频率限制";
            }
            stockLeft=orderService.createVerifiedOrder(sid,userId,verifyHash);
            LOGGER.info("购买成功,剩余库存为:[{}]",stockLeft);
        }catch (Exception e){
            LOGGER.error("购买失败:[{}]",e.getMessage());
            return  e.getMessage();
        }
        return  String.format("购买成功,剩余库存为:%d",stockLeft);
    }

    /**
     * 下单接口:先删除缓存,在更新数据库,再延时双删
     * @param sid
     * @return
     */
    @RequestMapping("/createOrderWithCacheV3/{sid}")
    @ResponseBody
    public String createOrderWithCacheV3(@PathVariable int sid){
        int count;
        try{
            // 删除库存缓存
            stockService.delStockCountCache(sid);
            // 完成扣库存下单事务
            count=orderService.createPessimisticOrder(sid);
            LOGGER.info("完成下单事务");
            // 延长指定时间后再次删除缓存
            cachedThreadPool.execute(new delCacheByThread(sid));
        }catch(Exception e){
            LOGGER.error("购买失败:[{}]",e.getMessage());
            return "购买失败,库存不足";
        }
        LOGGER.info("购买成功,剩余库存为:[{}]",count);
        return String.format("购买成功,剩余库存为:%d",count);
    }

    /**
     * 下单接口：先更新数据库，再删缓存，删除缓存失败重试，通知消息队列
     * @param sid
     * @return
     */
    @RequestMapping("/createOrderWithCacheV4/{sid}")
    @ResponseBody
    public String createOrderWithCacheV4(@PathVariable int sid){
        int count;
        try{
            // 完成口库存下单事务
            count=orderService.createPessimisticOrder(sid);
            LOGGER.info("完成下单事务");
            // 删除库存缓存
            // 延长指定时间再次删除缓存
             cachedThreadPool.execute(new delCacheByThread(sid));
            // 上方失败,通知消息队列进行删除缓存
            sendToDelCache(String.valueOf(sid));
        }catch (Exception e){
            LOGGER.error("购买失败:[{}]",e.getMessage());
            return "购买失败,库存不足";
        }
        LOGGER.info("购买成功,剩余库存为:[{}]",count);
        return "购买成功";
    }

    /**
     * 下单接口:异步处理订单
     * @param sid
     * @param userId
     * @return
     */
    @RequestMapping(value="/createUserOrderWithMq",method = {RequestMethod.GET})
    @ResponseBody
    public String createUserOrderWithMq(@RequestParam(value = "sid")Integer sid,
                                        @RequestParam(value="userId")Integer userId){
        try {
            // 检查缓存中该用户是否已经下单过
            Boolean hasOrder=orderService.checkUserOrderInfoInCache(sid,userId);
            if(hasOrder!=null&&hasOrder){
                LOGGER.info("该用户已经抢购过了");
                return "你已经去抢购过了,不要太贪心..";
            }
            // 没有下单,检查缓存中商品是否还有库存
            LOGGER.info("没有抢购过,检查缓存中商品是否还有库存");
            Integer count=stockService.getStockCount(sid);
            if(count==0){
                return "秒杀请求失败,库存不足..";
            }

            // 有库存,将用户id和商品id封装为消息体传给消息队列处理
            // 注意这里的有库存和已经下单都是缓存中的结论,存在不可靠性,在消息队列中会查表再次验证
            LOGGER.info("有库存:[{}]",count);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("sid",sid);
            jsonObject.put("userId",userId);
            sendToOrderQueue(jsonObject.toJSONString());
            return "秒杀请求提交成功";
        }catch (Exception e){
            LOGGER.error("下单接口:异步处理订单异常:",e);
            return "秒杀请求失败,服务器正忙...";
        }
    }

    /**
     * 检查缓存中用户是否已经生成过订单
     * @param sid
     * @param userId
     * @return
     */
    @RequestMapping(value="/checkOrderByUserIdCache",method ={RequestMethod.GET})
    @ResponseBody
    public String checkOrderByUserIdInCache(@RequestParam(value="sid") Integer sid,
                                            @RequestParam(value ="userId")Integer userId){
        // 检查缓存中该用户是否已经下单过
        try {
            Boolean hasOrder = orderService.checkUserOrderInfoInCache(sid,userId);
            if(hasOrder!=null&&hasOrder){
                return "恭喜您已经抢购成功!";
            }
        }catch (Exception e){
            LOGGER.error("检查订单异常:",e);
        }
        return "很抱歉你的订单尚未生成,继续排队";
    }

    /**
     * 缓存再删除的线程
     */
    private class delCacheByThread implements Runnable{
        private int sid;
        public  delCacheByThread(int sid){
            this.sid=sid;
        }
        @Override
        public void run(){
            try{
                LOGGER.info("异步执行缓存在删除,商品id:[{}],首先休眠:[{}]毫秒",sid,DELAY_MILLSECONDS);
                Thread.sleep(DELAY_MILLSECONDS);
                stockService.delStockCountCache(sid);
                LOGGER.info("再次删除商品id:[{}]",sid);
            }catch(Exception e){
                LOGGER.error("delCacheByThread执行出错",e);
            }
        }
    }

    /**
     * 像消息队列delCache发送消息
     * @param message
     */
    private void sendToDelCache(String message){
        LOGGER.info("这就去通知消息队列开始重试删除缓存:[{}]",message);
        this.rabbitTemplate.convertAndSend("delCache",message);
    }

    /**
     * 向消息队列orderQueue发送消息
     * @param message
     */
    private void sendToOrderQueue(String message){
        LOGGER.info("这就去通知消息队列开始下单:[{}]",message);
        this.rabbitTemplate.convertAndSend("orderQueue",message);
    }

}
