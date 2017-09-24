package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.module.user.model.LoginModel;
import com.hxs.fitnessroom.module.user.model.entity.UserBean;
import com.hxs.fitnessroom.module.user.ui.LoginUi;
import com.hxs.fitnessroom.module.web.WebActivity;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.widget.body.BodyDataDialogFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 登录窗口
 * Created by je on 9/11/17.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener,BodyDataDialogFragment.OnNextStepCallBack,TextView.OnEditorActionListener
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
        setContentView(R.layout.user_login_main);
        mLoginUi = new LoginUi(this);
        mLoginUi.setOnClick(this);
        mSendType = getIntent().getStringExtra(KEY_TYPE);
        mLoginUi.setLoginType(mSendType);
    }

    /**
     * 处理键盘登记键行为
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if( actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
        {
            onClick(findViewById(R.id.login_button));
            return true;
        }
        return false;
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
            case R.id.close_button:
                finish();
                break;
            case R.id.use_agreement1:
            case R.id.use_agreement2:
                WebActivity.gotoWeb(v.getContext(), ConstantsApiUrl.H5_deposit.getH5Url(""), "用户协议");
                break;
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
            super.onError(e);
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
            super.onError(e);
            mLoginUi.setIsLoggingIn(false);
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            APIResponse<UserBean> response = data;
            HXSUser.saveCurrentUserForLocal(response.data);
            HXSUser.updateUserAccountInfoAsync();
            setResult(RESULT_OK);
            //如果性别为未填
            if( UserBean.SEX_TYPE_NULL == response.data.sex)
            {
                BodyDataDialogFragment.show(getSupportFragmentManager(), LoginActivity.this);
            }
            else
            {
                finish();
            }
        }
    }

    private int bodyHeight;
    @UserBean.SexType
    private int sex;
    private int year;
    private int month;

    @Override
    public void onBodyInfo(int bodyHeight, @UserBean.SexType int sex)
    {
        this.bodyHeight = bodyHeight;
        this.sex = sex;
    }

    @Override
    public void onBirthday(String year, String month)
    {
        this.year = new Integer(year.split(" ")[0].trim());
        this.month= new Integer(month.split(" ")[0].trim());
        HXSUser hxsUser = HXSUser.getHXSUser();
        if(null != HXSUser.getHXSUser())
        {
            hxsUser.setSex(this.sex);
            hxsUser.setBirthday(this.year,this.month);
            hxsUser.setBodyHigh(this.bodyHeight);
            hxsUser.saveUserInfoAsync();
            finish();
        }
    }

}


