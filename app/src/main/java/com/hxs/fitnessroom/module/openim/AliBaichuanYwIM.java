package com.hxs.fitnessroom.module.openim;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.wxlib.util.SysUtil;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.openim.model.entity.OpenIMAccountBean;
import com.hxs.fitnessroom.module.openim.model.OpenIMModel;

/**
 * 阿里的IM SDK 工具类
 * Created by je on 9/25/17.
 */

public class AliBaichuanYwIM
{
    /**
     * APP KEY
     */
    private static final String APP_KEY = "24628171";


    private static Context mContext;

    private AliBaichuanYwIM()
    {
    }

    /**
     * APP启动时初始化
     *
     * @param context
     */
    public static void appInitialization(Context context)
    {
        mContext = context;
        //必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
        SysUtil.setApplication(mContext);
        if (SysUtil.isTCMSServiceProcess(mContext))
        {
            return;
        }
        //第一个参数是Application Context
        //这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
        if (SysUtil.isMainProcess())
        {
            YWAPI.init((Application) mContext, APP_KEY);
        }
    }

    public static void gotoIM()
    {
        new GetOpenIMAccountTask().go(mContext);
    }

    static class GetOpenIMAccountTask extends BaseAsyncTask
    {
        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return OpenIMModel.getOpenIMAccount();
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            APIResponse<OpenIMAccountBean> response = data;
            final YWIMKit mIMKit = YWAPI.getIMKitInstance(response.data.gym_account, APP_KEY);
            final String userid = response.data.gym_account;
            String password = "Ftp2Ub18";
            IYWLoginService loginService = mIMKit.getLoginService();
            YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
            loginService.login(loginParam, new IWxCallback()
            {

                @Override
                public void onSuccess(Object... arg0)
                {
                    //userid是客服帐号，第一个参数是客服帐号，第二个是组ID，如果没有，传0
                    EServiceContact contact = new EServiceContact(userid, 0);
                    //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
                    //的setNeedByPass方法，参数为false。
                    //contact.setNeedByPass(false);
                    Intent intent = mIMKit.getChattingActivityIntent(contact);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }

                @Override
                public void onProgress(int arg0)
                {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onError(int errCode, String description)
                {
                    //如果登录失败，errCode为错误码,description是错误的具体描述信息
                }

            });
        }
    }

}
