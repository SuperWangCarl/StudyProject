package com.util.encryption;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * 数字签名
 * @author SuperWang
 * @version Create Time：2018年1月4日 上午11:47:24
 * info
 * 数字签名：
 * 数字签名的基础是公钥和私钥的非对称加密，发送者使用私钥加密的消息摘要(签名)，接收者使用公钥解密消息摘要以验证签名是否是某个人。
 * 要证明这段数据是你发过来的，并且没有被别人改过，这就需要用到数字签名，首先我们对整个文档进行md5加密得到16个字节，然后把消息摘要和文档发过去，
 * 解密者首先对发过来的文档进行解密，解密后得到一个摘要(md5)，对接收的文档进行md5加密,得到的md5结果匹配解密后的摘要，如果匹配成功的话证明没有修改过，我们使用Signature进行签名
 */
public class DigitallySignedEncryption {

	/*  
	 * 使用私钥签名  
	 */    
	private static void sign()throws Exception {    
	    //实例化Key     
	    KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");    
	    //获取一对钥匙     
	    KeyPair keyPair=keyPairGenerator.generateKeyPair();    
	    //获得公钥     
	    PublicKey publicKey=keyPair.getPublic();    
	    //获得私钥      
	    PrivateKey privateKey=keyPair.getPrivate();    
	      
	    //数字签名  
	    Signature signature =Signature.getInstance("SHA1withRSA");  
	    signature.initSign(privateKey);//用私钥签名  
	    signature.update("这里签名".getBytes());//对怎样的数据进行签名  
	    byte [] sign=signature.sign();  //获取签名的结果  
	      
	    //保存公钥并写入文件中   
	    saveKey(publicKey,"zxx_private.key");    
	    //将签名后的数据写入到文件     
	    saveData(sign,"public_encryt.dat");    
	}  
	    
	/*  
	 * 公钥解密  
	 */    
	private static void verify() throws Exception {    
	    Signature signture =Signature.getInstance("SHA1withRSA");  
	    //获取到公钥  
	    PublicKey publicKey=(PublicKey)readKey("zxx_private.key");  
	    //初始化校验  
	    signture.initVerify(publicKey);  
	    //初始化签名对象  
	    signture.update("这里签名".getBytes());  
	    //读数据源     
	    byte [] sign =readData("public_encryt.dat");    
	    //返回匹配结果  
	    boolean isYouSigned=signture.verify(sign);  
	    //如果返回数据为true则数据没有发生修改，否则发生修改  
	    System.out.println(isYouSigned);  
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
