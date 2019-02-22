package com.liang.batterytestsystem.exts

import android.app.Activity
import com.liang.batterytestsystem.module.connect.DeviceConnect

/**
 * @author : Amarao
 * CreateAt : 12:02 2019/2/22
 * Describe : 界面跳转的封装，为了更好的页面解耦
 *
 */
class Router {
    companion object {


        @JvmStatic // 启动设备连接成功界面
        fun startDeviceConnect(activity: Activity) {
            DeviceConnect.startActivity(activity)
        }
    }
}