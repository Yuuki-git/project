package com.mwz.miaoshaservice.service;

/**
 * controller 调用此处的方法实际上是对对应的实现类中的调用
 */
public interface OrderService {

    /**
     * 最终情况
     * @param sid
     * @param userId
     * @param verifyHash
     * @throws Exception
     */
    void createOrder(Integer sid,Integer userId,String verifyHash) throws Exception;
    /**
     * 创建错误订单
     * @param sid
     * @return
     */
    int createWrongOrder(int sid);

    /**
     * 下单乐观锁
     * @param sid
     * @return
     */
    int createOptimisticOrder(int sid);

    /**
     * 下单悲观锁 for update
     * @param sid
     * @return
     */
    int createPessimisticOrder(int sid);
    /**
     * 创建正确订单：验证库存 + 用户 + 时间 合法性 + 下单乐观锁
     * @param sid
     * @param userId
     * @param verifyHash
     * @return
     * @throws Exception
     */
    int createVerifiedOrder(Integer sid,Integer userId,String verifyHash) throws  Exception;

    /**
     * 创建正确订单：验证库存 + 下单乐观锁 + 更新订单信息到缓存
     * @param sid
     * @param userId
     * @throws Exception
     */
    void createOrderByMq(Integer sid,Integer userId) throws Exception;

    /**
     * 检查缓存中是否已有订单
     * @param sid
     * @param userId
     * @return
     * @throws Exception
     */
    Boolean checkUserOrderInfoInCache(Integer sid,Integer userId) throws Exception;
}
