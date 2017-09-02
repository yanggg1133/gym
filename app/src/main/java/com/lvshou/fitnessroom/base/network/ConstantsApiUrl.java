package com.lvshou.fitnessroom.base.network;

/**
 * 用于获取后台接口Url地址的工具类
 * Created by jie on 16-2-14.
 */
public enum ConstantsApiUrl
{

    //测试
    TestUrl("api/Test/api"),

    ;
    private String linkAddress;
    ConstantsApiUrl(String linkAddress)
    {
        this.linkAddress = linkAddress;
    }

    /**
     * 普通接口域名
     * @return
     */
    private static String API_ROOT = "http://gym.hxsapp.com/";
    public String getAPIRootUrl()
    {
        return API_ROOT + this.linkAddress;
    }

    /**
     * H5 页面域名
     * @return
     */
    public String getH5RootUrl()
    {
        return "" + this.linkAddress;
    }


}
