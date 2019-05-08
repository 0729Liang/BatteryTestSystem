package com.liang.batterytestsystem.module.service

import android.support.annotation.NonNull
import com.liang.liangutils.msg.BusEvent

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
    var channelIndex: Int = -1

    var count = -1

    companion object {
        val DEVICE_QUERY_CHANNEL_STATUS_RESULT = "DEVICE_QUERY_CHANNEL_STATUS_RESULT"  // 通道状态查询结果

        val DEVICE_DATA_UPDATE_CHANNEL_STATUS_NOTIFICATION = "DEVICE_DATA_UPDATE_CHANNEL_STATUS_NOTIFICATION" // 通知主线程需要通道状态
        val DEVICE_DATA_UPDATE_DATA_NOTIFICATION = "DEVICE_DATA_UPDATE_DATA_NOTIFICATION" // 通知主线程更新设备数据

        val TEST = "TEST"
        @JvmStatic
        fun postQueryDataResult(@NonNull queryResultByteArray: ByteArray) {

            val event = DeviceQueryEvent()
            event.msg = DeviceQueryEvent.DEVICE_QUERY_CHANNEL_STATUS_RESULT
            event.queryResultByteArray = queryResultByteArray.copyOf()
            event.count = queryResultByteArray[4].toInt()
//            LLogX.e("设备号1 = "+ DigitalTrans.byte2hex(queryResultByteArray[4])+" count1 = "+event.count)
            BusEvent.post(event)
        }

        @JvmStatic
        fun postUpdateChannelStatusNotification(deviceId: Byte, channelId: Byte) {
            val event = DeviceQueryEvent()
            event.msg = DeviceQueryEvent.DEVICE_DATA_UPDATE_CHANNEL_STATUS_NOTIFICATION
            event.deviceId = deviceId
            event.channelId = channelId
            BusEvent.post(event)
        }


        @JvmStatic
        fun postUpdateDataNotification(deviceId: Byte, channelId: Byte) {
            val event = DeviceQueryEvent()
            event.msg = DeviceQueryEvent.DEVICE_DATA_UPDATE_DATA_NOTIFICATION
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
