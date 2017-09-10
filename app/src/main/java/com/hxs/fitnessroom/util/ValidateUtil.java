package com.hxs.fitnessroom.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据类型验证
 * 空值验证
 *
 * @author jie
 */
public class ValidateUtil
{
    /**
     * 是否为空
     *
     * @return
     */
    public static boolean isEmpty(Object o) {
        if (null == o)
            return true;

        if (o instanceof String)
            return "".equals(o) ? true : false;

        if (o instanceof List)
            return ((List) o).size() == 0 ? true : false;

        if (o instanceof Map)
            return ((Map) o).size() == 0 ? true : false;
        if (o instanceof String[])
            return ((String[]) o).length == 0 ? true : false;
        if (o instanceof int[])
            return ((int[]) o).length == 0 ? true : false;
        if (o instanceof Set)
            return ((Set) o).size() == 0 ? true : false;

        return false;
    }

    /**
     * 是否不为空
     *
     * @return
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    public static boolean isInt(String value)
    {
        try
        {
            new Integer(value);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

}
