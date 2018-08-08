package com.redis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
/**
 * 哨兵机制 主从
 * 由此证明 sentinel 需要手动回收连接  
 * @author SuperWang
 * @version 创建时间：2018年1月20日 上午2:45:48
 */
public class TestSentinel {

	/*
	 * 测试直接关闭连接池,无影响不会重新创建
	 */
	@Test
	public void testSentinel(){
		try {
			for (int i = 0; i < 500; i++) {
				//JedisSentinelPool pool = RedisFactory.getSentinlPool();
				//System.out.println(pool);
				//pool.close();
				TimeUnit.MILLISECONDS.sleep(100);
			}
			System.out.println("----------------------------------------------");
			TimeUnit.SECONDS.sleep(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 不添加close:
	 * 每次连接redis新创建一个连接,服务端redis的连接数不断增加,直到链接耗尽
	 * 顺序访问 服务器始终显示 为for循环的次数,及每次连接新创建一个连接
	 * 添加close:
	 * 每次服务端显示1次连接
	 */
	
	public void testSingleConnection() {
		try {
			for (int i = 0; i < 500; i++) {
				Jedis sentinl = RedisFactory.getSentinlConnect();
				System.out.println(sentinl.set("abc", "ss"));
				//RedisFactory.getCluster().close();
				//sentinl.close();
				TimeUnit.MILLISECONDS.sleep(100);
			}
			System.out.println("----------------------------------------------");
			TimeUnit.SECONDS.sleep(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 当for循环次数大于最大空闲连接数,则服务器连接数显示为 最大的空闲连接
	 * 当for循环次数小于最大空闲连接数,则服务器连接数显示为 for循环次数
	 * */
	public void testMultiConnection() {
		try {

			ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(500);
			for (int i = 0; i < 10; i++) {
			newFixedThreadPool.execute(() -> {
				//for (int a = 0; a < 10; a++) {
				System.out.println(RedisFactory.getSentinlConnect().set("abc", "ss"));
				//}
				try {
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			}
			System.out.println("----------------------------------------------");
			TimeUnit.SECONDS.sleep(10);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
