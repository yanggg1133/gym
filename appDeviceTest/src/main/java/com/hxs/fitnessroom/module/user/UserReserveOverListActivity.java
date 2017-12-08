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
import com.hxs.fitnessroom.module.home.model.StoreModel;
import com.hxs.fitnessroom.module.home.model.entity.StoreAppointment;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.widget.LoadingView;
import com.hxs.fitnessroom.widget.adapterwrapper.LoadMoreAdapterWrapper;

import java.util.ArrayList;
import java.util.List;

import static com.hxs.fitnessroom.Constants.PAGE_DEFAULT_LAST_ID;

/**
 * 已完成的预约
 * Created by shaojunjie on 17-11-6.
 */

public class UserReserveOverListActivity extends BaseActivity implements LoadingView.OnReloadListener
{

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, UserReserveOverListActivity.class);
    }

    private BaseUi mBaseUi;
    private RecyclerView mRecyclerView;
    private List<StoreAppointment> mBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_list_layout);

        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("历史预约");
        mBaseUi.setBackAction(true);

        initView();
        onReload();
    }

    private void initView()
    {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ReserveAdapter(this));
    }

    @Override
    public void onReload()
    {
        new GetStoreAppointmentList().go(this);
    }


    class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.ReserveViewHolder>
    {
        Context mContext;
        private final LayoutInflater mInflater;

        public ReserveAdapter(Context context)
        {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }


        @Override
        public ReserveAdapter.ReserveViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = mInflater.inflate(R.layout.user_reserve_list_item, parent, false);
            return new ReserveAdapter.ReserveViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ReserveAdapter.ReserveViewHolder holder, int position)
        {
            holder.bindData(mBeanList.get(position));
        }

        @Override
        public int getItemCount()
        {
            return mBeanList.size();
        }

        class ReserveViewHolder extends RecyclerView.ViewHolder
        {

            private final TextView store_name;
            private final TextView store_message;
            private final TextView status_text;

            public ReserveViewHolder(View itemView)
            {
                super(itemView);
                store_name = itemView.findViewById(R.id.store_name);
                store_message = itemView.findViewById(R.id.store_message);
                status_text = itemView.findViewById(R.id.status_text);
            }

            public void bindData(StoreAppointment storeAppointment)
            {
                store_name.setText(storeAppointment.name);
                store_message.setText(storeAppointment.timeDesc + "  " + "金额:￥"+storeAppointment.cost);
                status_text.setText(storeAppointment.getStateName());
            }
        }
    }


    /**
     * 获取帐户明细
     */
    class GetStoreAppointmentList extends BaseAsyncTask
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mBaseUi.getLoadingView().show();
        }

        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return StoreModel.getStoreAppointmentList(4);//查看已结算
        }

        @Override
        protected void onError(@Nullable Exception e)
        {
            super.onError(e);
            mBaseUi.getLoadingView().showNetworkError();
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            mBaseUi.getLoadingView().hide();
            APIResponse<List<StoreAppointment>> response = data;

            if(ValidateUtil.isNotEmpty(response.data))
            {
                mBeanList.addAll(response.data);
            }
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
