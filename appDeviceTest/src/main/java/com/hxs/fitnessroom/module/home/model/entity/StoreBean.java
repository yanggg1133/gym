package com.hxs.fitnessroom.module.home.model.entity;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 门店实体类
 * Created by je on 9/5/17.
 */

public class StoreBean
{
    /**
     * 0 已过期  ， 1,清闲  ，2 适中 3，拥挤，4 爆满 （0,4状态下禁止预约）
     */
    public static final int STATUS_Timeout = 0;//已过期
    public static final int STATUS_Idle = 1;//清闲
    public static final int STATUS_Moderate = 2;//适中
    public static final int STATUS_Crowded = 3;//拥挤
    public static final int STATUS_Full = 4;//爆满

    @IntDef({STATUS_Timeout ,STATUS_Idle,STATUS_Moderate,STATUS_Crowded,STATUS_Full})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StoreStatus{}

    public String uid;
    public String name;
    public String address;
    public String distance;
    public String img;
    public String stime;//: "2017/10/31 09:00",
    public String etime;//: "2017/11/02 22:00",
    public double fee;//: "33.00",
    public String points;//: "113.232193,23.060807",
    public String online;//: 3,
    public String statusDesc;//: "适中，先到先得"
    @StoreStatus
    public int status;
    /*
    "uid":2310972319,
    "name":"绿瘦健身房（天河店）",
    "address":"广东省广州市天河区六运二街43号",
    "distance":"2.57km",
    "img":"http://b.hiphotos.bdimg.com/lbsapi/wh%3D160%2C160/sign=6e0f891f9c2bd4074292dbfc4db9b260/f2deb48f8c5494ee9734182e26f5e0fe99257ef6.jpg"
    */


    public String getStatusName()
    {
        switch (status)
        {
            case STATUS_Idle:
                return "清闲";
            case STATUS_Moderate:
            case STATUS_Crowded:
                return "适中";
            case STATUS_Full:
                return "爆满";
        }
        return "清闲";
    }
}
