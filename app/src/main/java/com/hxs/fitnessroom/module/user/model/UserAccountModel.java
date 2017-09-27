package com.hxs.fitnessroom.module.user.model;

import android.support.annotation.IntDef;

import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.pay.model.entity.UserAccountBean;
import com.hxs.fitnessroom.module.user.model.entity.RealnameBean;
import com.hxs.fitnessroom.module.user.model.entity.WalletDetailBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * 获取用户状态和余额接口
 * Created by je on 17/09/17.
 */

public class UserAccountModel
{
    public static final int FROMPAGE_DEF = 0;//默认
    public static final int FROMPAGE_WALLET = 1;//我的钱包

    @IntDef({FROMPAGE_DEF, FROMPAGE_WALLET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FROMPAGE{};

    /**
     * 查询用户帐户余额 及 状态
     * @return
     */
    public static APIResponse<UserAccountBean> getGymUserAccount(@FROMPAGE int frompage)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.GetGymUserAccount.getUrl(),
                ParamsBuilder.buildFormParam()
                .putParam("frompage",frompage),
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


    /**
     * 保存实名认证信息
     * @return
     */
    public static APIResponse saveRealname(String realname,String idCard, String img1, String img2)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.SaveRealname.getUrl(),
                ParamsBuilder.buildFormParam().putParam("realname",realname)
                .putParam("IDCard",idCard)
                .putParam("img1",img1)
                .putParam("img2",img2),
                new TypeToken<APIResponse>() {}.getType()
        );
    }

    /**
     * 获取实名认证信息
     * @return
     */
    public static APIResponse<RealnameBean> getRealname()
    {
        return APIHttpClient.postForm(ConstantsApiUrl.getRealname.getUrl(),
                ParamsBuilder.buildFormParam(),
                new TypeToken<APIResponse<RealnameBean>>() {}.getType()
        );
    }




}
