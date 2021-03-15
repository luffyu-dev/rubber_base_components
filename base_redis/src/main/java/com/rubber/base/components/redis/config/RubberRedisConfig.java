package com.rubber.base.components.redis.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

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
public class RubberRedisConfig extends CachingConfigurerSupport {


    private Map<String,RedisClusterConfigProperties> cluster;



//    @Bean
//    public RubberJedisClusterProxy getJedisCluster() {
//        String redisSetName = "normalRedis";
//        RedisClusterConfigProperties setClusterProperties = cluster.get(redisSetName);
//        if (!RedisClusterType.CLUSTER.equals(setClusterProperties.getRedisClusterType())){
//            return null;
//        }
//        // 截取集群节点
//        String[] cluster = setClusterProperties.getNodes().toArray(new String[0]);
//        // 创建set集合
//        Set<HostAndPort> nodes = new HashSet<>();
//        // 循环数组把集群节点添加到set集合中
//        for (String node : cluster) {
//            String[] host = node.split(":");
//            //添加集群节点
//            nodes.add(new HostAndPort(host[0], Integer.parseInt(host[1])));
//        }
//        return new RubberJedisClusterProxy(nodes,
//                setClusterProperties.getConnectionTimeout(),
//                setClusterProperties.getSoTimeout(),
//                setClusterProperties.getMaxAttempts(),
//                setClusterProperties.getPassword(), setClusterProperties.getJedisPoolConfig());
//    }





}
