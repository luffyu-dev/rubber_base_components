package com.rubber.base.components.mysql.factory;

import com.rubber.base.components.mysql.factory.impl.RubberClusterDataSourceFactory;
import com.rubber.base.components.mysql.factory.impl.RubberSingleDataSourceFactory;
import com.rubber.base.components.mysql.properties.RubberDbProperties;

/**
 * @author luffyu
 * Created on 2020/12/27
 */
public class RubberDruidDataSourceFactoryBuilder {


    /**
     * 创建dataSource
     * @param rubberDbProperties
     * @return
     */
    public static RubberDataSourceFactory builder(RubberDbProperties rubberDbProperties){
        RubberDataSourceFactory rubberDataSourceFactory = null;
        switch (rubberDbProperties.getType()){
            case SINGLE:
                rubberDataSourceFactory = new RubberSingleDataSourceFactory();
                break;
            case CLUSTER:
                rubberDataSourceFactory = new RubberClusterDataSourceFactory();
                break;
            default:
        }
        //拿到配置db的配置信息
        return rubberDataSourceFactory;

    }
}
