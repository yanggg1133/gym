package com.hxs.fitnessroom.module.user;

import android.os.Bundle;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;

public class WalletDetailActivity extends BaseActivity {
    private BaseUi mBaseUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detail);

        mBaseUi = new BaseUi(this);
        mBaseUi.setTitle("钱包明细");
        mBaseUi.setBackAction(true);
    }
}