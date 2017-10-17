package fitnessroom.hxs.com.sharesdk;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * ShareSDk的所有调用函数封装
 * Created by je on 9/29/17.
 */

public class ShareUtil
{

    /**
     * 调用shareSDk的一键分享UI
     * @param context
     */
    private void showShare(Context context)
    {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("url");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        oks.setCallback(new OneKeyShareCallback());
        // 启动分享GUI
        oks.show(context);
    }


    /**
     * 分享回调
     */
    class  OneKeyShareCallback implements PlatformActionListener
    {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap)
        {

        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable)
        {

        }

        @Override
        public void onCancel(Platform platform, int i)
        {

        }
    }




    public static void loginByWechat(PlatformActionListener platformActionListener)
    {
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.SSOSetting(false);  //设置false表示使用SSO授权方式

        Log.e("ShareUtil",""+wechat.getDb().getUserId());
        wechat.setPlatformActionListener(platformActionListener);
//        wechat.setPlatformActionListener(new PlatformActionListener()
//        {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap)
//            {
//                /**
//                {sex=0,
//                 privilege=[],
//                 unionid=odparxBXSEJNE0xYnt1GFhQvcue4,
//                 province=,
//                 language=zh_CN,
//                 headimgurl=/0,
//                 city=,
//                 country=}
//                */
//
//                //weixin
//                //nickname=having ,
//                //openid=oMwZm1Zlkyo8d6fK7ZF1kt8zPdGs,
//                //bind_type
//                //bind_head_img
//
//                //qq
//
//                Log.e("ShareUtil","onComplete"+hashMap.toString());
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable)
//            {
//                Log.e("ShareUtil","onError="+throwable.getMessage());
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i)
//            {
//                Log.e("ShareUtil","onCancel");
//            }
//        }); // 设置分享事件回调

//        wechat.authorize();//单独授权
        wechat.showUser(null);//授权并获取用户信息
    }
}
