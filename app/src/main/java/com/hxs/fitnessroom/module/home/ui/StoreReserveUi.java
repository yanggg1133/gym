package com.hxs.fitnessroom.module.home.ui;

import com.hxs.fitnessroom.BuildConfig;
import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.module.home.model.StoreModel;
import com.hxs.fitnessroom.module.home.model.entity.StoreBean;
import com.hxs.fitnessroom.module.home.model.entity.StoreReserveBean;
import com.hxs.fitnessroom.module.home.widget.StoreInfoView;
import com.hxs.fitnessroom.module.home.widget.StoreReserveSelectTimeView;

import java.util.List;

/**
 * 预约界面的UI操作类
 * Created by je on 9/6/17.
 */

public class StoreReserveUi extends BaseUi
{

    private StoreInfoView store_info_view;
    private StoreReserveSelectTimeView select_time_view;

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
    }

    @Override
    public void startLoading()
    {
        getLoadingView().showByNullBackground();
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

}
