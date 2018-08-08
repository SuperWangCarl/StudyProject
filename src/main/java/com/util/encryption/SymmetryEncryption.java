package com.util.encryption;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * 对称加密
 * @author SuperWang
 * @version createtime：2018年1月4日 上午11:23:35
 * info 
 * 对称加密是最快速、最简单的一种加密方式，加密（encryption）与解密（decryption）用的是同样的密钥（secret key）。
 * 对称加密有很多种算法，由于它效率很高，所以被广泛使用在很多加密协议的核心当中。
	对称加密通常使用的是相对较小的密钥，一般小于256 bit。因为密钥越大，加密越强，但加密与解密的过程越慢。
	如果你只用1 bit来做这个密钥，那黑客们可以先试着用0来解密，不行的话就再用1解；但如果你的密钥有1 MB大，黑客们可能永远也无法破解，
	但加密和解密的过程要花费很长的时间。密钥的大小既要照顾到安全性，也要照顾到效率，是一个trade-off。
	2000年10月2日，美国国家标准与技术研究所（NIST--American National Institute of Standards and Technology）
	选择了Rijndael算法作为新的高级加密标准（AES--Advanced Encryption Standard）。.NET中包含了Rijndael算法，类名叫RijndaelManaged，下面举个例子。
 */
public class SymmetryEncryption {
	/**
	 * 对称加密解密：自动获取密钥
	 * @throws Exception
	 */
	/* 
     * 对称加密 
     */  
    private static void secretEncrypt() throws Exception {  
        //使用Cipher的实例  
        Cipher cipher =Cipher.getInstance("AES");  
          
        //得到加密的钥匙  
        SecretKey key =KeyGenerator.getInstance("AES").generateKey();  
          
        //初始化加密操作,传递加密的钥匙  
        cipher.init(Cipher.ENCRYPT_MODE,key);  
          
        //将加密的钥匙写入secretKey.key文件中  
        FileOutputStream fosKey=new FileOutputStream("secretKey.key");  
        ObjectOutputStream oosSecretKey =new ObjectOutputStream(fosKey);  
        oosSecretKey.writeObject(key);  
        oosSecretKey.close();  
        fosKey.close();  
           
         //将加密的内容传递进去，返回加密后的二进制数据  
        byte [] results =cipher.doFinal("哈哈哈哈哈".getBytes());  
          
        //将加密后的二进制数据写入到secretContent.dat文件中  
        FileOutputStream fosData=new FileOutputStream("secretContent.dat");  
        fosData.write(results);  
        fosData.close();  
    }  
      
    /* 
     * 对称解密 
     */  
    private static void secretDecrypt() throws Exception{  
        Cipher cipher =Cipher.getInstance("AES");  
          
        //获取文件中的key进行解密  
        FileInputStream fisKey=new FileInputStream("secretKey.key");  
        ObjectInputStream oisKey =new ObjectInputStream(fisKey);  
        Key key =(Key)oisKey.readObject();  
        oisKey.close();  
        fisKey.close();  
          
        //初始化解密操作,传递加密的钥匙  
        cipher.init(Cipher.DECRYPT_MODE,key);  
          
        //获取文件中的二进制数据  
        FileInputStream fisDat=new FileInputStream("secretContent.dat");  
        //获取数据第一种方式  
        byte [] src=new byte [fisDat.available()];  
        int len =fisDat.read(src);  
        int total =0;  
        while(total<src.length){  
            total +=len;  
            len=fisDat.read(src,total,src.length-total);  
        }  
        //执行解密  
        byte [] result=cipher.doFinal(src);  
        fisDat.close();  
        System.out.println(new String(result));  
          
//      读文件中的数据第二种方式  
//      ByteArrayOutputStream baos =new ByteArrayOutputStream();  
//      copyStream(fisDat, baos);  
//      byte [] result=cipher.doFinal(baos.toByteArray());  
//      fisDat.close();  
//      baos.close();  
    }  
      
//  private static void copyStream(InputStream ips,OutputStream ops) throws Exception{  
//      byte [] buf =new byte[1024];  
//      int len=ips.read(buf);  
//      while(len!=-1){  
//          ops.write(buf,0,len);  
//          len  =ips.read(buf);  
//      }  
//  }  
    
    /**
     * 基于口令的对称加密与解密
		系统自动生成的Key不容易记忆，我们可以使用我们容易记忆的口令同过java自带的一个工具将它转换成Key，在解密的时候我们就可以通过口令进行解密
     */
    /* 
     * 基于口令的对称加密 
     */  
    private static void secretEncryptByKey() throws Exception {  
        //实例化工具  
        Cipher cipher2=Cipher.getInstance("PBEWithMD5AndDES");  
          
        //使用该工具将基于密码的形式生成Key  
        SecretKey key2=SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec("123".toCharArray()));  
        PBEParameterSpec parameterspec=new PBEParameterSpec(new byte[]{1,2,3,4,5,6,7,8},1000);  
          
        //初始化加密操作，同时传递加密的算法  
        cipher2.init(Cipher.ENCRYPT_MODE,key2,parameterspec);  
          
         //将要加密的数据传递进去，返回加密后的数据  
        byte [] results =cipher2.doFinal("哈哈哈哈哈".getBytes());  
          
        //将加密后的数据写入到文件中  
        FileOutputStream fosData=new FileOutputStream("zxx.dat");  
        fosData.write(results);  
        fosData.close();  
    }  
      
    /* 
     * 基于口令的对称解密 
     */  
    private static void secretDecryptByKey() throws Exception{  
        Cipher cipher2=Cipher.getInstance("PBEWithMD5AndDES");  
        SecretKey key2=SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec("123".toCharArray()));  
        PBEParameterSpec parameterspec=new PBEParameterSpec(new byte[]{1,2,3,4,5,6,7,8},1000);  
        cipher2.init(Cipher.DECRYPT_MODE,key2,parameterspec);  
        FileInputStream fisDat=new FileInputStream("zxx.dat");  
        byte [] src=new byte [fisDat.available()];  
        int len =fisDat.read(src);  
        int total =0;  
        while(total<src.length){  
            total +=len;  
            len=fisDat.read(src,total,src.length-total);  
        }  
        byte [] result=cipher2.doFinal(src);  
        fisDat.close();  
        System.out.println(new String(result));  
    }  
}
