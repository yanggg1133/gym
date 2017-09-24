package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.user.model.UserMessageModel;
import com.hxs.fitnessroom.module.user.model.entity.MessageBean;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.util.ViewUtil;
import com.hxs.fitnessroom.widget.LoadingView;
import com.hxs.fitnessroom.widget.adapterwrapper.LoadMoreAdapterWrapper;

import java.util.ArrayList;
import java.util.List;

import static com.hxs.fitnessroom.Constants.PAGE_DEFAULT_LAST_ID;

/**
 * 我的消息 界面
 */
public class MessageActivity extends BaseActivity implements LoadingView.OnReloadListener,LoadMoreAdapterWrapper.RequestToLoadMoreListener
{

    private BaseUi mBaseUi;
    private RecyclerView message_recyclerview;
    private ArrayList mMessageList = new ArrayList<>();
    private LoadMoreAdapterWrapper loadMoreAdapterWrapper;
    private String mLastId = PAGE_DEFAULT_LAST_ID;
    private String mLastDateTime = "";

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, MessageActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_message_activity);
        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("消息");
        mBaseUi.setBackAction(true);

        initView();
        onReload();
    }

    private void initView()
    {
        message_recyclerview = (RecyclerView) findViewById(R.id.message_recyclerview);
        message_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        loadMoreAdapterWrapper = new LoadMoreAdapterWrapper(new MessageAdapter(),this);
        message_recyclerview.addItemDecoration(ViewUtil.getRecyclerViewItemDecorationCardView(true));
        message_recyclerview.setAdapter(loadMoreAdapterWrapper);
    }

    @Override
    public void onReload()
    {
        new GetMessageTask().execute(this);
    }

    @Override
    public void onLoadMoreRequested()
    {
        onReload();
    }

    class MessageAdapter extends RecyclerView.Adapter
    {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            if (0 == viewType)
            {
                return new MessageDateViewHolder(parent);
            } else if (1 == viewType)
            {
                return new MessageViewHolder(parent);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            if(holder instanceof MessageDateViewHolder)
            {
                ((MessageDateViewHolder)holder).bindData((MessageBean) mMessageList.get(position));
            }else
            {
                ((MessageViewHolder)holder).bindData((MessageBean.NoticeData) mMessageList.get(position));
            }
        }

        @Override
        public int getItemViewType(int position)
        {
            Object o = mMessageList.get(position);
            if (o instanceof MessageBean)
                return 0;
            else
                return 1;
        }

        @Override
        public int getItemCount()
        {
            return mMessageList.size();
        }

        class MessageViewHolder extends RecyclerView.ViewHolder
        {
            TextView message_item_title;
            TextView message_item_msg;

            public MessageViewHolder(View itemView)
            {
                super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.user_message_item, (ViewGroup) itemView,false));
                message_item_msg = (TextView) this.itemView.findViewById(R.id.message_item_msg);
                message_item_title = (TextView) this.itemView.findViewById(R.id.message_item_title);
            }

            public void bindData(MessageBean.NoticeData noticeData)
            {
                message_item_msg.setText(noticeData.content);
                message_item_title.setText(noticeData.title);
            }
        }

        class MessageDateViewHolder extends RecyclerView.ViewHolder
        {
            TextView message_date;

            public MessageDateViewHolder(View itemView)
            {
                super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.user_message_date_item, (ViewGroup) itemView,false));
                message_date = (TextView) this.itemView.findViewById(R.id.message_date);
            }

            public void bindData(MessageBean messageBean)
            {
                message_date.setText(messageBean.notice_time);
            }
        }
    }


    /**
     * 获取通知列表
     */
    class GetMessageTask extends BaseAsyncTask
    {
        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return UserMessageModel.getSystemMessage(mLastId);
        }

        @Override
        protected void onError(@Nullable Exception e)
        {
            super.onError(e);
            if (PAGE_DEFAULT_LAST_ID.equals(mLastId))
            {
                mBaseUi.getLoadingView().showNetworkError();
            } else
            {
                loadMoreAdapterWrapper.onDataReady(LoadMoreAdapterWrapper.ONDATAREADY_ERROR);
            }
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            APIResponse<List<MessageBean>> response = data;
            mBaseUi.getLoadingView().hide();


            if (ValidateUtil.isEmpty(response.data))
            {
                if (!PAGE_DEFAULT_LAST_ID.equals(mLastId))
                    loadMoreAdapterWrapper.onDataReady(0);
                return;
            }
            String lastid = "";
            int dataCount = 0;
            for (MessageBean messageBean : response.data)
            {
                if (!mLastDateTime.equals(messageBean.notice_time))
                {
                    mMessageList.add(messageBean);
                    mLastDateTime = messageBean.notice_time;
                    dataCount += 1;
                }
                mMessageList.addAll(messageBean.notice_data);
                dataCount += messageBean.notice_data.size();
                lastid = messageBean.notice_data.get(messageBean.notice_data.size() - 1).id;
            }

            if (PAGE_DEFAULT_LAST_ID == mLastId)
            {
                loadMoreAdapterWrapper.notifyDataSetChanged();
            } else
            {
                loadMoreAdapterWrapper.onDataReady(dataCount);
            }
            mLastId = lastid;
        }
    }
}
