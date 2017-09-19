package com.hxs.fitnessroom.module.pay.mode.entity;

/**
 * 用户状态和余额
 * Created by je on 17/09/17.
 */

public class UserAccountBean {

    /**
     * status : 1  0 未交押金 1 正常用户（已缴纳押金） 2黑名单
     * balance : 10.00 用户余额（不含押金）
     */

    public String status;
    public String balance;


}
