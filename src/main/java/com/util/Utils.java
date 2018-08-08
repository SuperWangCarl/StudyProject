package com.util;

import java.util.UUID;

public class Utils {

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

	

	public static String getUserIdMod(String user_id) {
		int mod = Math.abs(user_id.hashCode() % 10);
		if (mod < 0 || mod > 9) {
			mod = 0;
		}
		return mod + "";
	}
	public static String getUserIdMod(String user_id,int much) {
		int mod = Math.abs(user_id.hashCode() % much);
		return mod + "";
	}

}
