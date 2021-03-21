package com.rubber.base.components.mysql.bean;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2020/12/26
 */
@Data
public class RubberDruidDataSource {

    /**
     * 数据库的索引名称
     */
    private String instanceName;


    /**
     * 数据库的分库规则
     * @see DBClusterType
     */
    private DBClusterType shardingType;


    /**
     * 当前全部的数据源信息
     */
    private Map<String, DruidDataSource> clusters;


    /**
     * 读库配置
     * key未数据库的名称
     */
    private Map<String, List<DruidDataSource>> slaves;

}
