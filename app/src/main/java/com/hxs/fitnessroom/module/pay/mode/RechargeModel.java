package com.hxs.fitnessroom.module.pay.mode;

import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.pay.PayFactory;
import com.hxs.fitnessroom.module.pay.mode.entity.RechargeBean;
import com.hxs.fitnessroom.module.pay.mode.entity.TopupAmountBean;

import java.util.List;

/**
 * 充值相关接口
 * Created by je on 9/7/17.
 */

public class RechargeModel
{
    /**
     * 获取新的充值订单信息
     *
     * @param payMode 支付类型
     * @param amount  金额
     * @param type    支付动作
     * @return
     */
    public static APIResponse<RechargeBean> addRecharge(@PayFactory.PayType int payMode, String amount, int type)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.AddRecharge.getUrl(),
                ParamsBuilder.buildFormParam()
                        .putParam("payMode", payMode)
                        .putParam("type", type)
                        .putParam("amount", amount),
                new TypeToken<APIResponse<RechargeBean>>(){}.getType()
        );
    }

    /**
     * 获取充值金额列表
     *
     * @return
     */
    public static APIResponse<List<TopupAmountBean>> rechargeList()
    {
        return APIHttpClient.postForm(ConstantsApiUrl.RechargeList.getUrl(),
                ParamsBuilder.buildFormParam(),
                new TypeToken<APIResponse<List<TopupAmountBean>>>(){}.getType());
    }
}
