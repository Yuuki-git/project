package com.mwz.crm.annoation;

import java.lang.annotation.*;

/**
 * 定义方法需要对应资源的权限码
 * @author: yuuki
 * @Date: 2021-06-09 - 06 - 12:55
 * @Description: com.mwz.crm.annoation
 * @version: 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {
    // 权限码
    String code() default "";
}
