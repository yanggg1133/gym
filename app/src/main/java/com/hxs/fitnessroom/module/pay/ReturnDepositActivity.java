package com.hxs.fitnessroom.module.pay;

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
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.pay.model.DepositModel;
import com.hxs.fitnessroom.module.pay.model.entity.DepositBean;
import com.hxs.fitnessroom.widget.LoadingView;

/**
 * 退换押金
 * Created by je on 9/22/17.
 */

public class ReturnDepositActivity extends BaseActivity implements View.OnClickListener,LoadingView.OnReloadListener
{
    private BaseUi mBaseUi;
    private TextView pay_amount_text;
    private TextView pay_type_text;
    private View action_comfirm;

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context,ReturnDepositActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_return_deposit_activity);
        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("退押金");
        mBaseUi.setBackAction(true);

        pay_amount_text = mBaseUi.findViewById(R.id.pay_amount_text);
        pay_type_text = mBaseUi.findViewById(R.id.pay_type_text);
        action_comfirm = mBaseUi.findViewByIdAndSetClick(R.id.action_comfirm);

        onReload();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.action_comfirm:
                new SubmitRefundDepositTask().execute(v.getContext());
                break;
        }
    }

    @Override
    public void onReload()
    {
        new GetDepositInfoTask().execute(this);
    }


    /**
     * 查询押金详情
     */
    class GetDepositInfoTask extends BaseAsyncTask
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mBaseUi.getLoadingView().show();
        }

        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return DepositModel.refundDeposit();
        }

        @Override
        protected void onError(@Nullable Exception e)
        {
            super.onError(e);
            mBaseUi.getLoadingView().showNetworkError();
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            mBaseUi.getLoadingView().hide();
            APIResponse<DepositBean> response = data;
            pay_amount_text.setText(response.data.depositDesc);
            pay_type_text.setText(response.data.payDesc);
        }
    }


    /**
     * 提交退押金申请
     */
    class SubmitRefundDepositTask extends BaseAsyncTask
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mBaseUi.getLoadingView().showByNullBackground();
        }

        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return DepositModel.submitRefundDeposit();
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
            setResult(Activity.RESULT_OK);
            HXSUser.updateUserAccountInfoAsync();
            finish();
        }
    }


}
