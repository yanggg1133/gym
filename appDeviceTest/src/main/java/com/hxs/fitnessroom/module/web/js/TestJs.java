package com.hxs.fitnessroom.module.web.js;

import android.webkit.JavascriptInterface;

import com.hxs.fitnessroom.module.web.JsController;
import com.hxs.fitnessroom.module.web.WebActivity;
import com.tencent.smtt.sdk.WebView;

/**
 * 基础函数
 * Created by shaojunjie on 17-10-26.
 */

public class TestJs extends JsController.BaseJsImpl
{
    public TestJs(WebActivity webActivity, WebView webView)
    {
        super(webActivity, webView);
    }

    @JavascriptInterface
    public void postMessage(final String url)
    {
        getWebActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                WebActivity.gotoWeb(getWebActivity(),url);
            }
        });
    }

    @Override
    public String getJsObjectName()
    {
        return "webkit";
    }
}
