package com.hxs.fitnessroom.util;

import java.util.Calendar;

/**
 * 日期工具类
 * Created by je on 9/14/17.
 */

public class DateUtil
{


    /**
     * 返回 本年 的年份
     * @return
     */
    public static int getLastYear()
    {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 返回 一年 最近70年的年份数组
     * @return
     */
    public static String[] getLast70YearsArray()
    {
        String[] yearArray = new String[70];
        int currentYear = DateUtil.getLastYear();
        for(int i = 0,year=currentYear-yearArray.length+1; i < yearArray.length;i++)
        {
            yearArray[i] = (year+i)+" 年";
        }
        return yearArray;
    }

    /**
     * 返回 12个月的月份数组
     * @return
     */
    public static String[] getMonthsArray()
    {
        String[] monthArray = new String[]{
                "1 月","2 月","3 月","4 月","5 月","6 月","7 月","8 月",
                "9 月", "10 月","11 月","12 月", };
        return monthArray;
    }

}
