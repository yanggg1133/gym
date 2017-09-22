package com.hxs.fitnessroom.module.main;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.module.user.model.entity.UserBean;
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

        /**
         * 如果性别为空，清除用户登录状态
         */
        if(HXSUser.isLogin() && UserBean.SEX_TYPE_NULL == HXSUser.getSex())
        {
            HXSUser.signOut();
        }

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
                if(HXSUser.isLogin())
                    startActivity(MainActivity.getNewIntent(LoadingScreenActivity.this));
                else
                    startActivity(WelcomeActivity.getNewIntent(LoadingScreenActivity.this));
                finish();
            }
        },1000);
    }
}
