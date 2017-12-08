package com.hxs.fitnessroom.module.sports.model.entity;

/**
 * 扫描二维码实体类
 * Created by je on 17/09/17.
 */

public class QRCodeBean
{
    public static final String DEVICE_TYPE_DOOR = "door";
    public static final String DEVICE_TYPE_RUN = "run";
    public static final String DEVICE_TYPE_SHOP = "shop";
    public static final String DEVICE_TYPE_LOCKER = "locker";


    /**
     * data : 储物柜已打开
     * code : 200
     * msg : null
     * status : 0
     */

    public String result;//: "success",
    public String type;//: "door"  // door 门禁 run 跑步机  shop 售货机  locker 储物柜
    
    public String id;//: "1",
    public String name;//: "可乐",
    public String price;//: "￥3.50",
    public String number;//: "1个",
}
