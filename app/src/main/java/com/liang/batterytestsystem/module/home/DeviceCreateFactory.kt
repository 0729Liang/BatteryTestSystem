package com.liang.batterytestsystem.module.home

import com.liang.batterytestsystem.module.item.DeviceItemChannelBean

/**
 * @author : Amarao
 * CreateAt : 15:36 2019/3/15
 * Describe : 虚拟设备生产工厂
 *
 */
class DeviceCreateFactory {

    companion object {

        // 创建一个设备
        @JvmStatic
        fun createDevice(deviceId: Byte): DeviceItemBean {
            val deviceBean = DeviceItemBean(deviceId)
            return deviceBean
        }

        // 创建一个设备通道
        @JvmStatic
        fun createDeviceChannel(channelId: Byte): DeviceItemChannelBean {
            val channelBean = DeviceItemChannelBean(channelId)
            return channelBean
        }

        // 创建一个通道列表
        @JvmStatic
        fun createDeviceChannelList(vararg channelIdList: Byte): MutableList<DeviceItemChannelBean> {
            val channelList = arrayListOf<DeviceItemChannelBean>()
            channelIdList.forEach {
                val channelBean = DeviceItemChannelBean(it)
                channelList.add(channelBean)
            }
            return channelList
        }


    }
}