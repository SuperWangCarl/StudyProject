package com.redis.tranfer;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.redis.RedisFactory;
import com.util.DataBaseUtil;
import com.util.HibernateSessionFactory;

import redis.clients.jedis.Jedis;
/**
 * 将redis中的 sql语句写入 数据库中
 * @author SuperWang
 * @version 创建时间：2018年6月3日 下午11:29:09
 */
public class RedisToSql {

	private ExecutorService fixedThreadPool = Executors.newCachedThreadPool();
	private static Logger logger = Logger.getLogger(RedisToSql.class);
	
	public static void main(String[] args){
		Session session = HibernateSessionFactory.getSession();
		session.close();
		RedisToSql tranferToRedis = new RedisToSql();
		while(true){
			ReadSql readSql = tranferToRedis.new ReadSql();
			tranferToRedis.fixedThreadPool.execute(readSql);
			try {
				TimeUnit.SECONDS.sleep(10);
				//bool = bool ? false:true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class ReadSql extends Thread {

		//读redis写入数据库
		@SuppressWarnings("all")
		public void run() {
			Session session = null;
			Jedis jedis = null;
			try{
				jedis = RedisFactory.getSentinlConnect();
				int i = 0;
				Set<String> keysInsert = jedis.keys("*");
				//大于100条再存
				if(keysInsert.size() > 100){
					StringBuilder sqlSb = new StringBuilder();
					for (String key : keysInsert) {
						if(i < 100000){
							String sql = jedis.get(key);
							logger.info(key + "::" + sql);
							if(sql != null && !"null".equals(sql)){
								i++;
								sqlSb.append(sql).append("==================");
							}
							jedis.del(key);
							logger.info("删除key:" + key);
						}else{
							break;
						}
					}
					session = HibernateSessionFactory.getSession();
					DataBaseUtil.executeStatment(session, sqlSb.toString());
				}
				logger.info("sql查出" + keysInsert.size() + "条");
				logger.info("sql存储" + i + "条");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(jedis != null){
					jedis.close();
				}
				if(session != null){
					session.close();
				}
			}
		}
	}

}
