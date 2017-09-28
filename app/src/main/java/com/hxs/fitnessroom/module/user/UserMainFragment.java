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
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.module.main.WelcomeActivity;
import com.hxs.fitnessroom.module.openim.AliBaichuanYwIM;
import com.hxs.fitnessroom.module.pay.PayRechargeActivity;
import com.hxs.fitnessroom.module.user.ui.UserMainUi;
import com.hxs.fitnessroom.module.web.WebActivity;
import com.hxs.fitnessroom.util.LogUtil;

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
        registerUserAccountUpdateBroadcastReceiver();
    }


    @Override
    public void onClick(View v)
    {
        /**
         * 所有入口都要先判断是否登录
         */
        if (!HXSUser.isLogin())
        {
            startActivityForResult(WelcomeActivity.getNewIntent(v.getContext()), RequestCode_Login);
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
                WebActivity.gotoWeb(getBaseActivity(), ConstantsApiUrl.H5_myTutorialList.getH5Url(""));
                break;
            case R.id.setting_exercise://锻炼 H5
                WebActivity.gotoWeb(getBaseActivity(), ConstantsApiUrl.H5_meExercise.getH5Url(""));
                break;
            case R.id.setting_service://客服
                AliBaichuanYwIM.gotoIM();
                break;
            case R.id.setting_message://消息
                startActivity(MessageActivity.getNewIntent(v.getContext()));
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
        LogUtil.dClass("onActivityResult");
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

    @Override
    public void onUserAccountUpdate()
    {
        LogUtil.dClass("更新用户余额："+HXSUser.getUserAccountBalance());
        mUserMainUi.initUserAccount();
    }
}
