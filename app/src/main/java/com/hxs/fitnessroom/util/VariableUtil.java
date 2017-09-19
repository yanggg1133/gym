package com.hxs.fitnessroom.util;

/**
 * 变量转换工具类
 * Created by je on 9/19/17.
 */

public class VariableUtil
{
    public static double stringToDouble(String str)
    {
        try
        {
            return new Double(str);
        } catch (Exception e)
        {
            return 0d;
        }
    }

}
