package com.rubber.base.components.mysql.bean;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2020/10/28
 */
@Data
public class RubberDataSourceBean {

    /**
     * 数据库的分库规则
     * @see DBClusterType
     */
    private DBClusterType shardingType;

    /**
     * 当前全部的数据源信息
     */
    private Map<String,DruidDataSource>  shardingDbMap;


    /**
     * 主数据源的名称
     */
    private String masterName;


    /**
     * 从数据源的名称
     */
    private Set<String> slaveNames;

    /**
     * 默认的数据源配置
     * 不进行分库分表的时候，读取的数据源
     */
    private String defaultName;


    public RubberDataSourceBean() {
    }

    public RubberDataSourceBean(DBClusterType shardingType) {
        this.shardingType = shardingType;
    }


    public void addDb(String name,DruidDataSource druidDataSource){
        if (shardingDbMap == null){
            shardingDbMap = new HashMap<>(16);
        }
        shardingDbMap.put(name,druidDataSource);


    }


    public void addSlaveName(String name){
        if (slaveNames == null){
            slaveNames = new HashSet<>(16);
        }
        slaveNames.add(name);
    }
}
