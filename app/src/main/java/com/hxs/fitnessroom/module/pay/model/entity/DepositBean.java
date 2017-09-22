package com.hxs.fitnessroom.module.pay.model.entity;

/**
 * 押金金额实体类
 * Created by je on 17/09/17.
 */

public class DepositBean
{
    /**
     * deposit : 30
     */
    public String deposit;

    public String status;//: "1",      //0 未交押金 1 正常用户（已缴纳押金）  2 押金退回中  3 押金已退回 4  押金退回失败 5黑名单
    public String depositDesc;//: "￥30.00",
    public String payDesc;//: "支付宝支付"
}
