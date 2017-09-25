package com.hxs.fitnessroom.module.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.hxs.fitnessroom.BuildConfig;
import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.base.network.ParamsBuilder;
import com.hxs.fitnessroom.util.Base64Util;
import com.hxs.fitnessroom.util.LocationUtil;
import com.hxs.fitnessroom.util.LogUtil;
import com.hxs.fitnessroom.util.PhoneInfoUtil;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
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
    public static final String KEY_TITLE = "KEY_TITLE";

    private String mWebUrl;
    private String mWebTitle;

    private WebView mWebView;
    private BaseUi mBaseUi;

    public static void gotoWeb(Context context, String url, String title)
    {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_TITLE, title);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.web_activity);

        mWebUrl = getIntent().getStringExtra(KEY_URL);
        mWebTitle = getIntent().getStringExtra(KEY_TITLE);
        LogUtil.dClass(mWebUrl);
        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle(mWebTitle);
        mBaseUi.setBackAction(true);

        initWebView();
        mWebView.loadUrl(mWebUrl);
    }

    private void initWebView()
    {
        mWebView = (com.tencent.smtt.sdk.WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);// 支持js
        mWebView.getSettings().setUseWideViewPort(true); //自适应屏幕
        mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString() + getAPPUserAgent());
        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url)
            {
                LogUtil.dClass("shouldOverrideUrlLoading");
                mBaseUi.getLoadingView().show();
                return super.shouldOverrideUrlLoading(view, url);
            }

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

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError)
            {
                LogUtil.dClass("onReceivedError");
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }

            @Override
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse)
            {
                LogUtil.dClass("onReceivedHttpError");
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                LogUtil.dClass("onProgressChanged");
                super.onProgressChanged(view, newProgress);
                if(newProgress >= 88){
                }else {
                }

            }

            @Override
            public void onReceivedTitle(WebView view, final String title) {
                LogUtil.dClass("onReceivedTitle");
                super.onReceivedTitle(view, title);
//                setDefaleBarAndTitletext(title);
//                try {
//                    setWebTitle(title);
//                }catch (Exception e){
//
//                }
            }

            /**
             * create by shaojunjie at 2017.08.08
             * @see TbsWebviewActivity#initJsConsoleMessageView()
             * @param consoleMessage
             * @return
             */
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage)
            {
                if(BuildConfig.DEBUG)
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currUrl",mWebUrl);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mWebUrl = savedInstanceState.getString("currUrl");
    }


    @Override
    protected boolean onBackUp()
    {
        LogUtil.dClass("onBackUp");
        if(mWebView.canGoBack())
        {
            LogUtil.dClass("canGoBack");
            mWebView.goBack();
            return true;
        }
        return super.onBackUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mWebView)
            mWebView.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null != mWebView)
            mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(null != mWebView)
            mWebView.onPause();
    }
}
