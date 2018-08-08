package com.util.encryption;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;

/**
 * 非对称加密
 * @author SuperWang
 * @version createtime：2018年1月4日 上午11:23:19
 * info 非对称加密为数据的加密与解密提供了一个非常安全的方法，它使用了一对密钥，公钥（public key）和私钥（private key）。
 * 私钥只能由一方安全保管，不能外泄，而公钥则可以发给任何请求它的人。非对称加密使用这对密钥中的一个进行加密，而解密则需要另一个密钥。
 * 比如，你向银行请求公钥，银行将公钥发给你，你使用公钥对消息加密，那么只有私钥的持有人--银行才能对你的消息解密。与对称加密不同的是，
 * 银行不需要将私钥通过网络发送出去，因此安全性大大提高。
 * 目前最常用的非对称加密算法是RSA算法，是Rivest, Shamir, 和Adleman于1978年发明，他们那时都是在MIT。.NET中也有RSA算法，请看下面的例子：
 */
public class NoSymmetryEncryption {

	/**
	 * 非对称加密解密：
	 * 非对称加密是公钥加密，私钥来解密，这个个人做用的少一点，主要针对于大型的网站大型的企业
	 */
	/* 
	 * 公钥加密 
	 */  
	private static void PublicEnrypt()throws Exception {  
	    Cipher cipher =Cipher.getInstance("RSA");  
	    //实例化Key  
	    KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");  
	    //获取一对钥匙  
	    KeyPair keyPair=keyPairGenerator.generateKeyPair();  
	    //获得公钥  
	    Key publicKey=keyPair.getPublic();  
	    //获得私钥   
	    Key privateKey=keyPair.getPrivate();  
	    //用公钥加密  
	    cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
	    byte [] result=cipher.doFinal("传智播客".getBytes("UTF-8"));  
	    //将Key写入到文件  
	    saveKey(privateKey,"zxx_private.key");  
	    //加密后的数据写入到文件  
	    saveData(result,"public_encryt.dat");  
	}  
	  
	/* 
	 * 私钥解密 
	 */  
	private static void privateDecrypt() throws Exception {  
	    Cipher cipher=Cipher.getInstance("RSA");  
	    //得到Key  
	    Key privateKey=readKey("zxx_private.key");  
	    //用私钥去解密  
	    cipher.init(Cipher.DECRYPT_MODE, privateKey);  
	    //读数据源  
	    byte [] src =readData("public_encryt.dat");  
	    //得到解密后的结果  
	    byte[] result=cipher.doFinal(src);  
	    //二进制数据要变成字符串需解码  
	    System.out.println(new String(result,"UTF-8"));  
	}  
	  
	private static void saveData(byte[] result, String fileName) throws Exception {  
	    // TODO Auto-generated method stub  
	    FileOutputStream fosData=new FileOutputStream(fileName);  
	    fosData.write(result);  
	    fosData.close();  
	}  
	public static void saveKey(Key key,String fileName)throws Exception{  
	    FileOutputStream fosKey=new FileOutputStream(fileName);  
	    ObjectOutputStream oosSecretKey =new ObjectOutputStream(fosKey);  
	    oosSecretKey.writeObject(key);  
	    oosSecretKey.close();  
	    fosKey.close();  
	}  
	private static Key readKey(String fileName) throws Exception {  
	    FileInputStream fisKey=new FileInputStream(fileName);  
	    ObjectInputStream oisKey =new ObjectInputStream(fisKey);  
	    Key key=(Key)oisKey.readObject();  
	    oisKey.close();  
	    fisKey.close();  
	    return key;  
	}  
	private static byte[] readData(String filename) throws Exception {  
	    FileInputStream fisDat=new FileInputStream(filename);  
	    byte [] src=new byte [fisDat.available()];  
	    int len =fisDat.read(src);  
	    int total =0;  
	    while(total<src.length){  
	        total +=len;  
	        len=fisDat.read(src,total,src.length-total);  
	    }  
	    fisDat.close();  
	    return src;  
	}  
}
