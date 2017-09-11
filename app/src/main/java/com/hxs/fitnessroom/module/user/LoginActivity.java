package com.hxs.fitnessroom.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.module.user.ui.LoginUi;

/**
 * 登录窗口
 * Created by je on 9/11/17.
 */

public class LoginActivity extends BaseActivity
{

    private LoginUi mLoginUi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_main);
        mLoginUi = new LoginUi(this);
    }
}
