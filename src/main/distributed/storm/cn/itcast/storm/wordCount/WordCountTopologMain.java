package cn.itcast.storm.wordCount;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * Created by maoxiangyi on 2016/4/27.
 */
public class WordCountTopologMain {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
    	/*
    	Config conf = newConfig();
    	conf.setNumWorkers(2); //用2个worker
    	topologyBuilder.setSpout("blue-spout", newBlueSpout(), 2); //设置2个并发度
    	topologyBuilder.setBolt("green-bolt", newGreenBolt(), 2).setNumTasks(4).shuffleGrouping("blue-spout"); //设置2个并发度，4个任务
    	topologyBuilder.setBolt("yellow-bolt", newYellowBolt(), 6).shuffleGrouping("green-bolt"); //设置6个并发度
    	StormSubmitter.submitTopology("mytopology", conf, topologyBuilder.createTopology());
*/
    	
        //1、准备一个TopologyBuilder
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("mySpout",new MySpout(),2);
        topologyBuilder.setBolt("mybolt1",new MySplitBolt(),2).shuffleGrouping("mySpout");
        topologyBuilder.setBolt("mybolt2",new MyCountBolt(),4).fieldsGrouping("mybolt1", new Fields("word"));
        //2、创建一个configuration，用来指定当前topology 需要的worker的数量
        Config config =  new Config();
//        config.setDebug(true);
        config.setNumWorkers(2);
        //3、提交任务  -----两种模式 本地模式和集群模式
//        StormSubmitter.submitTopology("mywordcount",config,topologyBuilder.createTopology());
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("mywordcount",config,topologyBuilder.createTopology());
    }
}
