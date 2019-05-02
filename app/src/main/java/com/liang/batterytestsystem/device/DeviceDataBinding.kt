package com.liang.batterytestsystem.device

import java.util.*

/**
 * @author : Amarao
 * CreateAt : 10:15 2019/2/21
 * Describe : 数据绑定
 */
class DeviceDataBinding {

    internal var mDeviceBeanList: MutableList<DeviceBean> = ArrayList() // 所有设备
    internal var mChedkedDeviceList: MutableList<DeviceBean> = ArrayList() // 选中设备
    internal val mConnectDeviceList: MutableList<DeviceBean> = ArrayList() // 连接成功

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

    // 得到在线设备和将要连接的设备
    fun getOnlineDeviceList(): MutableList<DeviceBean> {
        return mDeviceBeanList.filter {
            it.checkStatus == true  // 连接
                    || (it.checkStatus == false && it.deviceStatus != DeviceStatus.OFFLINE) // 在线
        }.toMutableList()
    }


    // 得到连接成功的设备列表  Leak Canary​​​​​​​
    fun getConnectDeviceList(): MutableList<DeviceBean> {
        mConnectDeviceList.clear()
        mDeviceBeanList.forEach {
            if (it.deviceStatus == DeviceStatus.ONLINE) {
                mConnectDeviceList.add(it)
            }
        }
        return mConnectDeviceList
    }

    fun isAllSameDeviceStatus(list: List<DeviceBean>, vararg status: DeviceStatus): Boolean {
        list.forEachIndexed { index1, bean ->
            status.forEachIndexed { index2, state ->
                if (bean.deviceStatus != state) {
                    return false
                }
            }
        }

        return false
    }

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

//    fun addDevice(deviceSerialNumber: String, status: DeviceStatus, adapter: DeviceAdapter) {
//        val bean = DeviceBean(deviceSerialNumber)
//        bean.deviceStatus = status
//        mDeviceBeanList.add(bean)
//        adapter.notifyItemInserted(mDeviceBeanList.size)
//    }

    // 插入数据后的用户操作接口
    interface ConsumerInsertEvent {
        fun insertEvent()
    }
}
