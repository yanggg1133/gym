package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.pay.PayDepositActivity;
import com.hxs.fitnessroom.module.pay.PayRechargeActivity;
import com.hxs.fitnessroom.module.pay.model.UserAccountModel;
import com.hxs.fitnessroom.module.pay.model.entity.UserAccountBean;
import com.hxs.fitnessroom.widget.LoadingView;

/**
 * 我的钱包
 * Created by je on 9/17/17.
 */

public class UserWalletActivity extends BaseActivity implements View.OnClickListener,LoadingView.OnReloadListener
{

    private BaseUi mBaseUi;
    private UserAccountBean mUserAccountBean;
    private TextView goto_deposit_button;
    private TextView deposit;
    private TextView money;

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

        mBaseUi.findViewByIdAndSetClick(R.id.goto_recharge_button);
        goto_deposit_button = mBaseUi.findViewByIdAndSetClick(R.id.goto_deposit_button);
        money = mBaseUi.findViewByIdAndSetClick(R.id.money);
        deposit = mBaseUi.findViewByIdAndSetClick(R.id.deposit);
        onReload();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.toolbar_right_text_button://明细
                break;
            case R.id.goto_recharge_button://去充值
                startActivityForResult(PayRechargeActivity.getNewIntent(v.getContext()), RequestCode_Pay_Recharge);
                break;
            case R.id.goto_deposit_button://去交押金
                startActivityForResult(PayDepositActivity.getNewIntent(v.getContext()), RequestCode_Pay_Deposit);
                break;
        }
    }

    @Override
    public void onReload()
    {
        new QueryAccountTask().execute(this);
    }


    private void initViewData()
    {
        //deposit.setText("￥"+mUserAccountBean.);
        money.setText("￥"+mUserAccountBean.balance);
        if(mUserAccountBean.status == UserAccountBean.AccountStatus_NoDeposit)
        {
            goto_deposit_button.setEnabled(true);
        }
        else if(mUserAccountBean.status == UserAccountBean.AccountStatus_NORMAL)
        {
            goto_deposit_button.setText("退还");
        }

        //goto_deposit_button
    }

    /**
     * 查询用户帐户情况
     */
    class QueryAccountTask extends BaseAsyncTask
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mBaseUi.getLoadingView().show();
        }

        @Override
        protected void onError(@Nullable Exception e)
        {
            super.onError(e);
            mBaseUi.getLoadingView().showNetworkError();
        }

        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return UserAccountModel.getGymUserAccount();
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            mBaseUi.getLoadingView().hide();
            APIResponse<UserAccountBean> userAccount = data;
            mUserAccountBean = userAccount.data;
            initViewData();

        }
    }


}
