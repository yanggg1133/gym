package com.hxs.fitnessroom.module.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;

public class SettingActivity extends BaseActivity {
    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }
}
