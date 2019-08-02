package com.gossip.topkeyCount;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.Arrays;

/**
 * @Author: WK
 * @Data: 2019/8/1 23:36
 * @Description: com.gossip.topkeyCount
 */
public class TopkeySplitBolt extends BaseBasicBolt {

        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            System.out.println(tuple.toString());
            //tuple.getStringByField("value")
            //1. 获取上游发过来的数据: kakfa中日志流的数据
            String msg = tuple.getString(4);// 表示获取tuple中第五个数据

            //2. 切割处理
            int lastIndexOf = msg.lastIndexOf("#CS#");
            if((lastIndexOf)!=-1){
                String keywords = msg.substring(lastIndexOf + 4);
                collector.emit(Arrays.asList(keywords,1));
            }


            //3. 发送给下游


        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {

            declarer.declare(new Fields("keywords","count"));
    }
}
