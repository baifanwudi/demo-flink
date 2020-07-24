package com.demo.operator.selector;

import com.demo.analysis.label.bean.output.ItemAction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;

public class CityItemSelector implements KeySelector<Tuple2<ItemAction, Integer>, String> {

    @Override
    public String getKey(Tuple2<ItemAction, Integer> value) throws Exception {
        ItemAction itemAction =value.f0;
        return String.format("%s@%s@%s", itemAction.getStartCityId(), itemAction.getEndCityId(), itemAction.getAction());
    }
}
