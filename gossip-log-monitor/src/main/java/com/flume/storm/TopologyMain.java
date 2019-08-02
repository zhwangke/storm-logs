package com.flume.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

/**
 * @Author: WK
 * @Data: 2019/8/2 19:40
 * @Description: com.flume.storm
 */
public class TopologyMain {

    public static void main(String[] args) throws Exception {
        //1. 创建 构建器
        TopologyBuilder builder = new TopologyBuilder();

        KafkaSpoutConfig<String,String> kafkaSpoutConfig = KafkaSpoutConfig
                .builder("node01:9092,node02:9092,node03:9092","log-monitor")
                .setGroupId("test01")
                .setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_LATEST) //从未提交的最后一条数据读
                .build();

        builder.setSpout("KafkaSpout",new KafkaSpout<String,String>(kafkaSpoutConfig));

        builder.setBolt("StormTickBolt",new StormTickBolt()).localOrShuffleGrouping("KafkaSpout");
        builder.setBolt("ProcessDataBolt",new ProcessDataBolt()).localOrShuffleGrouping("StormTickBolt");
        builder.setBolt("NotifyMessageBolt",new NotifyMessageBolt()).localOrShuffleGrouping("ProcessDataBolt");
        builder.setBolt("SaveToDBBolt",new SaveToDBBolt()).localOrShuffleGrouping("NotifyMessageBolt");

        //2. 提交任务
        Config config = new Config();
        if(args != null && args.length >0 ){
            StormSubmitter.submitTopology(args[0],config,builder.createTopology());
        }else {
            LocalCluster cluster = new LocalCluster();

            cluster.submitTopology("log-monitor",config,builder.createTopology());
        }
    }
}
