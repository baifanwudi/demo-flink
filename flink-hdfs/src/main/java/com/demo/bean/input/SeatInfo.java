package com.demo.bean.input;

import lombok.Data;

@Data
public class SeatInfo {

    private String cn;

    private Double price;

    private Integer state;

    private String seats;

    private String iscanOrder;

    private Double upPrice;

    private Double midPrice;

    private Double downPrice;

    private String houBuSeatFlag;
}
