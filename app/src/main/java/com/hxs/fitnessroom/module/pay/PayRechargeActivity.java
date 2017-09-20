package com.hxs.fitnessroom.module.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.pay.mode.RechargeModel;
import com.hxs.fitnessroom.module.pay.mode.entity.TopupAmountBean;
import com.hxs.fitnessroom.util.ToastUtil;
import com.hxs.fitnessroom.util.VariableUtil;
import com.hxs.fitnessroom.util.ViewUtil;
import com.hxs.fitnessroom.widget.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * 帐户余额充值
 * Created by je on 9/18/17.
 */

public class PayRechargeActivity extends BaseActivity implements View.OnClickListener, LoadingView.OnReloadListener
{
    /**
     * 支付成功后activity返回的支付金额
     */
    public static final String RESULT_AMOUNT = "RESULT_AMOUNT";


    private BaseUi mBaseUi;
    private View pay_select_weixin;
    private View pay_select_alipy;
    private View goto_pay;
    private ImageView pay_select_weixin_icon;
    private ImageView pay_select_alipy_icon;
    private RecyclerView amount_list_recycler_view;
    private PayFactory.PayFlow mPayFlow;
    private List<TopupAmountBean> amountList = new ArrayList<>();

    private MyPayBroadcastReceiver myPayBroadcastReceiver;
    private String currentSelectId = "";
    private double currentSelectAmount = 0d;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (!isDestroyed())
            {
                finish();
            }
        }
    };


    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, PayRechargeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_recharge_activity);
        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("充值");
        mBaseUi.setBackAction(true);
        pay_select_weixin = findViewById(R.id.pay_select_weixin);
        pay_select_weixin_icon = (ImageView) findViewById(R.id.pay_select_weixin_icon);
        pay_select_alipy = findViewById(R.id.pay_select_alipy);
        pay_select_alipy_icon = (ImageView) findViewById(R.id.pay_select_alipy_icon);
        goto_pay = findViewById(R.id.goto_pay);

        amount_list_recycler_view = (RecyclerView) findViewById(R.id.amount_list_recycler_view);
        amount_list_recycler_view.setLayoutManager(new GridLayoutManager(this, 4));
        amount_list_recycler_view.setAdapter(new RechargeAdapter());

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
                mBaseUi.getLoadingView().showByNullBackground();
                mPayFlow.payForOrderData(currentSelectId, PayFactory.PAY_ACTION_RECHARGE);
                goto_pay.setEnabled(false);
                break;
        }
    }

    @Override
    public void onReload()
    {
        new QueryDepositTask().execute(this);
    }




    class RechargeAdapter extends RecyclerView.Adapter
    {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            TextView textView = new TextView(parent.getContext());
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.dpToPx(35,parent.getContext()));
            int px = ViewUtil.dpToPx(4,parent.getContext());
            marginLayoutParams.leftMargin = px;
            marginLayoutParams.rightMargin = px;
            textView.setLayoutParams(marginLayoutParams);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.bg_stroke_656c91_r5);
            textView.setTextColor(getResources().getColor(R.color.colorListItemContentText));
            textView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    currentSelectId = ((TopupAmountBean) v.getTag()).id;
                    currentSelectAmount = VariableUtil.stringToDouble(((TopupAmountBean) v.getTag()).amount);
                    notifyDataSetChanged();
                }
            });
            return new RecyclerView.ViewHolder(textView) {};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            ((TextView) holder.itemView).setText( amountList.get(position).name);
            holder.itemView.setTag(amountList.get(position));
            if(currentSelectId.equals(amountList.get(position).id))
            {
                holder.itemView.setBackgroundResource(R.drawable.bg_round_ffa3e4_r5);
                ((TextView) holder.itemView).setTextColor(0xffffffff);
            }
            else
            {
                holder.itemView.setBackgroundResource(R.drawable.bg_stroke_656c91_r5);
                ((TextView) holder.itemView).setTextColor(getResources().getColor(R.color.colorListItemContentText));
            }
        }

        @Override
        public int getItemCount()
        {
            return amountList.size();
        }
    }


    /**
     * 查询 押金金额
     */
    class QueryDepositTask extends BaseAsyncTask
    {

        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return RechargeModel.rechargeList();
        }

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
            mBaseUi.getLoadingView().hide();
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            APIResponse<List<TopupAmountBean>> listAPIResponse = data;
            amountList = listAPIResponse.data;
            currentSelectId = amountList.get(0).id; //把第一个默认为选中
            currentSelectAmount = VariableUtil.stringToDouble(amountList.get(0).amount);
            amount_list_recycler_view.getAdapter().notifyDataSetChanged();
            mBaseUi.getLoadingView().hide();
        }
    }

    /**
     * 接收支付结果通知
     */
    class MyPayBroadcastReceiver extends PayFactory.PayBroadcastReceiver
    {
        @Override
        public void onGetOrderNo(String orderNo)
        {
            mBaseUi.getLoadingView().hide();
            goto_pay.setEnabled(true);
        }

        @Override
        public void onSuccess(int payType)
        {
            goto_pay.setEnabled(true);

            Intent intent = new Intent();
            intent.putExtra(RESULT_AMOUNT,currentSelectAmount);
            PayRechargeActivity.this.setResult(RESULT_OK,intent);
            mBaseUi.getLoadingView().showSuccess("支付成功");
            mHandler.sendEmptyMessageDelayed(0, 1500);//1.5秒后关闭界面
            mBaseUi.getLoadingView().hide();
        }

        @Override
        public void onCancel()
        {
            goto_pay.setEnabled(true);
            mBaseUi.getLoadingView().hide();
        }

        @Override
        public void onFail()
        {
            goto_pay.setEnabled(true);
            mBaseUi.getLoadingView().hide();
            ToastUtil.toastShort("支付失败");
        }
    }
}
