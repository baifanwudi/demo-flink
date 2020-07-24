package com.demo.conf;

import com.demo.util.ConfigUtils;

/**
 * @author allen.bai
 */
public class KafkaConfig {

    public static final String KAFKA_BROKER_LIST= ConfigUtils.getPros("kafka.broker.list");
}
