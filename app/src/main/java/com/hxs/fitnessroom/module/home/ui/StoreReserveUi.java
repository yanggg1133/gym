package com.hxs.fitnessroom.module.home.ui;

import android.view.View;

import com.hxs.fitnessroom.BuildConfig;
import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.module.home.widget.StoreInfoView;
import com.hxs.fitnessroom.module.home.widget.StoreReserveSelectTimeView;

import java.util.ArrayList;
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
        if(BuildConfig.DEBUG)
        {
            List<List> lists = new ArrayList<>();
            List list = new ArrayList<>();
            list.add(new Object());
            list.add(new Object());
            list.add(new Object());
            list.add(new Object());
            list.add(new Object());
            list.add(new Object());
            list.add(new Object());
            list.add(new Object());
            list.add(new Object());
            list.add(new Object());
            lists.add(list);
            lists.add(list);
            lists.add(list);
            select_time_view.setData(lists);
        }
    }

}
