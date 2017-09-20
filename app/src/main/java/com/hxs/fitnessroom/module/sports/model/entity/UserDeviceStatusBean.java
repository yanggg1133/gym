package com.hxs.fitnessroom.module.sports.model.entity;

/**
 * 用户使用设备的状态
 * Created by je on 9/20/17.
 */

public class UserDeviceStatusBean
{
    public int doorStatus ; //: １,　　　//　 用户开门状态（0 正常 、1 用户停留健身房  待离开、2 已离开  订单待结算、3 订单异常）
    public String tips;//: "健身房使用中",　　//描述
    public String startTime;//  开始时间   毫秒数
    public String endTime;//结束时间       毫秒数
    public String nowTime;//服务器当前时间  毫秒数
    public String warmimg;//: "结束使用健身房请在关闭健身房设备后扫描二维码开门"

    public Store store;
    public Locker locker;
    public Run run;

    public static class Store
    {
        public String name;//: "绿瘦荔湾区健身房（西塱店）",
        public double fee;//: "11.00",
        public String feeDesc;//: "￥11.00",
        public String openTime;//: "营业时间：09:00:00-22:00:00"
    }

    public static class Locker
    {
        public int status;//: 1,
        public String desc;//: "使用中",
        public String num;//: "15"
    }

    public static class Run
    {
        public int status;//: 1,
        public String desc;//: "使用中"
    }
}
