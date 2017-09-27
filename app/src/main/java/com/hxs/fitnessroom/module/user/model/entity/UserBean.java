package com.hxs.fitnessroom.module.user.model.entity;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 登录/注册/第三方登录 用户信息体
 * Created by je on 9/12/17.
 */
public class UserBean
{
    public static final int SEX_TYPE_BOY = 1;
    public static final int SEX_TYPE_GIRL = 0;
    public static final int SEX_TYPE_NULL = -1;

    @IntDef({SEX_TYPE_BOY, SEX_TYPE_GIRL,SEX_TYPE_NULL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SexType {}

    public String user_id;   //用户账号
    @SexType
    public int sex;       //用户性别 -1表示没有填写过资料
    public String head_img;  //用户头像
    public String nickname;  //用户昵称
    public String realname;  //真实姓名
    public String sess_token;    //登录session
    public String descr;
    public String birthday;
    public String body_high;
    public String invited_code;
    public String is_shield;
    public String province;
    public String city;
    public String vocation;
    public String mobile;
}
