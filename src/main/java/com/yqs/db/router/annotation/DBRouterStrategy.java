package com.yqs.db.router.annotation;

import java.lang.annotation.*;

/**
 * 路由策略，分表标记
 * @author 盐汽水
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface DBRouterStrategy {

    boolean splitTable() default false;

}
