package cn.com.someday.fgnna.module_xingesdk;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * Created by shaojunjie on 17-11-13.
 */

public class MessageReceiver extends XGPushBaseReceiver
{
    public static final String ReceiverKey = "cn.com.someday.fgnna.module_xingesdk_closeAnAccount";


    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult)
    {
        Log.d("MessageReceiver","onRegisterResult");
    }

    @Override
    public void onUnregisterResult(Context context, int i)
    {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s)
    {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s)
    {

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage)
    {
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult)
    {

    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult)
    {
        CustomContentBean customContentBean = getCustomContentBean(xgPushShowedResult.getCustomContent());
        if(null != customContentBean && "1".equals(customContentBean.type))
        {

            context.sendBroadcast(new Intent(ReceiverKey));
        }

    }



    private CustomContentBean getCustomContentBean(String customContent)
    {
        if(null == customContent || "".equals(customContent))
            return null;

        try
        {
            return new Gson().fromJson(customContent,new TypeToken<CustomContentBean>(){}.getType());
        }
        catch (Exception e)
        {
            return null;
        }

    }


    /**
     * {
     *  "type":"2",
     *  "objId":""
     * }
     */
    class CustomContentBean
    {
        private String type;
        private String objId;
    }

}
