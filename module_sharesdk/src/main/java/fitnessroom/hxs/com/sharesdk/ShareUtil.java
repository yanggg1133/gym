package fitnessroom.hxs.com.sharesdk;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
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
     *
     * @param context
     */
    public static void showShare(Context context)
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
    static class OneKeyShareCallback implements PlatformActionListener
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


    /**
     * 第三方登陆授权
     *
     * @param name          授权类型名
     * @param loginCallBack 结果回调
     * @see WechatMoments#NAME
     * @see QQ#NAME
     */
    public static void thirdPartylogin(String name, final LoginCallBack loginCallBack)
    {
        final Platform _platform = ShareSDK.getPlatform(name);
//        _platform.SSOSetting(true);  //设置false表示使用SSO授权方式
        /**
         * 由于回调是非主程，所以要用handler切换到主线程
         */
        final Handler handle = new Handler();
        _platform.setPlatformActionListener(new PlatformActionListener()
        {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap)
            {
                handle.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loginCallBack.onComplete(_platform.getDb());
                    }
                });
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable)
            {
                handle.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loginCallBack.onComplete(null);
                    }
                });
            }

            @Override
            public void onCancel(Platform platform, int i)
            {
                handle.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loginCallBack.onComplete(null);
                    }
                });
            }
        }); // 设置分享事件回调

        _platform.authorize();//单独授权
    }

    /**
     * 第三方登录授权回调接口
     */
    public interface LoginCallBack
    {
        /**
         * 回调
         *
         * @param platformDb 如果为空，为授权失败
         */
        void onComplete(@Nullable PlatformDb platformDb);
    }
}
