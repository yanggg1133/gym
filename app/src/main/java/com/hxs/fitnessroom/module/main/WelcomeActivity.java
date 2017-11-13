package com.hxs.fitnessroom.module.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.module.user.LoginActivity;
import com.hxs.fitnessroom.module.web.WebActivity;
import com.hxs.fitnessroom.util.LogUtil;

import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.wechat.moments.WechatMoments;
import fitnessroom.hxs.com.sharesdk.ShareUtil;


/**
 * 欢迎登录界面
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener
{
    private static final String KEY_IS_NEED_JUMP_MAINACTIVITY = "KEY_IS_NEED_JUMP_MAINACTIVITY";
    private BaseUi mBaseUi;
    private boolean mIsNeedJumpMainActivity;


    public static Intent getNewIntent(Context context)
    {
        Intent intent = new Intent(context, WelcomeActivity.class);
        if(context instanceof WebActivity)
            intent.putExtra(KEY_IS_NEED_JUMP_MAINACTIVITY,false);
        else
            intent.putExtra(KEY_IS_NEED_JUMP_MAINACTIVITY,true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_welcome_activity);
        mIsNeedJumpMainActivity = getIntent().getBooleanExtra(KEY_IS_NEED_JUMP_MAINACTIVITY,true);
        mBaseUi = new BaseUi(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.register_button).setOnClickListener(this);
        findViewById(R.id.dont_login_button).setOnClickListener(this);
        findViewById(R.id.login_weixin).setOnClickListener(this);
        findViewById(R.id.login_qq).setOnClickListener(this);
        findViewById(R.id.login_weixin_text).setOnClickListener(this);
        findViewById(R.id.login_qq_text).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.login_button:
                startActivityForResult(LoginActivity.getNewIntent(v.getContext(), LoginActivity.VALUE_TYPE_LOGIN), RequestCode_Login);
                break;
            case R.id.register_button:
                startActivityForResult(LoginActivity.getNewIntent(v.getContext(), LoginActivity.VALUE_TYPE_REGISTER), RequestCode_Login);
                break;
            case R.id.login_weixin:
            case R.id.login_weixin_text:
                thirdParth();
                break;
            case R.id.login_qq:
            case R.id.login_qq_text:
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
        //weixin
        //nickname=having ,
        //openid=oMwZm1Zlkyo8d6fK7ZF1kt8zPdGs,
        //bind_type
        //bind_head_img
        ShareUtil.thirdPartylogin(WechatMoments.NAME, new ShareUtil.LoginCallBack()
        {
            @Override
            public void onComplete(@Nullable PlatformDb platformDb)
            {
                mBaseUi.getLoadingView().hide();
                LogUtil.e("onComplete");
                if(null != platformDb)
                {
                    LogUtil.e(platformDb.getUserId());
                    LogUtil.e(platformDb.getUserName());
                    LogUtil.e(platformDb.getUserIcon());
                    LogUtil.e(platformDb.get("headimgurl"));

                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode_Login && resultCode == RESULT_OK)
        {
            if(mIsNeedJumpMainActivity)
                startActivity(MainActivity.getNewIntent(WelcomeActivity.this));
            setResult(RESULT_OK);
            HXSUser.sendUserInfoUpdateBroadcastReceiver();
            finish();
        }
    }
}
