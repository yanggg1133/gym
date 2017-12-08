package com.hxs.fitnessroom.module.web.js;

import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.module.home.StoreReserveActivity;
import com.hxs.fitnessroom.module.main.WelcomeActivity;
import com.hxs.fitnessroom.module.openim.AliBaichuanYwIM;
import com.hxs.fitnessroom.module.pay.PayDepositActivity;
import com.hxs.fitnessroom.module.web.JsController;
import com.hxs.fitnessroom.module.web.WebActivity;
import com.tencent.smtt.sdk.WebView;

/**
 * 界面跳转js
 * Created by shaojunjie on 17-10-26.
 */

public class JumpWindowJs extends JsController.BaseJsImpl
{
    public JumpWindowJs(WebActivity webActivity, WebView webView)
    {
        super(webActivity, webView);
    }

    /**
     * 跳转缴押金
     */
    @JavascriptInterface
    public void jumpPayDeposit()
    {
        getWebActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(PayDepositActivity.getNewIntent(getWebActivity()));
            }
        });
    }


    /**
     * 跳转预约
     */
    @JavascriptInterface
    public void jumpStoreReserve(final String storeId)
    {
        getWebActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(StoreReserveActivity.getNewIntent(getWebActivity(),storeId));
            }
        });
    }


    /**
     * 跳转客服
     */
    @JavascriptInterface
    public void jumpCustomerService()
    {
        getWebActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                AliBaichuanYwIM.gotoIM();
            }
        });
    }


    /**
     * 获取用户登录状态
     */
    @JavascriptInterface
    public boolean isLogin()
    {
        return HXSUser.isLogin();
    }

    /**
     * 跳转登录界面
     */
    @JavascriptInterface
    public void jumpLogin()
    {
        getWebActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(WelcomeActivity.getNewIntent(getWebActivity()));
            }
        });
    }

    private void startActivity(Intent intent)
    {
        getWebActivity().startActivity(intent);
    }
}
