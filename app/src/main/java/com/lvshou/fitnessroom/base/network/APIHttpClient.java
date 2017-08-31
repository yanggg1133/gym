package com.lvshou.fitnessroom.base.network;

import android.accounts.NetworkErrorException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

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

    public static  <T> T post(String url,String params,Class<T> tclass) throws NetworkErrorException {
        try {
            return new Gson().fromJson(post(url,params), tclass);
        } catch (NetworkErrorException e) {
            throw e;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new NetworkErrorException(e);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String post(String urlapi,String params) throws NetworkErrorException
    {
        HttpURLConnection httpConn = null;
        OutputStream outputStream = null;
        try
        {
            //建立连接
            URL url = new URL(urlapi);
            httpConn = (HttpURLConnection) url.openConnection();
            if(httpConn instanceof HttpsURLConnection)
            {
                VerifyHttps((HttpsURLConnection) httpConn);
            }

            ////设置连接属性
            httpConn.setDoOutput(true);//使用 URL 连接进行输出
            httpConn.setDoInput(true);//使用 URL 连接进行输入
            httpConn.setUseCaches(true);//忽略缓存
            httpConn.setRequestMethod(REQUEST_METHOD);//设置URL请求方法
            httpConn.setConnectTimeout(10000);
            httpConn.setReadTimeout(10000);
            String requestString = params;
            //设置请求属性
            //获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
            byte[] requestStringBytes = requestString.getBytes("UTF-8");
            httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
            httpConn.setRequestProperty("Content-Type",CONTENT_TYPE_JSON);
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


                return sb.toString();
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
            throw new NetworkErrorException();
        } finally
        {
            if (null != outputStream) {
                try {
                    outputStream.flush();
                } catch (Exception e) {
                }
                try {
                    outputStream.close();
                } catch (Exception e) {
                }
            }
        }
        return null;
    }


    /**
     * 验证https证书
     * 预留，并无实现代码
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
                return impl.createSocket(s,host,port,autoClose);
            }

            @Override
            public Socket createSocket(String host, int port) throws IOException, UnknownHostException
            {
                return impl.createSocket(host,port);
            }

            @Override
            public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
                return impl.createSocket(host,port,localHost,localPort);
            }

            @Override
            public Socket createSocket(InetAddress host, int port) throws IOException {
                return impl.createSocket(host,port);
            }

            @Override
            public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
                return impl.createSocket(address,port,localAddress,localPort);
            }
        });
    }
}
