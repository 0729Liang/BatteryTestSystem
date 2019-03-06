package com.liang.batterytestsystem.module.service

import com.liang.batterytestsystem.device.DeviceBean
import com.liang.batterytestsystem.device.DeviceStatus
import com.liang.liangutils.utils.LJsonX
import java.util.*

/**
 * @author : Amarao
 * CreateAt : 13:16 2019/2/28
 * Describe : 设备管理类
 */
object DeviceMgr {

    var mDeviceBeanList: MutableList<DeviceBean> = ArrayList() // 所有设备

    fun initDeviceList(list: MutableList<DeviceBean>) {
        mDeviceBeanList.clear()
        mDeviceBeanList.addAll(list)
    }

    fun updateDeviceStatus(number: String, status: DeviceStatus) {
        mDeviceBeanList
                .filter { it.deviceSerialNumber.equals(number) }
                .forEach {
                    LJsonX.toJson(it)
                    it.deviceStatus = status
                }
    }

    fun getAllDevice(): MutableList<DeviceBean> {
        return mDeviceBeanList
    }

    fun insertDevice(bean: DeviceBean) {
        mDeviceBeanList.add(bean)
    }

    fun insertDevice(list: MutableList<DeviceBean>) {
        mDeviceBeanList.addAll(list)
    }

    fun deleteDevice(bean: DeviceBean) {
        mDeviceBeanList.remove(bean)
    }

    fun deleteDevice(list: MutableList<DeviceBean>) {
        mDeviceBeanList.removeAll(list)
    }


}
