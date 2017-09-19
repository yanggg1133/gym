package com.hxs.fitnessroom.module.user;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.module.user.model.NewsAdapter;
import com.hxs.fitnessroom.module.user.model.entity.NewsBean;

import java.util.ArrayList;

public class NewsActivity extends BaseActivity {

    private BaseUi mBaseUi;
    private RecyclerView news_rv;
    private ArrayList<NewsBean> mNewsBeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initView();
        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("消息");
        mBaseUi.setBackAction(true);
        initTest();

    }

    private void initTest() {
        mNewsBeen = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            NewsBean newsBean = new NewsBean();
            newsBean.date = "08月0"+i+"日";
            newsBean.detail = "测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度";
            newsBean.title = "标题标题标题标题标题";
        }
        NewsAdapter newsAdapter = new NewsAdapter(this, mNewsBeen);
        news_rv.setLayoutManager(new LinearLayoutManager(this));
        news_rv.setAdapter(newsAdapter);

    }

    private void initView() {
        news_rv = (RecyclerView) findViewById(R.id.news_rv);

    }
}
