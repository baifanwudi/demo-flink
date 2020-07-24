package com.demo.analysis.label.sink;

import com.alibaba.fastjson.JSONObject;
//import com.ly.tcbase.cacheclient.CacheClientHA;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import java.util.List;

public class TrafficUserClickedSink extends RichSinkFunction<Tuple2<String, List<String>>> {


    public transient static final String PREFIX_KEY = "userLatestClick:";

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void invoke(Tuple2<String, List<String>> keyValue, Context context) throws Exception {
        String key = PREFIX_KEY + keyValue.f0;
        String value = JSONObject.toJSONString(keyValue.f1);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }
}
