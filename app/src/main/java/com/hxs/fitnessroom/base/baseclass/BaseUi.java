package com.hxs.fitnessroom.base.baseclass;

import android.support.annotation.IdRes;
import android.view.View;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.widget.LoadingView;
import com.hxs.fitnessroom.widget.MyToolbar;

/**
 * 基础UI类，
 * 可提供常用的
 * 加载等待显示
 * Created by Administrator on 2015/11/4.
 */
public abstract class BaseUi
{
    private BaseActivity mBaseActivity;
    private BaseFragment mBaseFragment;
    private LoadingView loading_view;
    private LoadingView.OnReloadListener mReloadListener;
    private View.OnClickListener mOnClickListener;
    private MyToolbar myToolbar;

    public BaseUi(BaseActivity baseActivity)
    {
        mBaseActivity = baseActivity;
        mReloadListener = mBaseActivity instanceof LoadingView.OnReloadListener ? (LoadingView.OnReloadListener)mBaseActivity:null;
        mOnClickListener = mBaseActivity instanceof View.OnClickListener ? (View.OnClickListener) mBaseActivity :null;
        initView();
    }

    public BaseUi(BaseFragment baseFragment)
    {
        mBaseFragment = baseFragment;
        mReloadListener = mBaseFragment instanceof LoadingView.OnReloadListener ? (LoadingView.OnReloadListener)mBaseFragment:null;
        mOnClickListener = mBaseFragment instanceof View.OnClickListener ? (View.OnClickListener) mBaseFragment :null;
        initView();
    }

    private void initView()
    {
        initMyToolbar();
        initLoadingView();
    }

    public View.OnClickListener getBaseOnclick()
    {
        return mOnClickListener;
    }

    private void initMyToolbar()
    {
        myToolbar = findViewById(R.id.toolbar);
    }

    private void initLoadingView()
    {
        loading_view = findViewById(R.id.loading_view);
        if (loading_view != null)
        {
            loading_view.setReloadListener(mReloadListener);
        }
    }


    public <T extends View> T findViewById(@IdRes int id)
    {
        if (null != mBaseActivity)
            return (T) mBaseActivity.findViewById(id);
        else
            return (T) mBaseFragment.findViewById(id);
    }

    public BaseActivity getBaseActivity()
    {
        return mBaseActivity;
    }
    public BaseFragment getBaseFragment()
    {
        return mBaseFragment;
    }

    public  void setTitle(String title)
    {
        if(myToolbar != null)
            myToolbar.setTitle(title);
    }

    public void startLoading()
    {

    }

    public void endLoading()
    {

    }

    public LoadingView getLoadingView()
    {
        return loading_view;
    }
}
