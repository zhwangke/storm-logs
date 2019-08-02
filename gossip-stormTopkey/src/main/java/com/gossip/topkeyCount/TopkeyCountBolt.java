package com.gossip.topkeyCount;

import com.gossip.utils.JedisUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

import java.util.Arrays;

/**
 * @Author: WK
 * @Data: 2019/8/1 23:44
 * @Description: com.gossip.topkeyCount
 */
public class TopkeyCountBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        //1. 获取数据
        String keywords = tuple.getStringByField("keywords");
        Integer count = tuple.getIntegerByField("count");

        //2. 判断在redis中这个关键词存在, 如果存在,+1  不存在设置为1
        Jedis jedis = JedisUtils.getJedis();   //bigData:search:topKey    sortedSet(排行榜)
        Double score = jedis.zscore("bigData:search:topKey", keywords);

        if(score == null){
            //认为没有这个数据
            score = count*1.0;
            jedis.zadd("bigData:gossip:topkey",count,keywords);
        }else{
            // 返回值就是++ 后的值
            score = jedis.zincrby("bigData:gossip:topkey", 1, keywords);
        }

        //3. 判断, 这个元素出现了多少次, 如果大于5次的, 认为是热搜数据
        if(score>5){
            //    bigdata:gossip:keywords:page   string

            if(!jedis.exists("bigdata:gossip:" + keywords + ":1")){
                // 当前关键词没有缓存数据, 需要生成缓存数据
                collector.emit(Arrays.asList(keywords));
            }
        }
        jedis.close();

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("topkey"));
    }
}
