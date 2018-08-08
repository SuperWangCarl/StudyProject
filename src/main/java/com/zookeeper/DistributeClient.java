package com.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.ZooKeeper;

public class DistributeClient {

	private static final String connectString = "192.168.0.71:2181,192.168.0.72:2181,192.168.0.73:2181";
	private static final int sessionTimeout = 2000;
	private static final String parentNode = "/servers";
	private ZooKeeper zkClient = null;

	private volatile List<String> serverList ;
	/**
	 * 创建连接
	 * @throws Exception
	 */
	public void getConnect() throws Exception {
		zkClient = new ZooKeeper(connectString, sessionTimeout, (event) -> {
			System.out.println(event.getType() + "====" + event.getPath());
			//循环监听
			try {
				getServerList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		Thread.sleep(10000);
	}
	/**
	 * 获取服务器列表信息
	 * @throws Exception
	 */
	public void getServerList() throws Exception{
		//获取服务器子节点信息,并对父节点下子节点的变化进行监听
		List<String> children = zkClient.getChildren(parentNode, true);
		List<String> servers = new ArrayList<>();
		for (String child : children) {
			//不监听子节点的 数据变化
			servers.add(new String(zkClient.getData(parentNode+child, false, null)));
		}
		serverList = servers;
	}
	
	public static void main(String[] args) throws Exception{
		//获取zk连接
		DistributeClient clinet = new DistributeClient();
		clinet.getConnect();
		//获取servers的子节点信息(并监听),充服务器信息列表中获取
		clinet.getServerList();
	}
}
