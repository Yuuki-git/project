package com.mwz.crm.aspect;

import com.mwz.crm.annoation.RequiredPermission;
import com.mwz.crm.exceptions.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import java.util.List;

/**
 * 切面 会拦截指定包下的指定注解
 * 切入点和通知
 * @author: yuuki
 * @Date: 2021-06-09 - 06 - 13:03
 * @Description: aspect
 * @version: 1.0
 */
@Component
@Aspect
public class PermissionProxy {
    @Resource
    private HttpSession session;
    // 必须有调用方法和返回值
    @Around(value="@annotation(com.mwz.crm.annoation.RequiredPermission)")
    public Object around(ProceedingJoinPoint pjp) throws  Throwable{
        Object result= null;
        // 得到当前登录用户拥有的权限（session作用域）
        List<String> permissions= (List<String>) session.getAttribute("permissions");
        // 判断用户是否有权限
        if(null == permissions||permissions.size()<1){
            // 抛出异常
            throw new AuthException();
        }

        //得到对应的目标方法
        MethodSignature methodSignature= (MethodSignature) pjp.getSignature();
        //得到方法上的注解
        RequiredPermission requiredPermission=methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        // 判断注解上对应的状态码
        if(!(permissions.contains((requiredPermission.code())))){
            //如果权限中不包含当前方法指定的权限码，则抛出异常
            throw new AuthException();
        }
        result =pjp.proceed();
        return result;

    }
}
