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
    StoreList("https://api.hxsapp.com/gym/Store/storeList"),//门店列表
    AreaList("https://api.hxsapp.com/gym/Store/areaList"),//所有城市列表

    AddRecharge("https://api.hxsapp.com/mall/GymOrder/addPay"),//发起充值
    OrderQuery("https://api.hxsapp.com/mall/GymOrder/orderQuery"),//发起支付后主动查询
    RechargeList("https://api.hxsapp.com/gym/Base/rechargeList"),//充值金额列表
    DeQRCode("https://api.hxsapp.com/gym/Dispatcher/deQRCode"), //二维码扫描通用此入口
    Deposit("https://api.hxsapp.com/gym/Base/deposit"), //获取押金金额
    GetGymUserAccount("https://api.hxsapp.com/gym/Base/getGymUserAccount"), //获取用户状态和余额



    /** account **************************************************/
    SendSMS("https://account.hxsapp.com/user/userAccount/sendSMS"),//发验证码
    Register("https://account.hxsapp.com/user/userAccount/register"),//注册
    Login("https://account.hxsapp.com/user/userAccount/login"),//注册
    ThirdPartyLogin("https://account.hxsapp.com/user/userAccount/thirdPartyLogin"),//第三方登录
    GetSelfUserInfo("https://account.hxsapp.com/user/userInfo/getSelfUserInfo"),//获取用户信息
    SaveUserInfo("https://account.hxsapp.com/user/userInfo/saveInfo"),//保存用户信息

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
