package com.hxs.fitnessroom.util;

import org.junit.Test;

import java.util.Calendar;

/**
 * Created by je on 9/5/17.
 */
public class TestDateUtil
{

    @Test
    public void testGetNowYear() throws Exception
    {
        String[] yearArray = new String[70];
        int currentYear = DateUtil.getLastYear();
        for(int i = 0,year=currentYear-yearArray.length+1; i < yearArray.length;i++)
        {
            yearArray[i] = (year+i)+" 年";
            System.out.println((year+i)+" 年");
        }
    }
    @Test
    public void testTime() throws Exception
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1505898987000l);

        System.out.println(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE));
        System.out.println(calendar.get(Calendar.HOUR_OF_DAY)+"-"+calendar.get(Calendar.MINUTE)+"-"+calendar.get(Calendar.SECOND));
        calendar.setTimeInMillis(1505872258000l);
        System.out.println(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE));
        System.out.println(calendar.get(Calendar.HOUR_OF_DAY)+"-"+calendar.get(Calendar.MINUTE)+"-"+calendar.get(Calendar.SECOND));

    }



}
