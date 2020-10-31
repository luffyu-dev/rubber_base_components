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
     * @see DBShardingType
     */
    private DBShardingType shardingType;



    private Map<String,DruidDataSource>  shardingDbMap;



    private String masterName;


    private Set<String> slaveNames;


    public RubberDataSourceBean() {
    }

    public RubberDataSourceBean(DBShardingType shardingType) {
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
