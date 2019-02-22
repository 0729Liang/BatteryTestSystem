package com.liang.batterytestsystem.device

import com.liang.liangutils.msg.BusEvent

/**
 * @author : Amarao
 * CreateAt : 17:31 2019/2/21
 * Describe :
 */
class DeviceEvent : BusEvent() {

    var serialNumber: String? = null    // 设备编号
    var deviceBean: DeviceBean? = null  // 设备对象

    companion object {
        //val EVENT_START_CONNECT = "EVENT_START_CONNECT"; // 开始连接
        val EVENT_CONNECTED_OBJ = "EVENT_CONNECTED_OBJ" // 发送已经在线的设备对象
        val EVENT_START_CONNECT = "EVENT_START_CONNECT"   // 开始连接
        val EVENT_HIDE_DEVICE_INFO_WINDOW = "EVENT_HIDE_DEVICE_INFO_WINDOW" // 隐藏设备信息Window


        @JvmStatic
        fun postStartConnect(serialNumber: String) {
            val event = DeviceEvent()
            event.msg = EVENT_START_CONNECT
            event.serialNumber = serialNumber
            BusEvent.post(event)
        }


        @JvmStatic
        fun postConnectSussessDeviceObj(bean: DeviceBean) {
            val event = DeviceEvent()
            event.msg = EVENT_CONNECTED_OBJ
            event.deviceBean = bean
            BusEvent.post(event)
        }

        @JvmStatic
        fun postHideDeviceInfoWindow() {
            val event = DeviceEvent()
            event.msg = EVENT_HIDE_DEVICE_INFO_WINDOW
            BusEvent.post(event)
        }
    }
}
