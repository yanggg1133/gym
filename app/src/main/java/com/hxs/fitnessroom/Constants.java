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
    public static final int PAGE_SIZE = 20;

}
