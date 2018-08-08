package com.redis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class HelloJedis {

	//单实例链接
	public void testJedis(){
		//设置ip和端口
		Jedis jedis = new Jedis("192.168.0.72", 6379);
		//设置数据
		jedis.set("hello", "world");
		//获得数据
		String name = jedis.get("hello");
		System.out.println(name);
		//释放资源
		jedis.close();
	}
	//连接池 链接
	@Test
	public void testJedisPool(){
		//获得连接池配置对象,设置配置项
		JedisPoolConfig config = new JedisPoolConfig();
		//最大连接数
		config.setMaxTotal(30);
		//最大空闲连接数
		config.setMaxIdle(10);
		
		//获得连接池
		 JedisPool jedisPool = new JedisPool(config, "192.168.0.72", 6379);
		 
		 Jedis jedis = null;
		 try{
			 jedis = jedisPool.getResource();
			 String name = jedis.get("hello");
			 System.out.println(name);
		 }catch(Exception e){
			 
		 }finally {
			if (jedis != null){
				jedis.close();
			}
			if(jedisPool != null){
				jedisPool.close();
			}
		}
	}
	
	public void jedisCluster(){
		
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.0.72", 7001));
		nodes.add(new HostAndPort("192.168.0.72", 7001));
		nodes.add(new HostAndPort("192.168.0.72", 7001));
		nodes.add(new HostAndPort("192.168.0.72", 7001));
		nodes.add(new HostAndPort("192.168.0.72", 7001));
		nodes.add(new HostAndPort("192.168.0.72", 7001));
		nodes.add(new HostAndPort("192.168.0.72", 7001));
		new JedisCluster(nodes );
	}
}
/*配置spring
 	<!-- 连接池配置 -->
	<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
		<!-- 最大连接数 -->
		<property name="maxTotal" value="30" />
		<!-- 最大空闲连接数 -->
		<property name="maxIdle" value="10" />
		<!-- 每次释放连接的最大数目 -->
		<property name="numTestsPerEvictionRun" value="1024" />
		<!-- 释放连接的扫描间隔（毫秒） -->
		<property name="timeBetweenEvictionRunsMillis" value="30000" />
		<!-- 连接最小空闲时间 -->
		<property name="minEvictableIdleTimeMillis" value="1800000" />
		<!-- 连接空闲多久后释放, 当空闲时间>该值 且 空闲连接>最大空闲连接数 时直接释放 -->
		<property name="softMinEvictableIdleTimeMillis" value="10000" />
		<!-- 获取连接时的最大等待毫秒数,小于零:阻塞不确定的时间,默认-1 -->
		<property name="maxWaitMillis" value="1500" />
		<!-- 在获取连接的时候检查有效性, 默认false -->
		<property name="testOnBorrow" value="false" />
		<!-- 在空闲时检查有效性, 默认false -->
		<property name="testWhileIdle" value="true" />
		<!-- 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true -->
		<property name="blockWhenExhausted" value="false" />
	</bean>

	<!-- redis单机 通过连接池 -->
	<bean id="jedisPool" class="redis.clients.jedis.JedisPool"
		destroy-method="close">
		<constructor-arg name="poolConfig" ref="jedisPoolConfig" />
		<constructor-arg name="host" value="192.168.242.130" />
		<constructor-arg name="port" value="6379" />
	</bean>
 * */

/*
 <!-- 连接池配置 -->
<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
	<!-- 最大连接数 -->
	<property name="maxTotal" value="30" />
	<!-- 最大空闲连接数 -->
	<property name="maxIdle" value="10" />
	<!-- 每次释放连接的最大数目 -->
	<property name="numTestsPerEvictionRun" value="1024" />
	<!-- 释放连接的扫描间隔（毫秒） -->
	<property name="timeBetweenEvictionRunsMillis" value="30000" />
	<!-- 连接最小空闲时间 -->
	<property name="minEvictableIdleTimeMillis" value="1800000" />
	<!-- 连接空闲多久后释放, 当空闲时间>该值 且 空闲连接>最大空闲连接数 时直接释放 -->
	<property name="softMinEvictableIdleTimeMillis" value="10000" />
	<!-- 获取连接时的最大等待毫秒数,小于零:阻塞不确定的时间,默认-1 -->
	<property name="maxWaitMillis" value="1500" />
	<!-- 在获取连接的时候检查有效性, 默认false -->
	<property name="testOnBorrow" value="true" />
	<!-- 在空闲时检查有效性, 默认false -->
	<property name="testWhileIdle" value="true" />
	<!-- 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true -->
	<property name="blockWhenExhausted" value="false" />
</bean>
<!-- redis集群 -->
<bean id="jedisCluster" class="redis.clients.jedis.JedisCluster">
	<constructor-arg index="0">
		<set>
			<bean class="redis.clients.jedis.HostAndPort">
				<constructor-arg index="0" value="192.168.101.3"></constructor-arg>
				<constructor-arg index="1" value="7001"></constructor-arg>
			</bean>
			<bean class="redis.clients.jedis.HostAndPort">
				<constructor-arg index="0" value="192.168.101.3"></constructor-arg>
				<constructor-arg index="1" value="7002"></constructor-arg>
			</bean>
			<bean class="redis.clients.jedis.HostAndPort">
				<constructor-arg index="0" value="192.168.101.3"></constructor-arg>
				<constructor-arg index="1" value="7003"></constructor-arg>
			</bean>
			<bean class="redis.clients.jedis.HostAndPort">
				<constructor-arg index="0" value="192.168.101.3"></constructor-arg>
				<constructor-arg index="1" value="7004"></constructor-arg>
			</bean>
			<bean class="redis.clients.jedis.HostAndPort">
				<constructor-arg index="0" value="192.168.101.3"></constructor-arg>
				<constructor-arg index="1" value="7005"></constructor-arg>
			</bean>
			<bean class="redis.clients.jedis.HostAndPort">
				<constructor-arg index="0" value="192.168.101.3"></constructor-arg>
				<constructor-arg index="1" value="7006"></constructor-arg>
			</bean>
		</set>
	</constructor-arg>
	<constructor-arg index="1" ref="jedisPoolConfig"></constructor-arg>

 * */
