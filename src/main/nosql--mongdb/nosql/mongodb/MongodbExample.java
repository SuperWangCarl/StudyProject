package nosql.mongodb;

import java.util.Arrays;

import org.bson.BsonType;
import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

public class MongodbExample {

	public static void main(String[] args) throws Exception {

		//20000端口为分片后集群的mongos端口,不添加验证直接执行下面一句创建连接  
		MongoClient mongoClient = new MongoClient("192.168.46.133", 20000);

		//添加验证模式  
		//        List<ServerAddress> seeds = new ArrayList<ServerAddress>();  
		//        ServerAddress address1 = new ServerAddress("192.168.46.133" , 20000);  
		//        seeds.add(address1);  
		//给mongodb增加验证  
		//        MongoCredential credentials = MongoCredential.createScramSha1Credential("mongo", "mytest", "mongo".toCharArray());  
		//        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();  
		//        credentialsList.add(credentials);  
		//        MongoClient mongoClient = new MongoClient(seeds,credentialsList);  

		MongoDatabase database = mongoClient.getDatabase("mytest");
		MongodbExample client = new MongodbExample(database);
		client.show();
		mongoClient.close();
	}

	private MongoDatabase database;

	public MongodbExample(MongoDatabase database) {
		this.database = database;
	}

	private static Gson gson = new Gson();

	public void show() {
		MongoCollection<Document> mc = database.getCollection("blog");
		/*    Foo foo;  
		for(int i=1;i<=10000;i++) {  
		    foo = new Foo();  
		    foo.setId(i);  
		    foo.setName("name"+i);  
		    mc.insertOne(Document.parse(gson.toJson(foo)));  
		}*/
		//每次执行前清空集合以方便重复运行  
		//        mc.drop();  
		for (int i = 0; i < 10000; i++) {
			//插入用于测试的文档  
			/*  Document doc1 = new Document("title", "good day"+i).append("owner", "tom" + i).append("words", 300+i)  
			        .append("comments", Arrays.asList(new Document("author", "joe" + i).append("score", 3).append("comment", "good" + i), new Document("author", "white" + i).append("score", 1).append("comment", "oh no" + i)));  
			Document doc2 = new Document("title", "good"+i).append("owner", "john"+i).append("words", 400)  
			        .append("comments", Arrays.asList(new Document("author", "william"+i).append("score", 4).append("comment", "good"+i), new Document("author", "white"+i).append("score", 6).append("comment", "very good"+i)));  
			Document doc3 = new Document("title", "good night"+i).append("owner", "mike"+i).append("words", 200)  
			        .append("tag", Arrays.asList(1, 2, 3, 4));  
			Document doc4 = new Document("title", "happiness"+i).append("owner", "tom"+i).append("words", 1480)  
			        .append("tag", Arrays.asList(2, 3, 4));  
			Document doc5 = new Document("title", "a good thing"+i).append("owner", "tom"+i).append("words", 180)  
			        .append("tag", Arrays.asList(1, 2, 3, 4, 5));  
			mc.insertMany(Arrays.asList(doc1, doc2, doc3, doc4, doc5));*/

			Document doc = new Document("userId", i).append("name", "lihua").append("age", 12 + i);
			mc.insertOne(doc);
		}

		//测试: 查询全部  
		FindIterable<Document> iterable = mc.find();
		printResult("find all", iterable);

		//        toShow(mc);  
		//TODO: 将在这里填充更多查询示例  
	}

	//打印查询的结果集  
	public void printResult(String doing, FindIterable<Document> iterable) {
		System.out.println(doing);
		iterable.forEach(new Block<Document>() {
			public void apply(final Document Document) {
				System.out.println(Document);
				//                System.out.println(Document.get("userId"));  
			}
		});
		System.out.println("------------------------------------------------------");
		System.out.println();
	}

