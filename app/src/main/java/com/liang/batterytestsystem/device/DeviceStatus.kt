package com.liang.batterytestsystem.device

/**
 * @author : Amarao
 * CreateAt : 11:48 2019/2/20
 * Describe :
 */
enum class DeviceStatus(var statusName: String?, var statusKey: Int) {
    OFFLINE("offline", 0),
    ONLINE("online", 1),
    CONNECTING("connecting", 2),
    TESTING("testing", 3),
    TESTPAUSE("testPause", 4),
}
