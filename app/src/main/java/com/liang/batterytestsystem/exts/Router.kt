package com.liang.batterytestsystem.exts

import android.app.Activity
import android.content.Context
import com.liang.batterytestsystem.module.config.UdpConfigActivity
import com.liang.batterytestsystem.module.details.DeviceDetails
import com.liang.batterytestsystem.module.details.NewDeviceDetails
import com.liang.batterytestsystem.module.item.DeviceItemChannelBean
import com.liang.batterytestsystem.module.service.DeviceService

/**
 * @author : Amarao
 * CreateAt : 12:02 2019/2/22
 * Describe : 界面跳转的封装，为了更好的页面解耦
 *
 */
class Router {
    companion object {

        @JvmStatic // 启动设备详情页
        fun startDeviceDetailTest(context: Context, bean: DeviceItemChannelBean) {
            NewDeviceDetails.startActivity(context, bean)
        }

        @JvmStatic // 启动设备详情页
        fun startDeviceDetail(context: Context, bean: DeviceItemChannelBean) {
            DeviceDetails.startActivity(context, bean)
        }

        @JvmStatic
        fun startDeviceMgrService(context: Context) {
            DeviceService.startService(context)
        }

        @JvmStatic
        fun startUdpConfig(context: Context) {
            UdpConfigActivity.startActivity(context)
        }
    }
}