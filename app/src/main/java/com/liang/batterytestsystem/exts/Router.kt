package com.liang.batterytestsystem.exts

import android.content.Context
import com.liang.batterytestsystem.module.config.UdpConfigActivity
import com.liang.batterytestsystem.module.details.NewDeviceDetails
import com.liang.batterytestsystem.module.item.DeviceItemChannelBean
import com.liang.batterytestsystem.module.service.DeviceMgrService
import com.liang.batterytestsystem.module.service.DeviceQueryService

/**
 * @author : Amarao
 * CreateAt : 12:02 2019/2/22
 * Describe : 界面跳转的封装，为了更好的页面解耦
 *
 */
class Router {
    companion object {

        /**
         * 功能：启动设备详情页
         */
        @JvmStatic
        fun startDeviceDetailTest(context: Context, bean: DeviceItemChannelBean) {
            NewDeviceDetails.startActivity(context, bean)
        }

        /**
         * 功能：启动设备管理服务类
         */
        @JvmStatic
        fun startDeviceMgrService(context: Context) {
            DeviceMgrService.startService(context)
        }

        /**
         * 功能：启动配置界面
         */
        @JvmStatic
        fun startUdpConfig(context: Context) {
            UdpConfigActivity.startActivity(context)
        }

        /**
         * 功能：启动查询服务类
         */
        @JvmStatic
        fun startDeviceQueryService(context: Context) {
            DeviceQueryService.startService(context)
        }

//        @JvmStatic // 启动设备详情页
//        fun startDeviceDetail(context: Context, bean: DeviceItemChannelBean) {
//            DeviceDetails.startActivity(context, bean)
//        }

    }
}