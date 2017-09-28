package com.hxs.fitnessroom.module.main;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
public class LoadingScreenActivity extends BaseActivity implements BaseActivity.OnPermissionsCallback
{
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            if (!HXSUser.isLogin())
                startActivity(WelcomeActivity.getNewIntent(LoadingScreenActivity.this));
            else
                startActivity(MainActivity.getNewIntent(LoadingScreenActivity.this));

            finish();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen_activity);

        /**
         * 如果性别为空，清除用户登录状态
         */
        if (HXSUser.isLogin() && UserBean.SEX_TYPE_NULL == HXSUser.getSex())
        {
            HXSUser.signOut();
        }

        HXSUser.updateUserInfoAsync();
        LocationUtil.refreshLocation();

        requestPermission(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
        },this);
    }


    @Override
    public void onPermissionsFail()
    {
        mHandler.sendEmptyMessageDelayed(0,3000);
    }

    @Override
    public void onPermissionsPass()
    {
        mHandler.sendEmptyMessageDelayed(0,3000);
    }
}
