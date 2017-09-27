package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.module.user.ui.SettingUi;
import com.hxs.fitnessroom.module.web.WebActivity;

public class SettingActivity extends BaseActivity implements View.OnClickListener
{
    private SettingUi mSettingUi;

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSettingUi = new SettingUi(this);
        mSettingUi.setOnClickListener();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.user_protocol:
                WebActivity.gotoWeb(v.getContext(), ConstantsApiUrl.H5_agreement.getH5Url(""), "用户协议");
                break;
            case R.id.deposit_statement:
                WebActivity.gotoWeb(v.getContext(), ConstantsApiUrl.H5_deposit.getH5Url(""), "押金说明");
                break;
            case R.id.top_up_protocol:
                WebActivity.gotoWeb(v.getContext(), ConstantsApiUrl.H5_recharge.getH5Url(""), "充值协议");
                break;
            case R.id.about_our:
                startActivity(AboutActivity.getNewIntent(v.getContext()));
                break;
            case R.id.logout_button:
                HXSUser.signOut();
                finish();
                break;
        }
    }


}
