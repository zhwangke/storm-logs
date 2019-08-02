package com.flume.storm;

import com.flume.utils.CommonUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.Arrays;

/**
 * @Author: WK
 * @Data: 2019/8/2 19:39
 * @Description: com.flume.storm
 */
public class NotifyMessageBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        try {
            //1. 获取上游的数据
            String logs = tuple.getStringByField("logs");
            String rules = tuple.getStringByField("rules");

            //2. 发送短信和邮件
            CommonUtils.notifyPeople(rules,logs);

            //3. 如果发送完毕, 需要将此条发送记录, 记录到数据库中, 作为备份
            collector.emit(Arrays.asList(rules,logs));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("rules","logs"));
    }
}