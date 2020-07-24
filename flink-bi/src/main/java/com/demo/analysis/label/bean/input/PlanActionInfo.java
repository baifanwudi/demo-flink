package com.demo.analysis.label.bean.input;
import lombok.Data;

@Data
public class PlanActionInfo {

    private String fromCityId;

    private String transferCityId;

    private String toCityId;

    private String action;

    private String itemId;

    private String planId;

    private String time;

    private String unionId;

    private Integer rankIndex;

    private String transferType;
}
