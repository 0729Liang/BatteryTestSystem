package com.liang.batterytestsystem.module.service

import android.support.annotation.NonNull
import com.liang.batterytestsystem.module.device.DeviceEvent
import com.liang.batterytestsystem.module.item.DeviceItemChannelBean
import com.liang.batterytestsystem.utils.DigitalTrans
import com.liang.liangutils.msg.BusEvent
import com.liang.liangutils.utils.LLogX
import java.text.FieldPosition

/**
 * @author : Amarao
 * CreateAt : 4:41 PM 2019/5/4
 * Describe : 配合 '设备查询服务类'，发出相应通知
 *
 */
class DeviceQueryEvent : BusEvent() {

    var queryResultByteArray: ByteArray? = null
    var deviceId: Byte = -1
    var channelId: Byte = -1

    var count = -1

    companion object {
        val DEVICE_QUERY_CHANNEL_STATUS_RESULT = "DEVICE_QUERY_CHANNEL_STATUS_RESULT"  // 通道状态查询结果

        val DEVICE_DATA_UPDATE_NOTIFICATION = "DEVICE_DATA_UPDATE_NOTIFICATION" // 通知主线程需要数据更新

        val TEST = "TEST"
        @JvmStatic
        fun postQueryChannelStatusResult(@NonNull queryResultByteArray: ByteArray) {

            val event = DeviceQueryEvent()
            event.msg = DeviceQueryEvent.DEVICE_QUERY_CHANNEL_STATUS_RESULT
            event.queryResultByteArray = queryResultByteArray.copyOf()
            event.count = queryResultByteArray[4].toInt()
//            LLogX.e("设备号1 = "+ DigitalTrans.byte2hex(queryResultByteArray[4])+" count1 = "+event.count)
            BusEvent.post(event)
        }

        @JvmStatic
        fun postUpdateNotification(deviceId: Byte, channelId: Byte) {
            val event = DeviceQueryEvent()
            event.msg = DeviceQueryEvent.DEVICE_DATA_UPDATE_NOTIFICATION
            event.deviceId = deviceId
            event.channelId = channelId
            BusEvent.post(event)
        }

        @JvmStatic
        fun test(countb: Int) {
            val event = DeviceQueryEvent()
            event.msg = TEST
            event.count = countb
            event.queryResultByteArray = byteArrayOf(countb.toByte())
            BusEvent.post(event)
        }

    }
}
