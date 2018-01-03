package com.macyer.http;

import android.content.Context;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.macyer.utils.NetUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lenovo on 2017/11/30.
 */

public class HttpUtils {

    private static HttpUtils instance;
    private Context mContext;
    private boolean isDebug;
    private Gson gson;
    private IpmlTokenGetListener listener;

    private Object gankIo;
    private Object tingIo;
    private Object doubanIo;

    // gankio、豆瓣、（轮播图）
    private final static String API_GANKIO = "https://gank.io/api/";
    private final static String API_DOUBAN = "Https://api.douban.com/";
    private final static String API_TING = "https://tingapi.ting.baidu.com/v1/restserver/";

    public static final int HTTP_OFFLINE_CACHE_TIME = 20;
    public static final int HTTP_CACHE_TIME = 3;

    private HttpUtils() {
    }

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    public void init(Context context, boolean isDebug) {
        this.mContext = context;
        this.isDebug = isDebug;
        HttpHead.init(context);
    }

    public <T> T getGankIoServer(Class<T> tClass) {
        if (gankIo == null) {
            synchronized (HttpUtils.class) {
                if (gankIo == null) {
                    gankIo = getIoServer(API_GANKIO).build().create(tClass);
                }
            }
        }
        return (T) gankIo;
    }

    public <T> T getTingIoServer(Class<T> tClass) {
        if (tingIo == null) {
            synchronized (HttpUtils.class) {
                if (tingIo == null) {
                    tingIo = getIoServer(API_TING).build().create(tClass);
                }
            }
        }
        return (T) tingIo;
    }

    public <T> T getDoubanIoServer(Class<T> tClass) {
        if (doubanIo == null) {
            synchronized (HttpUtils.class) {
                if (doubanIo == null) {
                    doubanIo = getIoServer(API_DOUBAN).build().create(tClass);
                }
            }
        }
        return (T) doubanIo;
    }

    private Retrofit.Builder getIoServer(String api) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .client(getOkClient())
                .baseUrl(api)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder;
    }

    public Gson getGson() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .setLenient()
                    .serializeNulls()
                    .setFieldNamingStrategy(new FieldNamingStrategy() {
                        @Override
                        public String translateName(Field f) {
                            ParamNames paramNames = f.getAnnotation(ParamNames.class);
                            return paramNames != null ? paramNames.value() : FieldNamingPolicy.IDENTITY.translateName(f);
                        }
                    });
            gson = gsonBuilder.create();
        }
        return gson;
    }

    public OkHttpClient getOkClient() {
        OkHttpClient client1;
        client1 = getUnsafeOkHttpClient();
        return client1;
    }

    public OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};
            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
                    .readTimeout(HTTP_OFFLINE_CACHE_TIME, TimeUnit.SECONDS)
                    .connectTimeout(HTTP_OFFLINE_CACHE_TIME, TimeUnit.SECONDS)
                    .writeTimeout(HTTP_OFFLINE_CACHE_TIME, TimeUnit.SECONDS)
                    //离线可以缓存，在线就获取最新数据  HTTP_OFFLINE_CACHE_TIME
//                     .addInterceptor(CacheUtil.provideOfflineCacheInterceptor())
                    //有网络时在限定时间内多次请求取缓存，超过时间重新请求  HTTP_CACHE_TIME
//                    .addNetworkInterceptor(CacheUtil.provideCacheInterceptor())
                    //设置缓存目录
//                    .cache(CacheUtil.provideCache())
                    //添加公共参数
//                    .addInterceptor(new CommonParamsInterceptor())
                    .addInterceptor(new HttpHeadInterceptor())
                    //添加日志
                    .addInterceptor(getLogInterceptor())
                    //设置https信任所有证书
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });

            return okBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    class HttpHeadInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Accept", "application/json;versions=1");
            if (NetUtils.isNetConnected(mContext)) {
                int maxAge = 60;
                builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
            } else {
                int maxStale = 60 * 60 * 24 * 28;
                builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }
            // 可添加token
//            if (listener != null) {
//                builder.addHeader("token", listener.getToken());
//            }
            // 如有需要，添加请求头
//            builder.addHeader("a", HttpHead.getHeader(request.method()));
            return chain.proceed(builder.build());
        }
    }

    private HttpLoggingInterceptor getLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
//                if (isDebug)
//                    Log.e("HttpUtils===", message);
            }
        });
        if (isDebug) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 测试
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE); // 打包
        }
        return interceptor;
    }

    public void setTokenListener(IpmlTokenGetListener listener) {
        this.listener = listener;
    }

    public Context getContext() {
        return mContext;
    }
}
