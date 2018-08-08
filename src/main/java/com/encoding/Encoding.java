package com.encoding;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class Encoding {

	public static void main(String[] args) throws Exception {
		String name = "恋家";
		
	}
	
	public String url(String str) throws Exception{
		str = URLEncoder.encode(str,"utf-8");
		System.out.println(str);
		str = URLDecoder.decode(str,"iso-8859-1");
		System.out.println(str);
		str = URLEncoder.encode(str, "iso-8859-1");
		System.out.println(str);
		str = URLDecoder.decode(str,"utf-8");
		System.out.println(str);
		return str;
	}
}
