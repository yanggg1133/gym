package com.hxs.fitnessroom.base.baseclass;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.hxs.fitnessroom.base.network.APIResponse;

import java.lang.ref.WeakReference;

/**
 * 异步任务基类，用于处理一些须要重复处理的事情
 * Created by jie on 16-7-4.
 */
public abstract class BaseAsyncTask extends AsyncTask<Object, Object, APIResponse>
{
    private WeakReference<BaseActivity> mBaseActivity;
    private WeakReference<BaseUi> mBaseUi;

    /**
     * 线程任务执行必须调用下面三个方法的其中一个，不可调用原生中的{@link AsyncTask#execute(Object[])}
     * 否则会出现 {@link RuntimeException}
     * @param baseActivity
     */
    public final void  execute(BaseActivity baseActivity)
    {
        execute(baseActivity,null);
    }

    public final void  execute(BaseActivity baseActivity,Object... objects)
    {
        execute(baseActivity,null,objects);
    }

    public final void  execute(BaseActivity baseActivity,BaseUi baseUi,Object... objects)
    {
        if (null == baseActivity)
            throw new NullPointerException();

        mBaseActivity = new WeakReference<>(baseActivity);

        mBaseUi = new WeakReference<>(baseUi);

        execute(objects);
    }


    @Override
    protected void onPreExecute()
    {
        if(mBaseActivity == null )
            throw new RuntimeException("------调用了错误的execute()方法，请使用BaseAsyncTask的execute(BaseActivity baseActivity)方法---------------");

        if (null != mBaseUi.get())
            mBaseUi.get().endLoading();
    }

    @Override
    protected void onCancelled()
    {
        super.onCancelled();
        if (null != mBaseUi.get())
            mBaseUi = null;
        if (null != mBaseActivity.get())
            mBaseActivity = null;
    }

    private Exception mException;

    @Override
    public APIResponse doInBackground(Object[] params)
    {
        try
        {
            return this.doWorkBackground();
        } catch (Exception e)
        {
            e.printStackTrace();
            mException = e;
        }
        return null;
    }



    @Override
    protected void onPostExecute(APIResponse apiResponse)
    {
        if (null == mBaseActivity.get())
            return;

        if (mBaseActivity.get().isDestroyed())
            return;

        if (null != mBaseUi.get())
            mBaseUi.get().startLoading();

        if (mException instanceof NetworkErrorException)
        {
            onNetworkError((NetworkErrorException) mException);
            return;
        }
        if (mException instanceof Exception)
        {
            onError(mException);
            return;
        }

        try
        {
            if(!apiResponse.isSuccess())
            {
                onAPIError(apiResponse);
                return ;
            }
            onSuccess(apiResponse);
        } catch (Exception e)
        {
            e.printStackTrace();
            onError(e);
        }

    }

    /**
     * 其他异常
     * @param e
     */
    protected void onError(@Nullable Exception e)
    {
        if(null != mBaseActivity.get())
            Toast.makeText(mBaseActivity.get(),"数据加载出错！",Toast.LENGTH_SHORT).show();
        if(null != e)
            e.printStackTrace();
    }

    /**
     * 如果是服务器请求功能，但返回 非 200 码，调用此函数
     * @param apiResponse
     */
    protected void onAPIError(APIResponse apiResponse)
    {
        Toast.makeText(mBaseActivity.get(),apiResponse.msg,Toast.LENGTH_SHORT);
        onError(null);
    }

    /**
     * 网络请求异常 ，调用此函数
     */
    protected void onNetworkError(NetworkErrorException e)
    {
        onError(e);
    }

    protected abstract APIResponse doWorkBackground() throws Exception;

    protected abstract void onSuccess(APIResponse data);


}
