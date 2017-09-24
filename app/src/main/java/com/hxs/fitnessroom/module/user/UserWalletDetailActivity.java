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
import com.hxs.fitnessroom.module.user.model.UserAccountModel;
import com.hxs.fitnessroom.module.user.model.entity.WalletDetailBean;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.widget.LoadingView;
import com.hxs.fitnessroom.widget.adapterwrapper.LoadMoreAdapterWrapper;

import java.util.ArrayList;
import java.util.List;

import static com.hxs.fitnessroom.Constants.PAGE_DEFAULT_LAST_ID;

/**
 * 钱包明细
 */
public class UserWalletDetailActivity extends BaseActivity implements LoadingView.OnReloadListener, LoadMoreAdapterWrapper.RequestToLoadMoreListener
{
    private LoadMoreAdapterWrapper loadMoreAdapterWrapper;

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, UserWalletDetailActivity.class);
    }

    private BaseUi mBaseUi;
    private RecyclerView mWalletList;
    private List<WalletDetailBean> mBeanList = new ArrayList<>();
    private String mLastId = PAGE_DEFAULT_LAST_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detail);

        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("钱包明细");
        mBaseUi.setBackAction(true);

        initView();
        onReload();
    }

    private void initView()
    {
        mWalletList = (RecyclerView) findViewById(R.id.wallet_list);
        mWalletList.setLayoutManager(new LinearLayoutManager(this));
        loadMoreAdapterWrapper = new LoadMoreAdapterWrapper(new WalletAdapter(this), this);
        mWalletList.setAdapter(loadMoreAdapterWrapper);
    }

    @Override
    public void onReload()
    {
        new GetAccountLogTask().execute(this);
    }

    @Override
    public void onLoadMoreRequested()
    {
        onReload();
    }


    class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder>
    {
        Context mContext;
        private final LayoutInflater mInflater;

        public WalletAdapter(Context context)
        {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }


        @Override
        public WalletViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = mInflater.inflate(R.layout.walle_detail_item, parent, false);
            return new WalletViewHolder(view);
        }

        @Override
        public void onBindViewHolder(WalletViewHolder holder, int position)
        {
            WalletDetailBean walletDetailBean = mBeanList.get(position);
            holder.mPayMsg.setText(walletDetailBean.desc);
            holder.mWalletDate.setText(walletDetailBean.create_time);
            holder.mPayMode.setText(walletDetailBean.payDesc);
            holder.mWalletMoney.setText("￥" + walletDetailBean.money);

        }

        @Override
        public int getItemCount()
        {
            return mBeanList.size();
        }

        class WalletViewHolder extends RecyclerView.ViewHolder
        {
            TextView mPayMode;
            TextView mWalletDate;
            TextView mWalletMoney;
            TextView mPayMsg;

            public WalletViewHolder(View itemView)
            {
                super(itemView);
                mPayMode = (TextView) itemView.findViewById(R.id.pay_mode);
                mWalletDate = (TextView) itemView.findViewById(R.id.wallet_date);
                mWalletMoney = (TextView) itemView.findViewById(R.id.wallet_money);
                mPayMsg = (TextView) itemView.findViewById(R.id.pay_msg);
            }
        }
    }


    /**
     * 获取帐户明细
     */
    class GetAccountLogTask extends BaseAsyncTask
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            if (PAGE_DEFAULT_LAST_ID.equals(mLastId))
                mBaseUi.getLoadingView().show();
        }

        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return UserAccountModel.getAccountLog(mLastId);
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
            APIResponse<List<WalletDetailBean>> response = data;

            if (PAGE_DEFAULT_LAST_ID == mLastId)
            {
                mBaseUi.getLoadingView().hide();
                mWalletList.getAdapter().notifyDataSetChanged();
            } else
            {
                loadMoreAdapterWrapper.onDataReady(response.data.size());
            }

            if(ValidateUtil.isNotEmpty(response.data))
            {
                mBeanList.addAll(response.data);
                mLastId = response.data.get(response.data.size()-1).id;
            }

        }
    }

}
