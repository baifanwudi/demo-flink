package com.demo.analysis.label.sink;


import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class TrafficPlanClickedSink extends RichSinkFunction<Tuple2<String, String>> {


    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void invoke(Tuple2<String, String> keyValue, Context context) throws Exception {
        String key=keyValue.f0;
        String value=keyValue.f1;
    }

    @Override
    public void close() throws Exception {
        super.close();
    }
}