package com.rubber.base.components.mysql.plugins.sharding;

import java.lang.annotation.*;

/**
 * @author luffyu
 * Created on 2022/10/15
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ShardingField {
}
