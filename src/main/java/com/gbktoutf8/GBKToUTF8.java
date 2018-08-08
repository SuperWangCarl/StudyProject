package com.gbktoutf8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public class GBKToUTF8 {

	/*public static void main(String[] args) throws Exception {
		//GBK编码格式源码路径 
		String srcDirPath = "F:\\linux";
		//转为UTF-8编码格式源码路径 
		String utf8DirPath = "F:\\linux2";

		//获取所有java文件 
		Collection<File> javaGbkFileCol = FileUtils.listFiles(new File(srcDirPath), new String[] { "htm" }, true);

		for (File javaGbkFile : javaGbkFileCol) {
			//UTF8格式文件路径 
			String utf8FilePath = utf8DirPath + javaGbkFile.getAbsolutePath().substring(srcDirPath.length());
			//使用GBK读取数据，然后用UTF-8写入数据 
			FileUtils.writeLines(new File(utf8FilePath), "UTF-8", FileUtils.readLines(javaGbkFile, "GBK"));
		}
	}*/
	public static void main(String[] args) {

		File file = new File("F:\\linux");

		fileList(file);

		//GBKtoUTF8.codeConvert(file);

	}
	//遍历文件

	public static void fileList(File file) {

		File rootFile = file;

		File[] files = rootFile.listFiles();

		if (files != null) {

			for (File f : files) {

				if (!f.isDirectory()) {

					codeConvert(f);

				}

				System.out.println(f.getPath());

				fileList(f);//递归调用子文件夹下的文件

			}

		}

	}

	//另一种展示方式，自己玩的与实现功能无关

	public static void fileList1(File file, int node) {

		node++;

		File rootFile = file;

		File[] files = rootFile.listFiles();

		if (files != null) {

			for (File f : files) {

				for (int i = 0; i < node; i++) {

					if (i == node - 1) {

						System.out.print("|-");

					} else
						System.out.print(" ");

				}

				System.out.println(f.getName());

				fileList1(f, node);

			}

		}

	}

	public static void codeConvert(File file) {

		try {

			BufferedReader br = new BufferedReader(new InputStreamReader

			(new FileInputStream(file), Charset.forName("GBK")));

			StringBuilder sb = new StringBuilder();

			String str;

			while ((str = br.readLine()) != null) {

				sb.append(str);

				sb.append("\n");

			}

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter

			(new FileOutputStream(file), Charset.forName("UTF-8")));

			bw.write(sb.toString());

			bw.flush();

			bw.close();

			//br.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}
