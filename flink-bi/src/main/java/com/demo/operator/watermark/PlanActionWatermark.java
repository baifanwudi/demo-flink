package com.demo.operator.watermark;

import com.demo.analysis.label.bean.input.PlanActionInfo;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import javax.annotation.Nullable;

public class PlanActionWatermark implements  AssignerWithPeriodicWatermarks<PlanActionInfo> {
    //120s
    private final long maxTimeLag = 1000*120;

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        return new Watermark(System.currentTimeMillis() - maxTimeLag);
    }

    @Override
    public long extractTimestamp(PlanActionInfo element, long previousElementTimestamp) {
        return Long.parseLong(element.getTime()) * 1000;
    }
}
