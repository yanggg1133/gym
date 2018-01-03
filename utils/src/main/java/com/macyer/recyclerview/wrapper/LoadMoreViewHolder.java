package com.macyer.recyclerview.wrapper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.macyer.utils.R;

/**
 * Created by Lenovo on 2017/12/3.
 */

public class LoadMoreViewHolder extends RecyclerView.ViewHolder
{
    public LoadMoreViewHolder(View parent)
    {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_loadmore_adapter, (ViewGroup) parent, false));
    }
}
