package com.liang.batterytestsystem.module.home

import com.liang.batterytestsystem.device.DeviceStatus
import com.liang.batterytestsystem.module.item.DeviceItemChannelBean

/**
 * @author : Amarao
 * CreateAt : 14:24 2019/3/15
 * Describe : 设备信息，一个设备四个通道
 *
 */
class DeviceItemBean(var deviceId: Byte) {
    var checkState = false // 通道选中状态
    var deviceStatus = DeviceStatus.OFFLINE // 设备连接状态
    val channelList: MutableList<DeviceItemChannelBean> = ArrayList()// 设备通道

    // 添加通道
    fun addChannel(channelBean: DeviceItemChannelBean) {
        channelList.add(channelBean)
    }

    // 添加通道
    fun addChannelList(list: MutableList<DeviceItemChannelBean>) {
        val tempList: MutableList<DeviceItemChannelBean> = ArrayList()
        tempList.addAll(list)
        tempList.forEach {
            it.deviceId = deviceId
        }
        channelList.addAll(tempList)
    }

    // 添加通道
    fun addChannelList(deviceId: Byte, list: MutableList<DeviceItemChannelBean>) {
        val tempList: MutableList<DeviceItemChannelBean> = ArrayList()
        tempList.addAll(list)
        tempList.forEach {
            it.deviceId = deviceId
        }
        channelList.addAll(tempList)
    }
}