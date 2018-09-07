HA sentine 使用后必须关闭

cluster使用后不可以关闭  关闭后会抛 JedisNoReachableClusterNodeException 异常
JedisPool jp = clusterNodes.get(k);
Jedis connection = jp.getResource();
connection.close();//使用 getResource之后一定要关闭连接