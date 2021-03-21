package com.rubber.base.components.mysql.factory.db.impl;

import com.rubber.base.components.mysql.exception.NotSingleDataSourceException;
import com.rubber.base.components.mysql.factory.db.BaseRubberDateSourceFactory;
import com.rubber.base.components.mysql.properties.RubberDbProperties;

/**
 * @author luffyu
 * Created on 2020/12/26
 */
public class RubberSingleDataSourceFactory extends BaseRubberDateSourceFactory {


    /**
     * 检测配置
     */
    @Override
    protected void checkDbProperties(RubberDbProperties rubberDbProperties){
        super.checkDbProperties(rubberDbProperties);
        if (rubberDbProperties.getConnects().size() != 1){
            throw new NotSingleDataSourceException();
        }
    }

}
