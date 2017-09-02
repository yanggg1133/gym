package com.lvshou.fitnessroom.util.image;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * 定义内存缓存大小
 */
@GlideModule
public class MemoryCacheAppGlideModule extends AppGlideModule
{
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int memoryCacheSizeBytes = 1024 * 1024 * 30; // 30mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
    }
}
