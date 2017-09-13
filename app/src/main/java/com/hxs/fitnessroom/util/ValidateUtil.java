package com.hxs.fitnessroom.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    /**
     * 检查是否电话号码
     * @param mobiles
     * @return
     */
    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern.compile("^((1[0-9][0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    /**
     * md5加密
     * @param info
     * @return
     */
    public static String getMD5(String info)
    {
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++)
            {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1)
                {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                }
                else
                {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            return "";
        }
        catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }

}
