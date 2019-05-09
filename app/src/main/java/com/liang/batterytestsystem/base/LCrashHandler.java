package com.liang.batterytestsystem.base;

import android.content.Context;

import com.liang.liangutils.utils.LLogX;

/**
 * @author : Amarao
 * CreateAt : 1:31 PM 2019/4/4
 * Describe : 全局捕获异常
 */
public class LCrashHandler implements Thread.UncaughtExceptionHandler {


    // 需求是 整个应用程序 只有一个 MyCrash-Handler
    private static LCrashHandler INSTANCE;
    private Context context;

    //1.私有化构造方法
    private LCrashHandler() {
    }

    public static synchronized LCrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LCrashHandler();
        }
        return INSTANCE;
    }

    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread arg0, Throwable arg1) {
        // 在此可以把用户手机的一些信息以及异常信息捕获并上传,由于UMeng在 这方面有很程序的api接口来调用，故没有考虑
        // 干掉当前的程序

        LLogX.e("程序挂掉了");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
