package com.hxs.fitnessroom.module.openim.model.entity;

/**
 * 客服IM的帐号信息
 * Created by je on 9/25/17.
 */

public class OpenIMAccountBean
{
    public String openim_account;//": "hxs-1629",   //用户的IM账号
    public String service_openim_account;//": "server-1103542247545" //顾问的IM账号
    public String default_server;//": "brm-1103542247545"               //默认顾问账号（互联网中心顾问），用户咨询买手环啥的
    public String operate_server;//": "operate-0001"                    //使用咨询的客服账号（2.6.1新增）
    public String gym_server;//": "gym-0001"                            //健身房客服账号（健身房1.0新增）
    public String gym_account;//": "gym-1232"                           //健身房用户账号（健身房1.0新增）
    public String mall_server;//": "mall-0001"                          //商城客服账号（3.0新增）
}
