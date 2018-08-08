package com.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class StringAndObject {

	public static String objToStr(Object obj) {
		//Object 序列化成string  
		String str = null;
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);) {

			out.writeObject(obj);
			str = byteArrayOutputStream.toString("ISO-8859-1");//必须是ISO-8859-1  
			//System.out.println("===>字符转数组完毕");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static Object strToObj(String str) throws Exception {
		Object obj = null;
		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
				ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);) {
			obj = objectInputStream.readObject();
			//System.out.println(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//String 的反序列化  
		return obj;
	}

}
