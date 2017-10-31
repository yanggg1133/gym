package com.hxs.fitnessroom.util;

import android.util.Log;

import com.hxs.fitnessroom.BuildConfig;

public class LogUtil
{
    private static final String TAG = "Fitness";

    public LogUtil() {
    }

    // 下面四个是默认tag的函数
    public static void i(String msg)
    {
        if (BuildConfig.LOG_DEBUG && ValidateUtil.isNotEmpty(msg))
            Log.i(TAG, msg);
    }

    public static void d(String msg)
    {
        if (BuildConfig.LOG_DEBUG && ValidateUtil.isNotEmpty(msg))
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (BuildConfig.LOG_DEBUG && ValidateUtil.isNotEmpty(msg))
            Log.e(TAG, msg);
    }
    public static void dClass(String msg) {
        if (BuildConfig.LOG_DEBUG && ValidateUtil.isNotEmpty(msg))
        {
            String clazzName = new Throwable().getStackTrace()[1].getClassName();
            String[] clazzNames = clazzName.split("\\.");
            try
            {
                Log.d(clazzNames[clazzNames.length - 1], msg);
            }catch (RuntimeException e)
            {
                System.out.println(msg);
            }
        }
    }

    public static void dClass(Object msg) {
        if (BuildConfig.LOG_DEBUG && ValidateUtil.isNotEmpty(msg))
        {
            String clazzName = new Throwable().getStackTrace()[1].getClassName();
            String[] clazzNames = clazzName.split("\\.");
            try
            {
                Log.d(clazzNames[clazzNames.length - 1], msg.toString());
            }catch (RuntimeException e)
            {
                System.out.println(msg.toString());
            }
        }
    }

    public static void v(String msg) {
        if (BuildConfig.LOG_DEBUG && ValidateUtil.isNotEmpty(msg))
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg)
    {
        if (BuildConfig.LOG_DEBUG && ValidateUtil.isNotEmpty(msg))
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg)
    {
        if (BuildConfig.LOG_DEBUG && ValidateUtil.isNotEmpty(msg))
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG && ValidateUtil.isNotEmpty(msg))
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG && ValidateUtil.isNotEmpty(msg) )
            Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG && ValidateUtil.isNotEmpty(msg) )
            Log.w(tag, msg);
    }

    public static void wtf(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG && ValidateUtil.isNotEmpty(msg) )
            Log.wtf(tag, msg);
    }

}
