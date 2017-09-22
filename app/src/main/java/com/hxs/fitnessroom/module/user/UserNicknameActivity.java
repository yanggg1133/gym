package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseCallBack;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.module.user.model.entity.UserBean;
import com.hxs.fitnessroom.util.ValidateUtil;

/**
 * 用户修改昵称
 * Created by je on 9/17/17.
 */

public class UserNicknameActivity extends BaseActivity implements View.OnClickListener , TextView.OnEditorActionListener
{

    private TextInputLayout user_nickname_layout;
    private EditText user_nickname;
    private BaseUi mBaseUi;

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context,UserNicknameActivity.class);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_nickname_activity);
        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("昵称");
        mBaseUi.setBackAction(true);
        mBaseUi.getMyToolbar().setRightTextButton("保存",this);
        user_nickname_layout = (TextInputLayout) findViewById(R.id.user_nickname_layout);
        user_nickname = (EditText) findViewById(R.id.user_nickname);
        user_nickname.setOnEditorActionListener(this);
        user_nickname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
    }

    /**
     * 处理键盘保存键行为
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if( actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
        {
            save();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.toolbar_right_text_button:
                save();
                break;
        }
    }

    private boolean isSaveing = false;
    private void save()
    {
        if(ValidateUtil.isEmpty(user_nickname.getText().toString()))
        {
            user_nickname_layout.setError("不能为空");
            return;
        }
        if(isSaveing)
            return;

        UserBean userBean = new UserBean();
        userBean.nickname = user_nickname.getText().toString();

        mBaseUi.getLoadingView().showByNullBackground();
        isSaveing = true;
        HXSUser.saveUserInfoAsync(userBean, new BaseCallBack()
        {
            @Override
            public void onSuccess(Object o)
            {
                mBaseUi.getLoadingView().hide();
                isSaveing = false;
                finish();
            }

            @Override
            public void onFailure(Object o)
            {
                mBaseUi.getLoadingView().hide();
                isSaveing = false;
                if(ValidateUtil.isNotEmpty(o))
                {
                    user_nickname_layout.setError(o.toString());
                }
            }
        });
    }



}
