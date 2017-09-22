package com.hxs.fitnessroom.module.user;

import android.app.Activity;
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
import com.hxs.fitnessroom.module.pay.ReturnDepositActivity;
import com.hxs.fitnessroom.module.user.model.UserAccountModel;
import com.hxs.fitnessroom.module.pay.model.entity.UserAccountBean;
import com.hxs.fitnessroom.util.DialogUtil;
import com.hxs.fitnessroom.util.VariableUtil;
import com.hxs.fitnessroom.widget.LoadingView;
import com.hxs.fitnessroom.widget.dialog.ConfirmDialog;

/**
 * 我的钱包
 * Created by je on 9/17/17.
 */

public class UserWalletActivity extends BaseActivity implements View.OnClickListener, LoadingView.OnReloadListener
{

    private BaseUi mBaseUi;
    private UserAccountBean mUserAccountBean;
    private TextView goto_deposit_button;
    private TextView deposit;
    private TextView money;
    private TextView deposit_return_tip;

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, UserWalletActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_wallet_activity);
        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("钱包");
        mBaseUi.setBackAction(true);
        mBaseUi.getMyToolbar().setRightTextButton("明细", this);

        mBaseUi.findViewByIdAndSetClick(R.id.goto_recharge_button);
        goto_deposit_button = mBaseUi.findViewByIdAndSetClick(R.id.goto_deposit_button);
        money = mBaseUi.findViewByIdAndSetClick(R.id.money);
        deposit = mBaseUi.findViewByIdAndSetClick(R.id.deposit);
        deposit_return_tip = mBaseUi.findViewById(R.id.deposit_return_tip);
        onReload();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.toolbar_right_text_button://明细
                startActivity(UserWalletDetailActivity.getNewIntent(v.getContext()));
                break;
            case R.id.goto_recharge_button://去充值
                startActivityForResult(PayRechargeActivity.getNewIntent(v.getContext()), RequestCode_Pay_Recharge);
                break;
            case R.id.goto_deposit_button://去交押金
                switch (mUserAccountBean.status)
                {
                    case UserAccountBean.AccountStatus_Deposit_Success://押金已退回
                    case UserAccountBean.AccountStatus_NoDeposit://还没交押金
                        startActivityForResult(PayDepositActivity.getNewIntent(v.getContext()), RequestCode_Pay_Deposit);
                        break;
                    case UserAccountBean.AccountStatus_NORMAL://正常
                    case UserAccountBean.AccountStatus_Deposit_Fial: //押金退回失败
                        if (0d > VariableUtil.stringToDouble(mUserAccountBean.balance))
                        {
                            DialogUtil.showConfirmDialog("您还有尚未支付的费用\n支付后才能退回押金","充值","不退了",  getSupportFragmentManager(),
                                    new ConfirmDialog.OnDialogCallbackAdapter()
                                    {
                                        @Override
                                        public void onCancelClick()
                                        {
                                            startActivityForResult(PayRechargeActivity.getNewIntent(UserWalletActivity.this),RequestCode_Activity_ReturnDeposit);
                                        }
                                    });

                        } else
                        {
                            startActivity(ReturnDepositActivity.getNewIntent(UserWalletActivity.this));
                        }
                        break;
                    case UserAccountBean.AccountStatus_Deposit_Returning://押金退回中
                        break;
                }
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
        deposit.setText("￥" + mUserAccountBean.deposit);
        money.setText("￥" + mUserAccountBean.balance);
        goto_deposit_button.setTextColor(0xffffffff);
        switch (mUserAccountBean.status)
        {
            case UserAccountBean.AccountStatus_NoDeposit://还没交押金
                goto_deposit_button.setText("去支付");
                goto_deposit_button.setEnabled(true);
                goto_deposit_button.setBackgroundResource(R.drawable.bg_gradient_d068ff_e452b1);
                deposit_return_tip.setVisibility(View.GONE);
                break;
            case UserAccountBean.AccountStatus_NORMAL://正常
                goto_deposit_button.setText("退还");
                goto_deposit_button.setEnabled(true);
                deposit_return_tip.setVisibility(View.GONE);
                break;
            case UserAccountBean.AccountStatus_Deposit_Returning://押金退回中
                goto_deposit_button.setText("退还");
                goto_deposit_button.setTextColor(getResources().getColor(R.color.colorListItemSubTitleText));
                goto_deposit_button.setBackgroundResource(R.drawable.bg_round_656c91_r50);
                goto_deposit_button.setEnabled(false);
                deposit_return_tip.setVisibility(View.VISIBLE);
                deposit_return_tip.setText("您的押金退回正在审核中");
                break;
            case UserAccountBean.AccountStatus_Deposit_Success://押金已退回
                goto_deposit_button.setText("去支付");
                deposit_return_tip.setText("您的押金已退回");
                goto_deposit_button.setEnabled(true);
                deposit_return_tip.setVisibility(View.VISIBLE);
                break;
            case UserAccountBean.AccountStatus_Deposit_Fial: //押金退回失败
                goto_deposit_button.setText("退还");
                goto_deposit_button.setText("您的押金退回失败，请联系客服");
                goto_deposit_button.setEnabled(true);
                deposit_return_tip.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case RequestCode_Activity_ReturnDeposit:
                if(resultCode == Activity.RESULT_OK)
                {
                    onReload();
                }
                break;
        }
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
