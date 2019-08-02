package com.gossip.topkeyCount;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Properties;

/**
 * @Author: WK
 * @Data: 2019/8/2 0:02
 * @Description: com.gossip.topkeyCount
 */
public class TopkeyTopologMain {
    public static void main(String[] args) {
        //1. 创建 拓扑构造器
        TopologyBuilder builder = new TopologyBuilder();

        KafkaSpoutConfig<String, String> kafkaSpoutConfig = KafkaSpoutConfig.builder("node01:9092,node02:9092,node03:9092", "gossip-logs")
                .setKey(StringDeserializer.class)
                .setValue(StringDeserializer.class)
                .setGroupId("topkey01")
                .build();


        KafkaSpout<String, String> kafkaSpout = new KafkaSpout<>(kafkaSpoutConfig);
        builder.setSpout("kafkaSpout",kafkaSpout);


        builder.setBolt("topKeySplitBolt",new TopkeySplitBolt()).shuffleGrouping("kafkaSpout");


        builder.setBolt("topKeyCountBolt",new TopkeyCountBolt()).shuffleGrouping("topKeySplitBolt");


        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092,node02:9092,node03:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaBolt bolt = new KafkaBolt()
                .withProducerProperties(props)  //kakfa生成者的配置信息
                .withTopicSelector(new DefaultTopicSelector("topkeyTopic")) //指定默认的topic
                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper(null,"topkey")); //指定接收上游数据的filed值:参数1 key  参数2:value

        builder.setBolt("kafkaBolt",bolt).shuffleGrouping("topKeyCountBolt");



        //2. 提交任务

        LocalCluster cluster = new LocalCluster();

        cluster.submitTopology("topkeyCount",new Config(),builder.createTopology());


    }
}
