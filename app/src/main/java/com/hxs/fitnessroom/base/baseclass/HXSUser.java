package com.hxs.fitnessroom.base.baseclass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.hxs.fitnessroom.BuildConfig;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.pay.model.entity.UserAccountBean;
import com.hxs.fitnessroom.module.user.model.LoginModel;
import com.hxs.fitnessroom.module.user.model.UserAccountModel;
import com.hxs.fitnessroom.module.user.model.entity.RealnameBean;
import com.hxs.fitnessroom.module.user.model.entity.UserBean;
import com.hxs.fitnessroom.util.LogUtil;
import com.hxs.fitnessroom.util.ValidateUtil;

/**
 * 好享瘦用户信息处理类
 * 状态缓存,更新
 * 状态查询等
 * 所有用户信息操作都在这个类里
 * Created by je on 9/12/17.
 */

public class HXSUser
{


    private HXSUser()
    {
    }

    public String user_id;   //用户账号
    @UserBean.SexType
    public int sex;       //用户性别
    public String head_img;  //用户头像
    public String nickname;  //用户昵称
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

    public String status;//: "0",   //0 未认证 1 已认证 2 审核中 3 审核不通过
    public String realname;  //真实姓名

    private static Context mContext;
    private static HXSUser currentUser;

    public static void appInitialization(Context context)
    {
        mContext = context;
        SharedPreferences sp = mContext.getSharedPreferences(HXSUser.class.getName(), Context.MODE_PRIVATE);
        if (null == currentUser && ValidateUtil.isNotEmpty(sp.getString("user_id", "")))
        {
            HXSUser user = new HXSUser();
            user.user_id = sp.getString("user_id", "");
            //noinspection WrongConstant
            user.sex = sp.getInt("sex", 0);
            user.head_img = sp.getString("head_img", "");
            user.nickname = sp.getString("nickname", "");
            user.realname = sp.getString("realname", "");
            user.status = sp.getString("status", "");
            user.sess_token = sp.getString("sess_token", "");
            user.descr = sp.getString("descr", "");
            user.birthday = sp.getString("birthday", "");
            user.body_high = sp.getString("body_high", "");
            user.invited_code = sp.getString("invited_code", "");
            user.is_shield = sp.getString("is_shield", "");
            user.province = sp.getString("province", "");
            user.city = sp.getString("city", "");
            user.vocation = sp.getString("vocation", "");
            user.mobile = sp.getString("mobile", "");
            currentUser = user;
        }
    }

