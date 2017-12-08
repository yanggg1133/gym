package com.hxs.fitnessroom.module.home.model.entity;

/**
 * 我的预约实体
 * Created by shaojunjie on 17-11-7.
 */

public class StoreAppointment
{
    public String id;//: "1",
    public String appointment_id;//: "1509614415399",
    public String user_id;//: "61228",
    public String uid;//: "2344811063",
    public String cost;//: "66.000",
    public int status;//: "2",
    public String name;//: "好享瘦智能健身房（西塱店）",
    public String address;//: "广州市荔湾区花地大道404号绿瘦集团",
    public String timeDesc;//: "11-02 16:30-17:00"

    public String getStateName()
    {
        //2：已取消 3 已支付 4 已结算
          switch (status)
          {
              case 2:
                  return "已取消";
              case 4:
                  return "已完成";
          }
        return "";
    }
}
