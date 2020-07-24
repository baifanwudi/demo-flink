package com.demo.bean.out;

import lombok.Data;

@Data
public class BaseTransferBO {
    //中转方案加requestID的唯一标识符
    private String transferPlanId;
    // 出发站
    private String fromPlace;
    // 到达站
    private String toPlace;
    //出发城市
    private String fromCity;
    //到达城市
    private String toCity;
    //中转城市
    private String transferCity;
    // 第一程车次
    private String firstNo;
    // 第一程出发时间
    private String firstFromTime;
    // 第一程到达时间
    private String firstToTime;
    // 第一程耗时
    private int firstTime;
    // 第一段汽车班次号,拆分后
    private String firstScheduleNo;
    // 第二段汽车班次号,拆分后
    private String secondScheduleNo;
    // 第一程票价
    private double firstPrice;
    //第一程工具类型
    private String firstType;
    // 第一段中转站点
    private String firstTransferPlace;
    //第一程出发编码，火车-简码，飞机-三字码
    private String firstFromStationCode;
    //第一程到达编码，火车-简码，飞机-三字码
    private String firstToStationCode;
    // 第二程车次
    private String secondNo;
    // 第二程出发时间
    private String secondFromTime;
    // 第二程到达时间
    private String secondToTime;
    // 第二程耗时
    private int secondTime;
    // 第二程最低票价
    private double secondPrice;
    //第二程工具类型
    private String secondType;
    // 第二程中转站点
    private String secondTransferPlace;
    //第二程出发编码，火车-简码，飞机-三字码
    private String secondFromStationCode;
    //第二程到达编码，火车-简码，飞机-三字码
    private String secondToStationCode;
    // 等待时间
    private int waitTime;
    // 总耗时
    private int totalTime;
    // 总票价
    private double totalPrice;
    // 第一段出发日期,格式为2018-08-23
    private String firstFromDate;
    // 第二段出发日期,格式为2018-08-23
    private String secondFromDate;
    //会员id
    private String memberId;
    //小程序ID
    private String unionId;
    //方案标签(热门，历史等等)
    private String planLabel;
    //方案标识符
    private String itemId;
    //建表时间
    private Long createTime;
    //粗排分数
    private Double wukongScore;
    //bi返回评分
    private Double biScore;
    //根据标签修正评分
    private Double finalScore;
    //request_id请求
    private String requestId;
    //第一程推荐座席数量
    private String firRecSeatNum;
    //第二程推荐座席数量
    private String secRecSeatNum;
}
