package com.hxs.fitnessroom.module.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hxs.fitnessroom.BuildConfig;
import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.util.LocationUtil;
import com.hxs.fitnessroom.util.LogUtil;
import com.hxs.fitnessroom.util.PhoneInfoUtil;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * H5页面
 * Created by je on 9/13/17.
 */

public class WebActivity extends BaseActivity
{
    public static final String KEY_URL = "KEY_URL";

    private String mWebUrl;
    private String mWebTitle;

    public WebView mWebView;
    public BaseUi mBaseUi;

    public static void gotoWeb(Context context, String url)
    {
        Intent intent = new Intent(context, WebActivity.class);
//        if(BuildConfig.DEBUG)
//            intent.putExtra(KEY_URL, "file:///android_asset/test.html");
//        else
            intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.web_activity);

        mWebUrl = getIntent().getStringExtra(KEY_URL);
        LogUtil.dClass(mWebUrl);
        mBaseUi = new BaseUi(this);
        mBaseUi.setBackAction(true);

        initWebView();

        mBaseUi.getLoadingView().show();
        mWebView.loadUrl(mWebUrl);
    }

    /**
     * 初始货webview的一些参数
     */
    private void initWebView()
    {
        mWebView = (com.tencent.smtt.sdk.WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);// 支持js
        mWebView.getSettings().setUseWideViewPort(true); //自适应屏幕
        mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString() + getAPPUserAgent());
        JsController.initJs(this,mWebView);//注册js
        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(final WebView view, String url)
            {
                LogUtil.dClass("onPageFinished");
                mBaseUi.getLoadingView().hide();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError)
            {
                LogUtil.dClass("onReceivedSslError");
                sslErrorHandler.proceed();//接受所有证书HTTS
            }

        });

        mWebView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {

            }

            @Override
            public void onReceivedTitle(WebView view, final String title)
            {
                super.onReceivedTitle(view, title);
                mBaseUi.setTitle(title);
            }

            /**
             * create by shaojunjie at 2017.08.08
             * @param consoleMessage
             * @return
             */
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage)
            {
                if (BuildConfig.DEBUG)
                {
//                    jsConsoleMessage.append("\n-------------------------------------------------------\n");
//                    jsConsoleMessage.append(consoleMessage.message()+"----js:"+consoleMessage.sourceId()+"-----第"+consoleMessage.lineNumber()+"行");
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });
    }

    /**
     * 获取默认的必传的APP参数
     * 放到UserAgent中
     *
     * @return
     */
    public String getAPPUserAgent()
    {
        return new StringBuilder()
                .append("&hxsapp-version-").append(PhoneInfoUtil.AppVersion)//app版本号
                .append("&mobile_system-").append("Android " + PhoneInfoUtil.SystemVersion)//os版本号
                .append("&longitude-").append(LocationUtil.getLongitude())//经度
                .append("&latitude-").append(LocationUtil.getLatitude())//纬度
                .append("&system_resolution-").append(PhoneInfoUtil.DisplaySize)//屏幕分辩率
                .append("&version_number-").append(PhoneInfoUtil.AppVersion)//app版本号
                .append("&register_id-").append(HXSUser.getUserId())//登录的帐号id
                .append("&network_environment-").append(PhoneInfoUtil.getNetworkTypename())//网络状态
                .append("&conversation_id-").append(PhoneInfoUtil.getDonversationId())//会话id
                .append("&channel_number-").append(PhoneInfoUtil.ChannelName)//渠道号
                .append("&user_id-").append(PhoneInfoUtil.PhoneIMEI)//这里的userid 代表android设备的唯一ID
                .append("&sess_token-").append(HXSUser.getUserSessToken())//这里的userid 代表android设备的唯一ID
                .append("&model-").append(PhoneInfoUtil.ModelVersion).toString();//设备名称
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("currUrl", mWebUrl);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        mWebUrl = savedInstanceState.getString("currUrl");
    }


    @Override
    protected boolean onBackUp()
    {
        LogUtil.dClass("onBackUp");
        if (mWebView.canGoBack())
        {
            LogUtil.dClass("canGoBack");
            mWebView.goBack();
            return true;
        }
        return super.onBackUp();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (null != mWebView)
            mWebView.destroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (null != mWebView)
            mWebView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (null != mWebView)
            mWebView.onPause();
    }
}
