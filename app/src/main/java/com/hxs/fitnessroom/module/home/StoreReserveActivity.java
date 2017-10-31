package com.hxs.fitnessroom.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.module.home.ui.StoreReserveUi;

/**
 * 预约界面
 * Created by shaojunjie on 17-10-30.
 */

public class StoreReserveActivity extends BaseActivity
{
    private static final String KEY_STROE_ID = "KEY_STROE_ID";
    private StoreReserveUi mUi;

    public static Intent getNewIntent(Context context,String stroeId)
    {
        Intent intent = new Intent(context,StoreReserveActivity.class);
        intent.putExtra(KEY_STROE_ID,stroeId);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_reserve);
        mUi = new StoreReserveUi(this);

//        RecyclerView recyclerView = new RecyclerView(this);
//        recyclerView.setLayoutManager(new GridLayoutManager());
    }
}
