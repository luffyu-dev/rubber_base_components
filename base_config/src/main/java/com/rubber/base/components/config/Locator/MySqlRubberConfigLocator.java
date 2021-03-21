package com.rubber.base.components.config.Locator;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.rubber.base.components.config.properties.RubberProxyConfigProperties;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.HashSet;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2021/1/24
 */
@Order(1)
public class MySqlRubberConfigLocator extends BaseRubberConfigLocator {


    public MySqlRubberConfigLocator(NacosConfigManager nacosConfigManager,RubberProxyConfigProperties rubberProxyConfigProperties) {
       super(nacosConfigManager,rubberProxyConfigProperties);
    }

    @Override
    public Set<String> doCreateDataIds(Environment environment,String[] activeProfiles) {
        String dbSetNames = getRubberProxyConfigProperties().getDbSet();
        if (StrUtil.isEmpty(dbSetNames)){
            return null;
        }
        String[] dbSetNameArray = dbSetNames.split(",");
        Set<String> mysqlDataId = new HashSet<>();
        for (String activeProfile:activeProfiles){
            StringBuilder dataId = new StringBuilder(activeProfile+"-rubber-config-mysql-");
            for (String dbSetName:dbSetNameArray){
                dataId.append(dbSetName).append(".yml");
            }
            mysqlDataId.add(dataId.toString());
        }
        //rubber-config-mysql-dev-userDb.yml
        return mysqlDataId;
    }

    @Override
    public String configLocatorName() {
        return "rubberConfigMysql";
    }
}
