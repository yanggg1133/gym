package fitnessroom.hxs.com.paylib;

import android.app.Activity;
import android.content.Context;

import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;

/**
 * 支付调用接入类
 * Created by je on 8/19/17.
 */

public class PayFactory
{
    public static String WEIXIN_APP_ID = "";

    private Activity mContext;

    private PayFactory(Activity context)
    {
        mContext = context;
    }

    public static PayFactory build(Activity context)
    {
        return new PayFactory(context);
    }
    /**
     * 威富通
     * @param tokenId
     */
    public void weifutong(String tokenId,String appid)
    {
        WEIXIN_APP_ID = appid;
        RequestMsg msg = new RequestMsg();
        msg.setTokenId(tokenId); //token_id为服务端预下单返回
        msg.setTradeType(MainApplication.WX_APP_TYPE); //app支付类型
        msg.setAppId(appid);//appid为商户自己在微信开放平台的应用appid
        PayPlugin.unifiedAppPay(mContext, msg);
    }

//    public static final String KEY_PAY_TYPE = "KEY_PAY_TYPE";
//    public static final int PAY_TYPE_ALIPAY = 1;
//    public static final int PAY_TYPE_WEIXIN = 2;
//
//    public static final java.lang.String PAY_APP_ID_WEIXIN = "wxc8dffd8b43fbc9a4";
//
//    public static Alipay CreateAlipay(Context context)
//    {
//        return new Alipay(baseActivity);
//
//    }
//
//    public static WeixinPay CreateWeixinPay(BaseActivity baseActivity)
//    {
//        return new WeixinPay(baseActivity);
//    }
//
//
//    /**
//     * 支付宝
//     */
//    private static class Alipay extends PayFlow
//    {
//        public Alipay(BaseActivity baseActivity)
//        {
//            super(baseActivity);
//        }
//
//        @Override
//        void gotoPay(final PayBean payBean)
//        {
//            final String orderInfo = payBean.alipay;   // 订单信息
//
//            Runnable payRunnable = new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    PayTask alipay = new PayTask(getContext());
//                    Map result = alipay.payV2(orderInfo,true);
//                    PayResult payResult = new PayResult(result);
//                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//                    String resultStatus = payResult.getResultStatus();
//                    // 判断resultStatus 为9000则代表支付成功
//                    if (TextUtils.equals(resultStatus, "9000")) {
//                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        getContext().runOnUiThread(new Runnable()
//                        {
//                            @Override
//                            public void run()
//                            {
//                                PayFactory.PayBroadcastReceiver.sendSuccess(getContext(),getPayType());
//                            }
//                        });
//                    } else {
//                        getContext().runOnUiThread(new Runnable()
//                        {
//                            @Override
//                            public void run()
//                            {
//                                PayFactory.PayBroadcastReceiver.sendFail(getContext());
//                            }
//                        });
//                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                    }
//                }
//            };
//            // 必须异步调用
//            Thread payThread = new Thread(payRunnable);
//            payThread.start();
//
//        }
//
//        @Override
//        int getPayType()
//        {
//            return PAY_TYPE_ALIPAY;
//        }
//
//    }
//
//    /**
//     * 微信
//     */
//    private static class WeixinPay extends PayFlow
//    {
//        public WeixinPay(BaseActivity baseToolBarActivity)
//        {
//            super(baseActivity);
//        }
//
//        @Override
//        void gotoPay(PayBean payBean)
//        {
//            final IWXAPI msgApi = WXAPIFactory.createWXAPI(getContext(), PAY_APP_ID_WEIXIN);
//            msgApi.registerApp(PAY_APP_ID_WEIXIN);
//            PayReq request = new PayReq();
//            request.appId = PAY_APP_ID_WEIXIN;
//
//            request.partnerId =  payBean.wxpay.partnerid;
//            request.prepayId= payBean.wxpay.prepayid;
//            request.packageValue = payBean.wxpay.packageValue;
//            request.nonceStr= payBean.wxpay.noncestr;
//            request.timeStamp= payBean.wxpay.timestamp;
//            request.sign= payBean.wxpay.sign;
//
//            if(!msgApi.sendReq(request))
//            {
//                TT.showLong(getContext(),"失败，请检查微信是否为最新版本");
//                PayFactory.PayBroadcastReceiver.sendFail(getContext());
//            }
//        }
//
//        @Override
//        int getPayType()
//        {
//            return PAY_TYPE_WEIXIN;
//        }
//
//    }
//
//
//    /**
//     * 公共支付流程
//     */
//    public static abstract class PayFlow
//    {
//        private BaseActivity baseActivity;
//
//        public PayFlow(BaseActivity baseActivity)
//        {
//            this.baseActivity = baseActivity;
//        }
//
//        BaseActivity getContext()
//        {
//            return this.baseToolBarActivity;
//        }
//
//        public void payForOrderId(String order_id)
//        {
//            getOrderInfo(order_id,null,null);
//        }
//        public void payForOrderData(String orderData, AddressBaseBean.AddressBean addressBean)
//        {
//            getOrderInfo(null,orderData,addressBean);
//        }
//
//        private void getOrderInfo(@Nullable String order_id, @Nullable String order_data, @Nullable AddressBaseBean.AddressBean addressBean)
//        {
//            XutilHttps.addOrder(order_id,order_data,addressBean,getPayType(), new MyCallback.CallbackFace()
//            {
//                @Override
//                public void onSuccess(Object result, int apiInt, Object tag)
//                {
//                    getContext().onSuccess(result, apiInt, tag);
//                    if (apiInt == XutilHttps.addOrderInt)
//                    {
//                        BaseMapBean<PayBean> payBase = AppUtil.jsonToBean(result.toString(), new TypeReference<BaseMapBean<PayBean>>() {});
//                        if(getContext().handelResult(payBase) && null != payBase.data)
//                        {
//                            PayFactory.PayBroadcastReceiver.sendOrderNo(getContext(),payBase.data.OrderNo);
//                            gotoPay(payBase.data);
//                        }
//                    }
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback, int apiInt, Object tag, String sendUrl, String sendData)
//                {
//                    getContext().onError(ex, isOnCallback, apiInt, tag, sendUrl, sendData);
//                    PayFactory.PayBroadcastReceiver.sendFail(getContext());
//                }
//
//                @Override
//                public void onCancelled(Callback.CancelledException cex, int apiInt, Object tag)
//                {
//                    getContext().onCancelled(cex, apiInt, tag);
//                }
//
//                @Override
//                public void onFinished(int apiInt, Object tag)
//                {
//                    getContext().onFinished(apiInt, tag);
//                }
//            });
//
//        }
//
//        abstract void gotoPay(PayBean payBean);
//
//        abstract int getPayType();
//
//    }
//
//
//    /**
//     * 实现接收支付结果 的状态提示
//     */
//    public static abstract class PayBroadcastReceiver extends BroadcastReceiver
//    {
//
//
//
//        private static String PAY_BROADCAST = "";
//        private static final String PAY_STATUS_KEY = "PAY_STATUS_KEY";
//        private static final String PAY_OrderNo = "PAY_OrderNo";
//        private static final int  PAY_STATUS_SUCCESS = 1;
//        private static final int  PAY_STATUS_CANCEL = 2;
//        private static final int  PAY_STATUS_FAIL = 3;
//        private static final int  PAY_STATUS_ORDERNO = 4;//接收订单ID
//
//        private static String LAST_ORDER_NO = "";
//
//        public void  reSetPayBroadcast(Context context)
//        {
//            PAY_BROADCAST = context.getClass().getName();
//        }
//
//        public void register(Context context)
//        {
//            IntentFilter intentFilter = new IntentFilter();
//            PAY_BROADCAST = context.getClass().getName();
//            //设置接收广播的类型
//            intentFilter.addAction(PAY_BROADCAST);
//            context.registerReceiver(this,intentFilter);
//        }
//
//        public void unregister(Context context)
//        {
//            context.unregisterReceiver(this);
//        }
//
//        @Override
//        public void onReceive(Context context, Intent intent)
//        {
//            int payStatus = intent.getIntExtra(PAY_STATUS_KEY,PAY_STATUS_FAIL);
//            switch (payStatus)
//            {
//                case PAY_STATUS_SUCCESS:
//                    onSuccess(intent.getIntExtra(KEY_PAY_TYPE,PAY_TYPE_WEIXIN));
//                    break;
//                case PAY_STATUS_CANCEL:
//                    onCancel();
//                    break;
//                case PAY_STATUS_FAIL:
//                    onFail();
//                    break;
//                case PAY_STATUS_ORDERNO:
//                    onGetOrderNo(intent.getStringExtra(PAY_OrderNo));
//                    break;
//
//
//            }
//        }
//
//        public abstract void onGetOrderNo(String orderNo);
//
//        public abstract void onSuccess(int payType);
//        public abstract void onCancel();
//        public abstract void onFail();
//
//
//        public static void sendSuccess(Context context, int type)
//        {
//
//            Intent intent = new Intent(PAY_BROADCAST);
//            intent.putExtra(PAY_STATUS_KEY,PAY_STATUS_SUCCESS);
//            intent.putExtra(KEY_PAY_TYPE,type);
//            context.sendBroadcast(intent);
//        }
//
//        public static void sendCancel(Context context)
//        {
//            Intent intent = new Intent(PAY_BROADCAST);
//            intent.putExtra(PAY_STATUS_KEY,PAY_STATUS_CANCEL);
//            context.sendBroadcast(intent);
//        }
//
//        public static void sendFail(Context context)
//        {
//            Intent intent = new Intent(PAY_BROADCAST);
//            intent.putExtra(PAY_STATUS_KEY,PAY_STATUS_FAIL);
//            context.sendBroadcast(intent);
//        }
//        public static void sendOrderNo(Context context, String OrderNo)
//        {
//            Intent intent = new Intent(PAY_BROADCAST);
//            intent.putExtra(PAY_STATUS_KEY,PAY_STATUS_ORDERNO);
//            intent.putExtra(PAY_OrderNo,OrderNo);
//            context.sendBroadcast(intent);
//        }
//    }

}
