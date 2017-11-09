package com.hxs.fitnessroom.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.module.home.model.StoreModel;
import com.hxs.fitnessroom.module.home.model.entity.StoreAppointment;
import com.hxs.fitnessroom.module.web.WebActivity;
import com.hxs.fitnessroom.util.DialogUtil;
import com.hxs.fitnessroom.widget.SettingItemView;
import com.hxs.fitnessroom.widget.dialog.ConfirmDialog;

/**
 * 预约列表的单个详情页
 * Created by shaojunjie on 17-11-6.
 */

public class UserReserveItemFragment extends BaseFragment implements View.OnClickListener
{

    private BaseUi mUi;
    private SettingItemView store_address;
    private SettingItemView time;
    private SettingItemView money;
    private StoreAppointment mStoreAppointment;
    private View cancel_action;
    private View goto_open;

    public static Fragment getNewFragment(StoreAppointment storeAppointment)
    {
        UserReserveItemFragment fragment = new UserReserveItemFragment();
        fragment.setStoreAppointment(storeAppointment);
        return fragment;
    }

    @Nullable
    public void setStoreAppointment(StoreAppointment storeAppointment)
    {
        mStoreAppointment = storeAppointment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.user_reserve_item_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        mUi = new BaseUi(this);
        store_address = mUi.findViewByIdAndSetClick(R.id.store_address);
        time = mUi.findViewById(R.id.time);
        money = mUi.findViewById(R.id.money);

        cancel_action = mUi.findViewByIdAndSetClick(R.id.cancel_action);
        goto_open = mUi.findViewByIdAndSetClick(R.id.goto_open);

        store_address.setNoTintIcon(R.mipmap.card_dingwei);
        store_address.hideLine();
        time.setNoTintIcon(R.mipmap.card_time);
        time.hideLine();
        time.hideRightGotoIcon();
        money.setNoTintIcon(R.mipmap.card_jinqian);
        money.hideRightGotoIcon();
        money.hideLine();



        store_address.setValue(mStoreAppointment.address,"");
        time.setValue(mStoreAppointment.timeDesc,"");
        money.setValue(mStoreAppointment.cost,"");
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.store_address:
                WebActivity.gotoWeb(getContext(),ConstantsApiUrl.H5_gymDetailBaiduMap.getH5Url(mStoreAppointment.uid));
                break;
            case R.id.cancel_action:
                DialogUtil.showConfirmDialog("确定要取消该预约吗？",
                        "需提前半小时取消，取消成功后款项将自动退回至你的钱包余额",
                        "我再想想","确定",getFragmentManager(),new ConfirmDialog.OnDialogCallbackAdapter()
                        {
                            @Override
                            public void onConfirm()
                            {
                                new CancelReserveTask().go(getContext());
                            }
                        });
                break;
            case R.id.goto_open:

                break;
        }
    }


    /**
     * 取消预约
     */
    class CancelReserveTask extends BaseAsyncTask
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            ((UserReserveActivity)getContext()).mUi.getLoadingView().showByNullBackground();
        }

        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return StoreModel.storeAppointmenCancel(mStoreAppointment.id);
        }

        @Override
        protected void onError(@Nullable Exception e)
        {
            super.onError(e);
            ((UserReserveActivity)getContext()).mUi.getLoadingView().hide();
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            ((UserReserveActivity)getContext()).mUi.getLoadingView().hide();
        }
    }


}
