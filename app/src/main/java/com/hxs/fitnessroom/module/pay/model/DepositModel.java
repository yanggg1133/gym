package com.hxs.fitnessroom.module.pay.model;

import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.pay.model.entity.DepositBean;

/**
 * 获取押金金额API
 * Created by je on 17/09/17.
 */

public class DepositModel
{
    public static APIResponse<DepositBean> deposit()
    {
        return APIHttpClient.postForm(ConstantsApiUrl.Deposit.getUrl(),
                ParamsBuilder.buildFormParam(),
                new TypeToken<APIResponse<DepositBean>>() {}.getType()
        );

    }

    /**
     * 获取退押金详情
     * @return
     */
    public static APIResponse<DepositBean> refundDeposit()
    {
        return APIHttpClient.postForm(ConstantsApiUrl.RefundDeposit.getUrl(),
                ParamsBuilder.buildFormParam(),
                new TypeToken<APIResponse<DepositBean>>() {}.getType()
        );

    }

    /**
     * 获取退押金详情
     * @return
     */
    public static APIResponse<DepositBean> submitRefundDeposit()
    {
        return APIHttpClient.postForm(ConstantsApiUrl.SubmitRefundDeposit.getUrl(),
                ParamsBuilder.buildFormParam(),
                new TypeToken<APIResponse<DepositBean>>() {}.getType()
        );

    }
}
