package com.hxs.fitnessroom.base.network;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.http.HttpResponseCache;

import com.google.gson.Gson;
import com.hxs.fitnessroom.BuildConfig;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.util.Base64Util;
import com.hxs.fitnessroom.util.LogUtil;
import com.hxs.fitnessroom.util.PhoneInfoUtil;
import com.hxs.fitnessroom.util.ValidateUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import static com.hxs.fitnessroom.Constants.PAGE_SIZE;
import static com.hxs.fitnessroom.Constants.SIGN_KEY;

/**
 * API请求的封装类
 * 所有API请求都 应该使用此类执行
 * Created by je on 8/31/17.
 */

public class APIHttpClient
{

    public static final String LOG_TAG = "ModelHttpClientFactory";

    public static final String CONTENT_TYPE_STREAM = "application/octet-stream";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_URLENCODED = "application/x-www-form-urlencoded";
    public static final String REQUEST_METHOD = "POST";


    /**
     * 该方法只应该在app主进程启动时调用
     *
     * @param context
     * @see com.hxs.fitnessroom.base.baseclass.BaseApplication
     */
    public static void appInitialization(final Context context)
    {
        /**
         * 开启http缓存
         */
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    long httpCacheSize = 20 * 1024 * 1024;// 20M
                    File httpCacheDir = new File(context.getCacheDir(), "httpCacheSize");
                    HttpResponseCache.install(httpCacheDir, httpCacheSize);
                } catch (Exception e)
                {
                    LogUtil.e(e.getMessage());
                }
            }
        }.start();

    }

    /**
     * post请求
     *
     * @param url
     * @param params
     * @param typeToken
     * @param <T>
     * @return
     */
    public static <T> APIResponse<T> postForm(String url, ParamsBuilder params, Type typeToken)
    {
        try
        {
            setDefParams(params);
            APIResponse<T> apiResponse = new Gson().fromJson(post(url, params.toParamsStr()), typeToken);
            return apiResponse;
        } catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 设置接口必传默认参数
     *
     * @param params
     */
    private static void setDefParams(ParamsBuilder params)
    {
        params.putParam("page_size", String.valueOf(PAGE_SIZE));
        params.putParam("model_idfa", PhoneInfoUtil.PhoneIMEI);
        params.putParam("model_version", PhoneInfoUtil.ModelVersion);
        params.putParam("system_version", PhoneInfoUtil.SystemVersion);
        params.putParam("app_version", PhoneInfoUtil.AppVersion);
        params.putParam("platform", PhoneInfoUtil.HxsAppType);
        params.putParam("app_name", PhoneInfoUtil.AppName);
        long utime = System.currentTimeMillis();
        params.putParam("utime", utime);
        params.putParam("sign", Base64Util.encodeToString(ValidateUtil.getMD5(utime + SIGN_KEY).getBytes()));
        params.putParam("sess_token", HXSUser.getUserSessToken());
    }

    private static String post(String urlapi, String params) throws NetworkErrorException, UnknownHostException
    {
        HttpURLConnection httpConn = null;
        OutputStream outputStream = null;

        if (BuildConfig.DEBUG)
        {
            LogUtil.dClass("url:" + urlapi);
            LogUtil.dClass("params:" + params);
        }

        try
        {
            //建立连接
            URL url = new URL(urlapi);
            httpConn = (HttpURLConnection) url.openConnection();
//            if(httpConn instanceof HttpsURLConnection)//验证证书
//            {
//                VerifyHttps((HttpsURLConnection) httpConn);
//            }

            //设置连接属性
            httpConn.setDoOutput(true);//使用 URL 连接进行输出
            httpConn.setDoInput(true);//使用 URL 连接进行输入
            httpConn.setUseCaches(true);//忽略缓存
            httpConn.setRequestMethod(REQUEST_METHOD);//设置URL请求方法
            httpConn.setConnectTimeout(10000);//连接超时
            httpConn.setReadTimeout(10000);//读取超时
            String requestString = params;
            //设置请求属性
            //获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
            byte[] requestStringBytes = requestString.getBytes("UTF-8");
            httpConn.setRequestProperty("Content-length", String.valueOf(requestStringBytes.length));
            httpConn.setRequestProperty("Content-Type", CONTENT_TYPE_URLENCODED);
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "UTF-8");

            //建立输出流，并写入数据
            outputStream = httpConn.getOutputStream();
            outputStream.write(requestStringBytes);
            outputStream.close();
            //获得响应状态
            int responseCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode)
            {
                //连接成功
                //当正确响应时处理数据
                StringBuffer sb = new StringBuffer();
                String readLine;
                BufferedReader responseReader;
                //处理响应流，必须与服务器响应流输出的编码一致
                responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                while ((readLine = responseReader.readLine()) != null)
                {
                    sb.append(readLine).append("\n");
                }
                responseReader.close();
                showDebugLog(sb);
                return sb.toString();
            }
        } catch (UnknownHostException ue)
        {
            throw ue;
        } catch (Exception ex)
        {
            throw new NetworkErrorException(ex);
        } finally
        {
            if (null != outputStream)
            {
                try
                {
                    outputStream.flush();
                } catch (Exception e)
                {
                }
                try
                {
                    outputStream.close();
                } catch (Exception e)
                {
                }
            }
        }
        return null;
    }

    /**
     * debug状态下打印log
     * @param sb
     */
    private static void showDebugLog(StringBuffer sb)
    {
        if (BuildConfig.DEBUG)
        {
            try
            {
                LogUtil.dClass("response:" + new Gson().fromJson(sb.toString(), Object.class));
            } catch (Exception e)
            {
                LogUtil.dClass("response:" + sb.toString());
            }
        }
    }


    /**
     * 验证https证书
     * 预留，并无实现代码
     *
     * @param httpConn
     */
    private static void VerifyHttps(HttpsURLConnection httpConn)
    {
        final SSLSocketFactory impl = httpConn.getSSLSocketFactory();
        httpConn.setSSLSocketFactory(new SSLSocketFactory()
        {
            @Override
            public String[] getDefaultCipherSuites()
            {
                return impl.getDefaultCipherSuites();
            }

            @Override
            public String[] getSupportedCipherSuites()
            {
                return impl.getSupportedCipherSuites();
            }

            @Override
            public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException
            {
                return impl.createSocket(s, host, port, autoClose);
            }

            @Override
            public Socket createSocket(String host, int port) throws IOException, UnknownHostException
            {
                return impl.createSocket(host, port);
            }

            @Override
            public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException
            {
                return impl.createSocket(host, port, localHost, localPort);
            }

            @Override
            public Socket createSocket(InetAddress host, int port) throws IOException
            {
                return impl.createSocket(host, port);
            }

            @Override
            public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException
            {
                return impl.createSocket(address, port, localAddress, localPort);
            }
        });
    }
}

