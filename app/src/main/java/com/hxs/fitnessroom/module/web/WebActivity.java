package com.hxs.fitnessroom.module.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.util.LogUtil;
import com.tencent.smtt.sdk.WebView;

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

    public static void gotoWeb(Context context,String url,String title)
    {
        Intent intent = new Intent(context,WebActivity.class);
        intent.putExtra(KEY_URL,url);
        intent.putExtra(KEY_TITLE,title);
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
    }

    /**
     * 获取默认的必传的APP参数
     * 放到UserAgent中
     * @return
     */
    public String getAPPUserAgent()
    {


        return "";
    }
}
