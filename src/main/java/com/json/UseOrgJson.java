package com.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 * 需要先创建对象
 * @author SuperWang
 * @version 创建时间：2018年5月31日 上午10:51:07
 */
public class UseOrgJson {
	// Array <---> JSON	
	@Test
	public void array2JSON() throws Exception{
		String[] arr = {"tom", "jack", "jerry"};
		JSONArray json = new JSONArray(arr);
		System.out.println(json);
		
		for (int i = 0; i < json.length(); i++) {
			System.out.println(json.get(i));
			System.out.println(json.get(i).getClass());
			System.out.println("----------------------------------------------");
		}
	}	// array2JSON
	
	// Collection <---> JSON.
		@Test
		public void collection2JSON() throws Exception{
			ArrayList<String> arrList = new ArrayList<String>();
			arrList.add("aa");
			arrList.add("aa");
			arrList.add("aa");
			JSONArray json = new JSONArray(arrList);
			System.out.println(json);
			
			for (int i = 0; i < json.length(); i++) {
				System.out.println(json.get(i));
				System.out.println(json.get(i).getClass());
				System.out.println("----------------------------------------------");
			}
		}	// collection2JSON
		
		// -------------------------------------------------------------------------------
		// Object <---> JSON
		@Test
		public void object2JSON() throws Exception{
			Dept dept = new Dept(10, "SALES", "BOSTON");
			JSONObject json = new JSONObject(dept);
			System.out.println(json);
			/*
			 * 	get()取值不正确会抛出异常，必须用try catch或者throw包起
				opt()取值不正确则会试图进行转化或者输出友好值，不会抛出异常
			 */
			System.out.println(json.get("dname"));
			System.out.println(json.opt("loc"));
			System.out.println(json.length());
			
			
			
			/*JSONArray js = JSONArray.fromObject(dept);
			System.out.println(js);
			Object[] array = js.toArray();
			for (Object obj : array) {
				System.out.println(obj);
			}*/
			
		} // object2JSON
}