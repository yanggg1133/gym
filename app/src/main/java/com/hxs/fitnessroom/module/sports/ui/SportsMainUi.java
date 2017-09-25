package com.hxs.fitnessroom.module.sports.ui;

import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.module.sports.model.entity.UserDeviceStatusBean;
import com.hxs.fitnessroom.util.DateUtil;

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
    private final TextView sport_using_tip;
    private final TextView sport_store_name;
    private final TextView sport_store_time;
    private final TextView sport_using_money;
    private final TextView sport_lockers_status;
    private final TextView sport_readmill_status;
    private final TextView leave_tip_content;
    private Long startTimeCaceh;
    private boolean isUsingSport = false;

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

        /**
         * 界面显示字段
         */
        sport_using_tip = findViewById(R.id.sport_using_tip);
        sport_store_name = findViewById(R.id.sport_store_name);
        sport_store_time = findViewById(R.id.sport_store_time);
        sport_using_money = findViewById(R.id.sport_using_money);
        sport_lockers_status = findViewById(R.id.sport_lockers_status);
        sport_readmill_status = findViewById(R.id.sport_readmill_status);
        leave_tip_content = findViewById(R.id.leave_tip_content);


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
     *
     * @param userDeviceStatus
     */
    public void startSportView(UserDeviceStatusBean userDeviceStatus)
    {
        sport_action_using_layout.setVisibility(View.VISIBLE);
        start_scan.setVisibility(View.GONE);
        text_welcome.setVisibility(View.GONE);

        sport_using_tip.setText(userDeviceStatus.tips);
        sport_store_name.setText(userDeviceStatus.store.name);
        sport_store_time.setText(userDeviceStatus.store.openTime);
        leave_tip_content.setText(userDeviceStatus.warmimg);
        sport_using_money.setText(userDeviceStatus.store.feeDesc);

        setLockerIsUsing(userDeviceStatus.locker.status);
        setRunIsUsing(userDeviceStatus.run.status);
        /**
         * 计算 开始使用时间与服务当前时间的时间差
         * 然后用
         */
        startTimeCaceh = new Long(userDeviceStatus.startTime)*1000;
        long time = DateUtil.timeMillsDifference(System.currentTimeMillis(),startTimeCaceh);
        sport_using_time.setBase(SystemClock.elapsedRealtime() - time);
        sport_using_time.start();
        isUsingSport = true;
    }


    /**
     * 设置食物柜是否使用中的状态
     */
    public void setLockerIsUsing(int status)
    {
        setIsUsing(sport_lockers_status, status);
    }

    /**
     * 设置跑步机是否使用中
     */
    public void setRunIsUsing(int status)
    {
        setIsUsing(sport_readmill_status, status);
    }

    private void setIsUsing(TextView usingView, int status)
    {
        if (1 == status)
        {
            usingView.setText("使用中");
            usingView.setTextColor(getBaseFragment().getResources().getColor(R.color.colorListItemTitleText));
        } else
        {
            usingView.setTextColor(getBaseFragment().getResources().getColor(R.color.colorListItemContentText));
            usingView.setText("未开启");
        }
    }

    public void onStart()
    {
        if(isUsingSport)
            sport_using_time.start();
    }

    public void onStop()
    {
        if(isUsingSport)
            sport_using_time.stop();
    }

    public void stopSportUsingUi()
    {
        isUsingSport = false;
        sport_action_using_layout.setVisibility(View.GONE);
        start_scan.setVisibility(View.VISIBLE);
        text_welcome.setVisibility(View.VISIBLE);
    }
}
