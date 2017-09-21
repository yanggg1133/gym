package com.hxs.fitnessroom.module.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.pay.model.DepositModel;
import com.hxs.fitnessroom.module.pay.model.entity.DepositBean;
import com.hxs.fitnessroom.util.ToastUtil;
import com.hxs.fitnessroom.widget.LoadingView;

/**
 * 充押金
 * Created by je on 9/18/17.
 */

public class PayDepositActivity extends BaseActivity implements View.OnClickListener, LoadingView.OnReloadListener
{
    private BaseUi mBaseUi;
    private View pay_select_weixin;
    private View pay_select_alipy;
    private View goto_pay;
    private ImageView pay_select_weixin_icon;
    private ImageView pay_select_alipy_icon;

    private PayFactory.PayFlow mPayFlow;
    private TextView pay_amount;//押金View

    private String mAmount;//押金金额
    private MyPayBroadcastReceiver myPayBroadcastReceiver;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if(!isDestroyed())
            {
                finish();
            }
        }
    };


    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, PayDepositActivity.class);
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
        pay_select_weixin_icon = (ImageView) findViewById(R.id.pay_select_weixin_icon);
        pay_select_alipy = findViewById(R.id.pay_select_alipy);
        pay_select_alipy_icon = (ImageView) findViewById(R.id.pay_select_alipy_icon);
        goto_pay = findViewById(R.id.goto_pay);
        pay_amount = (TextView) findViewById(R.id.pay_amount);


        pay_select_weixin.setOnClickListener(this);
        pay_select_alipy.setOnClickListener(this);
        goto_pay.setOnClickListener(this);

        mPayFlow = PayFactory.createAlipay(this);//先默认为阿里支付

        onReload();

        myPayBroadcastReceiver = new MyPayBroadcastReceiver();
        myPayBroadcastReceiver.register(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        myPayBroadcastReceiver.unregister(this);
        mHandler.removeMessages(0);
        mHandler = null;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.pay_select_weixin:
                pay_select_alipy_icon.setImageResource(R.mipmap.ic_pay_select_no);
                pay_select_weixin_icon.setImageResource(R.mipmap.ic_pay_select_yes);
                mPayFlow = PayFactory.createWeixinPay(this);
                break;
            case R.id.pay_select_alipy:
                pay_select_alipy_icon.setImageResource(R.mipmap.ic_pay_select_yes);
                pay_select_weixin_icon.setImageResource(R.mipmap.ic_pay_select_no);
                mPayFlow = PayFactory.createAlipay(this);
                break;
            case R.id.goto_pay:
                mPayFlow.payForOrderData("", PayFactory.PAY_ACTION_DEPOSIT);
                goto_pay.setEnabled(false);
                break;
        }
    }

    @Override
    public void onReload()
    {
        new QueryDepositTask().execute(this);
    }


    /**
     * 查询 押金金额
     */
    class QueryDepositTask extends BaseAsyncTask
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
            return DepositModel.deposit();
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
            APIResponse<DepositBean> depositBeanAPIResponse = data;
            mAmount = depositBeanAPIResponse.data.deposit;
            pay_amount.setText("￥" + mAmount);
            mBaseUi.getLoadingView().hide();
        }
    }

    /**
     * 接收支付结果通知
     */
    class MyPayBroadcastReceiver extends PayFactory.PayBroadcastReceiver
    {
        @Override
        public void onGetOrderNo(String orderNo) {
            goto_pay.setEnabled(true);
        }

        @Override
        public void onSuccess(int payType)
        {
            goto_pay.setEnabled(true);
            PayDepositActivity.this.setResult(RESULT_OK);
            mBaseUi.getLoadingView().showSuccess("支付成功");
            mHandler.sendEmptyMessageDelayed(0,1500);//1.5秒后关闭界面
        }

        @Override
        public void onCancel() {
            goto_pay.setEnabled(true);

        }

        @Override
        public void onFail()
        {
            goto_pay.setEnabled(true);
            ToastUtil.toastShort("支付失败");
        }
    }
}
