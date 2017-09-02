package com.lvshou.fitnessroom.module.main;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lvshou.fitnessroom.base.baseclass.BaseActivity;

/**
 * APP启动页
 * Created by je on 9/2/17.
 */
public class LoadingScreenActivity extends BaseActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestPermission(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION});
    }
}
