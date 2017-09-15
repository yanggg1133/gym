package com.hxs.fitnessroom.util;

import org.junit.Test;

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


}
