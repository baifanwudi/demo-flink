package com.demo.bean.input;

import lombok.Data;

@Data
public class SeatType {
    //硬座
    private  SeatInfo hardseat;
    //无座
    private  SeatInfo noseat;
    //软卧
    private  SeatInfo softsleeperdown;
    //硬卧
    private  SeatInfo hardsleepermid;
    //二等座
    private  SeatInfo secondseat;
    //一等座
    private  SeatInfo firstseat;
    //商务座
    private  SeatInfo businessseat;
    //特等座
    private SeatInfo specialseat;
    //一等卧
    private SeatInfo firstsoftdown;
    //二等卧
    private SeatInfo secondsoftmid;
}
