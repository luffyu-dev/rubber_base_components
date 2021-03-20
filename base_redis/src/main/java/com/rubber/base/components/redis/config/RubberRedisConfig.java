package com.rubber.base.components.redis.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.rubber.base.components.redis.RedisClientProxy;
import com.rubber.base.components.redis.client.JedisClientProxy;
import com.rubber.base.components.redis.client.JedisClusterClientProxy;
import com.rubber.base.components.redis.client.NoCacheClientProxy;
import com.rubber.base.components.redis.exception.RedisInstanceNotFoundException;
import com.rubber.base.components.redis.exception.RedisInstanceTypeNotSupportException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
public class RubberRedisConfig {

    /**
     * 当前选中的集群名称
     */
    private String setName = "";

    /**
     * redis的实例配置
     */
    private Map<String,RedisClusterConfigProperties> instance;


    @Bean
    public RedisClientProxy redisClientProxy(){
        if (MapUtil.isEmpty(instance)){
            log.warn("[Redis Config]rubber.redis.instance is null. create default NoCacheClientProxy");
            return new NoCacheClientProxy();
        }
        RedisClusterConfigProperties configProperties = null;
        if (StrUtil.isEmpty(setName)){
            //读取第一个
            configProperties = instance.values().iterator().next();
        }else {
            configProperties = instance.get(setName);
        }
        if (configProperties == null){
            log.error("[Redis Config]rubber.redis.setName {} is error . can't find instance",setName);
            throw new RedisInstanceNotFoundException("[Redis Config] "+setName + " can't find instance");
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
            return new JedisClientProxy(shardInfo);
        }else if (RedisClusterType.CLUSTER.equals(configProperties.getRedisClusterType())){
            return new JedisClusterClientProxy(
                    nodes,
                    configProperties.getConnectionTimeout(),
                    configProperties.getSoTimeout(),
                    configProperties.getMaxAttempts(),
                    configProperties.getPassword(),
                    configProperties.getJedisPoolConfig());
        }
        log.error("[Redis Config]rubber.redis.instance type {} is not support . create default NoCacheClientProxy",configProperties.getRedisClusterType());
        throw new RedisInstanceTypeNotSupportException(configProperties.getRedisClusterType() + " is not support");
    }

}
