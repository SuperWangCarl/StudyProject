package nosql.mongodb;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

/**
 * 读写分离
 * @author SuperWang
 * @version 创建时间：2018年2月22日 下午8:44:19
 */
public class TestMongoDBReplSetReadSplit {
	public static void main1(String[] args) {
		try {
			List<ServerAddress> addresses = new ArrayList<ServerAddress>();
			ServerAddress address1 = new ServerAddress("192.168.0.71", 27011);
			ServerAddress address2 = new ServerAddress("192.168.0.72", 27011);
			ServerAddress address3 = new ServerAddress("192.168.0.73", 27011);
			addresses.add(address1);
			addresses.add(address2);
			addresses.add(address3);
			MongoClient client = new MongoClient(addresses);
			DB db = client.getDB("test");
			DBCollection coll = db.getCollection("testdb");
			// 插入
			BasicDBObject object = new BasicDBObject();
			object.append("test2", "testval2");
			coll.insert(object);
			//查询
			DBCursor dbCursor = coll.find();
			while (dbCursor.hasNext()) {
				DBObject dbObject = dbCursor.next();
				System.out.println(dbObject.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从库读
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			List<ServerAddress> addresses = new ArrayList<ServerAddress>();
			ServerAddress address1 = new ServerAddress("192.168.1.136", 27017);
			ServerAddress address2 = new ServerAddress("192.168.1.137", 27017);
			ServerAddress address3 = new ServerAddress("192.168.1.138", 27017);
			addresses.add(address1);
			addresses.add(address2);
			addresses.add(address3);
			MongoClient client = new MongoClient(addresses);
			DB db = client.getDB("test");
			DBCollection coll = db.getCollection("testdb");
			BasicDBObject object = new BasicDBObject();
			object.append("test2", "testval2");
			//读操作从副本节点读取
			ReadPreference preference = ReadPreference.secondary();
			DBObject dbObject = coll.findOne(object, null, preference);
			System.out.println(dbObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}