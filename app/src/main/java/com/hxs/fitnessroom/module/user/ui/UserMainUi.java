package com.hxs.fitnessroom.module.user.ui;

import android.view.View;
import android.widget.ImageView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.widget.MyToolbar;
import com.hxs.fitnessroom.widget.SettingItemView;


/**
 * 用户中心UI操作类
 * Created by je on 9/11/17.
 */

public class UserMainUi extends BaseUi
{
    private final SettingItemView setting_wallet;
    private final SettingItemView setting_tutorial;
    private final SettingItemView setting_exercise;
    private final SettingItemView setting_service;
    private final SettingItemView setting_message;
    private final SettingItemView setting_system;
    private final View goto_recharge;
    private final ImageView user_avatar;

    public UserMainUi(BaseFragment baseFragment)
    {
        super(baseFragment);
        MyToolbar myToolbar = findViewById(R.id.toolbar);
        myToolbar.setBackground(null);
        myToolbar.setTitleColor(0xffffffff);
        myToolbar.setTitle("个人中心");

        goto_recharge = findViewById(R.id.goto_recharge);
        user_avatar = findViewById(R.id.user_avatar);

        setting_wallet  = findViewById(R.id.setting_wallet);
        setting_wallet.setValue(R.drawable.ic_user_tixian,"我的钱包",null);
        setting_tutorial  = findViewById(R.id.setting_tutorial);
        setting_tutorial.setValue(R.drawable.ic_user_kecheng,"我的教程",null);
        setting_exercise  = findViewById(R.id.setting_exercise);
        setting_exercise.setValue(R.drawable.ic_user_yundong_icon,"我的锻炼",null);
        setting_service  = findViewById(R.id.setting_service);
        setting_service.setValue(R.drawable.ic_user_kefu,"我的客服",null);
        setting_service.hideLine();
        setting_message  = findViewById(R.id.setting_message);
        setting_message.setValue(R.drawable.ic_user_xiaoxi,"我的消息",null);
        setting_system  = findViewById(R.id.setting_system);
        setting_system.setValue(R.drawable.ic_user_shezhi,"我的消息",null);
    }

    public void setOnClickListener(View.OnClickListener onClickListener)
    {
        setting_wallet.setOnClickListener(onClickListener);
        setting_tutorial.setOnClickListener(onClickListener);
        setting_exercise.setOnClickListener(onClickListener);
        setting_service.setOnClickListener(onClickListener);
        setting_message.setOnClickListener(onClickListener);
        setting_system.setOnClickListener(onClickListener);
        goto_recharge.setOnClickListener(onClickListener);
        user_avatar.setOnClickListener(onClickListener);
    }
}
