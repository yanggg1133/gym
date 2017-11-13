package com.hxs.fitnessroom.base.network;

import com.hxs.fitnessroom.BuildConfig;

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
    Appointment("https://api.hxsapp.com/gym/Appointment/add"),//门店预约列表
    AppointmentUPay("https://api.hxsapp.com/gym/Appointment/upay"),//门店预约支付
    AppointmentList("https://api.hxsapp.com/gym/Appointment/myList"),//我的预约列表
    AppointmentCancel("https://api.hxsapp.com/gym/Appointment/cancel"),//取消预约
    AreaList("https://api.hxsapp.com/gym/Store/areaList"),//所有城市列表

    AddRecharge("https://api.hxsapp.com/mall/GymOrder/addPay"),//发起充值 发起支付 通用
    OrderQuery("https://api.hxsapp.com/mall/GymOrder/orderQuery"),//发起支付后主动查询
    RechargeList("https://api.hxsapp.com/gym/Base/rechargeList"),//充值金额列表
    DeQRCode("https://api.hxsapp.com/gym/Dispatcher/deQRCode"), //二维码扫描通用此入口
    Deposit("https://api.hxsapp.com/gym/Base/deposit"), //获取押金金额
    RefundDeposit("https://api.hxsapp.com/gym/Base/refundDeposit"), //获取退押金详情
    SubmitRefundDeposit("https://api.hxsapp.com/gym/Base/submitRefundDeposit"), //获取退押金详情
    GetGymUserAccount("https://api.hxsapp.com/gym/Base/getGymUserAccount"), //获取用户状态和余额
    GetUserDeviceStatus("https://api.hxsapp.com/gym/Base/getUserDeviceStatus"), //获取当前用户设备使用情况
    GetAccountLog("https://api.hxsapp.com/mall/GymOrder/getAccountLog"), //获取钱包明细
    GetNoticeList("https://api.hxsapp.com/base/notice/getNoticeList"),//取系统通知列表


    /** account **************************************************/
    SendSMS("https://api.hxsapp.com/account/userAccount/sendSMS"),//发验证码
    Register("https://api.hxsapp.com/account/userAccount/register"),//注册
    Login("https://api.hxsapp.com/account/userAccount/login"),//注册
    ThirdPartyLogin("https://api.hxsapp.com/account/userAccount/thirdPartyLogin"),//第三方登录
    GetSelfUserInfo("https://api.hxsapp.com/account/userInfo/getSelfUserInfo"),//获取用户信息
    SaveUserInfo("https://api.hxsapp.com/account/userInfo/saveInfo"),//保存用户信息
    GetOpenIMAccount("https://api.hxsapp.com/account/UserAccount/getOpenIMAccount"),//获取用户的openim账号
    SaveRealname("https://api.hxsapp.com/account/UserInfo/realname"),//提交实名认证
    getRealname("https://api.hxsapp.com/account/UserInfo/getRealname"),//提交实名认证

    /** h5相关页面 **********************************************************/
    H5_gymDetail("https://gymweb.hxsapp.com/gymDetail"),//健身房详情                     ***必须传id***
    H5_gymDetailBaiduMap("https://gymweb.hxsapp.com/baiduMap"),//健身房地图                     ***必须传id***
    H5_myTutorialList("https://gymweb.hxsapp.com/myTutorialList"),//我的课程
    //H5_myTutorialDetail("https://gym.hxsapp.com/myTutorialDetail/"),//我的课程详情
    H5_meExercise("https://gymweb.hxsapp.com/meExercise"),//我的锻炼
    H5_recharge("https://gymweb.hxsapp.com/recharge"),//充值协议
    H5_agreement("https://gymweb.hxsapp.com/agreement"),//用户协议
    H5_deposit("https://gymweb.hxsapp.com/deposit"),//押金说明
    ;

    private String linkAddress;
    ConstantsApiUrl(String linkAddress)
    {
        if(BuildConfig.DEBUG)
            linkAddress = linkAddress.replace("https","http");
        this.linkAddress = linkAddress;
    }

    public String getUrl()
    {
        return  this.linkAddress;
    }

    public String getH5Url(String... parames)
    {
        String tempLinkAddress = linkAddress;
        if(null != parames)
        {
            for(String param:parames)
                tempLinkAddress += "/" + param;
        }
        return tempLinkAddress;
    }

}
