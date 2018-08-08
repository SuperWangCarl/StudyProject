package nosql.mongodb;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class MongoDBFactory {
	private static Builder options = null;
	private static MongoClient mongoClient = null;

	static{
		initPool();
	}
	/**
	 * 初始化配置文件
	 */
	private static void initPool() {
		options = new MongoClientOptions.Builder();
		options.cursorFinalizerEnabled(true);
		// options.autoConnectRetry(true);// 自动重连true
		// options.maxAutoConnectRetryTime(10); // the maximum auto connect retry time
		options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
		options.connectTimeout(30000);// 连接超时，推荐>3000毫秒
		options.maxWaitTime(5000); //
		options.socketTimeout(0);// 套接字超时时间，0无限制
		options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
		/**
		 *  WriteConcern.NONE:没有异常抛出
		    WriteConcern.NORMAL:仅抛出网络错误异常，没有服务器错误异常
		    WriteConcern.SAFE:抛出网络错误异常、服务器错误异常；并等待服务器完成写操作。
		    WriteConcern.MAJORITY: 抛出网络错误异常、服务器错误异常；并等待一个主服务器完成写操作。
		    WriteConcern.FSYNC_SAFE: 抛出网络错误异常、服务器错误异常；写操作等待服务器将数据刷新到磁盘。
		    WriteConcern.JOURNAL_SAFE:抛出网络错误异常、服务器错误异常；写操作等待服务器提交到磁盘的日志文件。
		    WriteConcern.REPLICAS_SAFE:抛出网络错误异常、服务器错误异常；等待至少2台服务器完成写操作。
		 */
		//options.writeConcern(WriteConcern.SAFE);
		/**
		 * primary:默认参数，只从主节点上进行读取操作；
			primaryPreferred:大部分从主节点上读取数据,只有主节点不可用时从secondary节点读取数据。
			secondary:只从secondary节点上进行读取操作，存在的问题是secondary节点的数据会比primary节点数据“旧”。
			secondaryPreferred:优先从secondary节点进行读取操作，secondary节点不可用时从主节点读取数据；
			nearest:不管是主节点、secondary节点，从网络延迟最低的节点上读取数据。
		 */
		options.readPreference(ReadPreference.secondaryPreferred());
	}
	
	//副本连接
	public static MongoDatabase getReplSet(){
		if(mongoClient == null )
			initReplSet();
		return mongoClient.getDatabase("zyjh");
	}
	public static void initReplSet(){
		List<ServerAddress> addresses = new ArrayList<ServerAddress>();
		ServerAddress address1 = new ServerAddress("192.168.0.71", 27011);
		ServerAddress address2 = new ServerAddress("192.168.0.72", 27011);
		ServerAddress address3 = new ServerAddress("192.168.0.73", 27011);
		addresses.add(address1);
		addresses.add(address2);
		addresses.add(address3);
		mongoClient = new MongoClient(addresses,options.build());
	}
}
