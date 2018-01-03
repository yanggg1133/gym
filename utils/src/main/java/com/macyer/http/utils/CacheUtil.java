package com.macyer.http.utils;


import com.macyer.http.HttpUtils;
import com.macyer.utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lenovo on 2017/11/24.
 */

public class CacheUtil {

    private static final String CACHE_CONTROL = "Cache-Control";
    
    /**
     * 离线缓存
     */
    public static Interceptor provideOfflineCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                /**
                 * 未联网获取缓存数据
                 */
                if (!NetUtils.isNetConnected(HttpUtils.getInstance().getContext())) {
                    //在20秒缓存有效，此处测试用，实际根据需求设置具体缓存有效时间
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(HttpUtils.HTTP_OFFLINE_CACHE_TIME, TimeUnit.SECONDS)
                            .build();

                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }

    /**
     * 有网络时在限定时间内多次请求取缓存，超过时间重新请求
     */
    public static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                // re-write response header to force use of cache
                // 正常访问同一请求接口（多次访问同一接口），给30秒缓存，超过时间重新发送请求，否则取缓存数据
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(HttpUtils.HTTP_CACHE_TIME, TimeUnit.SECONDS)
                        .build();

                return response.newBuilder()
                        .header(CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    //设置缓存目录和缓存空间大小
    public static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(HttpUtils.getInstance().getContext().getCacheDir(), "http-cache"),
                    10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
        }
        return cache;
    }
}
