package com.lvshou.fitnessroom.base.baseclass;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lvshou.fitnessroom.R;
import com.lvshou.fitnessroom.util.DialogUtil;
import com.lvshou.fitnessroom.widget.MyToolbar;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity基类
 * Created by je on 8/31/17.
 */

public class BaseActivity extends AppCompatActivity
{
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 111;

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        MyToolbar myToolbar = (MyToolbar) findViewById(R.id.toolbar);
        if (null != myToolbar)
        {
            setSupportActionBar(myToolbar);
            ActionBar actionbar = getSupportActionBar();
            actionbar.setHomeAsUpIndicator(R.mipmap.nav_icon_back);
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

    }


    @Override
    public void finish()
    {
        Glide.with(this).onDestroy();
        super.finish();
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
        Glide.with(this).onStop();
        super.onStop();
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
                    DialogUtil.showConfirmationDialog(this, "运行权限", "应用正常运行需要获取相应的权限！", new DialogUtil.OnConfirmCallback()
                    {
                        @Override
                        public void onConfirm()
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            {
                                retryRequestPermission(permissions);
                            }
                        }

                        @Override
                        public void onCancel(DialogInterface dialog)
                        {
                            finish();
                        }
                    },"设置","取消");
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


}
