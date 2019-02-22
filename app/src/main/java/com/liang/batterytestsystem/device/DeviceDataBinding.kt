package com.liang.batterytestsystem.device

import com.liang.batterytestsystem.main.DeviceAdapter
import java.util.*

/**
 * @author : Amarao
 * CreateAt : 10:15 2019/2/21
 * Describe : 数据绑定
 */
class DeviceDataBinding {

    internal var mDeviceBeanList: MutableList<DeviceBean> = ArrayList() // 所有设备
    internal var mChedkedDeviceList: MutableList<DeviceBean> = ArrayList() // 选中设备
    // internal val mConnectDeviceList: MutableList<DeviceBean> = ArrayList() // 连接成功

    constructor(deviceBeanList: MutableList<DeviceBean>) {
        mDeviceBeanList.addAll(deviceBeanList)
    }

    constructor(bean: DeviceBean) {
        mDeviceBeanList.add(bean)
    }

    constructor()


    // 得到选中的设备列表
    fun getCheckedDeviceList(): MutableList<DeviceBean> {
        mChedkedDeviceList.clear()
        mDeviceBeanList.forEach {
            if (it.checkStatus) {
                mChedkedDeviceList.add(it)
            }
        }
        return mChedkedDeviceList
    }

    // 得到连接成功的设备列表
//    fun getConnectDeviceList(): MutableList<DeviceBean> {
//        mConnectDeviceList.clear()
//        mDeviceBeanList.forEach {
//            if (it.deviceStatus == DeviceStatus.ONLINE) {
//                mConnectDeviceList.add(it)
//            }
//        }
//        return mConnectDeviceList
//    }

    fun addDevice(bean: DeviceBean, event: ConsumerInsertEvent?) {
        mDeviceBeanList.add(bean)
        event?.insertEvent()
    }

    fun addDevice(bean: DeviceBean) {
        mDeviceBeanList.add(bean)
    }

    fun addDevice(deviceSerialNumber: String, status: DeviceStatus, event: ConsumerInsertEvent?) {
        val bean = DeviceBean(deviceSerialNumber)
        bean.deviceStatus = status
        mDeviceBeanList.add(bean)
        event?.insertEvent()
    }

    fun addDevice(deviceSerialNumber: String, status: DeviceStatus, adapter: DeviceAdapter) {
        val bean = DeviceBean(deviceSerialNumber)
        bean.deviceStatus = status
        mDeviceBeanList.add(bean)
        adapter.notifyItemInserted(mDeviceBeanList.size)
    }

    // 插入数据后的用户操作接口
    interface ConsumerInsertEvent {
        fun insertEvent()
    }
}
