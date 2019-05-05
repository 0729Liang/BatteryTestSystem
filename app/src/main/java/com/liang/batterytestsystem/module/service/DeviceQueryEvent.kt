package com.liang.batterytestsystem.module.service

import android.support.annotation.NonNull
import com.liang.batterytestsystem.module.item.DeviceItemChannelBean
import com.liang.liangutils.msg.BusEvent

/**
 * @author : Amarao
 * CreateAt : 4:41 PM 2019/5/4
 * Describe : 配合 '设备查询服务类'，发出相应通知
 *
 */
class DeviceQueryEvent : BusEvent() {

    lateinit var queryResultByteArray: ByteArray

    companion object {
        val DEVICE_QUERY_CHANNEL_STATUS_RESULT = "DEVICE_QUERY_CHANNEL_STATUS_RESULT"

        @JvmStatic
        fun postQueryChannelStatusResult(@NonNull queryResultByteArray: ByteArray) {
            val event = DeviceQueryEvent()
            event.msg = DeviceQueryEvent.DEVICE_QUERY_CHANNEL_STATUS_RESULT
            event.queryResultByteArray = queryResultByteArray
            BusEvent.post(event)
        }

    }
}
