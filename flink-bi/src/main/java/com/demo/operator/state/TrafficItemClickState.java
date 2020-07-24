package com.demo.operator.state;

import com.alibaba.fastjson.JSON;
import com.demo.analysis.label.bean.output.ItemAction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TrafficItemClickState extends RichMapFunction<Tuple2<ItemAction, Integer>, Tuple2<String, String>> {

    private transient MapState<ItemAction, Integer> map;

    public transient static final String CLICK_PREFIX_KEY = "cityClicked";

    public transient static final String CREATE_PREFIX_KEY = "cityCreated";

    @Override
    public void open(Configuration parameters) throws Exception {
        StateTtlConfig ttlConfig = StateTtlConfig.newBuilder(Time.minutes(60 * 2)).setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite).setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired).build();
        MapStateDescriptor<ItemAction, Integer> descriptor = new MapStateDescriptor<ItemAction, Integer>("paln_click_num", ItemAction.class, Integer.class);
        descriptor.enableTimeToLive(ttlConfig);
        map = getRuntimeContext().getMapState(descriptor);
        super.open(parameters);
    }

    @Override
    public Tuple2<String, String> map(Tuple2<ItemAction, Integer> keyValue) throws Exception {
        Integer num = keyValue.f1;
        ItemAction itemAction = keyValue.f0;
        if (num.equals(map.get(itemAction))) {
            return Tuple2.of(null, null);
        }
        map.put(itemAction, num);
        String prefixKey = itemAction.getAction().equals("click") ? CLICK_PREFIX_KEY : CREATE_PREFIX_KEY;
        String key = String.format("%s@%s@%s", prefixKey, itemAction.getStartCityId(), itemAction.getEndCityId());
        Map<String, Integer> valueMap = new HashMap<String, Integer>();
        Iterator< Map.Entry<ItemAction,Integer>> mapIterator= map.iterator();
        while(mapIterator.hasNext()){
            Map.Entry<ItemAction,Integer>  entry= mapIterator.next();
            valueMap.put(entry.getKey().getItemId(),entry.getValue());
        }
        return Tuple2.of(key, JSON.toJSONString(valueMap));
    }
}
