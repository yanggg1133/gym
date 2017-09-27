package com.hxs.fitnessroom.module.user.ui;

import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.widget.MyToolbar;
import com.hxs.fitnessroom.widget.SettingItemView;

/**
 * 设置中心操作类
 * Created by je on 20/09/17.
 */

public class SettingUi extends BaseUi
{
    private final SettingItemView user_protocol;
    private final SettingItemView deposit_statement;
    private final SettingItemView top_up_protocol;
    private final SettingItemView about_our;
    private final View logout_button;

    public SettingUi(BaseActivity baseActivity)
    {
        super(baseActivity);
        MyToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("设置");
        setBackAction(true);
        user_protocol = findViewById(R.id.user_protocol);
        deposit_statement = findViewById(R.id.deposit_statement);
        top_up_protocol = findViewById(R.id.top_up_protocol);
        about_our = findViewById(R.id.about_our);
        logout_button = findViewById(R.id.logout_button);
        initView();
    }

    private void initView()
    {
        user_protocol.setValue(R.mipmap.ic_setting_yonghu, "用户协议", null);
        deposit_statement.setValue(R.mipmap.ic_setting_yajin, "押金说明", null);
        top_up_protocol.setValue(R.mipmap.ic_setting_chongzhi, "充值协议", null);
        about_our.setValue(R.mipmap.ic_setting_guanyu, "关于我们", null);
    }


    public void setOnClickListener()
    {
        user_protocol.setOnClickListener(getBaseOnclick());
        deposit_statement.setOnClickListener(getBaseOnclick());
        top_up_protocol.setOnClickListener(getBaseOnclick());
        about_our.setOnClickListener(getBaseOnclick());
        logout_button.setOnClickListener(getBaseOnclick());
    }
}
