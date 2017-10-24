package com.hxs.fitnessroom.base.baseclass;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.util.ToastUtil;

import java.lang.ref.WeakReference;
import java.net.UnknownHostException;

/**
 * 异步任务基类，用于处理一些须要重复处理的事情
 *
 * 注意：如果传入的Context context为BaseActivity
 *      任务异步执行完毕后，会判断{@link BaseActivity#isDestroyed()},如果为true ,
 *      则不会继续往下执行
 *
 * 重点：继承后，线程执行函数必须使用{@link #go(Context)...}
 *      绝对不可用{@link #execute(Object[])}
 * @see #go(Context)
 * @see #go(Context,BaseUi)
 * reated by jie on 16-7-4.
 */
public abstract class BaseAsyncTask extends AsyncTask<Object, Object, APIResponse>
{
    private WeakReference<Context> mContext;
    private WeakReference<BaseUi> mBaseUi;

    /**
     * 线程任务执行必须调用下面三个方法的其中一个，不可调用原生中的{@link AsyncTask#execute(Object[])}
     * 否则会出现 {@link RuntimeException}
     * @param context
     */
    public final void go(Context context)
    {
        go(context,null,null);
    }

    public final void go(Context context, BaseUi baseUi)
    {
        go(context,baseUi,null);
    }

    public final void go(Context context, BaseUi baseUi, Object... objects)
    {
        if (null == context)
            throw new NullPointerException();

        mContext = new WeakReference<>(context);

        mBaseUi = new WeakReference<>(baseUi);

        execute(objects);
    }


    @Override
    protected void onPreExecute()
    {
        if(mContext == null )
            throw new RuntimeException("------调用了错误的execute()方法，请使用BaseAsyncTask的execute(BaseActivity baseActivity)方法---------------");

        if (null != mBaseUi.get())
            mBaseUi.get().startLoading();

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

        if (mContext.get() instanceof BaseActivity && ((BaseActivity)mContext.get()).isDestroyed())
            return;

        if (null != mBaseUi.get())
            mBaseUi.get().endLoading();

        if(mException != null)
        {
            if (mException instanceof IllegalArgumentException
                    && mException.getCause() != null
                    && mException.getCause() instanceof UnknownHostException)
            {
                ToastUtil.toastShort("没找到Host地址");
                onError(null);
                return;
            }
            else if (mException instanceof IllegalArgumentException
                    && mException.getCause() != null
                    && mException.getCause() instanceof NetworkErrorException)
            {
                onNetworkError((NetworkErrorException) mException.getCause());
                return;
            }
            else
            {
                onError(mException);
                return;
            }
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
        if(null != mContext.get() && e != null)
        {
            Toast.makeText(mContext.get(),"网络加载出错！",Toast.LENGTH_SHORT).show();
        }
        if(null != e)
            e.printStackTrace();
    }

    /**
     * 如果是服务器请求功能，但返回 非 200 码，调用此函数
     * @param apiResponse
     */
    protected void onAPIError(APIResponse apiResponse)
    {
        ToastUtil.toastShort(apiResponse.msg);
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


    public Context getContext()
    {
        return mContext.get();
    }

}
