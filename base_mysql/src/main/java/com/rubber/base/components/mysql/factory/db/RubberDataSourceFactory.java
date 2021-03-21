package com.rubber.base.components.mysql.factory.db;

import com.rubber.base.components.mysql.bean.RubberDruidDataSource;
import com.rubber.base.components.mysql.bean.RubberShardingRuleBean;
import com.rubber.base.components.mysql.properties.RubberDbProperties;

/**
 * @author luffyu
 * Created on 2020/12/26
 */
public interface RubberDataSourceFactory {

    /**
     * 创建db配置尊重
     * @param rubberDbProperties
     * @return
     */
    RubberDruidDataSource createDbSource(RubberDbProperties rubberDbProperties);


    /**
     * 创建路由分片规则
     * @param rubberDbProperties
     * @return
     */
    RubberShardingRuleBean createDbRule(RubberDbProperties rubberDbProperties);

}
