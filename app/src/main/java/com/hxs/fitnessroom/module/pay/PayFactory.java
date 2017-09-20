package com.hxs.fitnessroom.module.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.pay.model.RechargeModel;
import com.hxs.fitnessroom.module.pay.model.entity.RechargeBean;
import com.hxs.fitnessroom.util.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

/**
 * 支付调用接入类
 *
 * 使用者只需调用 {@link #createAlipay(BaseActivity)}
 *            或 {@link #createWeixinPay(BaseActivity)}
 *
 *
 * Created by je on 8/19/17.
 */

public class PayFactory
{

    public static final String KEY_PAY_TYPE = "KEY_PAY_TYPE";
    /**
     * 充值类型
     */
    public static final int PAY_TYPE_ALIPAY = 1;//支付宝支付
    public static final int PAY_TYPE_WEIXIN = 2;//微信支付
    public static final int PAY_TYPE_BALANCE = 3;//帐户余额

    /**
     * 充值动作
     */
    public static final int PAY_ACTION_RECHARGE = 1; //充值
    public static final int PAY_ACTION_DEPOSIT = 2; //押金
    public static final int PAY_ACTION_SHOP = 3; //售货机
    public static final int PAY_ACTION_SPORTS = 4; //健身房结算


    @IntDef({PAY_TYPE_ALIPAY, PAY_TYPE_WEIXIN, PAY_TYPE_BALANCE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PayType
    {
    }

    @IntDef({PAY_ACTION_RECHARGE, PAY_ACTION_DEPOSIT, PAY_ACTION_SHOP,PAY_ACTION_SPORTS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PayAction
    {
    }
    public static final String PAY_APP_ID_WEIXIN = "wxd65b048e01717c3c";

    public static Alipay createAlipay(BaseActivity baseActivity)
    {
        return new Alipay(baseActivity);

    }

    public static WeixinPay createWeixinPay(BaseActivity baseActivity)
    {
        return new WeixinPay(baseActivity);
    }

    /**
     * 支付宝
     */
    private static class Alipay extends PayFlow
    {
        public Alipay(BaseActivity baseActivity)
        {
            super(baseActivity);
        }

        @Override
        protected void gotoPay(final RechargeBean payBean)
        {
            final String orderInfo = payBean.alipay;   // 订单信息

            Runnable payRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    PayTask alipay = new PayTask(getContext());
                    Map result = alipay.payV2(orderInfo, true);
                    PayResult payResult = new PayResult(result);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000"))
                    {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        getContext().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                PayFactory.PayBroadcastReceiver.sendSuccess(getContext(), getPayType());
                            }
                        });
                    } else
                    {
                        getContext().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                PayFactory.PayBroadcastReceiver.sendFail(getContext());
                            }
                        });
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    }
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();

        }

        @Override
        protected int getPayType()
        {
            return PAY_TYPE_ALIPAY;
        }

    }

    /**
     * 微信
     */
    private static class WeixinPay extends PayFlow
    {
        public WeixinPay(BaseActivity baseActivity)
        {
            super(baseActivity);
        }

        @Override
        protected void gotoPay(RechargeBean payBean)
        {
            final IWXAPI msgApi = WXAPIFactory.createWXAPI(getContext(), PAY_APP_ID_WEIXIN);
            msgApi.registerApp(PAY_APP_ID_WEIXIN);
            PayReq request = new PayReq();
            request.appId = PAY_APP_ID_WEIXIN;

            request.partnerId = payBean.wxpay.partnerid;
            request.prepayId = payBean.wxpay.prepayid;
            request.packageValue = payBean.wxpay.packageValue;
            request.nonceStr = payBean.wxpay.noncestr;
            request.timeStamp = payBean.wxpay.timestamp;
            request.sign = payBean.wxpay.sign;

            if (!msgApi.sendReq(request))
            {
                ToastUtil.toastLong("失败，请检查微信是否为最新版本");
                PayFactory.PayBroadcastReceiver.sendFail(getContext());
            }
        }

        @Override
        protected int getPayType()
        {
            return PAY_TYPE_WEIXIN;
        }

    }


    /**
     * 公共支付流程
     */
    public static abstract class PayFlow
    {
        private BaseActivity baseActivity;

        public PayFlow(BaseActivity baseActivity)
        {
            this.baseActivity = baseActivity;
        }

        BaseActivity getContext()
        {
            return this.baseActivity;
        }

        /**
         * 售货机 专用
         *
         * @param order_id
         */
        public void payForOrderId(String order_id)
        {
            getOrderInfo(PAY_TYPE_BALANCE, order_id, PAY_ACTION_SHOP);
        }

        /**
         * 充值，充押金使用
         *
         * @param amount
         * @param action
         */
        public void payForOrderData(String amount, @PayAction int action)
        {
            getOrderInfo(getPayType(), amount, action);
        }

        private void getOrderInfo(@PayType final int payMode, final String amount, final int action)
        {
            new BaseAsyncTask()
            {
                @Override
                protected APIResponse doWorkBackground() throws Exception
                {
                    return RechargeModel.addRecharge(payMode,amount,action);
                }

                @Override
                protected void onError(@Nullable Exception e)
                {
                    super.onError(e);
                    PayFactory.PayBroadcastReceiver.sendFail(getContext());
                }

                @Override
                protected void onSuccess(APIResponse data)
                {
                    APIResponse<RechargeBean> response = data;
                    PayFactory.PayBroadcastReceiver.sendOrderNo(baseActivity,response.data.orderNo);
                    gotoPay(response.data);
                }
            }.execute(baseActivity);
        }

        abstract void gotoPay(RechargeBean payBean);

        abstract int getPayType();

    }


    /**
     * 实现接收支付结果 的状态提示
     */
    public static abstract class PayBroadcastReceiver extends BroadcastReceiver
    {


        private static String PAY_BROADCAST = "";
        private static final String PAY_STATUS_KEY = "PAY_STATUS_KEY";
        private static final String PAY_OrderNo = "PAY_OrderNo";
        private static final int PAY_STATUS_SUCCESS = 1;
        private static final int PAY_STATUS_CANCEL = 2;
        private static final int PAY_STATUS_FAIL = 3;
        private static final int PAY_STATUS_ORDERNO = 4;//接收订单ID

        private static String LAST_ORDER_NO = "";
        private static int LAST_PAY_MODE = PAY_TYPE_ALIPAY;

        public void reSetPayBroadcast(Context context)
        {
            PAY_BROADCAST = context.getClass().getName();
        }

        public void register(Context context)
        {
            IntentFilter intentFilter = new IntentFilter();
            PAY_BROADCAST = context.getClass().getName();
            //设置接收广播的类型
            intentFilter.addAction(PAY_BROADCAST);
            context.registerReceiver(this, intentFilter);
        }

        public void unregister(Context context)
        {
            context.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            int payStatus = intent.getIntExtra(PAY_STATUS_KEY, PAY_STATUS_FAIL);
            switch (payStatus)
            {
                case PAY_STATUS_SUCCESS:
                    LAST_PAY_MODE = intent.getIntExtra(KEY_PAY_TYPE, PAY_TYPE_ALIPAY);
                    onSuccess(LAST_PAY_MODE);
                    new BaseAsyncTask(){
                        @Override
                        protected APIResponse doWorkBackground() throws Exception
                        {
                            return RechargeModel.orderQuery(LAST_ORDER_NO,LAST_PAY_MODE);
                        }
                        @Override
                        protected void onSuccess(APIResponse data)
                        {
                            //不做处理
                        }
                    }.execute(context.getApplicationContext());
                    break;
                case PAY_STATUS_CANCEL:
                    onCancel();
                    break;
                case PAY_STATUS_FAIL:
                    onFail();
                    break;
                case PAY_STATUS_ORDERNO:
                    LAST_ORDER_NO = intent.getStringExtra(PAY_OrderNo);
                    onGetOrderNo(intent.getStringExtra(LAST_ORDER_NO));
                    break;


            }
        }

        public abstract void onGetOrderNo(String orderNo);

        public abstract void onSuccess(int payType);

        public abstract void onCancel();

        public abstract void onFail();


        public static void sendSuccess(Context context, int type)
        {

            Intent intent = new Intent(PAY_BROADCAST);
            intent.putExtra(PAY_STATUS_KEY, PAY_STATUS_SUCCESS);
            intent.putExtra(KEY_PAY_TYPE, type);
            context.sendBroadcast(intent);
        }

        public static void sendCancel(Context context)
        {
            Intent intent = new Intent(PAY_BROADCAST);
            intent.putExtra(PAY_STATUS_KEY, PAY_STATUS_CANCEL);
            context.sendBroadcast(intent);
        }

        public static void sendFail(Context context)
        {
            Intent intent = new Intent(PAY_BROADCAST);
            intent.putExtra(PAY_STATUS_KEY, PAY_STATUS_FAIL);
            context.sendBroadcast(intent);
        }

        public static void sendOrderNo(Context context, String OrderNo)
        {
            Intent intent = new Intent(PAY_BROADCAST);
            intent.putExtra(PAY_STATUS_KEY, PAY_STATUS_ORDERNO);
            intent.putExtra(PAY_OrderNo, OrderNo);
            context.sendBroadcast(intent);
        }
    }

}

/**
 * 支付宝自己的请求返回结构
 */
class PayResult
{
    private String resultStatus;
    private String result;
    private String memo;

    public PayResult(Map<String, String> rawResult)
    {
        if (rawResult == null)
        {
            return;
        }

        for (String key : rawResult.keySet())
        {
            if (TextUtils.equals(key, "resultStatus"))
            {
                resultStatus = rawResult.get(key);
            } else if (TextUtils.equals(key, "result"))
            {
                result = rawResult.get(key);
            } else if (TextUtils.equals(key, "memo"))
            {
                memo = rawResult.get(key);
            }
        }
    }

    @Override
    public String toString()
    {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }

    /**
     * @return the resultStatus
     */
    public String getResultStatus()
    {
        return resultStatus;
    }

    /**
     * @return the memo
     */
    public String getMemo()
    {
        return memo;
    }

    /**
     * @return the result
     */
    public String getResult()
    {
        return result;
    }
}