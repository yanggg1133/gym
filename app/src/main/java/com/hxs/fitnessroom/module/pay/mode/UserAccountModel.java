package com.hxs.fitnessroom.module.pay.mode;

import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.pay.mode.entity.UserAccountBean;

/**
 * 获取用户状态和余额接口
 * Created by je on 17/09/17.
 */

public class UserAccountModel {
    public static APIResponse<UserAccountBean> getGymUserAccount(){
        return APIHttpClient.postForm(ConstantsApiUrl.GetGymUserAccount.getUrl(),
                ParamsBuilder.buildFormParam(),
                new TypeToken<APIResponse<UserAccountBean>>(){}.getType()
        );
    }
}
