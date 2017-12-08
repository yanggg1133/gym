package com.hxs.fitnessroom.module.home.model.entity;

import java.util.List;

/**
 * 门店预约信息
 * Created by shaojunjie on 17-11-1.
 */

public class StoreReserveBean
{

    public StoreBean store;
    public List<Appointment> appointment;


    public class Appointment
    {
        public String date;
        public List<Time> time;
    }

    public class Time
    {
        public String timeDesc;//: "09:00-09:30",
        public String time;//: "201710310930",
        @StoreBean.StoreStatus
        public int status;
        public String statusDesc;//: "已过期",
        public int number;//: 0
    }
}
