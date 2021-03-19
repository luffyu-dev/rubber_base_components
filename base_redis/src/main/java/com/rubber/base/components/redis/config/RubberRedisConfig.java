package com.rubber.base.components.redis.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.rubber.base.components.redis.RedisClientProxy;
import com.rubber.base.components.redis.client.JedisClientProxy;
import com.rubber.base.components.redis.client.JedisClusterClientProxy;
import com.rubber.base.components.redis.exception.RedisInstanceNotFoundException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisShardInfo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>redis配置文件</p>
 *
 * @author WangXue
 * @date 2018-09-03 16:34
 */
@Data
@Slf4j
@SuppressWarnings("Duplicates")
@Configuration
@ConfigurationProperties(prefix = "rubber.redis")
public class RubberRedisConfig implements InitializingBean {

    /**
     * redis的实例配置
     */
    private Map<String,RedisClusterConfigProperties> instance;


    private final ConfigurableApplicationContext applicationContext;


    public RubberRedisConfig(ConfigurableApplicationContext configurableApplicationContext) {
        this.applicationContext = configurableApplicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        createRedisClient();
        Map<String,RedisClientProxy> map = applicationContext.getBeansOfType(RedisClientProxy.class);
        System.out.println(map);
    }


    public void createRedisClient(){
        if (MapUtil.isEmpty(instance)){
            return;
        }
        for (Map.Entry<String,RedisClusterConfigProperties> propertiesEntry:instance.entrySet()){
            String name = propertiesEntry.getKey();
            RedisClusterConfigProperties configProperties = propertiesEntry.getValue();
            if (CollUtil.isEmpty(configProperties.getNodes())){
                throw new RedisInstanceNotFoundException(name + "can't find instance");
            }
            //获取node节点
            Set<HostAndPort> nodes = new HashSet<>();
            configProperties.getNodes().forEach(i->{
                String[] host = i.split(":");
                //添加集群节点
                nodes.add(new HostAndPort(host[0], Integer.parseInt(host[1])));
            });
            if (RedisClusterType.SINGLE.equals(configProperties.getRedisClusterType())) {
                JedisShardInfo shardInfo = new JedisShardInfo(nodes.iterator().next());
                shardInfo.setPassword(configProperties.getPassword());
                registerBean(name,JedisClientProxy.class, shardInfo);
            }else if (RedisClusterType.CLUSTER.equals(configProperties.getRedisClusterType())){
                registerBean(name,JedisClusterClientProxy.class,
                        nodes,
                        configProperties.getConnectionTimeout(),
                        configProperties.getSoTimeout(),
                        configProperties.getMaxAttempts(),
                        configProperties.getPassword(),
                        configProperties.getJedisPoolConfig());
            }else {
                log.error("redis cluster type {} not support ",name);
            }
        }
    }

    /**
     * todo 待解决的问题是 需要加载多个Bean，怎么操作
     * @return
     */
//    public RedisClientProxy createJedisClient(){
//        String name = "normalRedis_Set";
//        RedisClusterConfigProperties properties = instance.get(name);
//        if (properties == null){
//            throw new RedisInstanceNotFoundException(name + "can't find instance");
//        }
//        Set<HostAndPort> nodes = new HashSet<>();
//        properties.getNodes().forEach(i->{
//            String[] host = i.split(":");
//            //添加集群节点
//            nodes.add(new HostAndPort(host[0], Integer.parseInt(host[1])));
//        });
//        if (RedisClusterType.SINGLE.equals(properties.getRedisClusterType())){
//            JedisShardInfo shardInfo = new JedisShardInfo(nodes.iterator().next());
//            shardInfo.setPassword(properties.getPassword());
//            return new JedisClientProxy(shardInfo);
//        }else {
//            return new JedisClusterClientProxy(nodes,
//                    properties.getConnectionTimeout(),
//                    properties.getSoTimeout(),
//                    properties.getMaxAttempts(),
//                    properties.getPassword(),
//                    properties.getJedisPoolConfig());
//        }
//    }
    private <T> T registerBean(String name, Class<T> clazz, Object... args) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        if (args.length > 0) {
            for (Object arg : args) {
                beanDefinitionBuilder.addConstructorArgValue(arg);
            }
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }


}