    /**
     * 用户信息缓存更新
     *
     * @param userBean
     */
    public static void saveCurrentUserForLocal(@Nullable UserBean userBean)
    {
        if (null == userBean)
            return;

        if (currentUser == null)
            currentUser = new HXSUser();

        currentUser.user_id = userBean.user_id;
        currentUser.sex = userBean.sex;
        currentUser.head_img = userBean.head_img;
        currentUser.nickname = userBean.nickname;
        currentUser.descr = userBean.descr;
        currentUser.birthday = userBean.birthday;
        currentUser.body_high = userBean.body_high;
        currentUser.invited_code = userBean.invited_code;
        currentUser.is_shield = userBean.is_shield;
        currentUser.province = userBean.province;
        currentUser.city = userBean.city;
        currentUser.vocation = userBean.vocation;
        currentUser.mobile = userBean.mobile;
        if (ValidateUtil.isNotEmpty(userBean.sess_token))
            currentUser.sess_token = userBean.sess_token;

        SharedPreferences sp = mContext.getSharedPreferences(HXSUser.class.getName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_id", userBean.user_id);
        editor.putInt("sex", userBean.sex);
        editor.putString("head_img", userBean.head_img);
        editor.putString("nickname", userBean.nickname);
        editor.putString("descr", userBean.descr);
        editor.putString("birthday", userBean.birthday);
        editor.putString("body_high", userBean.body_high);
        editor.putString("invited_code", userBean.invited_code);
        editor.putString("is_shield", userBean.is_shield);
        editor.putString("province", userBean.province);
        editor.putString("city", userBean.city);
        editor.putString("vocation", userBean.vocation);
        editor.putString("mobile", userBean.mobile);
        if (ValidateUtil.isNotEmpty(userBean.sess_token))
            editor.putString("sess_token", userBean.sess_token);
        editor.commit();
        editor.apply();
    }

    /**
     * 用户实名信息缓存更新
     *
     */
    public static void saveRealnameForLocal(RealnameBean realnameBean)
    {
        if(!isLogin())
            return;
        if (null == realnameBean)
            return;

        currentUser.status = realnameBean.status;//: "0",   //0 未认证 1 已认证 2 审核中 3 审核不通过
        currentUser.realname = realnameBean.realname;  //真实姓名

        SharedPreferences sp = mContext.getSharedPreferences(HXSUser.class.getName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("realname", realnameBean.realname);
        editor.putString("status", realnameBean.status);
        editor.commit();
        editor.apply();
    }


    public static void updateUserInfoAsync()
    {
        if(!isLogin())
            return;

        new BaseAsyncTask(){
            private APIResponse<UserBean> userResponse;
            private APIResponse<RealnameBean> realnameResponse;

            @Override
            protected APIResponse doWorkBackground() throws Exception
            {
                userResponse = LoginModel.getSelfUserInfo();
                realnameResponse =  UserAccountModel.getRealname();
                return realnameResponse;
            }

            @Override
            protected void onSuccess(APIResponse data)
            {

                if(userResponse.isSuccess())
                {
                    saveCurrentUserForLocal(userResponse.data);
                }

                if(realnameResponse.isSuccess())
                {
                    saveRealnameForLocal(realnameResponse.data);
                }

                sendUserInfoUpdateBroadcastReceiver();

                /**
                 * 更新用户信息后，再更新用户帐户余额信息
                 */
                HXSUser.updateUserAccountInfoAsync();
            }
        }.execute(mContext);
    }


    public static HXSUser getHXSUser()
    {
        return currentUser;
    }

    public static String getUserSessToken()
    {
        return null == currentUser ? "" : currentUser.sess_token;
    }

    public static String getHeadImg()
    {
        return null == currentUser ? "" : currentUser.head_img;
    }

    public static String getNickname()
    {
        return null == currentUser ? "" : currentUser.nickname;
    }

    public static String getSexname()
    {
        return null == currentUser ? "" : currentUser.sex == UserBean.SEX_TYPE_BOY ? "男" : "女";
    }
    public static int getSex()
    {
        return null == currentUser ? UserBean.SEX_TYPE_NULL : currentUser.sex ;
    }

    public static String getUserId()
    {
        return null == currentUser ? "" : currentUser.user_id;
    }

    public static String getRealname()
    {
        return null == currentUser ? "" : currentUser.realname;
    }
    public static String getMobile()
    {
        return  null == currentUser ? "" : currentUser.mobile;
    }

    public static boolean isLogin()
    {
        return currentUser != null;
    }

    public static int getDepositIsReturning()
    {
        return mUserAccountBean == null ? -1 : mUserAccountBean.status;
    }

    public static String getRealnameStatus()
    {
        if(currentUser == null)
            return "未认证";
        else if("0".equals(currentUser.status))
            return "未认证";
        else if("1".equals(currentUser.status))
            return "已认证";
        else if("2".equals(currentUser.status))
            return "审核中";
        else if("3".equals(currentUser.status))
            return "审核不通过";
        return "未认证";
    }


    public void setBodyHigh(int bodyHigh)
    {
        this.body_high = "" + bodyHigh;
    }

    public void setSex(@UserBean.SexType int sex)
    {
        this.sex = sex;
    }

    public void setBirthday(int year, int month)
    {
        this.birthday = year + "-" + month;
    }

    /**
     * 把本地的用户信息更新到服务器
     */
    public void saveUserInfoAsync()
    {
        new AsyncTask<Void, Void, APIResponse<UserBean>>()
        {
            @Override
            protected APIResponse<UserBean> doInBackground(Void... params)
            {
                UserBean userBean = new UserBean();
                userBean.birthday = birthday;
                userBean.body_high = body_high;
                userBean.sex = sex;
                userBean.nickname = nickname;
                try
                {
                    APIResponse<UserBean> apiResponse = LoginModel.saveSelfUserInfo(userBean);
                    return apiResponse;
                } catch (Exception e) {}
                return null;
            }

            @Override
            protected void onPostExecute(APIResponse<UserBean> userBeanAPIResponse)
            {
                if (null != userBeanAPIResponse && userBeanAPIResponse.isSuccess())
                {
                    HXSUser.saveCurrentUserForLocal(userBeanAPIResponse.data);
                    HXSUser.sendUserInfoUpdateBroadcastReceiver();
                }
                else
                    Toast.makeText(mContext, "用户信息保存失败", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }


    /**
     * 保存用户信息
     */
    public static void saveUserInfoAsync(final UserBean userBean, @Nullable final BaseCallBack baseCallBack)
    {
        new AsyncTask<Void, Void, APIResponse<UserBean>>()
        {
            @Override
            protected APIResponse<UserBean> doInBackground(Void... params)
            {
                try
                {
                    APIResponse<UserBean> apiResponse = LoginModel.saveSelfUserInfo(userBean);
                    return apiResponse;
                } catch (Exception e) {}
                return null;
            }

            @Override
            protected void onPostExecute(APIResponse<UserBean> userBeanAPIResponse)
            {
                if (null != userBeanAPIResponse && userBeanAPIResponse.isSuccess())
                {
                    HXSUser.saveCurrentUserForLocal(userBeanAPIResponse.data);
                    if (null != baseCallBack)
                        baseCallBack.onSuccess(null);
                    HXSUser.sendUserInfoUpdateBroadcastReceiver();
                } else
                {
                    if (null != baseCallBack)
                        baseCallBack.onFailure(null != userBeanAPIResponse ? userBeanAPIResponse.msg : null);
                }
            }
        }.execute();
    }


    private static UserAccountBean mUserAccountBean;
    /**
     * 更新用户帐帐状态及余额信息
     */
    public static void updateUserAccountInfoAsync()
    {
        updateUserAccountInfoAsync(0);
    }

    /**
     *
     * @param lateTime 延时查询
     */
    public static void updateUserAccountInfoAsync(final long lateTime)
    {
        if(!isLogin())
        {
            mUserAccountBean = null;
            return ;
        }

        new BaseAsyncTask()
        {
            @Override
            protected APIResponse doWorkBackground() throws Exception
            {
                Thread.sleep(lateTime == 0 ? 2000 : lateTime);
                return UserAccountModel.getGymUserAccount(UserAccountModel.FROMPAGE_DEF);
            }

            @Override
            protected void onSuccess(APIResponse data)
            {
                APIResponse<UserAccountBean> userAccount = data;
                mUserAccountBean = userAccount.data;
                if(BuildConfig.DEBUG)
                    LogUtil.dClass("更新余额"+ mUserAccountBean.balance);
                if(null != mUserAccountBean)
                {
                    sendUserAccountUpdateBroadcastReceiver();
                }

            }
        }.execute(mContext);


    }

    public static String getUserAccountBalance()
    {
        return null != mUserAccountBean ? mUserAccountBean.balance : "";
    }

    /**
     * 退出登录
     */
    public static void signOut()
    {
        currentUser = null;
        mUserAccountBean = null;
        SharedPreferences sp = mContext.getSharedPreferences(HXSUser.class.getName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
        sendUserInfoUpdateBroadcastReceiver();
        sendUserAccountUpdateBroadcastReceiver();
    }


    /**
     * 发送用户信息更新广播
     */
    public static void sendUserInfoUpdateBroadcastReceiver()
    {
        mContext.sendBroadcast(new Intent(HXSUser.class.getName()+"_UserUpate"));
    }

    /**
     * 注册用户更新广播
     * @param context
     * @param userUpdateBroadcastReceiver
     * @return
     */
    public static UserUpdateBroadcastReceiver registerUserUpateBroadcastReceiver(Context context, UserUpdateBroadcastReceiver userUpdateBroadcastReceiver)
    {
        context.registerReceiver(userUpdateBroadcastReceiver, new IntentFilter(HXSUser.class.getName()+"_UserUpate"));
        return userUpdateBroadcastReceiver;
    }

    /**
     * 用户数据产生变化时的广播
     */
    public static class UserUpdateBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

        }
    }



    /**
     * 发送用户帐户信息发生变化广播
     */
    public static void sendUserAccountUpdateBroadcastReceiver()
    {
        mContext.sendBroadcast(new Intent(HXSUser.class.getName()+"_UserAccountUpdate"));
    }


    /**
     * 注册用户帐户信息更新广播
     *
     * @param context
     * @return
     */
    public static UserAccountUpdateBroadcastReceiver registerUserAccountUpateBroadcastReceiver(Context context, UserAccountUpdateBroadcastReceiver userAccountUpdateBroadcastReceiver)
    {
        context.registerReceiver(userAccountUpdateBroadcastReceiver, new IntentFilter(HXSUser.class.getName()+"_UserAccountUpdate"));
        return userAccountUpdateBroadcastReceiver;
    }


    /**
     * 用户帐户余额数据产生变化时的广播
     */
    public static class UserAccountUpdateBroadcastReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {

        }
    }


}
