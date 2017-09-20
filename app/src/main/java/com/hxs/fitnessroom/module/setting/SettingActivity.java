package com.hxs.fitnessroom.module.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.module.user.AboutActivity;
import com.hxs.fitnessroom.module.user.ui.SettingUi;

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private SettingUi mSettingUi;
    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSettingUi = new SettingUi(this);
        mSettingUi.setOnClickListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_protocol:
                break;
            case R.id.deposit_statement:
                break;
            case R.id.top_up_protocol:
                break;
            case R.id.about_our:
                startActivity(AboutActivity.getNewIntent(v.getContext()));
                break;
        }
    }


}
