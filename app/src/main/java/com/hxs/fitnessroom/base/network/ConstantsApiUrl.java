package com.hxs.fitnessroom.base.network;

/**
 * 用于获取后台接口Url地址的工具类
 * Created by jie on 16-2-14.
 */
public enum ConstantsApiUrl
{
    //测试
    TestUrl("api/Test/api"),
    /** gym **************************************************/
    StoreList("https://gym.hxsapp.com/server/Store/storeList"),//门店列表
    AreaList("https://gym.hxsapp.com/server/Store/areaList"),//所有城市列表
    AddRecharge("https://gym.hxsapp.com/server/Order/addRecharge"),//发起充值

    /** account **************************************************/
    SendSMS("https://account.hxsapp.com/user/userAccount/sendSMS"),//发验证码
    Register("https://account.hxsapp.com/user/userAccount/register"),//注册
    Login("https://account.hxsapp.com/user/userAccount/login"),//注册
    ThirdPartyLogin("https://account.hxsapp.com/user/userAccount/thirdPartyLogin"),//第三方登录
    GetSelfUserInfo("https://account.hxsapp.com/user/userInfo/getSelfUserInfo"),//获取用户信息
    ;

    private String linkAddress;
    ConstantsApiUrl(String linkAddress)
    {
        this.linkAddress = linkAddress;
    }

    public String getUrl()
    {
        return  this.linkAddress;
    }
}
