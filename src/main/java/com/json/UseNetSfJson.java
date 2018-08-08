package com.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 不需要先创建对象 直接调用静态方法
 * @author SuperWang
 * @version 创建时间：2018年5月31日 上午10:49:58
 */
public class UseNetSfJson {
	// Array <---> JSON	
	
	public void array2JSON(){
		String[] arr = {"tom", "jack", "jerry"};
		JSONArray json = JSONArray.fromObject(arr);
		System.out.println(json);
		Object arr1 = JSONArray.toArray(json);
		for (Object obj : json) {
			System.out.println(obj);
		}
	}	// array2JSON
	
	// Collection <---> JSON.

	public void collection2JSON(){
		ArrayList<String> arrList = new ArrayList<String>();
		arrList.add("aa");
		arrList.add("aa");
		arrList.add("aa");
		JSONArray json = JSONArray.fromObject(arrList);
		System.out.println(json);
		Collection collection = JSONArray.toCollection(json);
		System.out.println(collection);
	}	// collection2JSON
	
	// -------------------------------------------------------------------------------
	// Object <---> JSON
	@Test
	public void object2JSON(){
		
		Dept dept = new Dept(10, "SALES", "BOSTON");
		//Object 转为 json对象
		JSONObject json = JSONObject.fromObject(dept);
		System.out.println(json);
		
		//json对象转为json数组
		JSONArray js = JSONArray.fromObject(dept);
		System.out.println(js);
		
		//json数组转为  数组 (数组中元素是jsonObject对象)
		Object[] array = js.toArray();
		for (Object obj : array) {
			System.out.println(obj.getClass());
		}
		
	} // object2JSON
	
	// Map <---> JSON
	@Test
	public void map2JSON(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", "tom");
		map.put("age", 20);
		map.put("birthdate", new Date());
		
		JSONObject json = JSONObject.fromObject(map);
		System.out.println(json);
		System.out.println("----------------------------------------------");
		Object obj = json.get("birthdate");
		System.out.println(obj);
		System.out.println("----------------------------------------------");
		JSONObject fromObject = JSONObject.fromObject(obj);
		System.out.println("----------------------------------------------");
		Date d = (Date) JSONObject.toBean(fromObject, Date.class);
		System.out.println(d);
		System.out.println("----------------------------------------------");
		Map m = (Map) JSONObject.toBean(json,Map.class);
		System.out.println(m);
	}	// map2JSON
	
}