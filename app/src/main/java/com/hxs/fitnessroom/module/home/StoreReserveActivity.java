package com.hxs.fitnessroom.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.home.model.StoreModel;
import com.hxs.fitnessroom.module.home.model.entity.StoreReserveBean;
import com.hxs.fitnessroom.module.home.ui.StoreReserveUi;

/**
 * 预约界面
 * Created by shaojunjie on 17-10-30.
 */

public class StoreReserveActivity extends BaseActivity
{
    private static final String KEY_STROE_ID = "KEY_STROE_ID";
    private StoreReserveUi mUi;
    private String mStoreId;

    public static Intent getNewIntent(Context context,String storeId)
    {
        Intent intent = new Intent(context,StoreReserveActivity.class);
        intent.putExtra(KEY_STROE_ID,storeId);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_reserve);
        mStoreId = getIntent().getStringExtra(KEY_STROE_ID);

        mUi = new StoreReserveUi(this);
        doWork();
    }

    private void doWork()
    {
        new StoreReserveInfoTask().go(this,mUi);
    }


    class StoreReserveInfoTask extends BaseAsyncTask
    {
        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return StoreModel.getStoreAppointment(mStoreId);
        }

        @Override
        protected void onError(@Nullable Exception e)
        {
            mUi.getLoadingView().showNetworkError();
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            StoreReserveBean responseBean = (StoreReserveBean) data.data;

            mUi.setStoreInfoViewData(responseBean.store);
            mUi.setStoreReserveSelectTimeViewData(responseBean);
        }
    }

}
