package cn.itcast.storm.kafkaAndStorm;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class MyKafkaBolt extends BaseRichBolt{

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
	}

	@Override
	public void execute(Tuple input) {
		byte[] o = (byte[])input.getValue(0);
		String str = new String(o);
		System.out.println(str);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

}
