package com.ftp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * ClassName:FtpUtils
 *
 * @author   Lishiguang
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-2-27		上午11:27:03
 *
 * @see 	 
 */
public class FtpUtils{

	/**
	 * downFtpFileForUrl:(通过ftp url 下载ftp文件)
	 *
	 * @param ftpUrl
	 * @return 保存到本地的路径
	 * @throws Exception 
	*/
	public static int downFtpFileForUrl(String ftpUrl,String localPath,String type)throws Exception {

		String url = "", username = "", password = "", remotePath = "", fileName = "";
		int port = 21;
		// 截取FTP地址
		final String ftpFlag = "ftp://";
		if (ftpUrl != null && ftpUrl.length() > 0
				&& ftpUrl.toLowerCase().contains(ftpFlag)) {
			// 首先去掉FTP
			final String cutedFtp = ftpUrl.substring(ftpUrl.indexOf(ftpFlag)
					+ ftpFlag.length());
			// 首先截取ip和端口
			String ipAndPort = "";
			if (cutedFtp.indexOf("/") != -1) {
				ipAndPort = cutedFtp.substring(cutedFtp.indexOf("@")+1,cutedFtp.indexOf("/"));
			} else {
				ipAndPort = cutedFtp;
			}
			// 开始获取ip和端口
			if (!"".equals(ipAndPort)) {
				if (ipAndPort.indexOf(":") != -1) {
					url = ipAndPort.substring(0, ipAndPort.indexOf(":"));
					String strPort = ipAndPort.substring(
							ipAndPort.indexOf(":") + 1, ipAndPort.length());
					if (strPort != null)
						port = Integer.parseInt(strPort);
				} else {
					// 如果没有端口只获取IP
					url = ipAndPort;
				}
			}

			// 截取ftp文件路径和文件名
			String fileNameAndPath = "";
			if (cutedFtp.indexOf("/") != -1) {
				fileNameAndPath = cutedFtp.substring(cutedFtp.indexOf("/") + 1,
						cutedFtp.length());
			} else {
				fileNameAndPath = "";
			}
			// 开始获取ftp文件路径和文件名
			if (!"".equals(ipAndPort)) {
				if (fileNameAndPath.indexOf("/") != -1) {
					remotePath = fileNameAndPath.substring(0,
							fileNameAndPath.lastIndexOf("/"));
					fileName = fileNameAndPath.substring(
							fileNameAndPath.lastIndexOf("/") + 1,
							fileNameAndPath.length());
				} else {
					fileName = fileNameAndPath;
				}
			}

			// 获取FTP账号密码
			String userAndPass = cutedFtp.substring(0,cutedFtp.indexOf("@"));
			/*if (type != null && (type == 2 || type == 3)) {
				userAndPass = Vutil.getPropertieValueForLocal("group_ftp_user_password");
			} else {
				userAndPass = Vutil.getPropertieValueForLocal("local_ftp_user_password");
			}*/
			// 开始截取FTP账号密码
			
			if (!"".equals(userAndPass)) {
				if (userAndPass.indexOf(":") != -1) {
					username = userAndPass.substring(0,userAndPass.indexOf(":"));
					password = userAndPass.substring(userAndPass.indexOf(":") + 1, userAndPass.length());
				} else {
					username = userAndPass;
					password = username;
				}
			}
			//开始下载文件
			boolean isSuccess=downFtpFile(url, port, username, password, remotePath, fileName,localPath,type);
			if(isSuccess){
				return 1;
			}
		}
		return -1;
	}

	/**
	 * Description: 从FTP服务器下载文件
	 * 
	 * @Version1.0
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * 
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * 
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	public static boolean downFtpFile(String url, int port, String username,
			String password, String remotePath, String fileName,
			String localPath,String type) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				String fname = new String(ff.getName().getBytes("iso-8859-1"),
						"gbk");
				if(type.equals("files")){
					File localFile = new File(localPath);
					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
					break;
				}else{
					if (fname.equals(fileName)) {
						File localFile = new File(localPath);
						OutputStream is = new FileOutputStream(localFile);
						ftp.retrieveFile(ff.getName(), is);
						is.close();
						break;
					}
				}
			}
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}
	
}