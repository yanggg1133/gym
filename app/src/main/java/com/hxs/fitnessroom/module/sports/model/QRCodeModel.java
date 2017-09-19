package com.hxs.fitnessroom.module.sports.model;

import com.google.gson.reflect.TypeToken;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.module.sports.model.entity.QRCodeBean;

/**
 * 扫描处理接口
 * Created by je on 17/09/17.
 */

public class QRCodeModel
{
    /**
     * 处理二维码
     * @param token       二维码内容
     * @return
     */
    public static APIResponse<QRCodeBean> deQRCode(String token)
    {
        return APIHttpClient.postForm(ConstantsApiUrl.DeQRCode.getUrl(),
                ParamsBuilder.buildFormParam()
                        .putParam("token", token),
                new TypeToken<APIResponse<QRCodeBean>>() {}.getType()
        );

    }
}
