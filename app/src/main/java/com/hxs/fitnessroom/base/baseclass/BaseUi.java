package com.hxs.fitnessroom.base.baseclass;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * 基础UI类，
 * 可提供常用的
 *         加载等待显示
 * Created by Administrator on 2015/11/4.
 */
public abstract class BaseUi
{
    private BaseActivity mBaseActivity;
    private BaseFragment mBaseFragment;

    public BaseUi(BaseActivity baseActivity)
    {
        mBaseActivity = baseActivity;
    }
    public BaseUi(BaseFragment baseFragment)
    {
        mBaseFragment = baseFragment;
    }

    public <T extends View> T findViewById(@IdRes int id)
    {
        if(null != mBaseActivity)
            return (T) mBaseActivity.findViewById(id);
        else
            return (T) mBaseFragment.findViewById(id);
    }

    public BaseActivity getBaseActivity()
    {
        return mBaseActivity;
    }

    public void startLoading()
    {

    }

    public void endLoading()
    {


    }
}
