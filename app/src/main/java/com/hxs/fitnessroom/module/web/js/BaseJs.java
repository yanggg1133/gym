package com.hxs.fitnessroom.module.web.js;

import android.webkit.JavascriptInterface;

import com.hxs.fitnessroom.module.web.JsController;
import com.hxs.fitnessroom.module.web.WebActivity;
import com.tencent.smtt.sdk.WebView;

/**
 * 基础函数
 * Created by shaojunjie on 17-10-26.
 */

public class BaseJs extends JsController.BaseJsImpl
{
    public BaseJs(WebActivity webActivity, WebView webView)
    {
        super(webActivity, webView);
    }

    @JavascriptInterface
    public void openNewWindow(final String url)
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
}
