
package com.hxs.fitnessroom.util.image;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.hxs.fitnessroom.module.user.HXSUser;
import com.hxs.fitnessroom.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Jiang Qi Date: 12-8-3
 */
public class ImageUtil
{
    private static final String accessKeyId = "LTAIIS96ahjxEzKr";
    private static final String accessKeySecret = "SbNRy80EsgRa3TPtZb2SJCvdROFVlM";
    public static final String bucketName = "hxsupload";
    public static OSS init(Context context){
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(6); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);
        return oss;
    }


    /**
     * 上传图片到阿里云
     *
     * @param uploadFilePath
     * @param callback
     */
    public static OSSAsyncTask uploadImg(Context context, String uploadFilePath, OSSCompletedCallback<PutObjectRequest, PutObjectResult> callback)
    {
        OSS oss = ImageUtil.init(context);

        uploadFilePath = ImageUtil.compressPic1(context, uploadFilePath, 10);//把图片压缩

        //服务器要求地址样式
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd/");
        String objectKey = date.format(new Date()) + System.currentTimeMillis() +
                HXSUser.getUserId() + ".jpg";

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(ImageUtil.bucketName, objectKey, uploadFilePath);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>()
        {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize)
            {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, callback);
        // task.cancel(); // 可以取消任务
        // task.waitUntilFinished(); // 可以等待直到任务完成
        return task;
    }




    /**
     * 多图
     * 将图片进行压缩后上传，大图缩小
     * 
     * @param picPath 图片的路径
     * */
    public static String compressPic1(Context context, String picPath , int i) {
        File file  = new File(picPath);
        System.out.println("------> " + file.length()/1024 + " k ");
        
        if(file.length()/1024 < 100){
            return picPath;
        }
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inInputShareable = true;
        options.inPurgeable = true;
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeFile(picPath , options);
            return compressPic(bitmap , FileUtil.getUploadPicTempFileMore(context , i));
        } catch (Exception e) {
            return picPath;
        } catch (OutOfMemoryError e) {
            return picPath;
        }
    }

    /**
     * 上传图片前进行压缩bitmap
     * @param bitmap
     * @param tmp 创建临时路径
     * @return
     * @throws Exception throws IOException
     */
    public static String compressPic(Bitmap bitmap , String tmp) throws Exception
    {
        
        FileOutputStream stream;
        File file = new File(tmp);
        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }

        stream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        long len = file.length() / 1024;
        System.out.println("原来---------len : " + len + " k");
        
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            float maxWidth = 960;// 服务器要求短边最大不超过
            
            Matrix m = new Matrix();
            if (( w > maxWidth && h > maxWidth ) && len > 100) {// 大图缩小，且图片质量大于100k
                
                if(w < h){
                    float xy = maxWidth / (float)(w);
                    m.postScale(xy, xy);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, m, true);
                }else{
                    float xy = maxWidth / (float)(h);
                    m.postScale(xy, xy);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, m, true);
                }
                
                File four = new File(tmp);
                four.delete();
                four.createNewFile();
                stream = new FileOutputStream(four);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                len = four.length() / 1024;
                System.out.println("尺寸改变---------len : " + len + " k");
            }
            
            int quality = 90;
            while(len > 100){
                File four = new File(tmp);
                four.delete();
                four.createNewFile();
                stream = new FileOutputStream(four);
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
                
                len = four.length() / 1024;
                
                System.out.println("压缩中---------len : " + len + " k ， quality ： " + quality );
                
                quality -= 10;
                if(quality <= 0){
                    break;
                }
                
            }
            System.out.println("压缩后---------len : " + len + " k");
       
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ignored) {

            }
        }
        if(bitmap.isRecycled()) bitmap.recycle();
        return tmp;
    }
}
