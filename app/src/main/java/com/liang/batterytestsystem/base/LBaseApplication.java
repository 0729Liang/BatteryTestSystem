package com.liang.batterytestsystem.base;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.liang.liangutils.BuildConfig;
import com.liang.liangutils.init.LCommon;
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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
