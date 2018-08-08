package com.cache.ehcache;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheDemo {
	static String str = "abc";          
	public static void main(String[] args) throws Exception {
		//创建缓存 管理器
		CacheManager manager = CacheManager.create();
		//或得名为 的缓存
		//创建Element对象
		//添加到缓存
    }

	private static void demo() {
		//CacheManager主要的缓存管理类，一般一个应用为一个实例，如下
	    //CacheManager.create();也可以使用new CacheManager的方式创建
	    //默认的配置文件为ehcache.xml文件，也可以使用不同的配置：CacheManager manager = new CacheManager("src/config/other.xml");     
		createCacheByDefaultXml();     
		//或者直接创建Cache
		createCacheByParamter();     
		//删除cache
		removeCache();     
		//在使用ehcache后，需要关闭
		closeCache();   
		//创建
		createElement();     
		//update
		updateElement();     
		//get Serializable
		getSerializable();     
		//get non serializable
		getNonSerializable();     
		//remove
		removeElement();
	}

	private static void removeElement() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("sampleCache1");
		Element element = new Element("key1", "value1");
		cache.remove("key1");
	}

	private static void getNonSerializable() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("sampleCache1");
		Element element = cache.get("key1");
		Object value = element.getObjectValue();
	}

	private static void getSerializable() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("sampleCache1");
		Element element = cache.get("key1");
		Serializable value = element.getValue();
	}

	private static void updateElement() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("sampleCache1");
		cache.put(new Element("key1", "value1"));
		//This updates the entry for "key1"
		cache.put(new Element("key1", "value2"));
	}

	private static void createElement() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("sampleCache1");
		Element element = new Element("key1", "value1");
		cache.put(element);
	}

	private static void closeCache() {
		CacheManager.getInstance().shutdown();
	}

	private static void removeCache() {
		CacheManager singletonManager = CacheManager.create();
		singletonManager.removeCache("sampleCache1");
	}

	private static void createCacheByParamter() {
		CacheManager singletonManager = CacheManager.create();
		Cache memoryOnlyCache = new Cache("testCache", 5000, false, false, 5, 2);
		singletonManager.addCache(memoryOnlyCache);
		Cache test = singletonManager.getCache("testCache");
	}

	private static void createCacheByDefaultXml() {
		//缓存的创建，采用自动的方式
		 
		CacheManager singletonManager = CacheManager.create();
		singletonManager.addCache("testCache");
		Cache test = singletonManager.getCache("testCache");
	}
}
