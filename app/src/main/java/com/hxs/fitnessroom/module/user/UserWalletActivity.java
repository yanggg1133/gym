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
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.pay.PayDepositActivity;
import com.hxs.fitnessroom.module.pay.PayRechargeActivity;
import com.hxs.fitnessroom.module.pay.ReturnDepositActivity;
import com.hxs.fitnessroom.module.user.model.UserAccountModel;
import com.hxs.fitnessroom.module.pay.model.entity.UserAccountBean;
import com.hxs.fitnessroom.util.DialogUtil;
import com.hxs.fitnessroom.util.ToastUtil;
import com.hxs.fitnessroom.util.VariableUtil;
import com.hxs.fitnessroom.widget.LoadingView;
import com.hxs.fitnessroom.widget.dialog.ConfirmDialog;

import static com.hxs.fitnessroom.module.pay.model.entity.UserAccountBean.DoorStatus_USING;

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
    private View deposit_tips;
    private TextView deposit_return_tip_confirm;

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, UserWalletActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_wallet_activity);

        registerUserAccountUpdateBroadcastReceiver();

        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("我的钱包");
        mBaseUi.setBackAction(true);
        mBaseUi.getMyToolbar().setRightTextButton("明细", this);

        mBaseUi.findViewByIdAndSetClick(R.id.goto_recharge_button);
        goto_deposit_button = mBaseUi.findViewByIdAndSetClick(R.id.goto_deposit_button);
        money = mBaseUi.findViewByIdAndSetClick(R.id.money);
        deposit = mBaseUi.findViewByIdAndSetClick(R.id.deposit);
        deposit_return_tip_confirm = mBaseUi.findViewByIdAndSetClick(R.id.deposit_return_tip_confirm);
        deposit_return_tip = mBaseUi.findViewById(R.id.deposit_return_tip);
        deposit_tips = mBaseUi.findViewById(R.id.deposit_tips);
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
            case R.id.deposit_return_tip_confirm://底部提示文案 按钮
                if("重新申请".equals(deposit_return_tip_confirm.getText().toString()))
                {
                    gotoReturnDeposit();
                }
                else
                {
                    deposit_return_tip.setVisibility(View.GONE);
                    deposit_return_tip_confirm.setVisibility(View.GONE);
                }
                break;
            case R.id.goto_recharge_button://去充值
                startActivity(PayRechargeActivity.getNewIntent(v.getContext()));
                break;
            case R.id.goto_deposit_button://去交押金
                if (mUserAccountBean.doorStatus == DoorStatus_USING)
                {
                    ToastUtil.toastShort("健身房使用中，无法退回");
                    return ;
                }

                switch (mUserAccountBean.status)
                {
                    case UserAccountBean.AccountStatus_Deposit_Success://押金已退回
                    case UserAccountBean.AccountStatus_NoDeposit://还没交押金
                        startActivity(PayDepositActivity.getNewIntent(v.getContext()));
                        break;
                    case UserAccountBean.AccountStatus_NORMAL://正常
                    case UserAccountBean.AccountStatus_Deposit_Fial: //押金退回失败
                        gotoReturnDeposit();
                        break;
                    case UserAccountBean.AccountStatus_Deposit_Returning://押金退回中
                        break;
                }
                break;
        }
    }

    private void gotoReturnDeposit()
    {
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
        deposit_tips.setVisibility(View.GONE);
        deposit_return_tip_confirm.setVisibility(View.GONE);

        switch (mUserAccountBean.status)
        {
            case UserAccountBean.AccountStatus_NoDeposit://还没交押金
                goto_deposit_button.setText("去支付");
                goto_deposit_button.setEnabled(true);
                goto_deposit_button.setBackgroundResource(R.drawable.bg_gradient_d068ff_e452b1);
                deposit_return_tip.setVisibility(View.GONE);
                deposit_tips.setVisibility(View.VISIBLE);
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
                deposit_tips.setVisibility(View.VISIBLE);
                deposit_return_tip_confirm.setVisibility(View.VISIBLE);
                deposit_return_tip_confirm.setText("确定");
                break;
            case UserAccountBean.AccountStatus_Deposit_Fial: //押金退回失败
                goto_deposit_button.setText("退还");
                deposit_return_tip.setText("您的押金退回失败");
                goto_deposit_button.setEnabled(false);
                goto_deposit_button.setTextColor(getResources().getColor(R.color.colorListItemSubTitleText));
                goto_deposit_button.setBackgroundResource(R.drawable.bg_round_656c91_r50);
                deposit_return_tip.setVisibility(View.VISIBLE);
                deposit_return_tip_confirm.setVisibility(View.VISIBLE);
                deposit_return_tip_confirm.setText("重新申请");
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
            return UserAccountModel.getGymUserAccount(UserAccountModel.FROMPAGE_WALLET);
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


    @Override
    public void onUserAccountUpdate()
    {
        onReload();
    }
}
