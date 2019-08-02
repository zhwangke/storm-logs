package com.gossip.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

/**
 * @Author: WK
 * @Data: 2019/8/1 23:09
 * @Description: com.gossip.storm
 */
public class TopologMain {

    public static void main(String[] args) {
        //1. 构建 拓扑关系
        TopologyBuilder builder = new TopologyBuilder();


        builder.setSpout("readFileSpout", new ReadFileSpout());

        builder.setBolt("splitBolt", new SplitBolt()).shuffleGrouping("readFileSpout");

        builder.setBolt("countBolt", new CountBolt()).shuffleGrouping("splitBolt");


        //2.  提交任务 : 2种  1_本地提交  2. 集群提交

        LocalCluster localCluster = new LocalCluster();
        // 参数1: 任务的名称    参数2:  运行的配置  参数3  拓扑图(任务)
        localCluster.submitTopology("wordCount", new Config(), builder.createTopology());

    }
}