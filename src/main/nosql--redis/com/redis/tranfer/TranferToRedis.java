package com.redis.tranfer;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.redis.RedisFactory;
import com.util.DataBaseUtil;
import com.util.HibernateSessionFactory;
import com.util.Utils;

import redis.clients.jedis.Jedis;
/**
 * 将记录表中的数据 转移到redis中
 * @author SuperWang
 * @version 创建时间：2018年6月3日 下午11:19:14
 */
public class TranferToRedis {

	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);
	private static Logger logger = Logger.getLogger(TranferToRedis.class);
	
	public static void main(String[] args) throws InterruptedException {
		Session session = HibernateSessionFactory.getSession();
		session.close();
		TranferToRedis tranferToRedis = new TranferToRedis();
		for (int i = 0; i < 50; i++) {
			ReadSql readSql = tranferToRedis.new ReadSql(i);
			tranferToRedis.fixedThreadPool.execute(readSql);
		}
		tranferToRedis.fixedThreadPool.shutdown();
	}

	class ReadSql extends Thread {
		Integer mod = 0;

		public ReadSql(Integer mod) {
			this.mod = mod;
		}

		//读数据库
		@SuppressWarnings("all")
		public void run() {
			int i = 0;
			Session session = HibernateSessionFactory.getSession();
			StringBuilder sb = new StringBuilder();
			//获取每个表的用户id
			sb.append("select user_id from t_record_item_").append(mod).append(" group by user_id");
			logger.info(sb);
			Jedis sentinl = RedisFactory.getSentinlConnect();
			try {
				List<String> userIdList = DataBaseUtil.getDataList(session, sb.toString());
				//System.out.println(userIdList);
				for (String userId : userIdList) {
					sb.setLength(0);
					sb.append("select * from t_record_item_").append(mod).append(" where user_id = '").append(userId).append("'");
					List<Map<String, Object>> userWatchInfoList = DataBaseUtil.getDataList(session, sb.toString(), null, true);

					Map<String, String> userWatchInfoMap = new HashMap<String, String>();
					for (Map<String, Object> map : userWatchInfoList) {
						Set<Entry<String, Object>> entryUserWatchInfoSet = map.entrySet();
						for (Entry<String, Object> entryUserWatchInfo : entryUserWatchInfoSet) {
							userWatchInfoMap.put(entryUserWatchInfo.getKey(), entryUserWatchInfo.getValue() == null ? "" : entryUserWatchInfo.getValue().toString());
						}
						userWatchInfoMap.put("record_watch_id", Utils.getUUID());
						userWatchInfoMap.put("watch_datetime", new Timestamp(new Date().getTime()).toString());
						userWatchInfoMap.put("watch_endTime", new Timestamp(new Date().getTime()).toString());

						String key = userId + "==" + userWatchInfoMap.get("item_id");
						sentinl.hmset(key, userWatchInfoMap);
						sentinl.expire(key, RedisFactory.EXPIRE_TIME);
						userWatchInfoMap.clear();
					}
					System.out.println(i++);
				}
				logger.info("转移完毕");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sentinl.close();
				session.close();
			}
		}
	}

}
