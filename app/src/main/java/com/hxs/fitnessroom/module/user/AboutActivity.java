package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.util.PhoneInfoUtil;

public class AboutActivity extends BaseActivity {

    private BaseUi mBaseUi;
    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, AboutActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_about);
        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("关于我们");
        mBaseUi.setBackAction(true);

        TextView textView = (TextView) findViewById(R.id.about_version);
        textView.setText("好享瘦健身房v"+ PhoneInfoUtil.AppVersion);
    }
}
