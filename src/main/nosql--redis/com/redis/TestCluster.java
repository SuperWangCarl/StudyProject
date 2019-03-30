package com.redis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
/**
 * 高可用集群版
 * 由此证明cluster会自动回收连接
 * @author SuperWang
 * @version 创建时间：2018年1月20日 上午2:45:20
 */
public class TestCluster {

	/*
	 * 测试顺序连接时:
	 * 不添加close
	 * 顺序访问 服务器始终显示 增加了一个连接 及每次连接之后都会回收  
	 * 程序结束连接关闭  
	 * 程序连接时 使用占用一个连接 (小于最大空闲连接)
	 * 服务器连接数显示为 1
	 * 
	 * 添加close报错
	 */
	
	public void testSingleConnection() {
		try {
			TimeUnit.SECONDS.sleep(10);
			for (int i = 0; i < 500; i++) {
				System.out.println(RedisFactory.getCluster().set("abc", "ss"));
				//RedisFactory.getCluster().close();
				TimeUnit.MILLISECONDS.sleep(10);
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
	@Test
	public void testMultiConnection() {
		try {

			ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(500);
			for (int i = 0; i < 10; i++) {
			newFixedThreadPool.execute(() -> {
				//for (int a = 0; a < 10; a++) {
				System.out.println(RedisFactory.getCluster().set("abc", "ss"));
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
