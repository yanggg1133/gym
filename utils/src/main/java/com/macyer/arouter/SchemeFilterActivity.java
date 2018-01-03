package com.macyer.arouter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by Lenovo on 2017/12/22.
 * http://m.macyer.com
 */

public class SchemeFilterActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        直接通过ARouter处理外部Uri
        Uri uri = getIntent().getData();
        ARouter.getInstance().build(uri).navigation(this, new NavCallback() {
            @Override
            public void onArrival(Postcard postcard) {
                finish();
            }
        });
    }
}




/*
<h2>自定义Scheme[通常来说都是这样的]</h2>
<p><a href="arouter://m.aliyun.com/test/activity1">arouter://m.aliyun.com/test/activity1</a></p>
<p><a href="arouter://m.aliyun.com/test/activity1?url=https%3a%2f%2fm.abc.com%3fa%3db%26c%3dd">测试URL Encode情况</a></p>
<p><a href="arouter://m.aliyun.com/test/activity1?name=alex&age=18&boy=true&high=180&obj=%7b%22name%22%3a%22jack%22%2c%22id%22%3a666%7d">arouter://m.aliyun.com/test/activity1?name=alex&age=18&boy=true&high=180&obj={"name":"jack","id":"666"}</a></p>
<p><a href="arouter://m.aliyun.com/test/activity2">arouter://m.aliyun.com/test/activity2</a></p>
<p><a href="arouter://m.aliyun.com/test/activity2?key1=value1">arouter://m.aliyun.com/test/activity2?key1=value1</a></p>
<p><a href="arouter://m.aliyun.com/test/activity3?name=alex&age=18&boy=true&high=180">arouter://m.aliyun.com/test/activity3?name=alex&age=18&boy=true&high=180</a></p>

<h2>App Links[防止被App屏蔽]</h2>
<p><a href="http://m.aliyun.com/test/activity1">http://m.aliyun.com/test/activity1</a></p>
<p><a href="http://m.aliyun.com/test/activity2">http://m.aliyun.com/test/activity2</a></p>
 */
 
 
 