	public void insert(MongoCollection<Document> mc) {
		int i = 1;
		Document doc = new Document("userId", i).append("name", "lihua").append("age", 12 + i);
		mc.insertOne(doc);
		
	}
	/**
	 * .save()和update()
			update比find之后save()效率高，因为这样不用读取整个文档。
			Mongoose的update是MongoDB的update，但是Mongoose的save可能是MongoDB的插入或是update。
			关于save，mongoose会自动diff新文档，只更改更新部分。这有利于原子性。
			update不能触发中间件，validation默认不能，但是可以修改。
			update()和findOneAndUpdate()
			update()返回数据处理条数
		findOneAndUpdate()返回处理后的数据
			简单来说，你需要获取数据就用findOneAndUpdate()，只需要修改数据而不关注修改后数据那就用update()。
	 * @param mc
	 */
	public void update(MongoCollection<Document> mc) {
		mc.updateOne(Filters.eq("likes", 100), new Document("$set", new Document("likes", 200)));
	}
	public void query(MongoCollection<Document> mc) {
		//创建单字段索引  
		mc.createIndex(new Document("words", 1));
		//创建组合索引(同样遵循最左前缀原则)  
		mc.createIndex(new Document("title", 1).append("owner", -1));
		//创建全文索引  
		mc.createIndex(new Document("title", "text"));

		//查询全部  
		FindIterable<Document> iterable = mc.find();
		printResult("find all", iterable);

		//查询title=good  
		iterable = mc.find(new Document("title", "good"));
		printResult("find title=good", iterable);

		//查询title=good and owner=tom  
		iterable = mc.find(new Document("title", "good").append("owner", "tom"));
		printResult("find title=good and owner=tom", iterable);

		//查询title like %good% and owner=tom  
		iterable = mc.find(Filters.and(Filters.regex("title", "good"), Filters.eq("owner", "tom")));
		printResult("find title like %good% and owner=tom", iterable);

		//查询全部按title排序  
		iterable = mc.find().sort(Sorts.ascending("title"));
		printResult("find all and ascending title", iterable);

		//查询全部按owner,title排序  
		iterable = mc.find().sort(Sorts.ascending("owner", "title"));
		printResult("find all and ascending owner,title", iterable);

		//查询全部按words倒序排序  
		iterable = mc.find().sort(Sorts.descending("words"));
		printResult("find all and descending words", iterable);

		//查询owner=tom or words>350  
		iterable = mc.find(new Document("$or", Arrays.asList(new Document("owner", "tom"), new Document("words", new Document("$gt", 350)))));
		printResult("find owner=tom or words>350", iterable);

		//返回title和owner字段  
		iterable = mc.find().projection(Projections.include("title", "owner"));
		printResult("find all include (title,owner)", iterable);

		//返回除title外的其他字段  
		iterable = mc.find().projection(Projections.exclude("title"));
		printResult("find all exclude title", iterable);

		//不返回_id字段  
		iterable = mc.find().projection(Projections.excludeId());
		printResult("find all excludeId", iterable);
		
		//返回title和owner字段且不返回_id字段  
		iterable = mc.find().projection(Projections.fields(Projections.include("title", "owner"), Projections.excludeId()));
		printResult("find all include (title,owner) and excludeId", iterable);

		//内嵌文档匹配  
		iterable = mc.find(new Document("comments.author", "joe"));
		printResult("find comments.author=joe", iterable);

		//一个错误的示例, 想查询评论中包含作者是white且分值>2的, 返回结果不符合预期  
		iterable = mc.find(new Document("comments.author", "white").append("comments.score", new Document("$gt", 2)));
		printResult("find comments.author=white and comments.score>2 (wrong)", iterable);

		//上面的需求正确的写法  
		iterable = mc.find(Projections.elemMatch("comments", Filters.and(Filters.eq("author", "white"), Filters.gt("score", 2))));
		printResult("find comments.author=white and comments.score>2 using elemMatch", iterable);

		//查找title以good开头的, 并且comments只保留一个元素  
		iterable = mc.find(Filters.regex("title", "^good")).projection(Projections.slice("comments", 1));
		printResult("find regex ^good and slice comments 1", iterable);

		//全文索引查找  
		iterable = mc.find(Filters.text("good"));
		printResult("text good", iterable);

		//用Filters构建的title=good  
		iterable = mc.find(Filters.eq("title", "good"));
		printResult("Filters: title eq good", iterable);

		//$in 等同于sql的in  
		iterable = mc.find(Filters.in("owner", "joe", "john", "william"));
		printResult("Filters: owner in joe,john,william", iterable);

		//$nin 等同于sql的not in  
		iterable = mc.find(Filters.nin("owner", "joe", "john", "tom"));
		printResult("Filters: owner nin joe,john,tom", iterable);

		//查询内嵌文档  
		iterable = mc.find(Filters.in("comments.author", "joe", "tom"));
		printResult("Filters: comments.author in joe,tom", iterable);

		//$ne 不等于  
		iterable = mc.find(Filters.ne("words", 300));
		printResult("Filters: words ne 300", iterable);

		//$and 组合条件  
		iterable = mc.find(Filters.and(Filters.eq("owner", "tom"), Filters.gt("words", 300)));
		printResult("Filters: owner eq tom and words gt 300", iterable);

		//较复杂的组合  
		iterable = mc.find(Filters.and(Filters.or(Filters.eq("words", 300), Filters.eq("words", 400)), Filters.or(Filters.eq("owner", "joe"), Filters.size("comments", 2))));
		printResult("Filters: (words=300 or words=400) and (owner=joe or size(comments)=2)", iterable);

		//查询第2个元素值为2的数组  
		iterable = mc.find(Filters.eq("tag.1", 2));
		printResult("Filters: tag.1 eq 2", iterable);

		//查询匹配全部值的数组  
		iterable = mc.find(Filters.all("tag", Arrays.asList(1, 2, 3, 4)));
		printResult("Filters: tag match all (1, 2, 3, 4)", iterable);

		//$exists  
		iterable = mc.find(Filters.exists("tag"));
		printResult("Filters: exists tag", iterable);

		iterable = mc.find(Filters.type("words", BsonType.INT32));
		printResult("Filters: type words is int32", iterable);
	}

	/** 
	 * 分页查询 
	 * @param page 
	 * @param pageSize 
	 * @return 
	 */
	public void pageList(int page, int pageSize) {
		MongoClient mongoClient = new MongoClient("192.168.46.133", 20000);
		DB myMongo = mongoClient.getDB("myMongo");
		DBCollection userCollection = myMongo.getCollection("user");

		DBCursor limit = userCollection.find().skip((page - 1) * 10).sort(new BasicDBObject()).limit(pageSize);
		while (limit.hasNext()) {
			System.out.println(limit.next());
		}
	}

}