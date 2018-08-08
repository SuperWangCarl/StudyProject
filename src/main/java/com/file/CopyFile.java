package com.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 * 文件拷贝
 * @author SuperWang
 *
 */
public class CopyFile {

	private String srcPath;
	private String destPath;
	private String[] extensions;
	
	public static void main(String[] args) {
		 CopyFile copyFile = new CopyFile();
		 String srcPath = "F:\\practice"; //被拷贝的目录     
		 String destPath = "F:\\abc";    //目标目录
		 String[] extensions = new String[]{".java",".xml"};//指定的后缀名
		 copyFile.dowork(srcPath, destPath, extensions);
	}
	
	public void dowork(String srcPah,String destPath,String[] extensions){
		this.srcPath = srcPah;
		this.destPath = destPath;
		this.extensions = extensions;
		this.copyFile(srcPah);
	}
	
	/**
	 * 列出该目录下所有文件
	 */
	public void copyFile(String srcPath) {
		File srcFile = new File(srcPath);
		//通过filenameFilter获得指定的文件和目录
		File[] files = srcFile.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				boolean result = true;
				//当后缀名为空表示拷贝所有文件
				if(extensions == null || extensions.length == 0){
					result = true;
				}else{
					//将看该文件是否匹配该后缀名
					for (String extension : extensions) {
						extension = extension.trim();
						result = name.endsWith(extension);
						if(result)
							break;
					}
				}
				File file = new File(dir,name);
				return (file.isFile() && result) || file.isDirectory();
			}
		});
		
		for (File file : files) {
			//若此时是个目录则继续调用该方法
			if(file.isDirectory()){
				this.copyFile(file.toString());
			}else{
				//获得该文件的父目录
				String parentPath = file.getParent();
				//将文件名换到父目录中
				if(!this.srcPath.contains("\\\\")){
					//正则表达式 的转义符 也是 "\" 所以"\\\\"表示一个斜杆
					this.srcPath = this.srcPath.replaceAll("\\\\", "\\\\\\\\");
					this.destPath = this.destPath.replaceAll("\\\\", "\\\\\\\\");
				}
				parentPath = parentPath.replaceAll(this.srcPath, destPath);
//				File file2 = new File("F:/abc",file.getName());
				File file2 = new File(parentPath,file.getName());
				if(!file2.exists())
					file2.getParentFile().mkdirs();
				//file.renameTo(file2);
				try {
					Files.copy(Paths.get(file.toURI()), new FileOutputStream(file2));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
