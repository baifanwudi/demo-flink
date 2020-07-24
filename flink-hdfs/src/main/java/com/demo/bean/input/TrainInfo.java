package com.demo.bean.input;

import lombok.Data;

@Data
public class TrainInfo {

    private String trainNum;

    private String fromTime;

    private String toTime;

    private String BeginPlace;

    private String EndPlace;

    private String fromCity;

    private String fromCityPy;

    private String toCity;

    private String toCityPy;

    private String fromType;

    private String toType;

    private String ifBook;

    private String sort;

    private String isBook;

    private String SaleFlag;

    private String SaleDatetime;

    private String payByPoint;//不需要

    private SeatType ticketState;

    private String TrainLongNo;

    private Integer UsedTimeInt;

    private String TrainFlag;

    private String TrainFlagMsg;

    private int accessByIdCard;
}
