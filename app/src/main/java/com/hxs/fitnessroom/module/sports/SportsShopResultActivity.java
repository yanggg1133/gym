package com.hxs.fitnessroom.module.sports;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.module.openim.AliBaichuanYwIM;
import com.hxs.fitnessroom.module.pay.PayRechargeActivity;

/**
 * 商品购买结果
 * Created by je on 9/21/17.
 */

public class SportsShopResultActivity extends BaseActivity implements View.OnClickListener
{

    private static final String KEY_SHOPSUCCESS = "KEY_SHOPSUCCESS";
    private static final String KEY_SHOPNAME = "KEY_SHOPNAME";
    private static final String KEY_SHOPAMOUNT = "KEY_SHOPAMOUNT";
    private static final String KEY_SHOPCOUNT = "KEY_SHOPCOUNT";
    private static final String KEY_SHOPISFIAL = "KEY_SHOPISFIAL";
    private String mShopName;
    private String mShopAmount;
    private String mShopCount;
    private boolean mShopSuccess;
    private boolean mShopIsFial;


    private BaseUi mBaseUi;
    private TextView shop_name;
    private TextView shop_amount;
    private TextView shop_number;
    private TextView action_button;
    private TextView tips_text;
    private ImageView tips_icon;


    /**
     *
     * @param context
     * @param shopName
     * @param shopAmount
     * @param shopCount
     * @param isSuccess 是否成功
     * @param isFial 是否出货失败 否为余额不足
     * @return
     */
    public static Intent getNewIntent(Context context, String shopName, String shopAmount, String shopCount, boolean isSuccess,boolean isFial)
    {
        Intent intent = new Intent(context,SportsShopResultActivity.class);
        intent.putExtra(KEY_SHOPNAME,shopName);
        intent.putExtra(KEY_SHOPAMOUNT,shopAmount);
        intent.putExtra(KEY_SHOPCOUNT,shopCount);
        intent.putExtra(KEY_SHOPSUCCESS,isSuccess);
        intent.putExtra(KEY_SHOPISFIAL,isFial);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sports_shop_result_activity);

        mBaseUi = new BaseUi(this);
        mBaseUi.setBackAction(true);

        mShopName = getIntent().getStringExtra(KEY_SHOPNAME);
        mShopAmount = getIntent().getStringExtra(KEY_SHOPAMOUNT);
        mShopCount = getIntent().getStringExtra(KEY_SHOPCOUNT);
        mShopSuccess = getIntent().getBooleanExtra(KEY_SHOPSUCCESS,false);
        mShopIsFial = getIntent().getBooleanExtra(KEY_SHOPISFIAL,false);

        shop_name = mBaseUi.findViewById(R.id.shop_name);
        shop_amount = mBaseUi.findViewById(R.id.shop_amount);
        shop_number = mBaseUi.findViewById(R.id.shop_number);
        tips_text = mBaseUi.findViewById(R.id.tips_text);
        tips_icon = mBaseUi.findViewById(R.id.tips_icon);
        action_button = mBaseUi.findViewByIdAndSetClick(R.id.action_button);


        shop_name.setText(mShopName);
        shop_amount.setText(mShopAmount);
        shop_number.setText(mShopCount);
        if(mShopSuccess)
        {
            mBaseUi.setTitle("支付成功");
            action_button.setText("返回健身房");
            tips_text.setText("支付成功，请取走您的商品");
            tips_icon.setImageResource(R.mipmap.ic_pay_chenggong_small);
        }
        else if(mShopIsFial)
        {
            mBaseUi.setTitle("出货失败");
            action_button.setText("联系客服");
            tips_text.setText("商品出货失败");
            tips_icon.setImageResource(R.mipmap.ic_pay_shibai_small);
        }
        else
        {
            mBaseUi.setTitle("支付失败");
            action_button.setText("去充值");
            tips_text.setText("余额不足，支付失败");
            tips_icon.setImageResource(R.mipmap.ic_pay_shibai_small);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.action_button:
                if(mShopSuccess)
                {

                }
                else
                {
                    if(mShopIsFial)//联系客服
                        AliBaichuanYwIM.gotoIM();
                    else//去充值
                        startActivity(PayRechargeActivity.getNewIntent(v.getContext()));
                }
                finish();
                break;
        }
    }
}
