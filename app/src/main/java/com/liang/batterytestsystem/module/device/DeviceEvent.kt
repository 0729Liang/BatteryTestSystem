package com.liang.batterytestsystem.module.device

import com.liang.liangutils.msg.BusEvent

/**
 * @author : Amarao
 * CreateAt : 17:31 2019/2/21
 * Describe :
 */
class DeviceEvent : BusEvent() {

    var serialNumber: String? = null    // 设备编号
    //    var deviceBean: DeviceBean? = null  // 设备对象
    var recvMsg: String? = null // 接收到的信息

    companion object {
        //val EVENT_START_CONNECT = "EVENT_START_CONNECT"; // 开始连接
        val EVENT_START_CONNECT = "EVENT_START_CONNECT"   // 开始连接
        val EVENT_CONNECTED_OBJ = "EVENT_CONNECTED_OBJ" // 连接成功，发送已经在线的设备对象
        val EVENT_START_DISCONNECT = "EVENT_START_DISCONNECT"   // 断开连接
        val EVENT_START_TEST = "EVENT_START_TEST"   // 开始测试
        val EVENT_PAUSE_TEST = "EVENT_PAUSE_TEST"   // 暂停测试
        val EVENT_STOP_TEST = "EVENT_STOP_TEST"    // 停止测试
        val EVENT_HIDE_DEVICE_INFO_WINDOW = "EVENT_HIDE_DEVICE_INFO_WINDOW" // 隐藏设备信息Window
        val EVENT_RECV_MSG = "EVENT_RECV_MSG" // 收到信息


        @JvmStatic
        fun postStartConnect(serialNumber: String) {
            val event = DeviceEvent()
            event.msg = EVENT_START_CONNECT
            event.serialNumber = serialNumber
            BusEvent.post(event)
        }


        @JvmStatic
        fun postConnectSussessDeviceObj(serialNumber: String) {
            val event = DeviceEvent()
            event.msg = EVENT_CONNECTED_OBJ
            event.serialNumber = serialNumber
            BusEvent.post(event)
        }

        @JvmStatic
        fun postHideDeviceInfoWindow() {
            val event = DeviceEvent()
            event.msg = EVENT_HIDE_DEVICE_INFO_WINDOW
            BusEvent.post(event)
        }

        @JvmStatic
        fun postStartDisConnect(serialNumber: String) {
            val event = DeviceEvent()
            event.msg = EVENT_START_DISCONNECT
            event.serialNumber = serialNumber
            BusEvent.post(event)
        }

        @JvmStatic
        fun postStartTest(serialNumber: String) {
            val event = DeviceEvent()
            event.msg = EVENT_START_TEST
            event.serialNumber = serialNumber
            BusEvent.post(event)
        }

        @JvmStatic
        fun postPauseTest(serialNumber: String) {
            val event = DeviceEvent()
            event.msg = EVENT_PAUSE_TEST
            event.serialNumber = serialNumber
            BusEvent.post(event)
        }

        @JvmStatic
        fun postStopTest(serialNumber: String) {
            val event = DeviceEvent()
            event.msg = EVENT_STOP_TEST
            event.serialNumber = serialNumber
            BusEvent.post(event)
        }

        @JvmStatic
        fun postRecvMsg(msg: String) {
            val event = DeviceEvent()
            event.msg = EVENT_RECV_MSG
            event.recvMsg = msg
            BusEvent.post(event)
        }
    }
}
