package com.flume.storm;

import com.flume.utils.CommonUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * @Author: WK
 * @Data: 2019/8/2 19:39
 * @Description: com.flume.storm
 */
public class SaveToDBBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        //1. 获取上游的数据
        String rules = tuple.getStringByField("rules");
        String logs = tuple.getStringByField("logs");

        //2. 将记录保存数据库中
        CommonUtils.insertToDb(rules,logs);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
