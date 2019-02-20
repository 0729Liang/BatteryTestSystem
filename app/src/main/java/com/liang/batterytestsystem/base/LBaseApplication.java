package com.liang.batterytestsystem.base;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.liang.liangutils.BuildConfig;
import com.liang.liangutils.init.LCommon;

/**
 * @author : Amarao
 * CreateAt : 15:08 2019/2/20
 * Describe :
 */
public class LBaseApplication extends Application {
    public LBaseApplication() {
        LCommon.init(this, BuildConfig.class);
        Utils.init(this);
    }
}
