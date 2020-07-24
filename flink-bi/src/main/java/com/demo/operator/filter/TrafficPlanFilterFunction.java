package com.demo.operator.filter;

import com.demo.analysis.label.bean.input.PlanActionInfo;
import org.apache.flink.api.common.functions.FilterFunction;

public class TrafficPlanFilterFunction implements FilterFunction<PlanActionInfo> {

    @Override
    public boolean filter(PlanActionInfo value) throws Exception {
        try {
            if (value == null || value.getItemId() == null) {
                return false;
            }
            String action = value.getAction();
            return action.equals("click") || action.equals("create");
        } catch (Exception e) {
            return false;
        }
    }
}