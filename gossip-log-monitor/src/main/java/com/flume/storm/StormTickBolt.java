package com.flume.storm;

import com.flume.utils.CommonUtils;
import org.apache.storm.Config;
import org.apache.storm.Constants;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.Arrays;
import java.util.Map;

/**
 * @Author: WK
 * @Data: 2019/8/2 19:33
 * @Description: com.flume.storm
 */
public class StormTickBolt extends BaseBasicBolt {
    private CommonUtils commonUtils = new CommonUtils();
    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config config = new Config();
        config.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS,5);
        return config;
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {

        if (tuple.getSourceComponent().contains(Constants.SYSTEM_COMPONENT_ID) &&
                tuple.getSourceStreamId().contains(Constants.SYSTEM_TICK_STREAM_ID)){
            // 说明是一个系统的tuple
            // 需要定时将规则读取到内存中
            commonUtils.monitorApp();
            commonUtils.monitorRule();
            commonUtils.monitorUser();

        }else{
            String logs = tuple.getStringByField("value");//获取第五个数据
            System.out.println(logs);
            collector.emit(Arrays.asList(logs));
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("logs"));
    }
}
