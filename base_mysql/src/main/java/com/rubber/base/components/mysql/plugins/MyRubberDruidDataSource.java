package com.rubber.base.components.mysql.plugins;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;

import java.util.Map;

/**
 * @author luffyu
 * Created on 2022/10/14
 */
@Getter
public class MyRubberDruidDataSource extends DruidDataSource {


    private Map<String,MyRubberDruidDataSource> dataSourceMap;


    public MyRubberDruidDataSource() {
    }


    public void setDataSourceMap(Map<String,MyRubberDruidDataSource> dataSourceMap) {
       this.dataSourceMap = dataSourceMap;
    }


}
