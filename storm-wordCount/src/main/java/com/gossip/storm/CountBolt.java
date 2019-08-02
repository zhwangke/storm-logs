package com.gossip.storm;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: WK
 * @Data: 2019/8/1 23:00
 * @Description: com.gossip.storm
 */
public class CountBolt extends BaseRichBolt {
    private OutputCollector outputCollector;
    private Map<String,Integer> map = new HashMap<>();

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector=outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        //1 从tuple中获取数据
        String word = tuple.getStringByField("word");
        Integer count = tuple.getIntegerByField("count");


        if (map.get(word)!=null){
            Integer wordCount = map.get(word);
            wordCount++;

            map.put(word,wordCount);
        }else {
            map.put(word,count);
        }
        System.out.println(map);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
