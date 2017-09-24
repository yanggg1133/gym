package com.hxs.fitnessroom.base.baseclass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bumptech.glide.Glide;

/**
 * Fragment基类
 * Created by je on 8/31/17.
 */

public class BaseFragment extends Fragment
{
    private View mRootView;

    private HXSUser.UserUpdateBroadcastReceiver mUserUpdateBroadcastReceiver;
    private HXSUser.UserAccountUpdateBroadcastReceiver mUserAccountUpdateBroadcastReceiver;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        mRootView = view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Glide.with(this).onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Glide.with(this).onStop();
    }

    protected BaseActivity getBaseActivity()
    {
        return (BaseActivity) getActivity();
    }

    public <T extends View> T findViewById(@IdRes int id)
    {
        return (T) mRootView.findViewById(id);
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
            if (null != mUserUpdateBroadcastReceiver)
                getBaseActivity().unregisterReceiver(mUserUpdateBroadcastReceiver);
        } catch (Exception e)
        {
            //预防某种未情况引发的异常
        }

        mUserUpdateBroadcastReceiver = new HXSUser.UserUpdateBroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                onUserUpdate();
            }
        };
        HXSUser.registerUserUpateBroadcastReceiver(getBaseActivity(), mUserUpdateBroadcastReceiver);
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
                getBaseActivity().unregisterReceiver(mUserAccountUpdateBroadcastReceiver);

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

        HXSUser.registerUserAccountUpateBroadcastReceiver(getBaseActivity(), mUserAccountUpdateBroadcastReceiver);
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


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        try
        {
            Glide.with(this).onDestroy();
        } catch (Exception e)
        {
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (null != mUserUpdateBroadcastReceiver)
        {
            getBaseActivity().unregisterReceiver(mUserUpdateBroadcastReceiver);
            mUserUpdateBroadcastReceiver = null;
        }
        if (null != mUserAccountUpdateBroadcastReceiver)
        {
            getBaseActivity().unregisterReceiver(mUserAccountUpdateBroadcastReceiver);
            mUserAccountUpdateBroadcastReceiver = null;
        }
    }
}
