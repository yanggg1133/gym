package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.hxs.fitnessroom.module.user.model.entity.UserBean;
import com.hxs.fitnessroom.util.ValidateUtil;

import static android.R.attr.level;

/**
 * 好享瘦用户信息处理类
 * 状态缓存,更新
 * 状态查询等
 * 所有用户信息操作都在这个类里
 * Created by je on 9/12/17.
 */

public class HXSUser
{
    private HXSUser(){}
    public String user_id;   //用户账号
    @UserBean.SexType
    public int sex;       //用户性别
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


    private static Context mContext;
    private static HXSUser currentUser;

    public static void appInitialization(Context context)
    {
        mContext = context;
        SharedPreferences sp = mContext.getSharedPreferences(HXSUser.class.getName(), Context.MODE_PRIVATE);
        if(null == currentUser && ValidateUtil.isNotEmpty(sp.getString("user_id", "")))
        {
            HXSUser user = new HXSUser();
            user.user_id = sp.getString("user_id", "");
            //noinspection WrongConstant
            user.sex = sp.getInt("sex",0);
            user.head_img = sp.getString("head_img", "");
            user.nickname = sp.getString("nickname", "");
            user.realname = sp.getString("realname", "");
            user.sess_token = sp.getString("sess_token", "");
            user.descr = sp.getString("descr", "");
            user.birthday = sp.getString("birthday", "");
            user.body_high = sp.getString("body_high", "");
            user.invited_code = sp.getString("invited_code", "");
            user.is_shield = sp.getString("is_shield", "");
            user.province = sp.getString("province", "");
            user.city = sp.getString("city", "");
            user.vocation = sp.getString("vocation", "");
            currentUser = user;
        }
    }

    /**
     * 用户信息缓存更新
     * @param userBean
     */
    public static void saveCurrentUser(@Nullable UserBean userBean)
    {
        if(null == userBean)
            return;

        if(currentUser == null)
            currentUser = new HXSUser();

        currentUser.user_id = userBean.user_id;
        currentUser.sex = userBean.sex;
        currentUser.head_img = userBean.head_img;
        currentUser.nickname = userBean.nickname;
        currentUser.realname = userBean.realname;
        currentUser.descr = userBean.descr;
        currentUser.birthday = userBean.birthday;
        currentUser.body_high = userBean.body_high;
        currentUser.invited_code = userBean.invited_code;
        currentUser.is_shield = userBean.is_shield;
        currentUser.province = userBean.province;
        currentUser.city = userBean.city;
        currentUser.vocation = userBean.vocation;
        if(ValidateUtil.isNotEmpty(userBean.sess_token))
            currentUser.sess_token = userBean.sess_token;

        SharedPreferences sp = mContext.getSharedPreferences(HXSUser.class.getName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_id", userBean.user_id);
        editor.putInt("sex",userBean.sex);
        editor.putString("head_img", userBean.head_img);
        editor.putString("nickname", userBean.nickname);
        editor.putString("realname", userBean.realname);
        editor.putString("descr", userBean.descr);
        editor.putString("birthday", userBean.birthday);
        editor.putString("body_high", userBean.body_high);
        editor.putString("invited_code", userBean.invited_code);
        editor.putString("is_shield", userBean.is_shield);
        editor.putString("province", userBean.province);
        editor.putString("city", userBean.city);
        editor.putString("vocation", userBean.vocation);
        if(ValidateUtil.isNotEmpty(userBean.sess_token))
            editor.putString("sess_token", userBean.sess_token);
        editor.apply();
    }


    public static HXSUser getHXSUser()
    {
        return currentUser;
    }

    public static String getUserSessToken()
    {
        return null == currentUser?"":currentUser.sess_token;
    }
}
