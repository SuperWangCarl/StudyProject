package com.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
	/**
	 * 将日期格式转换成yyyyMMdd字符串
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		if (date == null){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	public static String objToString(Object obj) {
		String str = "";
		if (obj == null) {
			return str;
		}
		str = obj.toString();
		return str;
	}
}
