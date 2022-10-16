package com.rubber.base.components.mysql.config.properties;

import com.rubber.base.components.mysql.config.properties.bean.DruidDatasourceProperties;
import com.rubber.base.components.mysql.plugins.sharding.config.TableShardingConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * DruidDatasourceProperties
 * @author luffyu
 * Created on 2022/5/23
 */
@Data
@Configuration
@ConfigurationProperties(prefix="rubber.proxy.config")
public class RubberProxyConfigProperties {

    /**
     * db层面的数据配置
     */
    private Map<String, DruidDatasourceProperties> dbInstances;


    /**
     * 分库分表的配置
     */
    private List<TableShardingConfig> sharding;
}
