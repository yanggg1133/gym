package com.hxs.fitnessroom.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 注意思必须在app启动时初始化
 *
 * @see #appInitialization(Context)
 *
 * Created by je on 9/16/17.
 */

public class ToastUtil
{
    private static Context mContext;
    private ToastUtil(){}
    public static void appInitialization(Context context)
    {
        mContext = context;
    }


    public static void toastShort(String message)
    {
        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }
    public static void toastLong(String message)
    {
        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
    }

}
