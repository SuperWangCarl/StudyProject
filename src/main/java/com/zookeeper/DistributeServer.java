package com.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
/**
 * 创建服务端
 * @author SuperWang
 * @version 创建时间：2017年12月27日 下午2:50:41
 */
public class DistributeServer {

	private static final String connectString = "192.168.0.71:2181,192.168.0.72:2181,192.168.0.73:2181";
	private static final int sessionTimeout = 2000;
	private static final String parentNode = "/servers";
	private ZooKeeper zkClient = null;

	/**
	 * 创建连接
	 * @throws Exception
	 */
	public void getConnect() throws Exception {
		zkClient = new ZooKeeper(connectString, sessionTimeout, (event) -> {
			System.out.println(event.getType() + "====" + event.getPath());
			//循环监听
			try {
				zkClient.getChildren("/", true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		Thread.sleep(10000);
	}
	/**
	 * 注册服务
	 * @param hostname
	 * @throws Exception
	 */
	public void registerServer(String hostname) throws Exception{
		String create = zkClient.create(parentNode + "/server", hostname.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println(hostname + "is online ..." + create);
	}
	/**
	 * 业务功能
	 */
	public void handleBussiness(String hostname) throws Exception{
		System.out.println(hostname + "is work");
		Thread.sleep(Long.MAX_VALUE);
	}
	public static void main(String[] args) throws Exception{
		DistributeServer server = new DistributeServer();
		//获取zk连接
		server.getConnect();
		//利用zk连接注册服务器
		server.registerServer(args[0]);
		//启动业务功能
		server.handleBussiness(args[0]);
	}
}
