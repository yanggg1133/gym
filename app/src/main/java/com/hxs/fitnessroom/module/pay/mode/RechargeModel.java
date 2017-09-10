package com.hxs.fitnessroom.module.pay.mode;

import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.pay.mode.entity.RechargeBean;

/**
 * 充值相关接口
 * Created by je on 9/7/17.
 */

public class RechargeModel
{
    /**
     * 获取新的充值订单信息
     * @param type 支付类型
     * @param amount 金额
     * @return
     */
    public static APIResponse<RechargeBean> addRecharge(int type, String amount)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.AddRecharge.getAPIRootUrl(),
                ParamsBuilder.buildFormParam()
                        .putParam("payMode",type)
                        .putParam("amount",amount),
                new TypeToken<APIResponse<RechargeBean>>(){}.getType()
        );
    }
}
