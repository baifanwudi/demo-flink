package com.demo.operator.map;

import com.alibaba.fastjson.JSON;
import com.demo.analysis.label.bean.input.PlanActionInfo;
import org.apache.flink.api.common.functions.MapFunction;

public class PlanActionMapFunction implements MapFunction<String, PlanActionInfo> {

    @Override
    public PlanActionInfo map(String value) throws Exception {
        try {
            PlanActionInfo planActionInfo = JSON.parseObject(value, PlanActionInfo.class);
            return planActionInfo;
        } catch (Exception e) {
        }
        return null;
    }
}
