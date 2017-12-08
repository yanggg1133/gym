package com.hxs.fitnessroom.module.user.model;

import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.user.model.entity.MessageBean;
import com.hxs.fitnessroom.module.user.model.entity.WalletDetailBean;

import java.util.List;

/**
 * 用户消息相关
 * Created by je on 9/24/17.
 */

public class UserMessageModel
{
    /**
     * 获取系统通知列表
     * @param lastId
     * @return
     */
    public static APIResponse<List<MessageBean>> getSystemMessage(String lastId)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.GetNoticeList.getUrl(),
                ParamsBuilder.buildFormParam().putParam("last_id",lastId),
                new TypeToken<APIResponse<List<MessageBean>>>() {}.getType());
    }

}
