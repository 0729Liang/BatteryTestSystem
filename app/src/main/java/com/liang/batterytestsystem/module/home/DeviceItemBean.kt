package com.liang.batterytestsystem.module.home

import com.liang.batterytestsystem.module.device.DeviceStatus
import com.liang.batterytestsystem.module.item.DeviceItemChannelAdapter
import com.liang.batterytestsystem.module.item.DeviceItemChannelBean
import com.liang.batterytestsystem.utils.DigitalTrans
import com.liang.liangutils.utils.LLogX

/**
 * @author : Amarao
 * CreateAt : 14:24 2019/3/15
 * Describe : 设备信息，一个设备四个通道
 *
 */
class DeviceItemBean(var deviceId: Byte) {
    var checkState = false // 通道选中状态
    val channelList: MutableList<DeviceItemChannelBean> = ArrayList()// 所有设备通道
    val channeChooselList: MutableList<DeviceItemChannelBean> = ArrayList()// 选中设备通道

    val channelAdapter = DeviceItemChannelAdapter(channelList)

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
    fun addChosseChannel(channelBean: DeviceItemChannelBean) {
        channeChooselList.add(channelBean)
    }

    fun showChosseChannel() {
        channeChooselList.forEach {
            LLogX.e("当前连接通道数 = " + channeChooselList.size + " 通道ID = " + DigitalTrans.byte2hex(it.channelId))
        }
    }

}