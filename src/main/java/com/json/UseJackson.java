package com.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson相对来说比较高效，在项目中主要使用Jackson进行JSON和Java对象转换，下面给出一些Jackson的JSON操作方法。
 * @author SuperWang
 * @version 创建时间：2018年5月31日 上午11:49:53
 */
public class UseJackson {

	/**
	 * JAVA对象转JSON[JSON序列化]
	 * @throws Exception
	 */
	public void ObjToJson() throws Exception {
		User user = new User();
		user.setName("小民");
		user.setEmail("xiaomin@sina.com");
		user.setAge(20);

		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		user.setBirthday(dateformat.parse("1996-10-01"));

		/** 
		 * ObjectMapper是JSON操作的核心，Jackson的所有JSON操作都是在ObjectMapper中实现。 
		 * ObjectMapper有多个JSON序列化的方法，可以把JSON字符串保存File、OutputStream等不同的介质中。 
		 * writeValue(File arg0, Object arg1)把arg1转成json序列，并保存到arg0文件中。 
		 * writeValue(OutputStream arg0, Object arg1)把arg1转成json序列，并保存到arg0输出流中。 
		 * writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。 
		 * writeValueAsString(Object arg0)把arg0转成json序列，并把结果输出成字符串。 
		 */
		ObjectMapper mapper = new ObjectMapper();

		//User类转JSON  
		//输出结果：{"name":"小民","age":20,"birthday":844099200000,"email":"xiaomin@sina.com"}  
		String json = mapper.writeValueAsString(user);
		System.out.println(json);

		//Java集合转JSON  
		//输出结果：[{"name":"小民","age":20,"birthday":844099200000,"email":"xiaomin@sina.com"}]  
		List<User> users = new ArrayList<User>();
		users.add(user);
		String jsonlist = mapper.writeValueAsString(users);
		System.out.println(jsonlist);
	}

	public void JsonToObj() throws Exception {
		String json = "{\"name\":\"小民\",\"age\":20,\"birthday\":844099200000,\"email\":\"xiaomin@sina.com\"}";

		/** 
		 * ObjectMapper支持从byte[]、File、InputStream、字符串等数据的JSON反序列化。 
		 */
		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.readValue(json, User.class);
		System.out.println(user);
	}

	/**
	 * 	JSON注解
			Jackson提供了一系列注解，方便对JSON序列化和反序列化进行控制，下面介绍一些常用的注解。
			@JsonIgnore 此注解用于属性上，作用是进行JSON操作时忽略该属性。
			@JsonFormat 此注解用于属性上，作用是把Date类型直接转化为想要的格式，如@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")。
			@JsonProperty 此注解用于属性上，作用是把该属性的名称序列化为另外一个名称，如把trueName属性序列化为name，@JsonProperty("name")。
	 * @author SuperWang
	 * @version 创建时间：2018年5月31日 上午11:53:17
	 */
	class User {
		private String name;
		private Integer age;
		private Date birthday;
		private String email;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public Date getBirthday() {
			return birthday;
		}

		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}
}
