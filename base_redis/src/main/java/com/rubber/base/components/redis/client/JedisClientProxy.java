package com.rubber.base.components.redis.client;

import com.rubber.base.components.redis.RedisClientProxy;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.net.URI;

/**
 * @author luffyu
 * Created on 2021/3/17
 */
public class JedisClientProxy extends Jedis implements RedisClientProxy {

    public JedisClientProxy() {
    }

    public JedisClientProxy(String host) {
        super(host);
    }

    public JedisClientProxy(HostAndPort hp) {
        super(hp);
    }

    public JedisClientProxy(String host, int port) {
        super(host, port);
    }

    public JedisClientProxy(String host, int port, boolean ssl) {
        super(host, port, ssl);
    }

    public JedisClientProxy(String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, port, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisClientProxy(String host, int port, int timeout) {
        super(host, port, timeout);
    }

    public JedisClientProxy(String host, int port, int timeout, boolean ssl) {
        super(host, port, timeout, ssl);
    }

    public JedisClientProxy(String host, int port, int timeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, port, timeout, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisClientProxy(String host, int port, int connectionTimeout, int soTimeout) {
        super(host, port, connectionTimeout, soTimeout);
    }

    public JedisClientProxy(String host, int port, int connectionTimeout, int soTimeout, boolean ssl) {
        super(host, port, connectionTimeout, soTimeout, ssl);
    }

    public JedisClientProxy(String host, int port, int connectionTimeout, int soTimeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, port, connectionTimeout, soTimeout, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisClientProxy(JedisShardInfo shardInfo) {
        super(shardInfo);
    }

    public JedisClientProxy(URI uri) {
        super(uri);
    }

    public JedisClientProxy(URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisClientProxy(URI uri, int timeout) {
        super(uri, timeout);
    }

    public JedisClientProxy(URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisClientProxy(URI uri, int connectionTimeout, int soTimeout) {
        super(uri, connectionTimeout, soTimeout);
    }

    public JedisClientProxy(URI uri, int connectionTimeout, int soTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, connectionTimeout, soTimeout, sslSocketFactory, sslParameters, hostnameVerifier);
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
