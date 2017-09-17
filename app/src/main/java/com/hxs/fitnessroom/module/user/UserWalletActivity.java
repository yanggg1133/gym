package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;

/**
 * 我的钱包
 * Created by je on 9/17/17.
 */

public class UserWalletActivity extends BaseActivity implements View.OnClickListener
{

    private BaseUi mBaseUi;

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context,UserWalletActivity.class);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_wallet_activity);
        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("钱包");
        mBaseUi.setBackAction(true);
        mBaseUi.getMyToolbar().setRightTextButton("明细",this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.toolbar_right_text_button://明细
                break;
        }
    }
}
