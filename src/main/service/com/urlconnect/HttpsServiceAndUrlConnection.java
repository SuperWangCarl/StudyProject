package com.urlconnect;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpsServiceAndUrlConnection {

	@Test
	public void testPhoneInterface()throws Exception{
		String phone = getPhoneNumByInterface("14684753177");
		Thread.sleep(3000);
		System.out.println("phone" + phone);
	}
	 private String getPhoneNumByInterface(String joinNum) throws Exception{//根据活动码请求接口得到手机号
		 	String mac = encodeMd5(joinNum + "yoongooTicketByMobile");
		 	System.out.println(joinNum);
	    	PhoneNumInterfaceParams phoneNumInterfaceParams = new PhoneNumInterfaceParams(joinNum,mac);
	        String urlString = "https://117.71.39.22:28443/mps/activity/ticket/findMobile";
	        Gson gson = new GsonBuilder().create();
	        String params = gson.toJson(phoneNumInterfaceParams);
	        //ssl 验证  不需要可以去除
	        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
	        SSLContext sc = SSLContext.getInstance("TLS");
	        sc.init(null, trustAllCerts, new SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	        
	        String phoneNum = "";
	        URL url;  
	        try {  
	            url = new URL(urlString);  
	            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	            conn.setRequestProperty("Content-Type","application/json");  
	            conn.setRequestMethod("POST");  
	            conn.setDoOutput(true);  
	            conn.setDoInput(true);  
	            //设置超时时间
	            conn.setConnectTimeout(1000 * 2);  
	            conn.setReadTimeout(1000 * 2);
	            conn.getOutputStream().write(params.getBytes("utf8"));  
	            conn.getOutputStream().flush();  
	            conn.getOutputStream().close();  
	            conn.connect();            
	           
	            byte[] buffer = new byte[1024];  
	            StringBuffer sb = new StringBuffer();  
	            InputStream in = conn.getInputStream();  
	            int httpCode = conn.getResponseCode();  
	            System.out.println(in.available());  
	            while(in.read(buffer,0,1024) != -1) {  
	                sb.append(new String(buffer));  
	            }  
	            System.out.println("sb:" + sb.toString());
	            phoneNum = sb.toString();
	            in.close();  
	            System.out.println(httpCode); 
	            dataToFile(params,sb.toString(),httpCode);
	        }catch(Exception e) {  
	            e.printStackTrace();  
	        }  
	        return phoneNum;
	    }
	 static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	        }
	        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	        }
	        public X509Certificate[] getAcceptedIssuers() {
	            return null;
	        }
	    } };
	 public class NullHostNameVerifier implements HostnameVerifier {
	        public boolean verify(String arg0, SSLSession arg1) {
	            return true;
	        }
	    }
	class PhoneNumInterfaceParams {
		 private String code  ;
		 private String client_id = "33c1cfd3524386d9db1fe65a2ab29aba" ;
		 private String encrypt_id = "100411fc40c19a5b794bd467578781c5" ;
		 private String mac ;
	     public PhoneNumInterfaceParams(String code, String mac) {
			super();
			this.code = code;
			this.mac = mac;
		}
		public String getCode() {
	         return code;
	     }
	     public void setCode(String code) {
	         this.code = code;
	     }
		public String getClient_id() {
			return client_id;
		}
		public String getEncrypt_id() {
			return encrypt_id;
		}
		public String getMac() {
			return mac;
		}
	}
	public static String encodeMd5(String resultString) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        resultString = byteArrayToString(md.digest(resultString.getBytes("UTF-8")));
	    } catch (Exception localException) {
	    }
	    return resultString;
	}
	private static String byteArrayToString(byte[] b) {
	    StringBuffer resultSb = new StringBuffer();
	    for (int i = 0; i < b.length; i++) {
	        resultSb.append(byteToNumString(b[i]));
	    }
	    return resultSb.toString();
	}
	private static String byteToNumString(byte b) {
	    int _b = b;
	    if (_b < 0) {
	        _b += 256;
	    }
	    return String.valueOf(_b);
	}
	//将请求参数写入文本
    private void dataToFile(final String params,final String result,final int httpCode){
        new Thread(new Runnable(){
            public void run() {
                try{
                    java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyyMMdd");
                    java.util.Date date = new java.util.Date();
                    String timeStamp = simpleDateFormat.format(date);
                    java.io.File file = new java.io.File("F:/" + timeStamp + "_superwang.txt");
                    if (!file.exists())
                        file.createNewFile();
                    java.io.FileOutputStream out33 = new java.io.FileOutputStream(file, true);
                    StringBuilder sb = new StringBuilder();
                    sb.append("=======================================================\r\n");
                    sb.append(new Date()).append("\r\n").append(params).append("\r\n").append(result).append("\r\n").append(httpCode);
                    out33.write(sb.toString().getBytes("utf-8"));
                    out33.write("\r\n".getBytes("utf-8"));
                    out33.close();
                }catch(Exception e){
                	System.out.println("----------------------------------------------");
                	e.printStackTrace();
                }
            }
         }).start();
    }
}
