package com.mwz.crm;

import com.alibaba.fastjson.JSON;
import com.mwz.crm.base.ResultInfo;
import com.mwz.crm.exceptions.AuthException;
import com.mwz.crm.exceptions.NoLoginException;
import com.mwz.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 全局异常处理器
 * 两种方法返回值
 *      判断方法的返回值
 *      如果有@respondseBody注解，则返回的是json 否则返回的是对象
 * @author: yuuki
 * @Date: 2021-05-11 - 05 - 17:05
 * @Description: com.mwz.crm
 * @version: 1.0
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    /**
     *
     * @param httpServletRequest request对象
     * @param httpServletResponse response对象
     * @param handler  拦截的方法
     * @param e 异常对象
     * @return mv
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) {
        /**
         * 判断是否为非法请求拦截抛出的未登录异常
         */
        if(e instanceof NoLoginException){
        ModelAndView modelAndView=new ModelAndView();
        // 请求转发地址栏不会变
        modelAndView.setViewName("redirect:/index");
        return modelAndView;
        }
        // 设置的默认的异常处理
        ModelAndView mv=new ModelAndView();
        mv.setViewName("error");
        mv.addObject("msg","默认的异常处理");

        /**
         * 判断方法的返回值类型
         */
        // 得到方法对象handler
        if(handler instanceof HandlerMethod){
            // 类型转换
            HandlerMethod handlerMethod= (HandlerMethod) handler;
            // 得到方法
            Method method=handlerMethod.getMethod();
            // 得到方法名
            System.out.println("目前异常的方法名"+method.getName());
            /**
             * 判断方法上是否有指定的注解
             */
            ResponseBody responseBody=method.getDeclaredAnnotation(ResponseBody.class);
            if(responseBody==null){
                /**
                 * 方法返回的视图
                 */
                if(e instanceof ParamsException){
                    ParamsException p= (ParamsException) e;
                    mv.setViewName("error");
                    mv.addObject("msg",p.getMsg());
                    mv.addObject("code",p.getCode());
                }else if(e instanceof AuthException){ //认证异常
                    AuthException a= (AuthException) e;
                    mv.setViewName("error");
                    mv.addObject("msg",a.getMsg());
                    mv.addObject("code",a.getCode());
                }
                return mv;
            }else {
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("操作失败");
                if(e instanceof ParamsException){
                    ParamsException p= (ParamsException) e;
                    resultInfo.setMsg(p.getMsg());
                    resultInfo.setCode(p.getCode());
                }else if(e instanceof AuthException){ //认证异常
                    AuthException a= (AuthException) e;
                    resultInfo.setMsg(a.getMsg());
                    resultInfo.setCode(a.getCode());
                }


                //设置相应类型及编码格式
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter out=null;
                try {
                    out = httpServletResponse.getWriter();
                    //将相应结果输出
                    //转换成json格式
                    out.write(JSON.toJSONString(resultInfo));
                    out.flush();
                    out.close();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                return null;
            }
        }

        return mv;
    }

}
