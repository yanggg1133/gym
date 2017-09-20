package com.hxs.fitnessroom.module.sports.ui;

import android.view.View;
import android.widget.Chronometer;
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
    private final TextView sport_action_opendoor_text;
    private final TextView sport_action_lockers_text;
    private final TextView sport_action_readmill_text;
    private final TextView sport_action_shop_text;
    private final Chronometer sport_using_time;
    private final View sport_action_using_layout;
    private final View text_welcome;
    private long elapsedRealtime;

    public SportsMainUi(BaseFragment baseFragment)
    {
        super(baseFragment);
        start_scan = findViewById(R.id.start_scan);
        sport_action_opendoor_text = findViewById(R.id.sport_action_opendoor_text);
        sport_action_lockers_text = findViewById(R.id.sport_action_lockers_text);
        sport_action_readmill_text = findViewById(R.id.sport_action_readmill_text);
        sport_action_shop_text = findViewById(R.id.sport_action_shop_text);
        sport_using_time = findViewById(R.id.sport_using_time);
        text_welcome = findViewById(R.id.text_welcome);
        sport_action_using_layout = findViewById(R.id.sport_action_using_layout);
        initOnclick();
    }

    private void initOnclick()
    {
        start_scan.setOnClickListener(getBaseOnclick());
        sport_action_opendoor_text.setOnClickListener(getBaseOnclick());
        sport_action_lockers_text.setOnClickListener(getBaseOnclick());
        sport_action_readmill_text.setOnClickListener(getBaseOnclick());
        sport_action_shop_text.setOnClickListener(getBaseOnclick());
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

    /**
     * 开始显示运动界面
     */
    public void startSport()
    {
        sport_action_using_layout.setVisibility(View.VISIBLE);
        start_scan.setVisibility(View.GONE);
        text_welcome.setVisibility(View.GONE);
    }


    public void setBaseTime(long baseTime)
    {
        sport_using_time.setBase(baseTime);
        sport_using_time.start();
    }

    public void onStart()
    {
        sport_using_time.start();
    }

    public void onStop()
    {
        sport_using_time.stop();
    }
}
