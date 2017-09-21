package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.module.user.model.WalletAdapter;
import com.hxs.fitnessroom.module.user.model.entity.WalletDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 钱包明细
 */
public class WalletDetailActivity extends BaseActivity
{
    public static Intent getNewIntent(Context context)
    {
        return new Intent(context,WalletDetailActivity.class);
    }

    private BaseUi mBaseUi;
    private RecyclerView mWalletList;
    private List<WalletDetailBean> mBeanList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detail);
        initView();

        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("钱包明细");
        mBaseUi.setBackAction(true);

        //initTest();
    }

    private void initTest() {
        mBeanList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            WalletDetailBean walletDetailBean = new WalletDetailBean();
            walletDetailBean.date = "2017-06-22 14:25:54";
            walletDetailBean.msg = "付款成功";
            walletDetailBean.money = "20.00";
            walletDetailBean.payMode = "支付宝";
            mBeanList.add(walletDetailBean);

        }
        mWalletList.setLayoutManager(new LinearLayoutManager(this));
        WalletAdapter walletAdapter = new WalletAdapter(WalletDetailActivity.this, mBeanList);
        mWalletList.setAdapter(walletAdapter);
    }

    private void initView() {
        mWalletList = (RecyclerView) findViewById(R.id.wallet_list);
    }
}
