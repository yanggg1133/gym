package com.macyer.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created by liuxiu on 2016/12/5.
 * 
 * 使用方法
 * CrashHandler crashHandler = CrashHandler.getInstance();
 * crashHandler.init(getApplicationContext());
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler instance;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context ctx) {
        mContext = ctx; 
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler(); 
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread arg0, Throwable arg1) {
        String logdir ;
        if(Environment.getExternalStorageDirectory()!=null){
            logdir = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "crash";
            File file = new File(logdir);
            boolean mkSuccess;
            if (!file.isDirectory()) {
                mkSuccess = file.mkdirs();
                if (!mkSuccess) {
                    mkSuccess = file.mkdirs();
                }
            }
            try {
                FileWriter fw = new FileWriter(logdir+File.separator+"crash_handler.log",true);
                fw.write(new Date()+"\n");
                StackTraceElement[] stackTrace = arg1.getStackTrace();
                fw.write(arg1.getMessage() + "\n");
                for (int i = 0; i < stackTrace.length; i++) {
                    String logStr = "file:" + stackTrace[i].getFileName() + " class:" + stackTrace[i].getClassName()
                            + " method:" + stackTrace[i].getMethodName() + " line:" + stackTrace[i].getLineNumber()
                            + "\n";
                    Log.e("LV_crash","===log="+logStr);
                    fw.write(logStr);
                }
                fw.write("\n");
                fw.close();
            } catch (IOException e) {
                Log.e("LV_crash","load file failed..."+ e.getCause());
            }
        }
        arg1.printStackTrace();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
