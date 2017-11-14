package com.hxs.fitnessroom.module.sports;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.module.pay.PayRechargeActivity;
import com.hxs.fitnessroom.module.pay.model.entity.RechargeBean;
import com.hxs.fitnessroom.module.user.UserWalletDetailActivity;

/**
 * 结束使用健身房界面
 * Created by je on 9/21/17.
 */

public class SportsEndingActivity extends BaseActivity implements View.OnClickListener
{
    private static final String KEY_MONEY = "KEY_MONEY";
    private static final String KEY_BALANCE = "KEY_BALANCE";
    private static final String KEY_BALANCEDESC = "KEY_BALANCEDESC";
    private static final String KEY_USETIME = "KEY_USETIME";
    private static final String KEY_AppointmentDate = "KEY_AppointmentDate";
    private static final String KEY_useDate = "KEY_useDate";
    private static final String KEY_overTime = "KEY_overTime";
    private static final String KEY_allcost = "KEY_allcost";

    private BaseUi mBaseUi;

    private TextView goto_recharge_wallet_detail;
    private TextView goto_recharge_button;
    private View goto_recharge_wallet_detail_icon;
    private TextView use_time;
    private TextView use_time_text;
    private TextView money_text;
    private TextView balance_text;

    private String mMoney;
    private double mBalance;
    private String mUseTime;
    private String mBalanceDesc;
    private String mAppointmentDate;
    private String mOverTime;
    private String mUseDate;
    private String mAllcost;


    private TextView resvere_time_text;
    private TextView out_time_text;
    private TextView money2_text;
    private TextView over_time_text;
    private View over_time_title;
    private View divider_line_11;
    private TextView over_tiem_tip_text;

    public static Intent getNewIntent(Context context,RechargeBean.BalancePay balancePay)
    {
        Intent intent = new Intent(context,SportsEndingActivity.class);
        intent.putExtra(KEY_MONEY,balancePay.money);
        intent.putExtra(KEY_BALANCE,balancePay.balance);
        intent.putExtra(KEY_BALANCEDESC,balancePay.balanceDesc);
        intent.putExtra(KEY_USETIME,balancePay.useTime);
        intent.putExtra(KEY_AppointmentDate,balancePay.appointmentDate);
        intent.putExtra(KEY_overTime,balancePay.overTime);
        intent.putExtra(KEY_useDate,balancePay.useDate);
        intent.putExtra(KEY_allcost,balancePay.allcost);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sports_ending_activity);
        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("结束使用");
        mBaseUi.setBackAction(true);

        mMoney = getIntent().getStringExtra(KEY_MONEY);
        mBalance = getIntent().getDoubleExtra(KEY_BALANCE,0.0);
        mBalanceDesc = getIntent().getStringExtra(KEY_BALANCEDESC);
        mUseTime = getIntent().getStringExtra(KEY_USETIME);
        mAppointmentDate = getIntent().getStringExtra(KEY_AppointmentDate);
        mOverTime = getIntent().getStringExtra(KEY_overTime);
        mUseDate = getIntent().getStringExtra(KEY_useDate);
        mAllcost = getIntent().getStringExtra(KEY_allcost);

        use_time_text = mBaseUi.findViewById(R.id.use_time_text);
        money_text = mBaseUi.findViewById(R.id.money_text);
        balance_text = mBaseUi.findViewById(R.id.balance_text);

        resvere_time_text = mBaseUi.findViewById(R.id.resvere_time_text);
        out_time_text = mBaseUi.findViewById(R.id.out_time_text);
        money2_text = mBaseUi.findViewById(R.id.money2_text);
        over_time_text = mBaseUi.findViewById(R.id.over_time_text);
        over_time_title = mBaseUi.findViewById(R.id.over_time_title);
        divider_line_11 = mBaseUi.findViewById(R.id.divider_line_11);
        over_tiem_tip_text = mBaseUi.findViewById(R.id.over_tiem_tip_text);

        use_time_text.setText(mUseTime);
        money_text.setText(mAllcost);
        balance_text.setText(mBalanceDesc);

        resvere_time_text.setText(mAppointmentDate);
        out_time_text.setText(mUseDate);
        money2_text.setText(mMoney);
        if("00:00:00".equals(mOverTime))
        {
            over_time_text.setVisibility(View.GONE);
            over_time_title.setVisibility(View.GONE);
            divider_line_11.setVisibility(View.GONE);
            over_tiem_tip_text.setVisibility(View.GONE);
        }
        else
        {
            over_time_text.setText(mOverTime);
            over_tiem_tip_text.setVisibility(View.VISIBLE);
        }

        goto_recharge_button = mBaseUi.findViewByIdAndSetClick(R.id.goto_recharge_button);
        goto_recharge_wallet_detail = mBaseUi.findViewByIdAndSetClick(R.id.goto_recharge_wallet_detail);
        goto_recharge_wallet_detail_icon = mBaseUi.findViewByIdAndSetClick(R.id.goto_recharge_wallet_detail_icon);

        if(mBalance < 0.0)
        {
            goto_recharge_button.setText("去充值");
            goto_recharge_wallet_detail.setVisibility(View.VISIBLE);
            goto_recharge_wallet_detail_icon.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.goto_recharge_wallet_detail:
                startActivity(UserWalletDetailActivity.getNewIntent(v.getContext()));
                break;
            case R.id.goto_recharge_button:
                if(mBalance < 0.0)
                    startActivity(PayRechargeActivity.getNewIntent(v.getContext()));
                else
                    startActivity(UserWalletDetailActivity.getNewIntent(v.getContext()));
                break;
        }
        finish();
    }
}
