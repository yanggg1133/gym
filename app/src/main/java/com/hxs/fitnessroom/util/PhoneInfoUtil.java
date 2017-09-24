package com.hxs.fitnessroom.util;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.hxs.fitnessroom.BuildConfig;

/**
 * 用于获取各种手机设备信息
 * Created by je on 9/12/17.
 */

public class PhoneInfoUtil
{
    //获取IMEI码
    public static String PhoneIMEI = null;
    public static final String ModelVersion = android.os.Build.MODEL;
    public static final String SystemVersion = android.os.Build.VERSION.RELEASE;
    public static final String AppVersion = BuildConfig.VERSION_NAME;
    public static final int HxsAppType = 1;//1代表是健身房app, 0代表好享瘦app

    public static void appInitialization(Context context)
    {
        try
        {
            TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTm.getDeviceId() != null)
            {
                PhoneIMEI = mTm.getDeviceId();
            } else
            {
                PhoneIMEI = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        } catch (Exception e)
        {
        }
        PhoneIMEI = "";
    }


}
