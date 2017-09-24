package com.hxs.fitnessroom.module.user.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.util.image.ImageLoader;
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
    private final View not_login_tip;
    private final TextView user_name;
    private final TextView user_authenticate;
    private final View goto_recharge_icon;
    private final View goto_recharge_text;

    public UserMainUi(BaseFragment baseFragment)
    {
        super(baseFragment);
        MyToolbar myToolbar = findViewById(R.id.toolbar);
        myToolbar.setBackground(null);
        myToolbar.setTitleColor(0xffffffff);
        myToolbar.setTitle("个人中心");

        goto_recharge = findViewById(R.id.goto_recharge);
        goto_recharge_icon = findViewById(R.id.goto_recharge_icon);
        goto_recharge_text = findViewById(R.id.goto_recharge_text);
        user_avatar = findViewById(R.id.user_avatar);
        user_name = findViewById(R.id.user_name);
        user_authenticate = findViewById(R.id.user_authenticate);
        setting_wallet = findViewById(R.id.setting_wallet);
        setting_tutorial = findViewById(R.id.setting_tutorial);
        setting_exercise = findViewById(R.id.setting_exercise);
        setting_service = findViewById(R.id.setting_service);
        setting_message = findViewById(R.id.setting_message);
        setting_system = findViewById(R.id.setting_system);
        not_login_tip = findViewById(R.id.not_login_tip);
        initView();
        initUserInfo();
        initUserAccount();
    }

    public void setOnClickListener()
    {
        setting_wallet.setOnClickListener(getBaseOnclick());
        setting_tutorial.setOnClickListener(getBaseOnclick());
        setting_exercise.setOnClickListener(getBaseOnclick());
        setting_service.setOnClickListener(getBaseOnclick());
        setting_message.setOnClickListener(getBaseOnclick());
        setting_system.setOnClickListener(getBaseOnclick());
        goto_recharge.setOnClickListener(getBaseOnclick());
        user_avatar.setOnClickListener(getBaseOnclick());
    }

    private void initView()
    {
        setting_wallet.setValue(R.drawable.ic_user_tixian, "我的钱包", null);
        setting_tutorial.setValue(R.drawable.ic_user_kecheng, "我的教程", null);
        setting_exercise.setValue(R.drawable.ic_user_yundong_icon, "我的锻炼", null);
        setting_service.setValue(R.drawable.ic_user_kefu, "我的客服", null);
        setting_service.hideLine();
        setting_message.setValue(R.drawable.ic_user_xiaoxi, "我的消息", null);
        setting_system.setValue(R.drawable.ic_user_shezhi, "我的设置", null);
    }

    public void initUserInfo()
    {
        if(HXSUser.isLogin())
        {
            ImageLoader.loadHeadImageCircleCrop(HXSUser.getHXSUser().head_img,user_avatar);
            user_name.setText(HXSUser.getHXSUser().nickname);
            user_authenticate.setText("未认证");
            not_login_tip.setVisibility(View.GONE);
            user_name.setVisibility(View.VISIBLE);
            user_authenticate.setVisibility(View.VISIBLE);
            goto_recharge_icon.setVisibility(View.VISIBLE);
            goto_recharge.setVisibility(View.VISIBLE);
            goto_recharge_text.setVisibility(View.VISIBLE);
        }
        else
        {
            not_login_tip.setVisibility(View.VISIBLE);
            user_avatar.setImageResource(R.mipmap.ic_user_def_head);
            user_name.setVisibility(View.GONE);
            user_authenticate.setVisibility(View.GONE);
            goto_recharge_icon.setVisibility(View.GONE);
            goto_recharge.setVisibility(View.GONE);
            goto_recharge_text.setVisibility(View.GONE);
        }
    }


    public void initUserAccount()
    {
        setting_wallet.setConetnt(ValidateUtil.isNotEmpty(HXSUser.getUserAccountBalance())?
                "余额: " + HXSUser.getUserAccountBalance()+"元":"");
    }


}
