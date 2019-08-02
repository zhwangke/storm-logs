package com.gossip.storm;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: WK
 * @Data: 2019/8/1 22:41
 * @Description: com.gossip.storm
 */
//读取文件的spout程序
public class ReadFileSpout extends BaseRichSpout{
    //在创建这个 ReadFileSpout对象的时候就会调用这个方法进行初始化
    private SpoutOutputCollector collector;
    private  BufferedReader reader;

    /**
     *
     * @param map 进行对storm配置操作 一般不使用
     * @param topologyContext storm上下文对象，一般不使用
     * @param collector 向下游输出内容对象
     */

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector collector) {
        this.collector = collector;
        try {
            reader = new BufferedReader(new FileReader("E:\\word.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //当任务提交给storm程序后，storm程序会不断的调用
    //nextTuple方法 进行执行  一般在这个方法 循环的读取数据
    @Override
    public void nextTuple() {
        try {
            String line = reader.readLine();
            if (line!=null){
                //发送数据到下游
                collector.emit(Arrays.asList(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //数据传输 tuple 看成一个map  本质上是一个list
    //
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("line"));
    }
}
