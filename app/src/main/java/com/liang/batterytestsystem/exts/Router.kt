package com.liang.batterytestsystem.exts

import android.app.Activity
import android.content.Context
import com.liang.batterytestsystem.device.DeviceBean
import com.liang.batterytestsystem.module.connect.DeviceConnect
import com.liang.batterytestsystem.module.details.DeviceDetails
import com.liang.batterytestsystem.module.service.DeviceService

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

        @JvmStatic // 启动设备详情页
        fun startDeviceDetail(context: Context, bean: DeviceBean) {
            DeviceDetails.startActivity(context, bean)
        }

        @JvmStatic
        fun startDeviceMgrService(context: Context) {
            DeviceService.startService(context)
        }
    }
}