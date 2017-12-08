package com.hxs.fitnessroom.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hxs.fitnessroom.module.pay.PayFactory;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 微信支付回调
 * Created by je on 8/21/17.
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler
{
    private IWXAPI api;

    @Override
    public void onReq(BaseReq baseReq)
    {
        Log.d("WXPayEntryActivity", "onReq" );
    }

    @Override
    public void onResp(BaseResp baseResp)
    {
        int result = 0;

        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            switch (baseResp.errCode)
            {
                case BaseResp.ErrCode.ERR_OK:
                    PayFactory.PayBroadcastReceiver.sendSuccess(WXPayEntryActivity.this,PayFactory.PAY_TYPE_WEIXIN);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    PayFactory.PayBroadcastReceiver.sendCancel(WXPayEntryActivity.this);
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    PayFactory.PayBroadcastReceiver.sendFail(WXPayEntryActivity.this);
                    break;
                case BaseResp.ErrCode.ERR_UNSUPPORT:
                    PayFactory.PayBroadcastReceiver.sendFail(WXPayEntryActivity.this);
                    break;
                default:
                    PayFactory.PayBroadcastReceiver.sendFail(WXPayEntryActivity.this);
                    break;
            }
        }
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, PayFactory.PAY_APP_ID_WEIXIN, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

}
