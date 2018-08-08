package com.redis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;

import redis.clients.jedis.JedisCluster;
/**
 * 单机版
 * @author SuperWang
 * @version 创建时间：2018年6月5日 下午9:55:36
 */
public class TestRedis {

	@Test
	public void testItemType(){
		String entrance = "tjw_1_ss_zy";
		System.out.println(entrance.substring(entrance.lastIndexOf("_")+1, entrance.length()));
	}
	public void testTreeMap(){
		
		Map<Long,String> treeMap = new TreeMap();
		treeMap.put(4L, "1231");
		treeMap.put(6L, "1232");
		treeMap.put(3L, "123q");
		treeMap.put(20L, "123f");
		treeMap.put(15L, "123g");
		System.out.println(treeMap);
		Set<Entry<Long, String>> entrySet = treeMap.entrySet();
		Iterator<Entry<Long, String>> it = entrySet.iterator();
		while(it.hasNext()){
			Entry<Long, String> next = it.next();
			System.out.println(next.getKey() + "..." + next.getValue());
		}
	}
	public void testHashRedis(){
		JedisCluster cluster = RedisFactory.getCluster();
		Map<String, String> map = new HashMap<String,String>();
		map.put("super", "a");
		map.put("wang", "1");
		map.put("hao", "2");
		String user_id = "2";
		String item_id = "6";
		String key = user_id + "==" + item_id;
		cluster.hmset(key, map);
		cluster.expire(key, 1000);
		
		//System.out.println(cluster.getClusterNodes());
		//System.out.println("----------------------------------------------");
		System.out.println(RedisFactory.keysCount("2==*"));
		System.out.println("----------------------------------------------");
		Map<String, String> hgetAll = cluster.hgetAll("222");
		
		
		System.out.println(hgetAll.size());
		
	}
}
