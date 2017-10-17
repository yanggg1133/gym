package com.hxs.fitnessroom.module.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.module.user.LoginActivity;

import java.util.HashMap;

import MTT.ThirdAppInfoNew;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import fitnessroom.hxs.com.sharesdk.ShareUtil;


/**
 * 欢迎登录界面
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener
{
    private BaseUi mBaseUi;

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, WelcomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_welcome_activity);
        mBaseUi = new BaseUi(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.register_button).setOnClickListener(this);
        findViewById(R.id.dont_login_button).setOnClickListener(this);
        findViewById(R.id.login_weixin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.login_button:
                startActivityForResult(LoginActivity.getNewIntent(v.getContext(),LoginActivity.VALUE_TYPE_LOGIN),RequestCode_Login);
                break;
            case R.id.register_button:
                startActivityForResult(LoginActivity.getNewIntent(v.getContext(),LoginActivity.VALUE_TYPE_REGISTER),RequestCode_Login);
                break;
            case R.id.login_weixin:
                thirdParth();
                break;
            case R.id.dont_login_button:
                startActivity(MainActivity.getNewIntent(WelcomeActivity.this));
                finish();
                break;
        }
    }

    /**
     *
     */

    private void thirdParth()
    {

        mBaseUi.getLoadingView().showByNullBackground();
        ShareUtil.loginByWechat(new PlatformActionListener()
        {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap)
            {
                mBaseUi.getLoadingView().hide();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable)
            {
                mBaseUi.getLoadingView().hide();

            }

            @Override
            public void onCancel(Platform platform, int i)
            {
                mBaseUi.getLoadingView().hide();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RequestCode_Login && resultCode == RESULT_OK)
        {
            startActivity(MainActivity.getNewIntent(WelcomeActivity.this));
            setResult(RESULT_OK);
            HXSUser.sendUserInfoUpdateBroadcastReceiver();
            finish();
        }
    }
}
