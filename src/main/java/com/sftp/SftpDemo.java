package com.sftp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 
 * @author SuperWang
 * @version createtime：2018年1月4日 上午10:22:18
 */
public class SftpDemo {

	 
	/**
	 * @param args
	 */
    public static void main(String[] args) throws Exception{
        JSch jsch = new JSch();
        
        Session session = jsch.getSession("root", "115.28.192.49");
        session.setPassword("Xiejian123");
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        
        ChannelSftp channelSftp = (ChannelSftp)session.openChannel("sftp");
        channelSftp.connect();
        
        try{
        	String sourceDir = "/root/test/";
        	Date date = new Date();
        	String parentDir = "/root/" + new SimpleDateFormat("yyyyMM").format(date) + "/";
        	if (!openDir(parentDir, channelSftp)) {
        		channelSftp.mkdir(parentDir);
        	}
        	parentDir += new SimpleDateFormat("dd").format(date) + "/";
        	if (!openDir(parentDir, channelSftp)) {
        		channelSftp.mkdir(parentDir);
        	}
        	        	
	        channelSftp.rename(sourceDir + "3.txt", parentDir + "3.txt");
        }
        finally{
            channelSftp.quit();
            session.disconnect();
        }
    }
    
    private static boolean openDir(String dir, ChannelSftp channelSftp) {
    	try {
    		channelSftp.cd(dir);
    		return true;
    	} catch (Exception e) {
    		return false;
		}
    }
}
