package com.liang.batterytestsystem.base;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.liang.liangutils.BuildConfig;
import com.liang.liangutils.init.LCommon;
import com.liang.liangutils.utils.LLogX;
import com.squareup.leakcanary.LeakCanary;

/**
 * @author : Amarao
 * CreateAt : 15:08 2019/2/20
 * Describe :
 */
public class LBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LCommon.init(this, BuildConfig.class);
        Utils.init(this);

        // 崩溃问题
        LCrashHandler handler = LCrashHandler.getInstance();
        handler.init(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(handler);

        // TODO: 2019/5/4 待启动
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);


    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LLogX.e("内存不足，请注意回收！！！");
    }
}
