package com.hadoop.hdfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 客户端去操作hdfs时，是有一个用户身份的
 * 默认情况下，hdfs客户端api会从jvm中获取一个参数来作为自己的用户身份：-DHADOOP_USER_NAME=hadoop
 * 
 * 也可以在构造客户端fs对象时，通过参数传递进去
 * @author
 *
 */
public class HdfsClientDemo {
	FileSystem fs = null;
	Configuration conf = null;

	@Before
	public void init() throws Exception {

		
		
		conf = new Configuration();
		//conf.set("fs.defaultFS", "hdfs://192.168.0.71:9000");
		//System.setProperty("hadoop.home.dir", "D:/OpenSource/hadoop-2.6.5");
//conf.set("hadoop.home.dir", "D:/OpenSource/hadoop-2.6.5"); 
		//拿到一个文件系统操作的客户端实例对象
		fs = FileSystem.get(conf);
		//可以直接传入 uri和用户身份
		fs = FileSystem.get(new URI("hdfs://192.168.0.71:9000"), conf, "root"); //最后一个参数为用户名
	}

	@Test
	public void testUPload() throws Exception {

		fs.copyFromLocalFile(new Path("H:/access.log"), new Path("/aeees.log.copy"));
		fs.close();
	}

	@Test
	public void testDownload() throws Exception {

		fs.copyToLocalFile(new Path("/access.log.copy"), new Path("d:/"));
		/*
		 * 是否删除   服务器目录  本地目录  是否使用本地文件系统
		*/
		fs.copyToLocalFile(false, null, null, false);
		fs.close();
	}

	@Test
	public void testConf() {
		Iterator<Entry<String, String>> iterator = conf.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			System.out.println(entry.getValue() + "--" + entry.getValue());//conf加载的内容
		}
	}

	/**
	 * 创建目录
	 */
	@Test
	public void makdirTest() throws Exception {
		boolean mkdirs = fs.mkdirs(new Path("/aaa/bbb"));
		System.out.println(mkdirs);
	}

	/**
	 * 删除
	 */
	@Test
	public void deleteTest() throws Exception {
		boolean delete = fs.delete(new Path("/aaa"), true);//true， 递归删除
		System.out.println(delete);
	}

	@Test
	public void listTest() throws Exception {

		FileStatus[] listStatus = fs.listStatus(new Path("/"));
		//判断是文件还是文件夹
		for (FileStatus fileStatus : listStatus) {
			System.out.println(fileStatus.isFile());
			System.err.println(fileStatus.getPath() + "=================" + fileStatus.toString());
		}
		//会递归找到所有的文件
		RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
		while (listFiles.hasNext()) {
			LocatedFileStatus next = listFiles.next();
			String name = next.getPath().getName();
			Path path = next.getPath();
			System.out.println(name + "---" + path.toString());
		}
	}

	/**
	 * 查看目录信息，只显示文件
	 * 
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testListFiles() throws FileNotFoundException, IllegalArgumentException, IOException {

		// 思考：为什么返回迭代器，而不是List之类的容器， 如果文件特大， 那不就崩啦！ 迭代器是每迭代一次都向服务器取一次
		RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);

		while (listFiles.hasNext()) {
			LocatedFileStatus fileStatus = listFiles.next();
			System.out.println(fileStatus.getPath().getName());//文件名
			System.out.println(fileStatus.getBlockSize());//block块的大小
			System.out.println(fileStatus.getPermission());//文件的权限
			System.out.println(fileStatus.getLen());//字节数
			BlockLocation[] blockLocations = fileStatus.getBlockLocations();//获取block块
			for (BlockLocation bl : blockLocations) {
				System.out.println("block-length:" + bl.getLength() + "--" + "block-offset:" + bl.getOffset());
				String[] hosts = bl.getHosts(); //主机名
				for (String host : hosts) {
					System.out.println(host);
				}

			}

			System.out.println("--------------为angelababy打印的分割线--------------");

		}

	}

	/**
	 * 查看文件及文件夹信息
	 * 
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testListAll() throws FileNotFoundException, IllegalArgumentException, IOException {

		FileStatus[] listStatus = fs.listStatus(new Path("/"));

		String flag = "d-- ";
		for (FileStatus fstatus : listStatus) {
			if (fstatus.isFile())
				flag = "f-- ";
			System.out.println(flag + fstatus.getPath().getName());
			System.out.println(fstatus.getPermission());
		}
	}

	/*public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://master:9000");
		//拿到一个文件系统操作的客户端实例对象
		FileSystem fs = FileSystem.get(conf);

		fs.copyFromLocalFile(new Path("G:/access.log"), new Path("/access.log.copy"));
		fs.close();
	}*/

	public static void main(String[] args) {
		System.out.println("----------------------------------------------");
		System.out.println( System.getenv());
	}
}
