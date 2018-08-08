package com.util.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
/**
 * MD5加密
 * @author SuperWang
 * @version Create Time：2018年1月4日 上午11:25:11
 */
public class MD5Encryption {

	public static void main(String[] args) {
		String str = getStrToMD5("1234");
		System.out.println(0xFF & -84);
		System.out.println(Integer.toHexString(0xFF & -84));
		System.out.println(str);
	}
	
	public static String getStrToMD5(String str){
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		byte[] byteArray = messageDigest.digest();
		System.out.println(Arrays.toString(byteArray));
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		BigInteger bigInteger = new BigInteger(byteArray);
		System.out.println(bigInteger);
		System.out.println(bigInteger.toString(16));
		return md5StrBuff.toString();
	}
}
