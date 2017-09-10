package com.hxs.fitnessroom.util.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.hxs.fitnessroom.util.LogUtil;
import com.hxs.fitnessroom.util.ValidateUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

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
     * @param url
     * @param into
     */
    public static void load(String url, ImageView into)
    {
        glideWithMain(into.getContext(), url, into,false);
    }

    /**
     * 加载圆角图
     * @param url
     * @param into
     */
    public static void loadCorners(String url, ImageView into)
    {
        glideWithMain(into.getContext(), url, into,true);
    }

    private static void glideWithMain(Context context, String url, ImageView into,boolean isCorners)
    {
        if (ValidateUtil.isEmpty(context) || ValidateUtil.isEmpty(url))
        {
            return;
        }
        try
        {
            RequestBuilder<Drawable> glideRequest = Glide.with(context).load(url);
            if(isCorners)
            {
                glideRequest.apply(bitmapTransform(new RoundedCorners(20)));
            }
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

