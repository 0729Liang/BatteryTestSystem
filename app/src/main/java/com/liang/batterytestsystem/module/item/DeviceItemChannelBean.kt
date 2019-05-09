package com.liang.batterytestsystem.module.item

import com.liang.batterytestsystem.module.device.DeviceStatus
import com.liang.batterytestsystem.module.home.DeviceItemBean
import java.io.Serializable
import java.util.ArrayList

/**
 * @author : Amarao
 * CreateAt : 13:57 2019/3/15
 * Describe : 设备某一通道的详情信息
 *
 */
class DeviceItemChannelBean(var deviceItemBean: DeviceItemBean, var channelId: Byte) : Serializable {
    var deviceId: Byte = 0x00 // 设备号
    var checkState = false // 通道选中状态
    var deviceStatus = DeviceStatus.OFFLINE // 设备连接状态


    var stepTime: Float = 0f // 步时间S
    var electric: Float = 0f // 电流A
    var voltage: Float = 0f  // 电压V
    var power: Float = 0f    // 功率W
    var temperture: Float = 0f // 温度℃
    var ampereHour: Float = 0f // 安时Ah

    fun setMStepTime(data: Float) {
        stepTime = data
        addStepTimeList(data)
    }

    fun setMElectric(data: Float) {
        electric = data
        addElectricistList(data)
    }

    fun setMVoltage(data: Float) {
        voltage = data
        addVoltageList(data)
    }

    fun setMPower(data: Float) {
        power = data
        addPowerList(data)
    }

    fun setMTemperture(data: Float) {
        temperture = data
        addTempertureList(data)
    }

    fun setMAmpereHour(data: Float) {
        ampereHour = data
        addAmpereHourList(data)
    }


    // 历史数据
    var mStepTimeList: MutableList<Float> = ArrayList()
    var mElectricistList: MutableList<Float> = ArrayList()
    var mVoltageList: MutableList<Float> = ArrayList()
    var mPowerList: MutableList<Float> = ArrayList()
    var mTempertureList: MutableList<Float> = ArrayList()
    var mAmpereHourList: MutableList<Float> = ArrayList()

    private val limit = 500
    private val removeIndex = 0

    fun addStepTimeList(data: Float) {
        if (mStepTimeList.size > limit) {
            mStepTimeList.removeAt(removeIndex)
        }
        mStepTimeList.add(data)
    }

    fun addElectricistList(data: Float) {
        if (mElectricistList.size > limit) {
            mElectricistList.removeAt(removeIndex)
        }
        mElectricistList.add(data)
    }


    fun addVoltageList(data: Float) {
        if (mVoltageList.size > limit) {
            mVoltageList.removeAt(removeIndex)
        }
        mVoltageList.add(data)
    }


    fun addPowerList(data: Float) {
        if (mPowerList.size > limit) {
            mPowerList.removeAt(removeIndex)
        }
        mPowerList.add(data)
    }


    fun addTempertureList(data: Float) {
        if (mTempertureList.size > limit) {
            mTempertureList.removeAt(removeIndex)
        }
        mTempertureList.add(data)
    }


    fun addAmpereHourList(data: Float) {
        if (mStepTimeList.size > limit) {
            mStepTimeList.removeAt(removeIndex)
        }
        mAmpereHourList.add(data)
    }
}