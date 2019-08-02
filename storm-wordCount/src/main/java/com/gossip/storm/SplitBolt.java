package com.gossip.storm;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.Arrays;
import java.util.Map;

/**
 * @Author: WK
 * @Data: 2019/8/1 22:55
 * @Description: com.gossip.storm
 */
public class SplitBolt extends BaseRichBolt {
    private OutputCollector outputCollector;


    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector=outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        //从tuple中获取数据
        String line = tuple.getStringByField("line");
        //切割数据
        String[] words = line.split(" ");
        for (String word:words) {
            outputCollector.emit(Arrays.asList(word,1));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word","count"));
    }
}
