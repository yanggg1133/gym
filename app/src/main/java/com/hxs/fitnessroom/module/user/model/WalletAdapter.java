package com.hxs.fitnessroom.module.user.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.module.user.model.entity.WalletDetailBean;

import java.util.List;

/**
 * Created by je on 20/09/17.
 */

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {
    Context mContext;
    private final LayoutInflater mInflater;

    public WalletAdapter(Context context, List<WalletDetailBean> detailBeen) {
        mContext = context;
        mDetailBeen = detailBeen;
        mInflater = LayoutInflater.from(mContext);
    }

    List<WalletDetailBean> mDetailBeen;

    @Override
    public WalletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.walle_detail_item, parent, false);
        return new WalletViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WalletViewHolder holder, int position) {
        WalletDetailBean walletDetailBean = mDetailBeen.get(position);
        holder.mPayMsg.setText(walletDetailBean.msg);
        holder.mWalletDate.setText(walletDetailBean.date);
        holder.mPayMode.setText(walletDetailBean.payMode);
        holder.mWalletMoney.setText(walletDetailBean.money);

    }

    @Override
    public int getItemCount() {
        return mDetailBeen.size();
    }

    class WalletViewHolder extends RecyclerView.ViewHolder {
        TextView mPayMode;
        TextView mWalletDate;
        TextView mWalletMoney;
        TextView mPayMsg;

        public WalletViewHolder(View itemView) {
            super(itemView);
            mPayMode = (TextView) itemView.findViewById(R.id.pay_mode);
            mWalletDate = (TextView) itemView.findViewById(R.id.wallet_date);
            mWalletMoney = (TextView) itemView.findViewById(R.id.wallet_money);
            mPayMsg = (TextView) itemView.findViewById(R.id.pay_msg);
        }
    }
}
