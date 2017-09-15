package com.hxs.fitnessroom.module.sports.ui;

import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.baseclass.BaseUi;

/**
 * 运动 主界面UI操作类
 * Created by je on 9/15/17.
 */

public class SportsMainUi extends BaseUi
{
    private final View start_scan;

    public SportsMainUi(BaseFragment baseFragment)
    {
        super(baseFragment);
        start_scan = findViewById(R.id.start_scan);
        initOnclick();
    }

    private void initOnclick()
    {
        start_scan.setOnClickListener(getBaseOnclick());
    }




}
