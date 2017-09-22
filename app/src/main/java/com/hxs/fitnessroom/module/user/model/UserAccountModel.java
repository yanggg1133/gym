package com.hxs.fitnessroom.module.user.model;

import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.pay.model.entity.UserAccountBean;
import com.hxs.fitnessroom.module.user.model.entity.WalletDetailBean;

import java.util.List;

/**
 * 获取用户状态和余额接口
 * Created by je on 17/09/17.
 */

public class UserAccountModel
{
    /**
     * 查询用户帐户余额 及 状态
     * @return
     */
    public static APIResponse<UserAccountBean> getGymUserAccount()
    {
        return APIHttpClient.postForm(ConstantsApiUrl.GetGymUserAccount.getUrl(),
                ParamsBuilder.buildFormParam(),
                new TypeToken<APIResponse<UserAccountBean>>() {}.getType()
        );
    }

    /**
     * 查询钱包明细
     * @param lastId
     * @return
     */
    public static APIResponse<List<WalletDetailBean>> getAccountLog(String lastId)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.GetAccountLog.getUrl(),
                ParamsBuilder.buildFormParam().putParam("last_id",lastId),
                new TypeToken<APIResponse<List<WalletDetailBean>>>() {}.getType()
        );
    }



}
