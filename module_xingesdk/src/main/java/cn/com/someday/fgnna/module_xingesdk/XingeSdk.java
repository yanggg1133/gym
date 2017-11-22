package cn.com.someday.fgnna.module_xingesdk;

import android.content.Context;
import android.util.Log;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

/**
 * 信鸽推送sdk
 * Created by shaojunjie on 17-11-13.
 */

public class XingeSdk
{

    public static void initSdk(Context context, String id)
    {
        //开启信鸽日志输出

//        XGPushConfig.enableDebug(context, true,id);

        //信鸽注册代码
        XGIOperateCallback xgiOperateCallback = new XGIOperateCallback()
        {
            @Override
            public void onSuccess(Object data, int flag)
            {
                Log.d("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg)
            {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        };
        if (null == id || "".equals(id))
            XGPushManager.registerPush(context.getApplicationContext(), xgiOperateCallback);
        else
            XGPushManager.registerPush(context.getApplicationContext(), id, xgiOperateCallback);
    }
}
