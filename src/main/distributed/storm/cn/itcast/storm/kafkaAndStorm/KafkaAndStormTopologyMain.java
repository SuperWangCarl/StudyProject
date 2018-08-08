package cn.itcast.storm.kafkaAndStorm;


import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

public class KafkaAndStormTopologyMain {
    public static void main(String[] args) throws Exception{
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("kafkaSpout",
                new KafkaSpout(new SpoutConfig(
                        new ZkHosts("192.168.0.71:2181,192.168.0.72:2181,192.168.0.73:2181"),
                        "orderMq",
                        "/myKafka",
                        "kafkaSpout")),1);
        //topologyBuilder.setBolt("mybolt1",new ParserOrderMqBolt(),1).shuffleGrouping("kafkaSpout");
        topologyBuilder.setBolt("mybolt1",new MyKafkaBolt(),1).shuffleGrouping("kafkaSpout");

        Config config = new Config();
        config.setNumWorkers(1);

        //3、提交任务  -----两种模式 本地模式和集群模式
        if (args.length>0) {
            StormSubmitter.submitTopology(args[0], config, topologyBuilder.createTopology());
        }else {
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("storm2kafka", config, topologyBuilder.createTopology());
        }
    }
}
