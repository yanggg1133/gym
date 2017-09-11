package com.hxs.fitnessroom.module.user.ui;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;

/**
 * 登录UI操作类
 * Created by je on 9/11/17.
 */

public class LoginUi extends BaseUi
{

    private final TextInputLayout phone_text_layout;
    private final EditText phone_text;
    private final View send_verification_code;
    private final TextInputLayout password_text_layout;
    private final EditText password_text;
    private final TextView login_button;

    public LoginUi(BaseActivity baseActivity)
    {
        super(baseActivity);

        phone_text_layout = findViewById(R.id.phone_text_layout);
        phone_text = findViewById(R.id.phone_text);
        password_text_layout = findViewById(R.id.password_text_layout);
        password_text = findViewById(R.id.password_text);
        send_verification_code = findViewById(R.id.send_verification_code);
        login_button = findViewById(R.id.login_button);
    }

    private void initEditText()
    {

    }
}
