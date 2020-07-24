package com.demo.conf;

import com.demo.util.ConfigUtils;

public class KafkaTopicConfig {

    public static  final String BI_TRANSFER_STATS_GROUP_NAME= ConfigUtils.getPros("bi.stats.group.name");

    public static final String BI_FLIGHT_STATS_GROUP_NAME=ConfigUtils.getPros("bi.fight.stats.group.name");
}
