package com.yqs.db.router.annotation;


import java.lang.annotation.*;

/**
 * 路由注解
 * @author 盐汽水
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface DBRouter {

    /**
     * 需要进行分表分库的字段
     * 通过此字段来计算分到哪个表哪个库
     * @return
     */
    String key() default "";
}
