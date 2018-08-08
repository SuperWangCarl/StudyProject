package com.file;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Test;


public class Compare {

	@Test
	public void getAttr() throws Exception {
		ArrayList<String> excelName = new ArrayList<String>();
		FileInputStream fis = new FileInputStream("F:/DownloadRecord/abc.txt");
		Scanner sc = new Scanner(fis);
		while(sc.hasNext()){
			String name = sc.nextLine();
			name = name.subSequence(name.indexOf("<")+1, name.indexOf(">")) + "; //"+ name.substring(name.lastIndexOf("<"), name.lastIndexOf(">")+1);
			System.out.println(name);
		}
		
	}
	public void dowork() throws Exception {
		ArrayList<String> excelName = new ArrayList<String>();
		ArrayList<String> imageName = new ArrayList<String>();
		FileInputStream fis = new FileInputStream("C:/Users/SuperWang/Desktop/第2批 二级页面节目海报图-已切成180X236 - 副本/电视剧.txt");
		Scanner sc = new Scanner(fis);
		while(sc.hasNext()){
			String name = sc.nextLine();
			excelName.add(name);
		}
		File dir = new File("C:/Users/SuperWang/Desktop/第2批 二级页面节目海报图-已切成180X236 - 副本/第2批 电视剧 二级页节目海报180X236");
		File[] files = dir.listFiles();
		for (File file : files) {
			String name = file.getName().replace(".jpg", "");
			imageName.add(name);
		}
		ArrayList<String> clone = (ArrayList<String>) excelName.clone();
		excelName.removeAll(imageName);
		//System.out.println(clone);
		System.out.println("execlName:" + excelName);
		imageName.retainAll(clone);
		System.out.println("imageName:" + imageName);
		for (File file : files) {
			if(imageName.contains(file.getName().replace(".jpg", "")))
				file.delete();
		}
	}
}
