package com.rubber.base.components.redis.client;

import com.rubber.base.components.redis.RedisClientProxy;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterHostAndPortMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2021/3/17
 */
public class JedisClusterClientProxy extends JedisCluster implements RedisClientProxy {

    public JedisClusterClientProxy(HostAndPort node) {
        super(node);
    }

    public JedisClusterClientProxy(HostAndPort node, int timeout) {
        super(node, timeout);
    }

    public JedisClusterClientProxy(HostAndPort node, int timeout, int maxAttempts) {
        super(node, timeout, maxAttempts);
    }

    public JedisClusterClientProxy(HostAndPort node, GenericObjectPoolConfig poolConfig) {
        super(node, poolConfig);
    }

    public JedisClusterClientProxy(HostAndPort node, int timeout, GenericObjectPoolConfig poolConfig) {
        super(node, timeout, poolConfig);
    }

    public JedisClusterClientProxy(HostAndPort node, int timeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        super(node, timeout, maxAttempts, poolConfig);
    }

    public JedisClusterClientProxy(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        super(node, connectionTimeout, soTimeout, maxAttempts, poolConfig);
    }

    public JedisClusterClientProxy(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig poolConfig) {
        super(node, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
    }

    public JedisClusterClientProxy(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig poolConfig) {
        super(node, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig);
    }

    public JedisClusterClientProxy(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig poolConfig, boolean ssl) {
        super(node, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig, ssl);
    }

    public JedisClusterClientProxy(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig poolConfig, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier, JedisClusterHostAndPortMap hostAndPortMap) {
        super(node, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig, ssl, sslSocketFactory, sslParameters, hostnameVerifier, hostAndPortMap);
    }

    public JedisClusterClientProxy(Set<HostAndPort> nodes) {
        super(nodes);
    }

    public JedisClusterClientProxy(Set<HostAndPort> nodes, int timeout) {
        super(nodes, timeout);
    }

    public JedisClusterClientProxy(Set<HostAndPort> nodes, int timeout, int maxAttempts) {
        super(nodes, timeout, maxAttempts);
    }

    public JedisClusterClientProxy(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig) {
        super(nodes, poolConfig);
    }

    public JedisClusterClientProxy(Set<HostAndPort> nodes, int timeout, GenericObjectPoolConfig poolConfig) {
        super(nodes, timeout, poolConfig);
    }

    public JedisClusterClientProxy(Set<HostAndPort> jedisClusterNode, int timeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, timeout, maxAttempts, poolConfig);
    }

    public JedisClusterClientProxy(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, poolConfig);
    }

    public JedisClusterClientProxy(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
    }

    public JedisClusterClientProxy(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig);
    }

    public JedisClusterClientProxy(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig poolConfig, boolean ssl) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig, ssl);
    }

    public JedisClusterClientProxy(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig poolConfig, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier, JedisClusterHostAndPortMap hostAndPortMap) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig, ssl, sslSocketFactory, sslParameters, hostnameVerifier, hostAndPortMap);
    }

    /**
     * 单台实例的方法
     *
     * @param key
     * @param value
     * @param seconds
     */
    @Override
    public void set(String key, String value, int seconds) {
        set(key,value);
        expire(key,seconds);
    }
}
