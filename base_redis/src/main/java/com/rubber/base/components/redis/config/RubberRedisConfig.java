package com.rubber.base.components.redis.config;

import com.rubber.base.components.redis.RedisClientProxy;
import com.rubber.base.components.redis.client.JedisClientProxy;
import com.rubber.base.components.redis.client.JedisClusterClientProxy;
import com.rubber.base.components.redis.exception.RedisInstanceNotFoundException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
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
public class RubberRedisConfig extends CachingConfigurerSupport {

    /**
     * redis的实例配置
     */
    private Map<String,RedisClusterConfigProperties> instance;


    /**
     * todo 待解决的问题是 需要加载多个Bean，怎么操作
     * @return
     */
    @Bean
    public RedisClientProxy createJedisClient(){
        String name = "normalRedis_Set";
        RedisClusterConfigProperties properties = instance.get(name);
        if (properties == null){
            throw new RedisInstanceNotFoundException(name + "can't find instance");
        }
        Set<HostAndPort> nodes = new HashSet<>();
        properties.getNodes().forEach(i->{
            String[] host = i.split(":");
            //添加集群节点
            nodes.add(new HostAndPort(host[0], Integer.parseInt(host[1])));
        });
        if (RedisClusterType.SINGLE.equals(properties.getRedisClusterType())){
            JedisShardInfo shardInfo = new JedisShardInfo(nodes.iterator().next());
            shardInfo.setPassword(properties.getPassword());
            return new JedisClientProxy(shardInfo);
        }else {
            return new JedisClusterClientProxy(nodes,
                    properties.getConnectionTimeout(),
                    properties.getSoTimeout(),
                    properties.getMaxAttempts(),
                    properties.getPassword(),
                    properties.getJedisPoolConfig());
        }
    }




}
