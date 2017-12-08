package com.hxs.fitnessroom.module.openim.model;

import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.openim.model.entity.OpenIMAccountBean;

/**
 * 客服IM相关接口
 * Created by je on 9/25/17.
 */

public class OpenIMModel
{
    public static APIResponse<OpenIMAccountBean> getOpenIMAccount()
    {
        return APIHttpClient.postForm(ConstantsApiUrl.GetOpenIMAccount.getUrl(),
                ParamsBuilder.buildFormParam(),
                new TypeToken<APIResponse<OpenIMAccountBean>>(){}.getType()
        );
    }

}
