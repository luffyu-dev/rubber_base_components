package com.rubber.base.components.mysql.properties;

import com.rubber.base.components.mysql.bean.DBClusterType;
import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2020/12/26
 */
@Data
public class RubberDbProperties {

    /**
     * 实例名称
     */
    private String instanceName;

    /**
     * db的类型
     * @see DBClusterType
     */
    private DBClusterType type;


    /**
     * 多种配置
     */
    private List<DbConnectProperties> connects;


    /**
     * 配置信息
     */
    private DbConfigProperties config;


}
