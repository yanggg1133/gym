package com.hxs.fitnessroom;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will go on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{
    @Test
    public void addition_isCorrect() throws Exception
    {
        String[] sss = new String[0];
        sss[0] = "全部";
        System.out.print(sss[0]);
//        assertEquals(4, 2 + 2);
    }

    @Test
    public void testUtime() throws Exception
    {
        Calendar calendar = Calendar.getInstance();
        System.out.println(System.currentTimeMillis());
//        calendar.setTimeInMillis(1509987737144l);
        calendar.setTimeInMillis(1509959554636l);
        System.out.println(calendar.get(Calendar.AM_PM));
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println(calendar.get(Calendar.HOUR));
        System.out.println(calendar.get(Calendar.MINUTE));
    }


}