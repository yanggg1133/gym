package com.hxs.fitnessroom.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hxs.fitnessroom.BuildConfig;
import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.home.model.StoreModel;
import com.hxs.fitnessroom.module.home.model.entity.StoreReserveBean;
import com.hxs.fitnessroom.module.home.ui.StoreReserveUi;
import com.hxs.fitnessroom.module.home.widget.StoreReserveSelectTimeView;
import com.hxs.fitnessroom.util.DialogUtil;
import com.hxs.fitnessroom.util.ToastUtil;
import com.hxs.fitnessroom.widget.dialog.ConfirmDialog;

import java.util.List;

/**
 * 预约界面
 * Created by shaojunjie on 17-10-30.
 */

public class StoreReserveActivity extends BaseActivity implements StoreReserveSelectTimeView.OnSelectChangedListener, View.OnClickListener
{
    private static final String KEY_STROE_ID = "KEY_STROE_ID";
    private StoreReserveUi mUi;
    private String mStoreId;
    private double mFee = 0;//单个时段的费用
    private double mSumFee = 0;//总费用
    private List<StoreReserveBean.Time> mSelectTimes;//缓存用户所选时段
    private String mSelectTimesText;//缓存用户所选时段提示用文本
    private StoreReserveBean responseBean;

    public static Intent getNewIntent(Context context, String storeId)
    {
        Intent intent = new Intent(context, StoreReserveActivity.class);
        intent.putExtra(KEY_STROE_ID, storeId);
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
        new StoreReserveInfoTask().go(this, mUi);
    }

    @Override
    public boolean onSelectChangeing(List<StoreReserveBean.Time> times, StoreReserveBean.Time nowTime)
    {

        if (times.size() == 8)
        {
            ToastUtil.toastShort("最大预约时长不能超过4小时");
            return false;
        }
        return true;
    }

    @Override
    public void onSelectChanged(String date, List<StoreReserveBean.Time> times)
    {
        mSelectTimes = times;

        if (null == times)
        {
            mUi.setSelectTime("", "", "");
            mUi.setSelectAmount(0.0);
        } else
        {
            String dateSimple = date.split("/")[1] + "/" + date.split("/")[2];
            String start = times.get(0).timeDesc.split("-")[0];
            String end = times.get(times.size() - 1).timeDesc.split("-")[1];
            mUi.setSelectTime(date, start, end);
            mSelectTimesText = dateSimple + " " + start + " - " + dateSimple + " " + end;
            mSumFee = times.size() * mFee;
            mUi.setSelectAmount(mSumFee);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.action_comfirm_button:
                if (mSelectTimes == null)
                {

                } else
                {
                    DialogUtil.showConfirmDialog("你的预约时间段为\n\n" + mSelectTimesText + "\n\n预约成功后系统将在您的钱包余额自动扣款" + mSumFee + "元",
                            "取消", "确定", getSupportFragmentManager(), new ConfirmDialog.OnDialogCallbackAdapter()
                            {
                                @Override
                                public void onConfirm()
                                {
                                    new PayStoreReserveTask().go(StoreReserveActivity.this);
                                }
                            }
                    );
                }
                break;
            default:
                break;
        }
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
            responseBean = (StoreReserveBean) data.data;
            mFee = responseBean.store.fee;
            mUi.setStoreInfoViewData(responseBean.store);
            mUi.setStoreReserveSelectTimeViewData(responseBean);
        }
    }


    class PayStoreReserveTask extends BaseAsyncTask
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mUi.getLoadingView().showByNullBackground();
        }

        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return StoreModel.payStoreAppointment(mStoreId, mSelectTimes);
        }

        @Override
        protected void onError(@Nullable Exception e)
        {
            super.onError(e);
            mUi.getLoadingView().hide();
            if(BuildConfig.DEBUG)
            {
                startActivity(StoreReserveSuccessActivity.getNewIntent(StoreReserveActivity.this,
                        responseBean.store.name,mSelectTimesText,String.valueOf(mSumFee)));
            }
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            mUi.getLoadingView().hide();
            HXSUser.updateUserAccountInfoAsync();
            startActivity(StoreReserveSuccessActivity.getNewIntent(StoreReserveActivity.this,
                    responseBean.store.name,mSelectTimesText,String.valueOf(mSumFee)));
            finish();
        }
    }

}
