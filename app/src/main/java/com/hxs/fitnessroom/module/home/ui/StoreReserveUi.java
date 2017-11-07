package com.hxs.fitnessroom.module.home.ui;

import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.module.home.model.entity.StoreBean;
import com.hxs.fitnessroom.module.home.model.entity.StoreReserveBean;
import com.hxs.fitnessroom.module.home.widget.StoreInfoView;
import com.hxs.fitnessroom.module.home.widget.StoreReserveSelectTimeView;

/**
 * 预约界面的UI操作类
 * Created by je on 9/6/17.
 */

public class StoreReserveUi extends BaseUi
{

    private StoreInfoView store_info_view;
    private StoreReserveSelectTimeView select_time_view;
    private TextView sum_amount_text;
    private TextView action_comfirm_button;
    private TextView select_datetime_text;

    public StoreReserveUi(BaseActivity activity)
    {
        super(activity);
        setTitle("预约");
        setBackAction(true);
        initView();
    }

    private void initView()
    {
        store_info_view = findViewById(R.id.store_info_view);
        store_info_view.hideLine();
        select_time_view = findViewById(R.id.select_time_view);
        sum_amount_text = findViewById(R.id.sum_amount_text);
        select_datetime_text = findViewById(R.id.select_datetime_text);
        action_comfirm_button = findViewByIdAndSetClick(R.id.action_comfirm_button);
        select_time_view.setOnSelectChangedListener((StoreReserveSelectTimeView.OnSelectChangedListener) getBaseActivity());

        setSelectTime("","","");
        setSelectAmount(0.0);
    }

    @Override
    public void startLoading()
    {
        getLoadingView().show();
    }

    @Override
    public void endLoading()
    {
        getLoadingView().hide();
    }

    public void setStoreInfoViewData(StoreBean store)
    {
        store_info_view.setStoreBean(store);
    }
    public void setStoreReserveSelectTimeViewData(StoreReserveBean appointment)
    {
        select_time_view.setData(appointment);
    }

    public void setSelectTime(String date,String startTime,String endTime)
    {
        select_datetime_text.setText(date+"  "+startTime+"  -  "+date+"  "+endTime);
    }

    /**
     * 设置费用
     */
    public void setSelectAmount(double selectAmount)
    {
        sum_amount_text.setText("订单总额：￥"+selectAmount);
    }
}
