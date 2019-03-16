package com.liang.batterytestsystem.module.item

import com.liang.batterytestsystem.device.DeviceStatus
import java.io.Serializable

/**
 * @author : Amarao
 * CreateAt : 13:57 2019/3/15
 * Describe : 设备某一通道的详情信息
 *
 */
class DeviceItemChannelBean(var channelId: Byte) : Serializable {
    var deviceId: Byte = 0x01 // 设备号
    var checkState = false // 通道选中状态
    var deviceStatus = DeviceStatus.OFFLINE // 设备连接状态
    var stepTime: Float = 0f // 步时间S
    var electric: Float = 0f // 电流A
    var voltage: Float = 0f  // 电压V
    var power: Float = 0f    // 功率W
    var temperture: Float = 0f // 温度℃
    var ampereHour: Float = 0f // 安时Ah


}