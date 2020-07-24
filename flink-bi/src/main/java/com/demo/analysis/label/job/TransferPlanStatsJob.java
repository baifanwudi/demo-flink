package com.demo.analysis.label.job;

import com.alibaba.fastjson.JSON;
import com.demo.analysis.label.bean.input.PlanActionInfo;
import com.demo.analysis.label.bean.output.ItemAction;
import com.demo.analysis.label.sink.TrafficPlanClickedSink;
import com.demo.analysis.label.sink.TrafficUserClickedSink;
import com.demo.base.AbstractFlinkStreamBase;
import com.demo.base.KafkaConsumerBase;
import com.demo.conf.KafkaTopicConfig;
import com.demo.operator.map.ItemClickMapFunction;
import com.demo.operator.map.PlanActionMapFunction;
import com.demo.operator.selector.CityItemSelector;
import com.demo.operator.state.TrafficItemClickState;
import com.demo.operator.state.TrafficUserClickState;
import com.demo.operator.filter.TrafficPlanFilterFunction;
import com.demo.operator.watermark.PlanActionWatermark;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import java.io.IOException;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TransferPlanStatsJob extends AbstractFlinkStreamBase {

    @Override
    public void executeProgram(String[] args, StreamExecutionEnvironment env) throws IOException {
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        Properties propsConsumer = new KafkaConsumerBase().initKafkaProperties(KafkaTopicConfig.BI_TRANSFER_STATS_GROUP_NAME);
        propsConsumer.put("enable.auto.commit", true);
        FlinkKafkaConsumer011<String> actionLog = new FlinkKafkaConsumer011<String>("TRAIN_transferplan_show_record_product", new SimpleStringSchema(), propsConsumer);
        actionLog.setStartFromLatest();
        DataStream<String> actionStream = env.addSource(actionLog).name("action_log").disableChaining();
        DataStream<PlanActionInfo> exposure = actionStream.map(new PlanActionMapFunction()).filter(new TrafficPlanFilterFunction()).assignTimestampsAndWatermarks(new PlanActionWatermark());
        //方案过去两个小时被点击数
        DataStream<Tuple2<ItemAction, Integer>> itemWindowStream = exposure.map(new ItemClickMapFunction()).filter(s->s.f0!=null).keyBy(0).window(SlidingEventTimeWindows.of(Time.minutes(60 * 2), Time.seconds(30))).sum(1);
        //新的合并redis
        DataStream<Tuple2<String, String>> cityItemUpdateStream =itemWindowStream.keyBy( new CityItemSelector()).map(new TrafficItemClickState()).filter(s->s.f0!=null);
        cityItemUpdateStream.addSink(new TrafficPlanClickedSink()).name("city_stats_redis_sink").setParallelism(4);
        //用户最近10分钟，点击过方案
        DataStream<Tuple2<String, List<String>>> userClickStream = exposure.filter(s -> s.getAction().equals("click")&& s.getUnionId()!=null).keyBy(PlanActionInfo::getUnionId).map(new TrafficUserClickState());
        userClickStream.addSink(new TrafficUserClickedSink()).name("user_click_redis_sink").setParallelism(4);
    }

    private void cityItemSinkHdfsTest(DataStream<Tuple2<String, String>> cityItemUpdateStream) {
        BucketingSink<String> sink = new BucketingSink<String>("/data/twms/traffichuixing/item_click_create_log/");
        sink.setBucketer(new DateTimeBucketer<String>("yyyy-MM-dd", ZoneId.of("Asia/Shanghai")));
        sink.setBatchSize(1024*1024*1024*50L); // this is 50G,
        sink.setBatchRolloverInterval(60*60*1000L); // this is 60 mins
        sink.setPendingPrefix("");
        sink.setPendingSuffix("");
        sink.setInProgressPrefix(".");
        cityItemUpdateStream.map(record -> {
            String  key = record.f0;
            String value = record.f1;
            Map m = new HashMap<String, Object>();
            m.put("key", key);
            m.put("value", value);
            m.put("createTime", System.currentTimeMillis());
            return JSON.toJSONString(m);
        }).returns(Types.STRING).addSink(sink).name("city_stats_hdfs_sink").setParallelism(1);
    }

    private void userSinkHdfsTest(DataStream<Tuple2<String, List<String>>> userClickStream) {
        BucketingSink<String> sink = new BucketingSink<String>("/data/twms/traffichuixing/user_latest_click_log/");
        sink.setBucketer(new DateTimeBucketer<String>("yyyy-MM-dd", ZoneId.of("Asia/Shanghai")));
        sink.setBatchSize(1024*1024*1024*50L); // this is 50G,
        sink.setBatchRolloverInterval(60*60*1000L); // this is 60 mins
        sink.setPendingPrefix("");
        sink.setPendingSuffix("");
        sink.setInProgressPrefix(".");
        userClickStream.map(record -> {
            Map m = new HashMap<String, Object>();
            m.put("userId", record.f0);
            m.put("itemList", record.f1);
            m.put("createTime", System.currentTimeMillis());
            return JSON.toJSONString(m);
        }).returns(Types.STRING).addSink(sink).name("user_click_hdfs_sink").setParallelism(1);
    }

    public static void main(String[] args) throws Exception {
        TransferPlanStatsJob transferPlanStatsJobBak = new TransferPlanStatsJob();
        transferPlanStatsJobBak.runAll(args, false);
    }
}
