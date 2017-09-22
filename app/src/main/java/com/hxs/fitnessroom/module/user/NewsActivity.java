package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.module.user.model.entity.NewsBean;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends BaseActivity {

    private BaseUi mBaseUi;
    private RecyclerView news_rv;
    private ArrayList<NewsBean> mNewsBeen;

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, NewsActivity.class);
    }


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
            newsBean.msg = "测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度测度";
            newsBean.title = "标题标题标题标题标题";
            mNewsBeen.add(newsBean);
        }
        NewsAdapter newsAdapter = new NewsAdapter(this, mNewsBeen);
        news_rv.setLayoutManager(new LinearLayoutManager(this));
        news_rv.setAdapter(newsAdapter);

    }

    private void initView() {
        news_rv = (RecyclerView) findViewById(R.id.news_list);

    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{
        Context mContext;
        List<NewsBean> mBeanList;
        private final LayoutInflater mInflater;

        public NewsAdapter(Context context, List<NewsBean> beanList) {
            mContext = context;
            mBeanList = beanList;
            mInflater = LayoutInflater.from(mContext);

        }

        @Override
        public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.news_item_layout, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsViewHolder holder, int position) {
            NewsBean newsBean = mBeanList.get(position);
            holder.mNewsTitle.setText(newsBean.title);
            holder.mNewsMsg.setText(newsBean.msg);
            holder.mNewsDate.setText(newsBean.date);


        }

        @Override
        public int getItemCount() {
            return mBeanList.size();
        }

        class NewsViewHolder extends RecyclerView.ViewHolder
        {
            TextView mNewsTitle;
            TextView mNewsMsg;
            TextView mNewsDate;

            public NewsViewHolder(View itemView) {
                super(itemView);
                mNewsDate = (TextView) itemView.findViewById(R.id.news_date);
                mNewsMsg = (TextView) itemView.findViewById(R.id.news_item_msg);
                mNewsTitle = (TextView) itemView.findViewById(R.id.news_item_title);

            }
        }


    }
}
