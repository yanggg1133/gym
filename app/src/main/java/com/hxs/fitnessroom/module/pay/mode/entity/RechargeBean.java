package com.hxs.fitnessroom.module.pay.mode.entity;

/**
 * 发起充值信息实体
 * Created by je on 9/7/17.
 */

public class RechargeBean
{
    public String OrderNo;
    public Wxpay  wxpay;

    public class Wxpay
    {
        public String token_id;
        public String services;
        public String appid;
    }
}
