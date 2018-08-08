package com.zookeeper;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

public class SimpleZkClient {

	private static final String connectString = "192.168.0.71:2181,192.168.0.72:2181,192.168.0.73:2181";
	private static final int sessionTimeout = 2000;
	private ZooKeeper zkClient = null;

	@Before
	public void initZk() throws Exception {
		zkClient = new ZooKeeper(connectString, sessionTimeout, (event) -> {
			System.out.println(event.getType() + "====" + event.getPath());
			//循环监听
			/*
			try {
				zkClient.getChildren("/", true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
		});
		Thread.sleep(10000);
	}
	@Test
	/*保存数据*/
	public void createZk() throws Exception {
		String path = zkClient.create("/servers", "superwang".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println(path);
	}
	/*判断znode是否存在*/
	public void exist() throws Exception{
		Stat stat = zkClient.exists("/", false);
		System.out.println(stat == null ? "no exist" : "exist");
		
	}
	/*获取znode*/
	public void getChildren() throws Exception{
		List<String> listChildren = zkClient.getChildren("/ecgfrrrkl", true);
		System.out.println(listChildren);
		for (String children : listChildren) {
			System.out.println(children);
		}
		//Thread.sleep(Long.MAX_VALUE);
	}
	/*获取znode数据*/
	public void getData() throws Exception{
		byte[] data = zkClient.getData("/ecgfhrrlyylrkl", false, null);
		System.out.println(new String(data));
	}
	
	/*删除 znode*/
	public void deleteData() throws Exception{
		//param2: 指定删除哪个版本  -1 : 删除所有版本
		zkClient.delete("/ecgfhrrlyylrkl", -1);
	}
	/*修改znode*/
	public void setData() throws Exception{
		zkClient.setData("", "".getBytes(), -1);
	}
}
