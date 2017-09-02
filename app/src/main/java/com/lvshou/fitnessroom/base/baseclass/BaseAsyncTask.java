package com.lvshou.fitnessroom.base.baseclass;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.lvshou.fitnessroom.base.network.APIResponse;

import java.lang.ref.WeakReference;

/**
 * 异步任务基类，用于处理一些须要重复处理的事情
 * Created by jie on 16-7-4.
 */
public abstract class BaseAsyncTask extends AsyncTask<Object, Void, APIResponse>
{
    private WeakReference<Context> mContext;
    private WeakReference<BaseUi> mBaseUi;

    public BaseAsyncTask(Context context, @Nullable BaseUi baseUi)
    {
        if (null == context)
            throw new NullPointerException();
        mContext = new WeakReference<>(context);
        mBaseUi = new WeakReference<>(baseUi);
    }

    @Override
    protected void onPreExecute()
    {
        if (null != mBaseUi.get())
            mBaseUi.get().endLoading();
    }

    @Override
    protected void onCancelled()
    {
        super.onCancelled();
        if (null != mBaseUi.get())
            mBaseUi = null;
        if (null != mContext.get())
            mContext = null;
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
        if (null == mContext.get())
            return;

        if (mContext.get() instanceof BaseActivity && ((BaseActivity) mContext.get()).isDestroyed())
            return;

        if (null != mBaseUi.get())
            mBaseUi.get().startLoading();

        if (null == apiResponse)
        {
            onNullData();
            return;
        }

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
                Toast.makeText(getContext(),apiResponse.msg,Toast.LENGTH_SHORT);
                return ;
            }
            onSuccess(apiResponse);
        } catch (Exception e)
        {
            e.printStackTrace();
            onError(e);
        }

    }

    protected Context getContext()
    {
        return mContext.get();
    }
    protected void onError(Exception e)
    {
        onNullData();
    }

    protected void onNetworkError(NetworkErrorException e)
    {
        onError(e);
    }


    protected abstract APIResponse doWorkBackground() throws Exception;

    protected abstract void onNullData();

    protected abstract void onSuccess(APIResponse data);


}
