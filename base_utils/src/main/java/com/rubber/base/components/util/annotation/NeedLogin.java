package com.rubber.base.components.util.annotation;

import java.lang.annotation.*;

/**
 * @author luffyu
 * Created on 2022/9/17
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NeedLogin {

    /**
     * 是否碧玺
     */
    boolean request() default true;


    /**
     * 是否需要管理员
     */
    boolean needManager() default false;
}
