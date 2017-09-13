package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.View;
import android.widget.Toast;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.user.model.LoginModel;
import com.hxs.fitnessroom.module.user.model.entity.UserBean;
import com.hxs.fitnessroom.module.user.ui.LoginUi;
import com.hxs.fitnessroom.util.ValidateUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 登录窗口
 * Created by je on 9/11/17.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener
{
    public static final String KEY_TYPE = "KEY_TYPE";//界面显示类型

    public static final String VALUE_TYPE_LOGIN = "login";//登录
    public static final String VALUE_TYPE_REGISTER = "register";//注册
    public static final String VALUE_TYPE_BINDPHONE = "bindMobile";//绑定手机(第三方登录)

    @StringDef({VALUE_TYPE_LOGIN, VALUE_TYPE_REGISTER, VALUE_TYPE_BINDPHONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoginType {}


    public static Intent getNewIntent(Context context,@LoginType String type)
    {
        Intent intent = new Intent(context,LoginActivity.class);
        intent.putExtra(KEY_TYPE,type);
        return intent;
    }


    private LoginUi mLoginUi;
    private String mSendType;
    private String mPhoneNum;
    private String mVerifyCode;

    @SuppressWarnings({"WrongConstant"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mLoginUi = new LoginUi(this);
        mLoginUi.setOnClick(this);
        mSendType = getIntent().getStringExtra(KEY_TYPE);

        mLoginUi.setLoginType(mSendType);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.send_verify_code:
                mPhoneNum = mLoginUi.getPhoneNum();
                if(ValidateUtil.isNotEmpty(mPhoneNum))
                {
                    mLoginUi.setVerifyCodeIsSended(true);
                    new SendSMSTask().execute(LoginActivity.this);
                }
                break;
            case R.id.login_button:
                mPhoneNum = mLoginUi.getPhoneNum();
                if(ValidateUtil.isEmpty(mPhoneNum) )
                    return;
                mVerifyCode = mLoginUi.getVerifyCode();
                if(ValidateUtil.isNotEmpty(mVerifyCode))
                {
                    mLoginUi.setIsLoggingIn(true);
                    new LoginTask().execute(LoginActivity.this);
                }
                break;
            case R.id.use_agreement1:
            case R.id.use_agreement2:
                // TODO: 9/13/17 跳转用户协议
            case R.id.login_sub_button:
                if(VALUE_TYPE_LOGIN.equals(mSendType))
                {
                    mLoginUi.setLoginType(VALUE_TYPE_REGISTER);
                    mSendType = VALUE_TYPE_REGISTER;
                }
                else
                {
                    mLoginUi.setLoginType(VALUE_TYPE_LOGIN);
                    mSendType = VALUE_TYPE_LOGIN;
                }
                break;
        }
    }

    /**
     * 发送验证码
     */
    class SendSMSTask extends BaseAsyncTask
    {
        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return LoginModel.sendSMS(mPhoneNum,mSendType);
        }

        @Override
        protected void onError(Exception e)
        {
            Toast.makeText(LoginActivity.this,"不明异常",Toast.LENGTH_SHORT).show();
            mLoginUi.setVerifyCodeIsSended(false);
        }

        @Override
        protected void onAPIError(APIResponse apiResponse)
        {
            if(ValidateUtil.isNotEmpty(apiResponse.msg))
                mLoginUi.setPhoneError(apiResponse.msg);
            mLoginUi.setVerifyCodeIsSended(false);
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            Toast.makeText(LoginActivity.this,"短信已发送",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 录登/注册/第三方登录
     */
    class LoginTask extends BaseAsyncTask
    {
        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return LoginModel.Login(mPhoneNum,mSendType,mVerifyCode,null);
        }

        @Override
        protected void onAPIError(APIResponse apiResponse)
        {
            if(ValidateUtil.isNotEmpty(apiResponse.msg))
                mLoginUi.setPhoneError(apiResponse.msg);
            mLoginUi.setIsLoggingIn(false);
        }

        @Override
        protected void onError(@Nullable Exception e)
        {
            mLoginUi.setIsLoggingIn(false);
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            APIResponse<UserBean> response = data;
            HXSUser.saveCurrentUser(response.data);
            setResult(RESULT_OK);
            //如果身高数据为空，统一跳转设置身高，生日流程
            if(ValidateUtil.isEmpty(response.data.body_high))
            {

            }
            finish();
            mLoginUi.setIsLoggingIn(false);
        }
    }

}


