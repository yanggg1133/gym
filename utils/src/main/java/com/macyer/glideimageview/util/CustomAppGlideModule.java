package com.macyer.glideimageview.util;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.InputStream;

/**
 * Created by Lenovo on 2017/12/6.
 */

public class CustomAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        //设置内存缓存
        int memoryCacheSizeBytes = 1024 * 1024 * 250; // 250mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        //设置内存缓存
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
        //设置默认选项
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_RGB_565)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .disallowHardwareConfig());
        //设置日志级别
        builder.setLogLevel(Log.DEBUG);
    }

    /**
     * 完全移除和替换 Glide 对某种特定类型的默认处理，例如一个网络库，你应该使用 replace()
     *
     * @param context
     * @param glide
     * @param registry
     */
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    /**
     * 为了维持对 Glide v3 的 GlideModules 的向后兼容性，
     * Glide 仍然会解析应用程序和所有被包含的库中的 AndroidManifest.xml 文件，
     * 并包含在这些清单中列出的旧 GlideModules 模块类。
     * 如果你已经迁移到 Glide v4 的 AppGlideModule 和 LibraryGlideModule ，
     * 你可以完全禁用清单解析。这样可以改善 Glide 的初始启动时间，并避免尝试解析元数据时的一些潜在问题
     *
     * @return
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
