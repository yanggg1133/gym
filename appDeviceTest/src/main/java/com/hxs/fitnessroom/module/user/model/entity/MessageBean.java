package com.hxs.fitnessroom.module.user.model.entity;

import java.util.List;

/**
 * Created by je on 19/09/17.
 */

public class MessageBean
{
    public String notice_time;//: "08月22日",
    public List<NoticeData> notice_data;
    public static class NoticeData
    {
        public String id;//: "78",//通知id
        public String link;//: "http://www.baidu.com",//跳转链接
        public String title;//: "dffffffffffffffffffffffffffffff",//通知标题
        public String image;//: "",//图片
        public String content;//: "<p>fffffffffffffffffffffffffffff<br/></p>",//通知内容
        public String admin_user_id;//: "1",
        public String fli_ls;//: "0",
        public String platform;//: "0",
        public String state;//: "0",//0-正常，-1=禁用
        public String create_time;//: "2017-08-22 10:42:52",///创建时间
        public String update_time;//: "2017-08-22 10:42:52",//更新时间
        public String is_read;//: 1,//0-未读，1-已读
        public String notice_time;//: "08月22日"
    }
}
