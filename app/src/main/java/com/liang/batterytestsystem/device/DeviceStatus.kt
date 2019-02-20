package com.liang.batterytestsystem.device

/**
 * @author : Amarao
 * CreateAt : 11:48 2019/2/20
 * Describe :
 */
enum class DeviceStatus(var statusName: String?, var statusKey: Int) {
    OFFLINE("离线", 0),
    ONLINE("连接成功", 1),
    CONNECTING("正在连接", 2),
    TESTING("正在测试", 3),
    TESTPAUSE("测试暂停", 4),
}
