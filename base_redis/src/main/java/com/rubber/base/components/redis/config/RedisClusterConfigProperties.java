package com.rubber.base.components.redis.config;

import lombok.Data;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

/**
 * @description:
 * @author: WangXue
 * @create: 2018-09-03 18:12
 */
@Data
public class RedisClusterConfigProperties {


    /**
     * 集群的模式
     */
    private RedisClusterType redisClusterType = RedisClusterType.SINGLE;

    /**
     * master nodes
     */
    private List<String> nodes;
    /**
     * 密码
     */
    private String password;


    /**
     * max redirects
     */
    private Integer maxAttempts;
    private Integer connectionTimeout;
    private Integer soTimeout;


    /**
     * 配置信息
     */
    private JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();


}
