package com.hxs.fitnessroom.module.web.js;

import android.webkit.JavascriptInterface;

import com.hxs.fitnessroom.module.web.JsController;
import com.hxs.fitnessroom.module.web.WebActivity;
import com.tencent.smtt.sdk.WebView;

/**
 * 设置页面标题
 * Created by shaojunjie on 17-10-26.
 */

public class WebTitleJs extends JsController.BaseJsImpl
{
    public WebTitleJs(WebActivity webActivity, WebView webView)
    {
        super(webActivity, webView);
    }

    @JavascriptInterface
    public void setTitle(final String title)
    {
        getWebActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                getWebActivity().mBaseUi.setTitle(title);
            }
        });
    }
}
