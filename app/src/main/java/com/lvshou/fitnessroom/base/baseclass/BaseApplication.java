package com.lvshou.fitnessroom.base.baseclass;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.lvshou.fitnessroom.base.network.APIHttpClient;

/**
 * Application基类
 * Created by je on 8/31/17.
 */

public class BaseApplication extends Application
{

    @Override
    public void onCreate() {
        super.onCreate();

        APIHttpClient.appInitialization(this);

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
