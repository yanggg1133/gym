package com.hxs.fitnessroom.module.user.ui;

import android.view.View;

import com.bumptech.glide.Glide;
import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.util.image.ImageLoader;
import com.hxs.fitnessroom.widget.SettingItemView;

/**
 * Created by je on 9/16/17.
 */

public class UserInfoUi extends BaseUi
{
    private final SettingItemView user_avatar;
    private final SettingItemView user_nickname;
    private final SettingItemView user_sex;
    private final SettingItemView user_phone;
    private final SettingItemView user_realname;
    private final SettingItemView user_authenticate;

    public UserInfoUi(BaseActivity baseActivity)
    {
        super(baseActivity);
        setBackAction(true);

        user_avatar = findViewById(R.id.user_avatar);
        user_nickname = findViewById(R.id.user_nickname);
        user_sex = findViewById(R.id.user_sex);
        user_phone = findViewById(R.id.user_phone);
        user_realname = findViewById(R.id.user_realname);
        user_authenticate = findViewById(R.id.user_authenticate);

        initView();
        initOnclick();
    }

    private void initView()
    {
        user_avatar.setValue("头像", "");
        ImageLoader.loadHeadImageCircleCrop(HXSUser.getHeadImg(),user_avatar.getRightImageView());
        user_nickname.setValue("昵称",HXSUser.getNickname());
        user_sex.setValue("性别",HXSUser.getSexname());
        user_sex.hideRightGotoIcon();
        user_phone.setValue("手机号",HXSUser.getMobile());
        user_phone.hideRightGotoIcon();
        user_phone.hideLine();

        user_realname.setValue("姓名",HXSUser.getRealname());
        user_authenticate.setValue("实名认证",HXSUser.getRealnameStatus());
        if(! "未认证".equals(HXSUser.getRealnameStatus()) && ! "审核不通过".equals(HXSUser.getRealnameStatus()))
        {
            user_realname.hideRightGotoIcon();
            user_realname.setEnabled(false);
            user_authenticate.hideRightGotoIcon();
            user_authenticate.setEnabled(false);
        }

    }

    private void initOnclick()
    {
        user_avatar.setOnClickListener(getBaseOnclick());
        user_nickname.setOnClickListener(getBaseOnclick());
        user_sex.setOnClickListener(getBaseOnclick());
        user_phone.setOnClickListener(getBaseOnclick());
        user_realname.setOnClickListener(getBaseOnclick());
        user_authenticate.setOnClickListener(getBaseOnclick());
    }

    public  void updateHeadImg()
    {
        Glide.with(getBaseActivity()).clear(user_avatar.getRightImageView());
        ImageLoader.loadHeadImageCircleCrop(HXSUser.getHeadImg(),user_avatar.getRightImageView());
    }

    public void updateUserInfo()
    {
        initView();
    }
}
