package com.demo.job;

import com.demo.base.AbstractFlinkStreamBase;
import com.demo.base.KafkaConsumerBase;
import com.demo.bean.out.BaseTransferBO;
import com.demo.operator.map.DetailProductFlatMapFunction;
import com.demo.operator.writer.BeanCsvWriter;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import java.io.IOException;
import java.time.ZoneId;

public class TransferDetailLogJob extends AbstractFlinkStreamBase {

    @Override
    public void executeProgram(String[] args, StreamExecutionEnvironment env) throws IOException {
        KafkaConsumerBase consumer = new KafkaConsumerBase();
        FlinkKafkaConsumer011<String> detailLog = consumer.createKafkaConsumer("TRAIN_wukong_plan_detail_product","flink_wukong_plan_detail_product_etl");
        DataStream<String> actionStream = env.addSource(detailLog).name("detail_log_source").disableChaining();

        DataStream<BaseTransferBO> detailStream = actionStream.flatMap(new DetailProductFlatMapFunction());
        BucketingSink<BaseTransferBO> sink = new BucketingSink<BaseTransferBO>("/data/twms/traffichuixing/wukong_plan_detail_product/");
        sink.setBucketer(new DateTimeBucketer<BaseTransferBO>("yyyy-MM-dd", ZoneId.of("Asia/Shanghai")));
        sink.setBatchSize(1024*1024*1024*50L); // this is 50G,
        sink.setBatchRolloverInterval(60*60*1000L); // this is 60 mins
        sink.setPendingPrefix("");
        sink.setPendingSuffix("");
        sink.setInProgressPrefix(".");
        sink.setWriter(new BeanCsvWriter<BaseTransferBO>("$"));
        detailStream.addSink(sink).name("detail_log_sink").setParallelism(2);
    }

    public static void main(String[] args) throws Exception {
        TransferDetailLogJob transferDetailLogJob = new TransferDetailLogJob();
        transferDetailLogJob.runAll(args);
    }
}
