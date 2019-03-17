package com.liang.batterytestsystem.module.test

/**
 * @author : Amarao
 * CreateAt : 12:59 2019/3/17
 * Describe : 接收数据Bean，只包含固定长度的信息
 */
class DeviceTestBean {
    // 帧头 p=0
    val frameHeader: Byte = 0x00
    // 2字节 不管 p=1-2
    // 命令 p=3
    val command: Byte = 0x00
    // 1字节 设备号 p=4
    val deviceId: Byte = 0x00
    // 4字节 不管 p=5-8
    // 1字节 此数据包含的通道数 p=9
    val channelCount: Byte = 0x00
    // 1

}
