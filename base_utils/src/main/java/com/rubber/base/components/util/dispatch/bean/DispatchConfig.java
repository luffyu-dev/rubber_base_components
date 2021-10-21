package com.rubber.base.components.util.dispatch.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p></p>
 *
 * @author luffyu
 * @date 2020-04-26 11:20
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DispatchConfig {

    /**
     * 分发的关键值
     */
    String key() default "";


    /**
     * 当前分发的类型
     */
    Class<?> type();
}
