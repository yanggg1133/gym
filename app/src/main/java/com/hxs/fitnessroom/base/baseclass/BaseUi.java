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
public class BaseUi
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
        baseInitView();
    }

    public BaseUi(BaseFragment baseFragment)
    {
        mBaseFragment = baseFragment;
        mReloadListener = mBaseFragment instanceof LoadingView.OnReloadListener ? (LoadingView.OnReloadListener)mBaseFragment:null;
        mOnClickListener = mBaseFragment instanceof View.OnClickListener ? (View.OnClickListener) mBaseFragment :null;
        baseInitView();
    }

    private void baseInitView()
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

    /**
     * 查找View 并自动设置View的点击事件
     * 点击事件为 Activity 或 framgert的现实
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T findViewByIdAndSetClick(@IdRes int id)
    {
        T t = null;
        if (null != mBaseActivity)
            t = (T) mBaseActivity.findViewById(id);
        else
            t = (T) mBaseFragment.findViewById(id);
        if(null != t)
            t.setOnClickListener(mOnClickListener);
        return t;
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

    public MyToolbar getMyToolbar()
    {
        return myToolbar;
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

    public void setBackAction(boolean isCanBack)
    {
        if(myToolbar != null)
        {
            myToolbar.setBack(isCanBack, new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(mBaseActivity != null)
                        mBaseActivity.finish();
                }
            });
        }
    }
}
