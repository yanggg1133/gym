package com.hxs.fitnessroom.module.user.model;

import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.user.LoginActivity;
import com.hxs.fitnessroom.module.user.model.entity.UserBean;

import static android.R.attr.type;

/**
 * 登录注册相关API
 * Created by je on 9/12/17.
 */

public class LoginModel
{
    /**
     * 发验证码
     */
    public static APIResponse sendSMS(String phone, @LoginActivity.LoginType String type)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.SendSMS.getUrl(),
                ParamsBuilder.buildFormParam()
                        .putParam("mobile",phone)
                        .putParam("smsType",type),
                new TypeToken<APIResponse>(){}.getType()
        );
    }

    /**
     * 登录/注册/绑定
     * @param phone
     * @param verifyCode
     * @param type  登录/注册/绑定(第三方登录)
     *
     * @see com.hxs.fitnessroom.module.user.LoginActivity.LoginType
     * @return
     */
    public static APIResponse<UserBean> Login(String phone, @LoginActivity.LoginType String type, String verifyCode, @Nullable Object thirdPartyBean)
    {
        String url = "";
        if(LoginActivity.VALUE_TYPE_LOGIN.equals(type))
        {
            url = ConstantsApiUrl.Login.getUrl();
        }
        else if(LoginActivity.VALUE_TYPE_REGISTER.equals(type))
        {

            url = ConstantsApiUrl.Register.getUrl();
        }
        else
        {
            url = ConstantsApiUrl.ThirdPartyLogin.getUrl();
        }
        return APIHttpClient.postForm(url,
                ParamsBuilder.buildFormParam()
                        .putParam("username",phone)
                        .putParam("smsType",type)
                        .putParam("verifyCode",verifyCode),
                new TypeToken<APIResponse<UserBean>>(){}.getType()
                );
    }


    /**
     * 获取用户信息
     */
    public static APIResponse<UserBean> getSelfUserInfo(String sess_token)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.GetSelfUserInfo.getUrl(),
                ParamsBuilder.buildFormParam()
                        .putParam("sess_token",sess_token),
                new TypeToken<APIResponse<UserBean>>(){}.getType()
        );
    }

}
