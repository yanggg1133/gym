package com.hxs.fitnessroom.module.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.tencent.smtt.sdk.WebView;

/**
 * H5页面
 * Created by je on 9/13/17.
 */

public class WebActivity extends BaseActivity
{
    public static final String KEY_URL = "KEY_URL";

    private String mWebUrl;
    private WebView mWebView;

    public static void gotoWeb(Context context,String url)
    {
        Intent intent = new Intent(context,WebActivity.class);
        intent.putExtra(KEY_URL,url);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        mWebUrl = getIntent().getStringExtra(KEY_URL);
        mWebView = (com.tencent.smtt.sdk.WebView) findViewById(R.id.web_filechooser);
        mWebView.getSettings().setJavaScriptEnabled(true);// 支持js
        mWebView.getSettings().setUseWideViewPort(true); //自适应屏幕
        mWebView.loadUrl(mWebUrl);
    }
}
