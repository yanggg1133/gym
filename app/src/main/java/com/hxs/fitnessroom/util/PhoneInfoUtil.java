package com.hxs.fitnessroom.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.hxs.fitnessroom.BuildConfig;

import static com.hxs.fitnessroom.Constants.SESSION_REFRESH_TIMEOUT;

/**
 * 用于获取各种手机设备信息
 * Created by je on 9/12/17.
 */

public class PhoneInfoUtil
{
    private static Context mContext;

    //获取IMEI码
    public static String PhoneIMEI = null;
    public static final String ModelVersion = android.os.Build.MODEL;
    public static final String SystemVersion = android.os.Build.VERSION.RELEASE;
    public static final String AppVersion = BuildConfig.VERSION_NAME;
    public static final String AppName = "gym";
    public static final String ChannelName = BuildConfig.FLAVOR;
    public static String DisplaySize = BuildConfig.VERSION_NAME;//屏幕分辩率
    public static final int HxsAppType = 1;//1代表是健身房app, 0代表好享瘦app


    public static void appInitialization(Context context)
    {
        mContext = context;
        try
        {
            TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            {
                PhoneIMEI = "";
            }
            else if (mTm.getDeviceId() != null)
            {
                PhoneIMEI = mTm.getDeviceId();
            } else
            {
                PhoneIMEI = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }

        } catch (Exception e)
        {
            PhoneIMEI = "";
        }
        //分辩率
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        DisplaySize = metric.widthPixels + "*" + metric.heightPixels;
    }


    /**
     * 返回用户当前的上网类型
     *
     * @return
     */
    public static String getNetworkTypename()
    {
        try
        {
            ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null)
            {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info.getTypeName().equalsIgnoreCase("WIFI"))
                    return "WIFI";
                else if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                {
                    return "MOBILE";
                } else
                    return info.getTypeName();
            }
        } catch (Exception e)
        {
            return "unknown";
        }
        return "unknown";
    }


    private static String conversation_id = "";//会话ID
    private static long conversation_timeMillis = 0;//会话上次的会话时间

    /**
     * 获取会话ID，按时间算，
     * 如空，拿新的，
     * 如超30分钟，拿新的
     *
     * @return
     */
    public static String getDonversationId()
    {
        if (conversation_timeMillis == 0)
            conversation_timeMillis = System.currentTimeMillis();

        if (ValidateUtil.isEmpty(conversation_id))
            conversation_id = conversation_timeMillis + PhoneIMEI;

        //超30分钟 刷分会话id
        if (( System.currentTimeMillis() - conversation_timeMillis ) > SESSION_REFRESH_TIMEOUT)
        {
            conversation_timeMillis = System.currentTimeMillis();
            conversation_id = conversation_timeMillis + PhoneIMEI;
        }

        return conversation_id;
    }
}
