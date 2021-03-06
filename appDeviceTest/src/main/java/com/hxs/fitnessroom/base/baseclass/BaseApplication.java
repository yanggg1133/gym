package com.hxs.fitnessroom.base.baseclass;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.hxs.fitnessroom.BuildConfig;
import com.hxs.fitnessroom.module.openim.AliBaichuanYwIM;
import com.hxs.fitnessroom.util.LocationUtil;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.util.PhoneInfoUtil;
import com.hxs.fitnessroom.util.ToastUtil;
import com.mob.MobSDK;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;

import fitnessroom.hxs.com.singesdk.XingeSdk;


/**
 * Application基类
 * Created by je on 8/31/17.
 */

public class BaseApplication extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();

        /**********************************
         * 一些需要在app启动时预先初始化的操作
         **********************************/
        APIHttpClient.appInitialization(this);
        LocationUtil.appInitialization(this);
        PhoneInfoUtil.appInitialization(this);
        HXSUser.appInitialization(this);
        ToastUtil.appInitialization(this);
        HXSUser.updateUserAccountInfoAsync();
        XingeSdk.initSdk(this,HXSUser.getMobile());

        /**
         * 腾讯x5预加载
         */
        QbSdk.initX5Environment(this, null);

        /**
         * 友盟统计
         */
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, "59b2395776661331870000eb", BuildConfig.FLAVOR));
        MobclickAgent.setCatchUncaughtExceptions(!BuildConfig.DEBUG);

        /**
         * 阿里IM
         */
        AliBaichuanYwIM.appInitialization(this);

        /**
         * shareSDK
         */
        MobSDK.init(this, "2150d9f4cb6fcc", "60b3ab05fb7b2c155107af79e0c17a69");

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        Glide.with(this).onTrimMemory(level);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        Glide.with(this).onLowMemory();
    }

}
