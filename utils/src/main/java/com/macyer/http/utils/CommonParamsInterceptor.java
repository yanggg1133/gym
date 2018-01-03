package com.macyer.http.utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lenovo on 2017/11/22.
 */

public class CommonParamsInterceptor implements Interceptor {

    @Override 
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        long utime = System.currentTimeMillis()/1000;

        /*Request.Builder builder = chain.request().newBuilder();
        // 替换为自己的token
        builder.addHeader("token", "123");
        return chain.proceed(builder.build());*/
        
        // 添加新的参数
//        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
//                .newBuilder()
//                .scheme(oldRequest.url().scheme())
//                .host(oldRequest.url().host())
//                .addQueryParameter("page_size", String.valueOf(PAGE_SIZE))
//                .addQueryParameter("model_idfa", PhoneInfoUtil.PhoneIMEI)
//                .addQueryParameter("model_version", PhoneInfoUtil.ModelVersion)
//                .addQueryParameter("system_version", PhoneInfoUtil.SystemVersion)
//                .addQueryParameter("app_version", PhoneInfoUtil.AppVersion)
//                .addQueryParameter("platform", PhoneInfoUtil.HxsAppType + "")
//                .addQueryParameter("app_name", PhoneInfoUtil.AppName)
//                .addQueryParameter("utime", utime +"")
//                .addQueryParameter("sign", Base64Util.encodeToString(ValidateUtil.getMD5(utime + SIGN_KEY).getBytes()))
//                .addQueryParameter("sess_token", HXSUser.getUserSessToken());
        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
//                .url(authorizedUrlBuilder.build())
                .build();
        return chain.proceed(newRequest);
    }
}
