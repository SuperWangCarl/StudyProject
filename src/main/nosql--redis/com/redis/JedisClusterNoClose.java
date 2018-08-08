package com.redis;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisClusterNoClose extends JedisCluster {

	public static enum Reset {
		SOFT, HARD
	}

	public JedisClusterNoClose(HostAndPort node) {
		this(Collections.singleton(node), DEFAULT_TIMEOUT);
	}

	public JedisClusterNoClose(HostAndPort node, int timeout) {
		this(Collections.singleton(node), timeout, DEFAULT_MAX_REDIRECTIONS);
	}

	public JedisClusterNoClose(HostAndPort node, int timeout, int maxAttempts) {
		this(Collections.singleton(node), timeout, maxAttempts, new GenericObjectPoolConfig());
	}

	public JedisClusterNoClose(HostAndPort node, final GenericObjectPoolConfig poolConfig) {
		this(Collections.singleton(node), DEFAULT_TIMEOUT, DEFAULT_MAX_REDIRECTIONS, poolConfig);
	}

	public JedisClusterNoClose(HostAndPort node, int timeout, final GenericObjectPoolConfig poolConfig) {
		this(Collections.singleton(node), timeout, DEFAULT_MAX_REDIRECTIONS, poolConfig);
	}

	public JedisClusterNoClose(HostAndPort node, int timeout, int maxAttempts, final GenericObjectPoolConfig poolConfig) {
		this(Collections.singleton(node), timeout, maxAttempts, poolConfig);
	}

	public JedisClusterNoClose(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, final GenericObjectPoolConfig poolConfig) {
		super(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, poolConfig);
	}

	public JedisClusterNoClose(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, final GenericObjectPoolConfig poolConfig) {
		super(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
	}

	public JedisClusterNoClose(Set<HostAndPort> nodes) {
		this(nodes, DEFAULT_TIMEOUT);
	}

	public JedisClusterNoClose(Set<HostAndPort> nodes, int timeout) {
		this(nodes, timeout, DEFAULT_MAX_REDIRECTIONS);
	}

	public JedisClusterNoClose(Set<HostAndPort> nodes, int timeout, int maxAttempts) {
		this(nodes, timeout, maxAttempts, new GenericObjectPoolConfig());
	}

	public JedisClusterNoClose(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig) {
		this(nodes, DEFAULT_TIMEOUT, DEFAULT_MAX_REDIRECTIONS, poolConfig);
	}

	public JedisClusterNoClose(Set<HostAndPort> nodes, int timeout, final GenericObjectPoolConfig poolConfig) {
		this(nodes, timeout, DEFAULT_MAX_REDIRECTIONS, poolConfig);
	}

	public JedisClusterNoClose(Set<HostAndPort> jedisClusterNode, int timeout, int maxAttempts, final GenericObjectPoolConfig poolConfig) {
		super(jedisClusterNode, timeout, maxAttempts, poolConfig);
	}

	public JedisClusterNoClose(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, final GenericObjectPoolConfig poolConfig) {
		super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, poolConfig);
	}

	public JedisClusterNoClose(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, String password,
			final GenericObjectPoolConfig poolConfig) {
		super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
	}
	public void close(){
		
	}
}
