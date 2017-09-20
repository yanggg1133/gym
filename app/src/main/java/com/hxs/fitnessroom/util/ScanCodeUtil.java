package com.hxs.fitnessroom.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.zxing.activity.CaptureActivity;

/**
 * 开启扫码工具类
 * Created by je on 9/20/17.
 */

public class ScanCodeUtil
{

    /**
     * 跳转扫码
     * @param activity
     * @param requestCode
     */
    public static void startScanCode(Activity activity,int requestCode)
    {
        Intent intent = new Intent(activity, CaptureActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
    public static void startScanCode(Fragment fragment, int requestCode)
    {
        Intent intent = new Intent(fragment.getContext(), CaptureActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }


    /**
     * 扫描结果返回后 从请求结果中获取扫描信息
     *
     * @param resultCode
     * @param data
     * @return 如果扫描失败返回 null;
     */
    public static String getResultScanCode(int resultCode, Intent data)
    {

        if (resultCode == CaptureActivity.RESULT_CODE_QR_SCAN && null != data)
        {
            String code = data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
            if(null == code || "".equals(code))
                return null;
            return code;
        }
        return null;
    }



}
