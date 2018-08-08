package com.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

public class DownloadFileUtil {
	
	public static void main(String[] args) throws IOException {
		downloadFile("ftp://172.25.116.7//opt/wacos/CTMSData/dispatch/smg_dispatch/2018/01/30/14/NMTVLTZQ_smp_1472880.xml","F:\\");
	}

	public static String downloadFile(String urlStr,String filePath) throws IOException{
		URL url = new URL(urlStr);
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(3*1000);
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		InputStream inputStream = conn.getInputStream();
		String cont = IOUtils.toString(inputStream,"utf-8");
		System.out.println(cont);
		byte[] getData = readInputStream(inputStream);
		//文件保存位置  
        File saveDir = new File(filePath);
        if(!saveDir.exists()){  
            saveDir.mkdir();  
        } 
        String fileName = System.currentTimeMillis()+".xml";
        File file = new File(saveDir+File.separator+fileName);      
        FileOutputStream fos = new FileOutputStream(file);       
        fos.write(getData);
        if(fos!=null){  
            fos.close();    
        }  
        if(inputStream!=null){  
            inputStream.close();  
        }  
        System.out.println("info:"+url+" download success");
		return "";
	}
	
	public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    } 
}
