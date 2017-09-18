package com.hxs.fitnessroom.module.pay.mode;

import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.pay.mode.entity.DepositBean;

import java.util.List;

/**
 * 获取押金金额API
 * Created by je on 17/09/17.
 */

public class DepositModel
{
    public static APIResponse<List<DepositBean>> deposit()
    {
        return APIHttpClient.postForm(ConstantsApiUrl.Deposit.getUrl(),
                ParamsBuilder.buildFormParam(),
                new TypeToken<APIResponse<List<DepositBean>>>()
                {
                }.getType()
        );

    }
}
