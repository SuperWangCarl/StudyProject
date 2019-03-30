package com.redis;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * redis 
 * redis-sentinel
 * redis-sentinel + sharded = cluster
 * 因为 jedis 中 只提供了未分片的连接池  和分片的连接 没有提供分片的连接池 所有需要我们自己写
 * Jedis包中有个很恶心的问题，那就是包里面有支持分片的ShardeJedis客户端类，也有支持哨兵的池类JedisSentinelPool，就是没有既支持分片又支持哨兵的池类，所以必须自己自定义一个ShardedJedisSentinelPool
 * @author SuperWang
 * @version Create Time：2018年1月8日 下午4:32:32
 */
public class RedisFactory {

	private static JedisPoolConfig jedisPoolConfig;
	//自定义cluster取消close方法
	private static JedisClusterNoClose cluster;
	private static JedisSentinelPool sentinel;
	private static JedisPool jedisPool;

	public static final int EXPIRE_TIME = 1814400;

	static {
		initPool();
		//initJedisCluster();
		//getSentinl();

	}

	/**
	 * 获取之后需要关闭本次连接,但是不可以关闭连接池
	 * @return
	 */
	public static Jedis getSentinlConnect() {
		if (sentinel == null || sentinel.isClosed()) {
			Set<String> sentinels = new HashSet<String>();
			sentinels.add("192.168.207.71:26011");
			sentinels.add("192.168.207.71:26012");
			sentinels.add("192.168.207.71:26013");
			//masterName 分片的名称 
			//sentinels Redis Sentinel 服务地址列表 
			sentinel = new JedisSentinelPool("mymaster", sentinels, jedisPoolConfig);
		}
		return sentinel.getResource();
	}

	public static JedisSentinelPool getSentinlPool() {
		if (sentinel == null || sentinel.isClosed()) {
			Set<String> sentinels = new HashSet<String>();
			sentinels.add("192.168.207.71:26011");
			sentinels.add("192.168.207.71:26012");
			sentinels.add("192.168.207.71:26013");
			System.out.println("----------------------------------------------");
			sentinel = new JedisSentinelPool("mymaster", sentinels, jedisPoolConfig);
		}
		return sentinel;
	}

	/**
	 * 获取之后需要关闭本次连接,但是不可以关闭连接池
	 * @return
	 */
	public static Jedis getSinglePool() {
		if (jedisPool == null || jedisPool.isClosed()) {
			jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6001);
		}
		//从连接池中获得连接
		return jedisPool.getResource();
	}

	/**
	 * 获取之后不可关闭连接
	 * @return
	 */
	public static JedisCluster getCluster() {
		if (cluster == null) {
			initJedisCluster();
		}
		return cluster;
	}

	private static JedisPoolConfig initPool() {
		jedisPoolConfig = new JedisPoolConfig();
		//最大连接数
		jedisPoolConfig.setMaxTotal(200);
		//最大空闲连接数
		jedisPoolConfig.setMaxIdle(50);
		//最大空闲连接数
		jedisPoolConfig.setMinIdle(50);
		//每次释放连接的最大数目
		jedisPoolConfig.setNumTestsPerEvictionRun(50);
		//释放连接的扫描间隔（毫秒)
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(600000);
		//连接最小空闲时间
		jedisPoolConfig.setMinEvictableIdleTimeMillis(1800000);
		//连接空闲多久后释放, 当空闲时间>该值 且 空闲连接>最大空闲连接数 时直接释放
		jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(200000);
		//获取连接时的最大等待毫秒数,小于零:阻塞不确定的时间,默认-1 超出这时间报异常
		jedisPoolConfig.setMaxWaitMillis(1500);
		//连接耗尽时是否阻塞, false报异常
		jedisPoolConfig.setBlockWhenExhausted(true);
		//在获取连接的时候检查有效性, 默认false
		//jedisPoolConfig.setTestOnBorrow(true);
		// 在空闲时检查有效性, 默认false 
		jedisPoolConfig.setTestWhileIdle(true);
		return jedisPoolConfig;
	}

	private static void initJedisCluster() {
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		//60.169.122.58:6001 60.169.122.59:6003 60.169.122.60:6005 60.169.122.60:6006 60.169.122.58:6002 60.169.122.59:6004
		nodes.add(new HostAndPort("192.168.0.72", 6001));
		nodes.add(new HostAndPort("192.168.0.72", 6002));
		nodes.add(new HostAndPort("192.168.0.72", 6003));
		nodes.add(new HostAndPort("192.168.0.72", 6004));
		nodes.add(new HostAndPort("192.168.0.72", 6005));
		nodes.add(new HostAndPort("192.168.0.72", 6006));
		cluster = new JedisClusterNoClose(nodes, jedisPoolConfig);
	}

	/**
	 * 获取匹配的keys个数 集群
	 * @param pattern 匹配值
	 * @return 符合该keys的个数
	 */
	public static int keysCountByCluster(String pattern) {
		TreeSet<String> keys = new TreeSet<String>();
		Map<String, JedisPool> clusterNodes = getCluster().getClusterNodes();
		for (String k : clusterNodes.keySet()) {
			if (k != null && (k.contains("6001") || k.contains("6002") || k.contains("6003"))) {
				JedisPool jp = clusterNodes.get(k);
				Jedis connection = jp.getResource();
				try {
					keys.addAll(connection.keys(pattern));

				} catch (Exception e) {
				} finally {
					connection.close();//用完一定要close这个链接！！！  
				}
			}
		}
		return keys.size();
	}

	/**
	 * 获取匹配的keys的值 集群
	 * @param pattern 匹配值
	 * @return 符合该keys的个数
	 */
	public static TreeSet<String> keysByCluster(String pattern) {
		TreeSet<String> keys = new TreeSet<String>();
		Map<String, JedisPool> clusterNodes = getCluster().getClusterNodes();
		try {

			for (String k : clusterNodes.keySet()) {
				if (k != null && (k.contains("6001") || k.contains("6002") || k.contains("6003"))) {
					//通过每个节点获取相应的连接池
					JedisPool jp = clusterNodes.get(k);
					Jedis connection = jp.getResource();
					try {
						keys.addAll(connection.keys(pattern));
					} catch (Exception e) {
					} finally {
						connection.close();//用完一定要close这个链接！！！  
					}
				}
			}
		} finally {
		}
		return keys;
	}

	/**
	 * 返回key的剩余时间 集群
	 * @param key
	 * @return
	 */
	public static Long ttlByCluster(String key) {
		Long ttl = getCluster().ttl(key);
		return ttl;
	}

	/**
	 * 返回符合要求的key
	 * @param pattern
	 * @return
	 */
	public static int keysCount(String pattern) {
		return getSentinlConnect().keys(pattern).size();
	}

	/**
	 * 返回符合要求的key
	 * @param pattern
	 * @return
	 */
	public static Set<String> keys(String pattern) {
		return getSentinlConnect().keys(pattern);
	}
}
