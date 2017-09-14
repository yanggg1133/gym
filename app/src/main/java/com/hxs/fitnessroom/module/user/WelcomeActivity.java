package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.module.main.MainActivity;
import com.hxs.fitnessroom.module.user.model.entity.UserBean;
import com.hxs.fitnessroom.util.LogUtil;
import com.hxs.fitnessroom.widget.body.BodyDataDialogFragment;

import static com.baidu.location.h.k.r;


/**
 * 欢迎登录界面
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener
{
    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, WelcomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_welcome_activity);
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.register_button).setOnClickListener(this);
        findViewById(R.id.dont_login_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.login_button:
                startActivityForResult(LoginActivity.getNewIntent(v.getContext(),LoginActivity.VALUE_TYPE_LOGIN),0);
                break;
            case R.id.register_button:
                startActivityForResult(LoginActivity.getNewIntent(v.getContext(),LoginActivity.VALUE_TYPE_REGISTER),0);
                break;
            case R.id.dont_login_button:
                startActivity(MainActivity.getNewIntent(WelcomeActivity.this));
                finish();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            startActivity(MainActivity.getNewIntent(WelcomeActivity.this));
            finish();
        }
    }
}
