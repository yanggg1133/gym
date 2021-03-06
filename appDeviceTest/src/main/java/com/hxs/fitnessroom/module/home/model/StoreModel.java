package com.hxs.fitnessroom.module.home.model;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.home.model.entity.AreaBean;
import com.hxs.fitnessroom.module.home.model.entity.StoreAppointment;
import com.hxs.fitnessroom.module.home.model.entity.StoreBean;
import com.hxs.fitnessroom.module.home.model.entity.StoreReserveBean;

import java.util.List;


/**
 * 门店接口
 * Created by je on 9/5/17.
 */

public class StoreModel
{

    /**
     * 获取门店列表
     * @param filter 检索字段，如"广州市天河区"
     * @param location 坐标，如 '113.331084,23.112223'
     * @param page_index 第几页
     * @return
     */
    public static APIResponse<List<StoreBean>> storeList(@Nullable String filter, @Nullable String location, int page_index)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.StoreList.getUrl(),
                ParamsBuilder.buildFormParam()
                        .putParam("filter",filter)
                        .putParam("location",location)
                        .putParam("page_index",page_index),
                new TypeToken<APIResponse<List<StoreBean>>>(){}.getType()
                );
    }

    /**
     * 获取城市列表
     * @param location 当前坐标，"113.239811,23.068664"
     * @return
     */
    public static APIResponse<List<AreaBean>> areaList(@Nullable String location)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.AreaList.getUrl(),
                ParamsBuilder.buildFormParam()
                        .putParam("location",location),
                new TypeToken<APIResponse<List<AreaBean>>>(){}.getType()
                );
    }

    /**
     * 获取门店的预约时间段列表
     * @param storeId 门店ID
     * @return
     */
    public static APIResponse<StoreReserveBean> getStoreAppointment( String storeId)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.Appointment.getUrl(),
                ParamsBuilder.buildFormParam()
                        .putParam("uid",storeId),
                new TypeToken<APIResponse<StoreReserveBean> >(){}.getType()
        );
    }

    /**
     * 获取门店的预约确认
     * @return
     */
    public static APIResponse payStoreAppointment(String storeId,List<StoreReserveBean.Time> selectTimes)
    {
        String tiems = "";
        for(StoreReserveBean.Time time : selectTimes)
        {
            tiems += ","+time.time;
        }
        tiems = tiems.replaceFirst(",","");

        return APIHttpClient.postForm(ConstantsApiUrl.AppointmentUPay.getUrl(),
                ParamsBuilder.buildFormParam()
                        .putParam("uid",storeId)
                        .putParam("time",tiems),
                new TypeToken<APIResponse<StoreReserveBean> >(){}.getType()
        );
    }

    /**
     * 获取我的预约列表
     * @return
     */
    public static APIResponse<List<StoreAppointment>> getStoreAppointmentList(int type)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.AppointmentList.getUrl(),
                ParamsBuilder.buildFormParam()
                        .putParam("status",type ==3?"":type),
                new TypeToken<APIResponse<List<StoreAppointment>> >(){}.getType()
        );
    }

    /**
     * 取消预约
     * @return
     */
    public static APIResponse storeAppointmenCancel(String id)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.AppointmentCancel.getUrl(),
                ParamsBuilder.buildFormParam()
                        .putParam("id",id),
                new TypeToken<APIResponse >(){}.getType()
        );
    }

}
