package com.mwz.crm.interceptor;

import com.mwz.crm.dao.UserMapper;
import com.mwz.crm.exceptions.NoLoginException;
import com.mwz.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: yuuki
 * @Date: 2021-05-12 - 05 - 15:38
 * @Description: com.mwz.crm.interceptor
 * @version: 1.0
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserMapper userMapper;
    /**
     *拦截用户是否是登录状态
     *  在目标方法执行前执行
     *
     *  返回布尔类型
     *      返回true:目标方法可以执行
     *
     *  如果判断用户是否是登录状态:
     *      1.判断cookie中是否存在用户信息(获取用户ID)
     *      2.数据库中是否存在指定用户ID的值
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中拿用户id
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        // 判断id是否为空
        if(null == userId||userMapper.selectByPrimaryKey(userId)==null){
            // 抛出未登陆异常
            throw new NoLoginException();
        }
        return true;
    }
}
