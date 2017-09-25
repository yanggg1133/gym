package com.hxs.fitnessroom.base.baseclass;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hxs.fitnessroom.util.DialogUtil;
import com.hxs.fitnessroom.widget.dialog.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity基类
 * Created by je on 8/31/17.
 */

public class BaseActivity extends AppCompatActivity
{
    public static final int RequestCode_Login = 10;//登录跳转
    public static final int RequestCode_Scan_OpenDoor = 11;//扫码开门
    public static final int RequestCode_Pay_Deposit = 12;//押金充值
    public static final int RequestCode_Pay_Recharge = 13;//余额充值
    public static final int RequestCode_action_scan_code = 14;//进入健身房后的所有扫码行为


    public static final int RequestCode_Activity_ReturnDeposit = 15;//跳转提交退押金界面



    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 111;
    private HXSUser.UserUpdateBroadcastReceiver mUserUpdateBroadcastReceiver;
    private HXSUser.UserAccountUpdateBroadcastReceiver mUserAccountUpdateBroadcastReceiver;

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
    }

    public <T extends View> T findViewByIdAuto(@IdRes int id)
    {
        return (T) super.findViewById(id);
    }


    /**
     * 处理返回键事件
     * 请重写{@link #onBackUp()}
     * @param keyCode
     * @param event
     * @return
     *
     * @see #onBackUp()
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (onBackUp()) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 如需处理返回键，
     * 可重写此方法
     * @return
     */
    protected boolean onBackUp() {
        return false;
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        try {Glide.with(this).onDestroy();}catch (Exception e) {}
        if(null != mUserUpdateBroadcastReceiver)
        {
            unregisterReceiver(mUserUpdateBroadcastReceiver);
            mUserUpdateBroadcastReceiver = null;
        }

        if (null != mUserAccountUpdateBroadcastReceiver)
        {
            unregisterReceiver(mUserAccountUpdateBroadcastReceiver);
            mUserAccountUpdateBroadcastReceiver = null;
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Glide.with(this).onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Glide.with(this).onStop();
    }

    @Override
    public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        Glide.with(this).onTrimMemory(level);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        Glide.with(this).onLowMemory();
    }

    /**
     * 申请权限
     * @param permissions
     */
    public void requestPermission(String[] permissions)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(filterPermission(permissions).length != 0)
                ActivityCompat.requestPermissions(this, filterPermission(permissions), MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            else
                onPermissionsPass();
        }
        else
        {
            onPermissionsPass();
        }
    }

    /**
     * 重新尝试申请权限
     * @param permissions
     */
    private void retryRequestPermission(String[] permissions)
    {
        permissions = filterPermission(permissions);
        for(String permission: permissions)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if(! shouldShowRequestPermissionRationale(permission))
                {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",this.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    return ;
                }
            }
        }
        requestPermission(permissions);
    }

    /**
     * 过滤已允许的权限
     * 返回未允许的权限
     * @param permissions
     */
    private String[] filterPermission(String[] permissions)
    {
        List<String> list = new ArrayList<>();

        for (String permission : permissions)
        {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (!(PackageManager.PERMISSION_GRANTED == permissionCheck))
            {
                list.add(permission);
            }
        }
        permissions = new String[list.size()];
        for (int i = 0; i < permissions.length; i++)
        {
            permissions[i] = list.get(i);
        }
        return permissions;
    }

    /**
     * 请求权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
            {
                boolean isPass = true;
                for(int grantResult : grantResults)
                {
                    if(grantResult == PackageManager.PERMISSION_DENIED)
                    {
                        isPass = false;
                        break;
                    }
                }

                if(!isPass)
                {
                    DialogUtil.showConfirmDialog("应用正常运行需要获取相应的权限！", "取消", "去设置", getSupportFragmentManager(), new ConfirmDialog.OnDialogCallbackAdapter()
                    {
                        @Override
                        public void onCancel()
                        {
                            finish();
                        }

                        @Override
                        public void onConfirm()
                        {
                            retryRequestPermission(permissions);
                        }
                    });
                }
                else
                {
                    onPermissionsPass();
                }
            }

        }
    }

    /**
     * 权限请求是否通过
     * 当请求申请权限后，需要重写该方法来获得回调通知
     *
     */
    public void onPermissionsPass()
    {
        Toast.makeText(this,"权限通过",Toast.LENGTH_SHORT).show();
    }

    /**
     * 监听用户信息变化广播
     * 确保开启监听后，重写{@link #onUserUpdate()}方法
     *
     * @see #onUserUpdate()
     */
    public void registerUserUpdateBroadcastReceiver()
    {
        try
        {
            if(null != mUserUpdateBroadcastReceiver)
                unregisterReceiver(mUserUpdateBroadcastReceiver);
        }catch (Exception e)
        {
            //预防某种未情况引发的异常
        }

        mUserUpdateBroadcastReceiver = new HXSUser.UserUpdateBroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent)
            {
                onUserUpdate();
            }
        };
        HXSUser.registerUserUpateBroadcastReceiver(this,mUserUpdateBroadcastReceiver);
    }

    /**
     * 监听用户帐户余额信息变化广播
     * 确保开启监听后，重写{@link #onUserAccountUpdate()}方法
     * @see #onUserUpdate()
     */
    public void registerUserAccountUpdateBroadcastReceiver()
    {
        try
        {
            if (null != mUserAccountUpdateBroadcastReceiver)
                unregisterReceiver(mUserAccountUpdateBroadcastReceiver);

        } catch (Exception e)
        {
            //预防某种未情况引发的异常
        }

        mUserAccountUpdateBroadcastReceiver = new HXSUser.UserAccountUpdateBroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                onUserAccountUpdate();
            }
        };

        HXSUser.registerUserAccountUpateBroadcastReceiver(this, mUserAccountUpdateBroadcastReceiver);
    }

    /**
     * 接收用户帐户余额信息变化通知
     */
    public void onUserAccountUpdate()
    {

    }


    /**
     * 接收到用户信息变化后的广播回调
     */
    public void onUserUpdate()
    {

    }



}
