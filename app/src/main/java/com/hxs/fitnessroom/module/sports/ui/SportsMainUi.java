package com.hxs.fitnessroom.module.sports.ui;

import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

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
    private final TextView sport_action_opendoor;
    private final TextView sport_action_lockers;
    private final TextView sport_action_readmill;
    private final TextView sport_action_shop;
    private final Chronometer sport_using_time;

    public SportsMainUi(BaseFragment baseFragment)
    {
        super(baseFragment);
        start_scan = findViewById(R.id.start_scan);
        sport_action_opendoor = findViewById(R.id.sport_action_opendoor_text);
        sport_action_lockers = findViewById(R.id.sport_action_lockers_text);
        sport_action_readmill = findViewById(R.id.sport_action_readmill_text);
        sport_action_shop = findViewById(R.id.sport_action_shop_text);
        sport_using_time = findViewById(R.id.sport_using_time);
        initOnclick();
    }

    private void initOnclick()
    {
        start_scan.setOnClickListener(getBaseOnclick());
        sport_action_opendoor.setOnClickListener(getBaseOnclick());
        sport_action_lockers.setOnClickListener(getBaseOnclick());
        sport_action_readmill.setOnClickListener(getBaseOnclick());
        sport_action_shop.setOnClickListener(getBaseOnclick());
    }


    public Chronometer getSportUsingTimeView()
    {
        return sport_using_time;
    }



}
