package com.rubber.base.components.mysql.plugins.sharding.bean;

import lombok.Data;

import java.util.Map;

/**
 * @author luffyu
 * Created on 2022/10/15
 */
@Data
public class ShardingLogicTable {

    /**
     * 逻辑DB
     */
    private String logicDb;

    /**
     * 逻辑table
     */
    private String logicTable;

    /**
     * 真实的DB
     */
    private String realDb;

    /**
     * 正式的table
     */
    private String realTable;


    /**
     * 接口入参
     */
    private Map<String,Object> params;



    public String tableName(){
        return this.realDb + "." + this.realTable;
    }

}
