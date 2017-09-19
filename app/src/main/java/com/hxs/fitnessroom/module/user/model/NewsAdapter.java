package com.hxs.fitnessroom.module.user.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.module.user.model.entity.NewsBean;

import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by je on 19/09/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{
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

    class NewsViewHolder extends ViewHolder {
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
