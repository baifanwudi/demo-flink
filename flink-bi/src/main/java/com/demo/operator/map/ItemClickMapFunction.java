package com.demo.operator.map;

import com.demo.analysis.label.bean.output.ItemAction;
import com.demo.analysis.label.bean.input.PlanActionInfo;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class ItemClickMapFunction implements MapFunction<PlanActionInfo, Tuple2<ItemAction, Integer>> {

    @Override
    public Tuple2<ItemAction, Integer> map(PlanActionInfo value) throws Exception {
        try {
            ItemAction itemAction = new ItemAction();
            itemAction.setStartCityId(Integer.parseInt(value.getFromCityId().trim()));
            itemAction.setEndCityId(Integer.parseInt(value.getToCityId().trim()));
            itemAction.setAction(value.getAction());
            itemAction.setItemId(value.getItemId());
            return Tuple2.of(itemAction, 1);
        }catch (Exception e){
            e.printStackTrace();
            return Tuple2.of(null,null);
        }
    }
}