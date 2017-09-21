package com.hxs.fitnessroom.module.pay.model.entity;

/**
 * 发起充值信息实体
 * Created by je on 9/7/17.
 */

public class RechargeBean
{
    public String alipay;
    public String orderNo;
    public Wxpay wxpay;
    public BalancePay balancePay;

    public static class Wxpay
    {
        public String noncestr;//: "i3EmF4nlyk4iXldy",
        public String prepayid;//: "wx201708211352118d4af08bd90877969223",
        public String partnerid;//: "wx201708211352118d4af08bd90877969223",
        public String sign;//: "AB520E8E1E4CB0F47136A8047A78AD08",
        public String timestamp;//: "APP"
        public String packageValue;//: "APP"
    }

    public static class BalancePay
    {
        public String money;
        public double balance;
        public String balanceDesc;
        public String useTime;
    }

}
