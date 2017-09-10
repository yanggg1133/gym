package com.hxs.fitnessroom.base.baseclass;

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
        Glide.with(this).onStop();
        super.onStop();
    }

    protected BaseActivity getBaseActivity()
    {
        return (BaseActivity) getActivity();
    }

    public <T extends View> T findViewById(@IdRes int id)
    {
        return (T) mRootView.findViewById(id);
    }
}
