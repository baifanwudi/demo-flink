package com.demo.analysis.label.bean.output;


import com.google.common.base.Objects;
import lombok.Data;

@Data
public class ItemAction {

    private Integer startCityId;

    private Integer endCityId;

    private String itemId;

    private String action;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemAction that = (ItemAction) o;
        return Objects.equal(startCityId, that.startCityId) &&
                Objects.equal(endCityId, that.endCityId) &&
                Objects.equal(itemId, that.itemId) &&
                Objects.equal(action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(startCityId, endCityId, itemId, action);
    }
}
