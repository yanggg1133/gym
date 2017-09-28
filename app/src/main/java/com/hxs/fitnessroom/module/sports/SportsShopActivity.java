package com.hxs.fitnessroom.module.sports;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.pay.PayFactory;
import com.hxs.fitnessroom.module.pay.model.RechargeModel;
import com.hxs.fitnessroom.module.pay.model.entity.RechargeBean;

/**
 * 购物界面
 * Created by je on 9/21/17.
 */

public class SportsShopActivity extends BaseActivity implements View.OnClickListener
{
    private static final String KEY_SHOPID = "KEY_SHOPID";
    private static final String KEY_SHOPNAME = "KEY_SHOPNAME";
    private static final String KEY_SHOPAMOUNT = "KEY_SHOPAMOUNT";
    private static final String KEY_SHOPCOUNT = "KEY_SHOPCOUNT";

    public static Intent getNewIntent(Context context,String shopName,String shopAmount,String shopCount,String shopId)
    {
        Intent intent = new Intent(context,SportsShopActivity.class);
        intent.putExtra(KEY_SHOPNAME,shopName);
        intent.putExtra(KEY_SHOPAMOUNT,shopAmount);
        intent.putExtra(KEY_SHOPCOUNT,shopCount);
        intent.putExtra(KEY_SHOPID,shopId);
        return intent;
    }


    private String mShopName;
    private String mShopAmount;
    private String mShopCount;
    private String mShopId;



    private BaseUi mBaseUi;
    private TextView shop_name;
    private TextView shop_amount;
    private TextView shop_number;
    private TextView number_tip;
    private TextView sum_amount_tip;
    private TextView action_pay_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sports_shop_activity);
        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("购物");
        mBaseUi.setBackAction(true);

        mShopName = getIntent().getStringExtra(KEY_SHOPNAME);
        mShopAmount = getIntent().getStringExtra(KEY_SHOPAMOUNT);
        mShopCount = getIntent().getStringExtra(KEY_SHOPCOUNT);
        mShopId = getIntent().getStringExtra(KEY_SHOPID);

        shop_name = mBaseUi.findViewById(R.id.shop_name);
        shop_amount = mBaseUi.findViewById(R.id.shop_amount);
        shop_number = mBaseUi.findViewById(R.id.shop_number);
        number_tip = mBaseUi.findViewById(R.id.number_tip);
        sum_amount_tip = mBaseUi.findViewById(R.id.sum_amount_tip);
        action_pay_button = mBaseUi.findViewByIdAndSetClick(R.id.action_pay_button);


        shop_name.setText(mShopName);
        shop_amount.setText(mShopAmount);
        shop_number.setText(mShopCount);

        number_tip.setText("共"+mShopCount);
        sum_amount_tip.setText("总计"+mShopAmount);
        HXSUser.updateUserAccountInfoAsync();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.action_pay_button:
                new SportsPayTask().execute(SportsShopActivity.this);
                break;
        }
    }

    /**
     * 发起结算
     */
    class SportsPayTask extends BaseAsyncTask
    {


        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return RechargeModel.addRecharge(PayFactory.PAY_TYPE_BALANCE,mShopId,PayFactory.PAY_ACTION_SHOP);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mBaseUi.getLoadingView().showByNullBackground();
        }

        @Override
        protected void onAPIError(APIResponse apiResponse)
        {
            if(APIResponse.error_insufficient_balance.equals(apiResponse.code ))
            {
                startActivity(SportsShopResultActivity.getNewIntent(SportsShopActivity.this,mShopName,mShopAmount,mShopCount,false,false));
                mBaseUi.getLoadingView().hide();
            }
            else if(APIResponse.error_order_fail.equals(apiResponse.code ))
            {
                startActivity(SportsShopResultActivity.getNewIntent(SportsShopActivity.this,mShopName,mShopAmount,mShopCount,false,true));
                mBaseUi.getLoadingView().hide();
            }
            else
            {
                super.onAPIError(apiResponse);
            }
        }

        @Override
        protected void onError(@Nullable Exception e)
        {
            super.onError(e);
            mBaseUi.getLoadingView().hide();
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            mBaseUi.getLoadingView().hide();
            HXSUser.updateUserAccountInfoAsync();
            startActivity(SportsShopResultActivity.getNewIntent(SportsShopActivity.this,mShopName,mShopAmount,mShopCount,true,false));
            finish();
        }
    }
}
