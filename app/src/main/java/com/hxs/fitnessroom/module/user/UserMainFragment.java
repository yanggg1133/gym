package com.hxs.fitnessroom.module.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.module.pay.PayRechargeActivity;
import com.hxs.fitnessroom.module.setting.SettingActivity;
import com.hxs.fitnessroom.module.user.ui.UserMainUi;

import static com.hxs.fitnessroom.base.baseclass.BaseActivity.RequestCode_Login;
import static com.hxs.fitnessroom.base.baseclass.BaseActivity.RequestCode_Pay_Recharge;


/**
 * 我的 主界面
 * Created by je on 9/2/17.
 */

public class UserMainFragment extends BaseFragment implements View.OnClickListener
{
    private UserMainUi mUserMainUi;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.user_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mUserMainUi = new UserMainUi(this);
        mUserMainUi.setOnClickListener();
        registerUserUpdateBroadcastReceiver();
    }

    @Override
    public void onClick(View v)
    {
        /**
         * 所有入口都要先判断是否登录
         */
        if (!HXSUser.isLogin())
        {
            startActivityForResult(LoginActivity.getNewIntent(v.getContext(), LoginActivity.VALUE_TYPE_LOGIN), RequestCode_Login);
            return;
        }

        switch (v.getId())
        {
            case R.id.user_avatar://头像
                startActivity(UserInfoActivity.getNewIntent(v.getContext()));
                break;
            case R.id.setting_wallet://钱包
                startActivity(UserWalletActivity.getNewIntent(v.getContext()));
                break;
            case R.id.setting_tutorial://教程 H5
                startActivity(WelcomeActivity.getNewIntent(v.getContext()));
                break;
            case R.id.setting_exercise://锻炼 H5
                startActivity(WelcomeActivity.getNewIntent(v.getContext()));
                break;
            case R.id.setting_service://客服
                startActivity(WelcomeActivity.getNewIntent(v.getContext()));
                break;
            case R.id.setting_message://消息
                startActivity(WelcomeActivity.getNewIntent(v.getContext()));
                break;
            case R.id.setting_system://设置
                startActivity(SettingActivity.getNewIntent(v.getContext()));
                break;
            case R.id.goto_recharge://去充值
                startActivityForResult(PayRechargeActivity.getNewIntent(getBaseActivity()), RequestCode_Pay_Recharge);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RequestCode_Login && resultCode == Activity.RESULT_OK)
        {
            mUserMainUi.initUserInfo();
        }
    }

    @Override
    public void onUserUpdate()
    {
        mUserMainUi.initUserInfo();
    }
}
