package com.hxs.fitnessroom.module.pay.mode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;

/**
 * 充押金
 * Created by je on 9/18/17.
 */

public class PayDepositActivity extends BaseActivity implements View.OnClickListener
{
    private BaseUi mBaseUi;
    private View pay_select_weixin;
    private View pay_select_alipy;
    private View goto_pay;
    private ImageView pay_select_weixin_icon;
    private ImageView pay_select_alipy_icon;


    public static Intent getNewIntent(Context context)
    {
        return new Intent(context,PayDepositActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_deposit_activity);
        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("支付押金");
        mBaseUi.setBackAction(true);
        pay_select_weixin = findViewById(R.id.pay_select_weixin);
        pay_select_weixin_icon = (ImageView)findViewById(R.id.pay_select_weixin_icon);
        pay_select_alipy = findViewById(R.id.pay_select_alipy);
        pay_select_alipy_icon = (ImageView)findViewById(R.id.pay_select_alipy_icon);
        goto_pay = findViewById(R.id.goto_pay);
        pay_select_weixin.setOnClickListener(this);
        pay_select_alipy.setOnClickListener(this);
        goto_pay.setOnClickListener(this);

        mBaseUi.getLoadingView().show();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.pay_select_weixin:
                pay_select_alipy_icon.setImageResource(R.mipmap.ic_pay_select_no);
                pay_select_weixin_icon.setImageResource(R.mipmap.ic_pay_select_yes);
                break;
            case R.id.pay_select_alipy:
                pay_select_alipy_icon.setImageResource(R.mipmap.ic_pay_select_yes);
                pay_select_weixin_icon.setImageResource(R.mipmap.ic_pay_select_no);
                break;
            case R.id.goto_pay:
                break;
        }
    }


    
}
