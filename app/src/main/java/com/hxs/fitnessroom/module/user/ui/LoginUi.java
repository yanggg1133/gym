package com.hxs.fitnessroom.module.user.ui;

import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.module.user.LoginActivity;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.util.ViewUtil;

import static com.hxs.fitnessroom.R.id.register_button;

/**
 * 登录UI操作类
 * Created by je on 9/11/17.
 */

public class LoginUi extends BaseUi
{

    private final TextInputLayout phone_text_layout;
    private final EditText phone_text;
    private final View send_verify_code;
    private final TextInputLayout verifycode_text_layout;
    private final EditText verifycode_text;
    private final TextView login_button;
    private final TextView login_sub_button;
    private final View use_agreement1;
    private final View use_agreement2;
    private final TextView title;
    private final View close_button;

    public LoginUi(BaseActivity baseActivity)
    {
        super(baseActivity);
        baseActivity.setContentView(R.layout.user_login_main);
        phone_text_layout = findViewById(R.id.phone_text_layout);
        phone_text = findViewById(R.id.phone_text);
        verifycode_text_layout = findViewById(R.id.verifycode_text_layout);
        verifycode_text = findViewById(R.id.verifycode_text);
        send_verify_code = findViewById(R.id.send_verify_code);
        login_button = findViewById(R.id.login_button);
        login_sub_button = findViewById(R.id.login_sub_button);
        use_agreement1 = findViewById(R.id.use_agreement1);
        use_agreement2 = findViewById(R.id.use_agreement2);
        close_button = findViewById(R.id.close_button);
        title = findViewById(R.id.title);
        initEditText();
    }

    private void initEditText()
    {
        phone_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        verifycode_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        phone_text.addTextChangedListener(new ViewUtil.ClearErrorTextWatcher(phone_text_layout));
        verifycode_text.addTextChangedListener(new ViewUtil.ClearErrorTextWatcher(verifycode_text_layout));
    }

    public void setOnClick(View.OnClickListener onClick)
    {
        send_verify_code.setOnClickListener(onClick);
        login_button.setOnClickListener(onClick);
        use_agreement1.setOnClickListener(onClick);
        use_agreement2.setOnClickListener(onClick);
        login_sub_button.setOnClickListener(onClick);
        close_button.setOnClickListener(onClick);
    }

    public String getPhoneNum()
    {
        if(!ValidateUtil.isMobileNumber(phone_text.getText().toString()))
        {
            phone_text_layout.setError("手机号码不正确");
            return null;
        }
        return phone_text.getText().toString();
    }

    public void setPhoneError(String phoneError)
    {
        phone_text_layout.setError(phoneError);
    }

    public String getVerifyCode()
    {
        if(verifycode_text.getText().toString().length() != 4)
        {
            verifycode_text_layout.setError("验证码不正确");
            return null;
        }
        return verifycode_text.getText().toString();
    }

    /**
     * 设置验证码是否为已发送状态
     */
    public void setVerifyCodeIsSended(boolean isSended)
    {
        if(isSended)
            send_verify_code.setAlpha(0.4f);
        else
            send_verify_code.setAlpha(1f);
        send_verify_code.setEnabled(!isSended);
    }

    public void setIsLoggingIn(boolean isLoggingIn)
    {
        login_button.setEnabled(!isLoggingIn);
    }

    public void setLoginType(@LoginActivity.LoginType String loginType)
    {
        if(LoginActivity.VALUE_TYPE_LOGIN.equals(loginType))
        {
            title.setText("录登");
            login_button.setText("录登");
            login_sub_button.setText("没有帐号?");
        }
        else if(LoginActivity.VALUE_TYPE_REGISTER.equals(loginType))
        {
            title.setText("注册");
            login_button.setText("注册");
            login_sub_button.setText("已经有帐号?");
        }
        else
        {
            title.setText("绑定手机号");
            login_button.setText("绑定");
            login_sub_button.setVisibility(View.GONE);
        }
    }
}
