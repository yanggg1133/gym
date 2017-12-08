package com.hxs.fitnessroom.util;

import android.content.Context;

import java.io.File;

/**
 * Created by lsleidongran on 2016/9/14.
 */
public class FileUtil
{
    private static final String PICTURE = "picture";
    /**
     * 获取内部存储空间路径
     */
    public static String getFilesDirPath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }
    /**
     * 多图上传
     * 会在路径下创建9张图片
     * @return
     */
    public static String getUploadPicTempFileMore(Context context , int i) {
        return getFilesDirImagePath(context) + File.separator + "upload" + i + ".jpg";
    }
    /**
     * 创建图片在内部存储空间的保存地址
     */
    public static String getFilesDirImagePath(Context context) {
        String filesDirImagePath = getFilesDirPath(context) + File.separator + PICTURE;
        File file = new File(filesDirImagePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        return filesDirImagePath;
    }
}
