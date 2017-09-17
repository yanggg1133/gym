package com.hxs.fitnessroom.base.baseclass;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.hxs.fitnessroom.module.user.HXSUser;
import com.hxs.fitnessroom.util.LocationUtil;
import com.hxs.fitnessroom.base.network.APIHttpClient;
import com.hxs.fitnessroom.util.PhoneInfoUtil;
import com.hxs.fitnessroom.util.ToastUtil;

/**
 * Application基类
 * Created by je on 8/31/17.
 */

public class BaseApplication extends Application
{

    @Override
    public void onCreate() {
        super.onCreate();

        /**********************************
         * 一些需要在app启动时预先初始化的操作
         **********************************/
        APIHttpClient.appInitialization(this);
        LocationUtil.appInitialization(this);
        PhoneInfoUtil.appInitialization(this);
        HXSUser.appInitialization(this);
        ToastUtil.appInitialization(this);



//        initJpush();
//        com.saidian.zuqiukong.login.user.AVUser.initialize(this);

//        ZqkongDB.init(this);
//        ToastUtil.init(this);
//        SharedPreferencesUtils.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }



    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.with(this).onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.with(this).onLowMemory();
    }

}
