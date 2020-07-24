package com.demo.bean.out;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainDetailInfo {
    //搜索起始站
    private String startSearchName;
    //搜索到达站
    private String endSearchName;

    private String trainNo;

    private String trainCode;

    private String durationDate;

    private String fromStationName;

    private String toStationName;

    private String startStationName;

    private String endStationName;
    //路程时间(单位分)
    private Integer runTime;
    //硬座
    private Double hardSeatPrice;
    //无座
    private Double noSeatPrice;
    //软卧上
    private Double softSleeperUpPrice;
    //软卧下
    private Double softSleeperDownPrice;
    //硬卧上
    private Double hardSleeperUpPrice;
    //硬卧中
    private Double hardSleeperMidPrice;
    //硬卧下
    private Double hardSleeperDownPrice;
    //二等座
    private Double secondSeatPrice;
    //一等座
    private Double firstSeatPrice;
    //商务座
    private Double businessSeatPrice;
    //一等卧
    private Double firstSoftDown;
    //二等卧
    private Double secondSoftMid;
    //最小票价
    private Double price;
    //0或1
    private int hasTicket;

    private Long createTime;

    public void setMinPrice() {
        Double minPrice = Double.MAX_VALUE;
        minPrice = compareMinPrice(minPrice, hardSeatPrice);
        minPrice = compareMinPrice(minPrice, noSeatPrice);
        minPrice = compareMinPrice(minPrice, softSleeperUpPrice);
        minPrice = compareMinPrice(minPrice, softSleeperDownPrice);
        minPrice = compareMinPrice(minPrice, hardSleeperUpPrice);
        minPrice = compareMinPrice(minPrice, hardSleeperMidPrice);
        minPrice = compareMinPrice(minPrice, hardSleeperDownPrice);
        minPrice = compareMinPrice(minPrice, secondSeatPrice);
        minPrice = compareMinPrice(minPrice, firstSeatPrice);
        minPrice = compareMinPrice(minPrice, businessSeatPrice);
        minPrice = compareMinPrice(minPrice, firstSoftDown);
        minPrice = compareMinPrice(minPrice, secondSoftMid);
        this.price = minPrice;
    }

    private Double compareMinPrice(Double minPrice, Double comparePrice) {
        if (comparePrice != null && comparePrice != 0) {
            if (minPrice > comparePrice) {
                return comparePrice;
            }
        }
        return minPrice;
    }
}
