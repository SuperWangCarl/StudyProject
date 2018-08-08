package com.file;

import java.io.File;

import org.junit.Test;

public class FileReName {

	
	public void readFileName(){
		File dir = new File("E:/Work/活动/郑多燕专题/光标");
		File[] files = dir.listFiles();
		for (File file : files) {
			System.out.println(file.getName().substring(0,file.getName().indexOf(".")));
		}
	}
	@Test
	public void renameAll() {
		String path = "F:/DownloadRecord/开源分享资料";
		//String replaceName = "【更多精彩视频 请持续关注微信公众号：民工哥Linux运维】";
		String replaceName = "[更多精彩视频 请持续关注微信公众号：民工哥Linux运维]";
		renameFile(path, replaceName);
	}

	/**
	 * 
	 * @param path 替换的文件目录
	 * @param replaceName 需要替换的字段
	 */
	public void renameFile(String path, String replaceName) {
		File dir = new File(path);
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					if (file.getName().contains(replaceName))
						file.renameTo(new File(dir, file.getName().replaceAll(replaceName, "")));
				} else {
					String afterPath = new File(file.getPath()).getPath().replaceAll(replaceName, "");
					if (file.getName().contains(replaceName)) {
						file.renameTo(new File(dir, file.getName().replaceAll(replaceName, "")));
					}
					renameFile(afterPath, replaceName);
				}
			}
		}
	}
	public void renameDir() {
		
	}
	
	public void createFile(){
		
	}
}
