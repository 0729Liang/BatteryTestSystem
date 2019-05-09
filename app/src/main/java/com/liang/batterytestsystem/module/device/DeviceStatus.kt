package com.liang.batterytestsystem.module.device

/**
 * @author : Amarao
 * CreateAt : 11:48 2019/2/20
 * Describe : 设备状态
 */
enum class DeviceStatus(var statusName: String?, var statusKey: Byte) {

    OFFLINE("离线", 0x99.toByte()),
    ONLINE("在线", 0x01),
    TESTPAUSE("测试暂停", 0x06),
    STOP("停止", 0x09),

    // 不常用
    CONNECTING("正在连接", 2),
    TESTING("正在测试", 3),
/*
    Discharge  =0x2, //放电
    Shelve   =0x3, //间歇
    Charge_XiePo =0x4, //斜坡充电
    Discharge_XiePo =0x5, //斜坡放电
    Error   =0x10,     //出错
    Parallel  =0x20, //并机

    Charge   =0x1, //充电
    Pause   =0x6, //暂停
    Select   =0x7F, //选中状态
    Stop   =0x9, //停止
    Offline   =0x99 //离线
*/

}
