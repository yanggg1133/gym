package com.hxs.fitnessroom.module.pay.mode.entity;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用户状态和余额
 * Created by je on 17/09/17.
 */

public class UserAccountBean {

    /**
     * status : 1  0 未交押金 1 正常用户（已缴纳押金） 2黑名单
     * balance : 10.00 用户余额（不含押金）
     */
    public static final int AccountStatus_NoDeposit = 0; //没押金
    public static final int AccountStatus_NORMAL = 1; //正常
    public static final int AccountStatus_BlackList = 2; //黑名单

    /**
     * 0 正常 、1 用户停留健身房  待离开、2 已离开  订单待结算、3 订单异常
     */
    public static final int DoorStatus_NORMAL = 0; //正常
    public static final int DoorStatus_USING = 1; //用户在健身房内


    @IntDef({AccountStatus_BlackList ,AccountStatus_NORMAL,AccountStatus_NoDeposit})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AccountStatus{}




    @AccountStatus
    public int status;//0 未交押金 1 正常用户（已缴纳押金） 2黑名单

    public String balance;//用户余额（不含押金）

    public String mincost;//最低消费

    public int doorStatus;//最低消费

}
