package com.hxs.fitnessroom.widget.adapterwrapper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxs.fitnessroom.R;

import java.util.List;


/**
 * 加载更多包装类
 * 用于滑动到底部时自动响应加载更多的操作
 * 只适用于RecyclerView
 *
 * @author shaojunjie on 17-5-8
 * @Email fgnna@qq.com
 */
public class LoadMoreAdapterWrapper extends RecyclerView.Adapter
{
    private static final String TAG = "LoadMoreAdapterWrapper";
    private static final int LOAD_MORE_TYPE = 999;
    public static final int ONDATAREADY_ERROR = -1;

    private final RequestToLoadMoreListener mRequestToLoadMoreListener;
    /**
     * 加载更多时是否显示  加载中。。。的item
     * 一般使用{@ling GridLayoutManager}的时候要设置为false
     */
    private boolean mIsShowLoading = true;

    private RecyclerView.Adapter mAdapter;
    /**
     * 开始加载更多前先记录一个最后的条数索引
     */
    private int mTempLastPosition;
    /**
     * 滑到尾部是否继续加载更多
     */
    private boolean keepOnAppending = true;

    /**
     * 是否加载中
     */
    private boolean mOnLoading = false;

    public LoadMoreAdapterWrapper(RecyclerView.Adapter adapter, RequestToLoadMoreListener requestToLoadMoreListener)
    {
        mAdapter = adapter;
        mRequestToLoadMoreListener = requestToLoadMoreListener;
    }

    /**
     * @param adapter
     * @param requestToLoadMoreListener
     * @param isShowLoading             {@link #mIsShowLoading}
     * @see #mIsShowLoading
     */
    public LoadMoreAdapterWrapper(RecyclerView.Adapter adapter, RequestToLoadMoreListener requestToLoadMoreListener, boolean isShowLoading)
    {
        mAdapter = adapter;
        mRequestToLoadMoreListener = requestToLoadMoreListener;
        mIsShowLoading = isShowLoading;
    }

    /**
     * 新增的数据条数
     * @param addDataCount
     */
    public void onDataReady(int addDataCount)
    {
        if (mOnLoading)
        {
            if (mIsShowLoading)
                notifyItemRemoved(mTempLastPosition);
            if (addDataCount == 0)
            {
                //没有新数据
                keepOnAppending = false;
            } else if(addDataCount != ONDATAREADY_ERROR)
            {
                int updateIndex = mTempLastPosition + (mIsShowLoading ? 0 : 1);
                notifyItemRangeInserted(updateIndex, updateIndex + addDataCount);
                //有新数据
            }
            mOnLoading = false;

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == LOAD_MORE_TYPE)
        {
            return new LoadMoreViewHolder(parent);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (getItemViewType(position) == LOAD_MORE_TYPE)
        {
            return;
        }
        mAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount()
    {
        if (mAdapter.getItemCount() == 0)
            return 0;
        if (!keepOnAppending || !mIsShowLoading)
            return mAdapter.getItemCount();
        return mAdapter.getItemCount() + 1;
    }



    @Override
    public int getItemViewType(int position)
    {
        if (position == (mAdapter.getItemCount() - (!mIsShowLoading ? 1 : 0)) && keepOnAppending)
        {
            if (!mOnLoading && null != mRequestToLoadMoreListener)
            {
                mTempLastPosition = position;
                mRequestToLoadMoreListener.onLoadMoreRequested();
                mOnLoading = true;
            }
            if (mIsShowLoading)
                return LOAD_MORE_TYPE;
        }
        return mAdapter.getItemViewType(position);
    }


    public interface RequestToLoadMoreListener
    {
        /**
         * 触发加载更多事件
         * 加载完成必须后调用 进行更新
         */
        void onLoadMoreRequested();
    }
}

class LoadMoreViewHolder extends RecyclerView.ViewHolder
{
    public LoadMoreViewHolder(View parent)
    {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_loadmore_adapter, (ViewGroup) parent, false));
    }
}
