package com.hxs.fitnessroom.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.hxs.fitnessroom.module.pay.PayRechargeActivity;
import com.hxs.fitnessroom.util.DialogUtil;
import com.hxs.fitnessroom.util.ToastUtil;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.widget.dialog.ConfirmDialog;

import java.util.List;

/**
 * 预约界面
 * Created by shaojunjie on 17-10-30.
 */

public class StoreReserveActivity extends BaseActivity implements StoreReserveSelectTimeView.OnSelectChangedListener, View.OnClickListener,SwipeRefreshLayout.OnRefreshListener
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
                    DialogUtil.showConfirmDialog("你的预约时间段为\n\n" + mSelectTimesText + "\n\n预约成功后系统将在您的钱包余额自动扣款" + mSumFee + "元,取消预约需提前30分钟",
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

    @Override
    public void onRefresh()
    {
        doWork();
    }


    /**
     * 查询该店预约
     */
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


    /**
     * 确定预约
     */
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
        protected void onAPIError(APIResponse apiResponse)
        {
            if(APIResponse.error_not_money.equals(apiResponse.code))
            {
                mUi.getLoadingView().hide();
                DialogUtil.showConfirmDialog("余额不足，请先充值",
                        "取消", "支充值", getSupportFragmentManager(), new ConfirmDialog.OnDialogCallbackAdapter()
                        {
                            @Override
                            public void onConfirm()
                            {
                                startActivity(PayRechargeActivity.getNewIntent(getContext()));
                            }
                        }
                );
            }
            else if(ValidateUtil.isNotEmpty(apiResponse.msg))
            {
                DialogUtil.showConfirmDialog(apiResponse.msg,
                        null, "知道了", getSupportFragmentManager(),new ConfirmDialog.OnDialogCallbackAdapter());
            }
            mUi.getLoadingView().hide();
        }

        @Override
        protected void onError(@Nullable Exception e)
        {
            super.onError(e);
            mUi.getLoadingView().hide();
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
