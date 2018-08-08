package com.util.epg;

import java.io.ByteArrayOutputStream;

public class Utils {

	public static String getOriginalInfo(String key, String user_message) {
		if (key == null)
			return null;
		if (user_message == null)
			return null;
		if (!key.contains(":"))
			return null;
		String result = "";
		String[] keys = key.split(":");
		int[] key2 = { Integer.valueOf(keys[0]), Integer.valueOf(keys[1]) };
		try {
			char[] use = user_message.toCharArray();
			for (int i = 0; i < user_message.length(); i++) {
				if ((i + 1) % 2 == 1)
					use[i] = (char) (use[i] + key2[1]);
				else
					use[i] = (char) (use[i] - key2[1]);
			}
			for (int i = 0; i < use.length; i++)
				result += use[i];
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		String m1 = result.substring(0, result.length() - key2[0]);
		String m2 = result.substring(result.length() - key2[0], result.length());
		int s = 0, e = m1.length() - 1;
		char[] us = m1.toCharArray();
		while (s < e) {
			char temp = us[e];
			us[e] = us[s];
			us[s] = temp;
			s++;
			e--;
		}
		m1 = "";
		for (int i = 0; i < us.length; i++)
			m1 += us[i];
		result = m2 + m1;
		return result;
	}

	public static String byte2HexString(byte[] data) {
		StringBuffer checksumSb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			String hexStr = Integer.toHexString(0x00ff & data[i]);
			if (hexStr.length() < 2) {
				checksumSb.append("0");
			}
			checksumSb.append(hexStr);
		}
		return checksumSb.toString();
	}

	public static byte[] hexStr(String digits) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < digits.length(); i += 2) {
			char c1 = digits.charAt(i);
			if ((i + 1) >= digits.length())
				throw new IllegalArgumentException();
			char c2 = digits.charAt(i + 1);
			byte b = 0;
			if ((c1 >= '0') && (c1 <= '9'))
				b += ((c1 - '0') * 16);
			else if ((c1 >= 'a') && (c1 <= 'f'))
				b += ((c1 - 'a' + 10) * 16);
			else if ((c1 >= 'A') && (c1 <= 'F'))
				b += ((c1 - 'A' + 10) * 16);
			else
				throw new IllegalArgumentException();
			if ((c2 >= '0') && (c2 <= '9'))
				b += (c2 - '0');
			else if ((c2 >= 'a') && (c2 <= 'f'))
				b += (c2 - 'a' + 10);
			else if ((c2 >= 'A') && (c2 <= 'F'))
				b += (c2 - 'A' + 10);
			else
				throw new IllegalArgumentException();
			baos.write(b);
		}
		return (baos.toByteArray());
	}

}
