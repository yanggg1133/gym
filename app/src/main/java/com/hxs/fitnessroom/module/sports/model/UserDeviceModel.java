package com.hxs.fitnessroom.module.sports.model;

import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.sports.model.entity.UserDeviceStatusBean;

/**
 * 用户使用健身房或设备的情况
 * Created by je on 9/20/17.
 */

public class UserDeviceModel
{

    /**
     * 获取用户当前设备使用情况
     * @return
     */
    public static APIResponse<UserDeviceStatusBean> getUserDeviceStatus()
    {
        return APIHttpClient.postForm(ConstantsApiUrl.GetUserDeviceStatus.getUrl(),
                ParamsBuilder.buildFormParam(),
                new TypeToken<APIResponse<UserDeviceStatusBean>>() {}.getType()
        );

    }
}
