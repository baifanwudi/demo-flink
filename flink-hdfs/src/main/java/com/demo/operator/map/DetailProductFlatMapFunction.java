package com.demo.operator.map;

import com.alibaba.fastjson.JSON;
import com.demo.bean.out.BaseTransferBO;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import java.util.List;

public class DetailProductFlatMapFunction implements FlatMapFunction<String, BaseTransferBO> {

    @Override
    public void flatMap(String value, Collector<BaseTransferBO> out) throws Exception {
        try {
            List<BaseTransferBO> array = JSON.parseArray(value, BaseTransferBO.class);
            for (BaseTransferBO baseTransferBO : array) {
                out.collect(baseTransferBO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
