package com.hxs.fitnessroom.util;

import android.content.Context;
import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by je on 9/5/17.
 */
@RunWith(AndroidJUnit4.class)
public class TestDateUtil
{

    @Test
    public void testDateStrToMills() throws Exception
    {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

       Log.d("ss",System.currentTimeMillis()+"");
        Log.d("ss",SystemClock.elapsedRealtime()+"");
        assertEquals("com.hxs.fitnessroom", appContext.getPackageName());
    }



}
