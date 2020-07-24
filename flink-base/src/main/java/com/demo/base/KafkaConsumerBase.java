package com.demo.base;

import com.demo.conf.KafkaConfig;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import java.util.Properties;

public class KafkaConsumerBase {

    public final static String DEFAULT_KAFKA_GROUP_NAME = "xxx_streaming";

    public Properties initKafkaProperties(String groupName) {
        Properties propsConsumer = new Properties();
        propsConsumer.setProperty("bootstrap.servers", KafkaConfig.KAFKA_BROKER_LIST);
        propsConsumer.setProperty("group.id", groupName);
        //处理完再提交offset,防止kafka消息读完,任务没计算就挂，丢失数据
        propsConsumer.put("enable.auto.commit", false);
        propsConsumer.put("max.poll.records", 1000);
        return propsConsumer;
    }

    public FlinkKafkaConsumer011<String> createKafkaConsumer(String topicName) {
        return createKafkaConsumer(topicName, DEFAULT_KAFKA_GROUP_NAME, true);
    }

    public FlinkKafkaConsumer011<String> createKafkaConsumer(String topicName,String groupName) {
        return createKafkaConsumer(topicName, groupName, true);
    }

    public FlinkKafkaConsumer011<String> createKafkaConsumer(String topicName, Boolean isLatest) {
        return createKafkaConsumer(topicName, DEFAULT_KAFKA_GROUP_NAME, isLatest);
    }

    public FlinkKafkaConsumer011<String> createKafkaConsumer(String topicName, String groupName, Boolean isLatest) {
        Properties propsConsumer = initKafkaProperties(groupName);
        FlinkKafkaConsumer011<String> consumer = new FlinkKafkaConsumer011<String>(topicName, new SimpleStringSchema(), propsConsumer);
        if (isLatest) {
            consumer.setStartFromLatest();
        } else {
            consumer.setStartFromEarliest();
        }
        return consumer;
    }
}
