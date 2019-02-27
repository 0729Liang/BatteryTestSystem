package com.liang.batterytestsystem.device

/**
 * @author : Amarao
 * CreateAt : 11:42 2019/2/20
 * Describe :
 *
 */
class DeviceBean(var deviceSerialNumber: String) {
    var deviceName: String = ""
    var checkStatus: Boolean = false // 选中状态
    var deviceStatus: DeviceStatus = DeviceStatus.OFFLINE// 离线 在线 正在连接 正在测试 暂停测试 停止测试

    // 是否在线
    fun isConnected(): Boolean {
        return deviceStatus != DeviceStatus.OFFLINE && deviceStatus != DeviceStatus.CONNECTING
    }
}