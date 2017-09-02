package com.lvshou.fitnessroom.util.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.lvshou.fitnessroom.util.LogUtil;
import com.lvshou.fitnessroom.util.ValidateUtil;

/**
 * 图片加载处理类
 * 普通调用使用{@link #load(String, ImageView)}
 * <p>
 * <p>
 * 设置内存缓存大小{@link MemoryCacheAppGlideModule}
 * 目前为30M
 * <p>
 * Created by je on 9/2/17.
 */

public class ImageLoader
{
    /**
     * 普通加载
     *
     * @param url
     * @param into
     */
    public static void load(String url, ImageView into)
    {
        glideWithMain(into.getContext(), url, into);
    }

    private static void glideWithMain(Context context, String url, ImageView into)
    {
        if (ValidateUtil.isEmpty(context))
        {
            return;
        }

        try
        {
            RequestBuilder<Drawable> glideRequest = Glide.with(context).load(url);
            glideRequest.into(into);
        } catch (Exception e)
        {
            if (-1 == e.getMessage().indexOf("load for a destroyed activity"))
            {
                LogUtil.e("图片加载异常" + e.getMessage());
            }
        }

    }


    /**
     * 清除缓存
     */
    @SuppressWarnings("unchecked")
    public static void clearCache(final Context context)
    {
        try
        {
            new AsyncTask<Void, Void, Boolean>()
            {
                @Override
                protected Boolean doInBackground(Void... params)
                {
                    try
                    {
                        Glide.get(context.getApplicationContext()).clearDiskCache();
                        return true;
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return false;
                }
            }.execute();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

