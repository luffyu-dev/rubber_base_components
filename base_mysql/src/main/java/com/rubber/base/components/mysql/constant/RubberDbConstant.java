package com.rubber.base.components.mysql.constant;

/**
 * @author luffyu
 * Created on 2020/12/26
 */
public class RubberDbConstant {


    /**
     * 下划线
     */
    public static final String UNDER_LINE_KEY = "_";

    /**
     * masterKey
     */
    public static final String  MASTER_KEY = "master";

    /**
     * slaveKey
     */
    public static final String  SLAVE_KEY = "slave";





    public static String createSlaveDbName(String masterName,int index){
        return masterName + UNDER_LINE_KEY + SLAVE_KEY + UNDER_LINE_KEY +createIndex(index);
    }


    public static String createIndex(int i){

        return String.valueOf(i);
    }
}
