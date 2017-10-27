package com.hxs.fitnessroom.module.web;

import android.app.Activity;

import com.hxs.fitnessroom.BuildConfig;
import com.hxs.fitnessroom.module.web.WebActivity;
import com.hxs.fitnessroom.module.web.js.BaseJs;
import com.hxs.fitnessroom.module.web.js.WebTitleJs;
import com.tencent.smtt.sdk.WebView;

import java.util.LinkedList;
import java.util.List;

/**
 * 提供h5与原生的js交互控制器
 * 这属于一个总生产者，webview通过调用{@link #initJs(WebActivity webActivity, WebView)}来注册js函数
 * <p>
 * 具体提供的js函数由各自的类来实现,但实现类必须先继承{@link BaseJsImpl}
 * 同时实现类必须在{@link #initJs(WebActivity webActivity, WebView)} 函数中添加到jsList中
 *
 * @see #initJs(WebActivity webActivity, WebView)
 * @see BaseJsImpl
 * <p>
 * 例子参考:
 * @see WebTitleJs
 * <p>
 * Created by shaojunjie on 17-10-26.
 */

public class JsController
{
    /**
     * @param webView
     */
    public static void initJs(WebActivity webActivity, WebView webView)
    {
        List<BaseJsImpl> jsList = new LinkedList<>();
        jsList.add(new WebTitleJs(webActivity, webView));
        jsList.add(new BaseJs(webActivity, webView));

        for (BaseJsImpl jsObject : jsList)
        {
            webView.addJavascriptInterface(jsObject, jsObject.getJsObjectName());
        }
    }

    /**
     * 所有提供JS的调用类都必须先实现此基类
     *
     * 重要:
     *     由于js的调用有可能是异步的，所以如果所提供的方法是操作UI相关的，
     * 必须使用{@link Activity#runOnUiThread(Runnable)}...等UI线调方法来进行操作，
     * 否则函数调用必定失败
     *
     */
    public static abstract class BaseJsImpl
    {
        private WebActivity webActivity;
        private WebView webView;

        public BaseJsImpl(WebActivity webActivity, WebView webView)
        {
            this.webActivity = webActivity;
            this.webView = webView;
        }

        protected WebActivity getWebActivity()
        {
            return this.webActivity;
        }

        protected WebView getWebView()
        {
            return this.webView;
        }

        public String getJsObjectName()
        {
            return "HxsApp";
        }
    }

}
