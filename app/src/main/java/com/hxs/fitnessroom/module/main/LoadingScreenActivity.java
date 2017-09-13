package com.hxs.fitnessroom.module.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.module.user.WelcomeActivity;
import com.hxs.fitnessroom.util.LocationUtil;

/**
 * APP启动页
 * Created by je on 9/2/17.
 */
public class LoadingScreenActivity extends BaseActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen_activity);
        requestPermission(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        });
    }

    @Override
    public void onPermissionsPass()
    {
        super.onPermissionsPass();
        LocationUtil.refreshLocation();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(WelcomeActivity.getNewIntent(LoadingScreenActivity.this));
                finish();
            }
        },1000);
    }
}
