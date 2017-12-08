package com.hxs.fitnessroom;

import com.hxs.fitnessroom.base.network.ParamsBuilder;

import java.lang.reflect.Type;

/**
 * APP中的统一使用的常量
 * Created by je on 9/5/17.
 */

public class Constants
{
    /**
     * 查询列表时默认的一次查询多少条
     * 在下面的函数中已经硬编码
     * @see com.hxs.fitnessroom.base.network.APIHttpClient#postForm(String, ParamsBuilder, Type)
     */
    public static final int PAGE_SIZE = 10;

    /**
     * 翻页查询时，默认的last_id
     */
    public static final String PAGE_DEFAULT_LAST_ID = "0";

    /**
     * 接口验签密钥
     */
    public static String SIGN_KEY = "acol$!z%wh";


    /**
     * 相隔30分钟，刷新回话id
     */
    public static final int SESSION_REFRESH_TIMEOUT = 30 * 60 * 1000;//

}
