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
 * @Data: 2019/8/2 19:37
 * @Description: com.flume.storm
 */
public class ProcessDataBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {

        //1. 从tuple中读取数据
        String logs = tuple.getStringByField("logs");

        //2. 将logs切割处理, 获取 APPid  和数据

        String[] split = logs.split("\001");
        String appId = split[0];
        String errorLog = split[1];

        //3. 寻找规则信息:
        String rules = CommonUtils.checkRules(appId, errorLog);

        if(rules != null && !"".equals(rules)){
            // 认为找到了规则了, 往下游发送数据, 让下游发送邮件和短信
            collector.emit(Arrays.asList(logs,rules));
        }
        // 如果没找到规则, 直接丢弃, 不要了 ,
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("logs","rules"));
    }


}
