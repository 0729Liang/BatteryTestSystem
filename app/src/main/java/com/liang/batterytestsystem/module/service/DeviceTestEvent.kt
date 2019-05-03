package com.liang.batterytestsystem.module.service

import android.support.annotation.NonNull
import com.liang.batterytestsystem.module.home.DeviceCreateFactory
import com.liang.batterytestsystem.module.home.DeviceItemBean
import com.liang.batterytestsystem.module.item.DeviceItemChannelBean
import com.liang.batterytestsystem.utils.DigitalTrans
import com.liang.liangutils.msg.BusEvent
import com.liang.liangutils.utils.LLogX

/**
 * @author : Amarao
 * CreateAt : 11:10 2019/3/16
 * Describe :
 *
 */
class DeviceTestEvent : BusEvent() {

    var mDeviceId: Byte = 0x01 // 设备号
    var mDeviceChannelId: Byte = 0x01 // 通道号
    var mDeviceItemBean = DeviceItemBean(mDeviceId)
    var mDeviceItemChannelBean = DeviceItemChannelBean(DeviceCreateFactory.createDevice(mDeviceId), mDeviceChannelId)

    companion object {
        val EVENT_ADD_DEVICE_TEST_CHANNEL = "EVENT_ADD_DEVICE_TEST_CHANNEL" // 添加一个测试设备通道号
        val EVENT_REMOVE_DEVICE_TEST_CHANNEL = "EVENT_REMOVE_DEVICE_TEST_CHANNEL" // 删除一个测试设备通道号

        @JvmStatic
        fun postAddDeviceTestChannel(@NonNull channelBean: DeviceItemChannelBean) {
            val event = DeviceTestEvent()
            event.msg = EVENT_ADD_DEVICE_TEST_CHANNEL
            event.mDeviceItemChannelBean = channelBean
            BusEvent.post(event)
        }

        @JvmStatic
        fun postRemoveDeviceTestChannel(@NonNull channelBean: DeviceItemChannelBean) {
            val event = DeviceTestEvent()
            event.msg = EVENT_REMOVE_DEVICE_TEST_CHANNEL
            event.mDeviceItemChannelBean = channelBean
            BusEvent.post(event)
        }


        fun showDeviceInfo(add: Boolean, @NonNull channelBean: DeviceItemChannelBean) {
            var s = ""
            if (add) s = "添加" else s = "移除"

//            LLogX.e(s + "设备id=" + DigitalTrans.byte2hex(byteArrayOf(channelBean.deviceId)) +
//                    " 通道ID=" + DigitalTrans.byte2hex(byteArrayOf(channelBean.channelId)))

        }
    }
}