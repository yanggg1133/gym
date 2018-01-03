package com.macyer.utils;

import android.content.Context;

/**
 * Created by Lenovo on 2017/12/7.
 */

public class ContextUtils {

    private static Context context;

    public static void init(Context mContext){
        context = mContext;
    }

    public static Context getContext(){
        return context;
    }
}
