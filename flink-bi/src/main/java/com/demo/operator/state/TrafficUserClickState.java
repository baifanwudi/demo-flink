package com.demo.operator.state;

import com.google.common.collect.Lists;
import com.demo.analysis.label.bean.input.PlanActionInfo;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import java.util.List;

public class TrafficUserClickState extends RichMapFunction<PlanActionInfo, Tuple2<String, List<String>>> {

    private transient ListState<String> merges;

    @Override
    public void open(Configuration parameters) throws Exception {
        StateTtlConfig ttlConfig = StateTtlConfig.newBuilder(Time.minutes(10)).setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite).setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired).build();
        ListStateDescriptor<String> descriptor = new ListStateDescriptor<String>("user_latest_click", String.class);
        descriptor.enableTimeToLive(ttlConfig);
        merges = getRuntimeContext().getListState(descriptor);
        super.open(parameters);
    }

    @Override
    public Tuple2<String, List<String>> map(PlanActionInfo value) throws Exception {
        merges.add(value.getItemId());
        return  Tuple2.of(value.getUnionId(), Lists.newArrayList(merges.get()));
    }
}
