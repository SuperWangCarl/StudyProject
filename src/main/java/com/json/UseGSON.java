package com.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

/**
 * 谷歌的开源类库 兼并了net 和 org 的大部分功能 对于多重嵌套也有很好的兼容
 * Gson的两个基础方法
	toJson();
	fromJson();
 * @author SuperWang
 * @version 创建时间：2018年5月31日 上午11:19:34
 */
public class UseGSON {

	/**
	 * Gson的创建方式一：直接new Gson对象
	 */
	public void baseCreateObj() {
		// 使用new方法
		Gson gson = new Gson();
		//=====================================================Obj 和 json 的互相转换
		Dept dept = new Dept(10, "SALES", "BOSTON");
		// toJson 将bean对象转换为json字符串
		String jsonStr = gson.toJson(dept, Dept.class);
		// fromJson 将json字符串转为bean对象
		Dept user = gson.fromJson(jsonStr, Dept.class);

		//=====================================================List 和 json 的互相转换
		ArrayList<Object> list = new ArrayList<>();
		// **序列化List**
		String jsonStr2 = gson.toJson(list);
		// **反序列化成List时需要使用到TypeToken getType()**
		List<Dept> retList = gson.fromJson(jsonStr2, new TypeToken<List<Dept>>() {
		}.getType());

		//=====================================================List 和 json 的互相转换
		// ** map型的 等号键值对 转换为Map
		Map<String, Object> map = new HashMap<String, Object>();
		map = gson.fromJson(map.toString(), Map.class);
		System.out.println(map);
	}

	/**
	 * Gson的创建方式二：使用GsonBuilder
	 * 使用new Gson()，此时会创建一个带有默认配置选项的Gson实例，如果不想使用默认配置，那么就可以使用GsonBuilder。
	 * 使用GsonBuilder创建Gson实例的步骤：
		首先创建GsonBuilder,然后调用GsonBuilder提供的各种配置方法进行配置，
		最后调用GsonBuilder的create方法，将基于当前的配置创建一个Gson实例。
	 */
	public void baseGsonBuilder() {
		//serializeNulls()是GsonBuilder提供的一种配置，当字段值为空或null时，依然对该字段进行转换
		//Gson gson = new GsonBuilder().serializeNulls().create(); 

		//基础配置
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation() //不对没有用@Expose注解的属性进行操作
				.enableComplexMapKeySerialization() //当Map的key为复杂对象时,需要开启该方法
				.serializeNulls() //当字段值为空或null时，依然对该字段进行转换
				.setDateFormat("yyyy-MM-dd HH:mm:ss:SSS") //时间转化为特定格式
				.setPrettyPrinting() //对结果进行格式化，增加换行
				.disableHtmlEscaping() //防止特殊字符出现乱码
				//.registerTypeAdapter(User.class,new UserAdapter()) //为某特定对象设置固定的序列或反序列方式，自定义Adapter需实现JsonSerializer或者JsonDeserializer接口
				.create();
	}

	/**
	 * Gosn对复杂Map的处理时需要用到其中的 enableComplexMapKeySerialization() 配置：
	 * 注意：如果Map的key为String，则可以不使用GsonBuilder的enableComplexMapKeySerialization()方法，或者直接new Gson();
	 */
	public void mapToGson() {
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create(); //开启复杂处理Map方法
		Map<List<Dept>, String> map = new HashMap<List<Dept>, String>();
		// TODO 向map中添加数据
		String jsonStr = gson.toJson(map); //toJson
		Map<List<Dept>, String> resultMap = gson.fromJson(jsonStr, new TypeToken<Map<List<Dept>, String>>() {
		}.getType()); //fromJson
	}

	//Gson的注解：
	//实体类
	//@Expose注解
	class User {
		@Expose
		private String firstName;

		@Expose(serialize = false)
		private String lastName;

		@Expose(deserialize = false)
		private String emailAddress;

		private String password;
	}

	/*
		@Expose中serialize和deserialize属性是可选的，默认两个都为true。
		如果serialize为true，调用toJson时会序列化该属性，
		如果deserialize为true，调用fromJson生成Java对象时不会进行反序列化。
		注意：如果采用new Gson()方式创建Gson，@Expose没有任何效果。需要使用 gsonBuilder.excludeFieldsWithoutExposeAnnotation()方法。
		@SerializedName注解    能指定该字段在序列化成json时的名称
	*/
	@SerializedName("w")
	private int width;
}
