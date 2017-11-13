package cn.com.someday.fgnna.module_xingesdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult)
    {
        Log.d("MessageReceiver","onRegisterResult");
    }

    @Override
    public void onUnregisterResult(Context context, int i)
    {
        Log.d("MessageReceiver","onUnregisterResult");

    }

    @Override
    public void onSetTagResult(Context context, int i, String s)
    {
        Log.d("MessageReceiver","onSetTagResult");

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s)
    {
        Log.d("MessageReceiver","onDeleteTagResult");

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage)
    {
        Log.d("MessageReceiver","onTextMessage");

    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult)
    {
        Log.d("MessageReceiver","onNotifactionClickedResult");

    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult)
    {
        Log.d("MessageReceiver","onNotifactionShowedResult");

    }
}
