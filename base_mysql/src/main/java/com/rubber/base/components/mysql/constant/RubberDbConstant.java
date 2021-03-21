package com.rubber.base.components.mysql.constant;

import cn.hutool.core.util.StrUtil;

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




    /**
     * 返回hash值
     * @param str 字符串
     * @param maxNumber hash最大值
     * @return 返回一个正数到hash值
     */
    public static Integer modHash(String str,int maxNumber){
        if(maxNumber == 0){
            maxNumber = 1;
        }
        int hash =  createHash(str) % maxNumber;
        if(hash < 0){
            return -hash;
        }else {
            return hash;
        }
    }

    /**
     * 获取到hashCode
     * @param str
     * @return
     */
    public static Integer createHash(String str){
        if(StrUtil.isEmpty(str)){
            return "null".hashCode();
        }
        return str.toLowerCase().hashCode();
    }

    public static String createSlaveDbName(String masterName,int index){
        return masterName + UNDER_LINE_KEY + SLAVE_KEY + UNDER_LINE_KEY +createIndex(index);
    }


    public static String createIndex(int i){

        return String.valueOf(i);
    }
}
