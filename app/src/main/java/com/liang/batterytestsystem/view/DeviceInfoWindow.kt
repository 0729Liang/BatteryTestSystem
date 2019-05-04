//package com.liang.batterytestsystem.view
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.PopupWindow
//import android.widget.TextView
//import com.liang.batterytestsystem.R
//import com.liang.batterytestsystem.module.device.DeviceBean
//
///**
// * @author : Amarao
// * CreateAt : 15:29 2019/2/22
// * Describe : 设备信息展示弹框
// */
//class DeviceInfoWindow private constructor(context: Context) : PopupWindow() {
//
//    init {
//        val contentView = LayoutInflater.from(context).inflate(R.layout.window_device_info, null)
//        this.contentView = contentView
//        this.width = ViewGroup.LayoutParams.WRAP_CONTENT
//        this.height = ViewGroup.LayoutParams.WRAP_CONTENT
//        this.isOutsideTouchable = true
//        this.animationStyle = R.style.device_info_window
//    }
//
//    fun show(view: View, x: Int, y: Int, bean: DeviceBean): DeviceInfoWindow {
//        this.showAsDropDown(view, x, y)
//        val deviceNumber = contentView.findViewById<TextView>(R.id.mvWindowDeviceNumber)
//        val deviceName = contentView.findViewById<TextView>(R.id.mvWindowDeviceName)
//        val deviceCheckStatus = contentView.findViewById<TextView>(R.id.mvWindowDeviceCheckStatus)
//        val deviceStatus = contentView.findViewById<TextView>(R.id.mvWindowDeviceStatus)
//        deviceNumber.text = "设备编号：" + bean.deviceSerialNumber
//        deviceName.text = "设备名称: " + bean.deviceName
//        deviceCheckStatus.text = "是否选中: " + bean.checkStatus
//        deviceStatus.text = "设备状态: " + bean.deviceStatus
//        return this
//    }
//
//    fun hide() {
//        this.dismiss()
//    }
//
//    companion object {
//
//        fun create(context: Context): DeviceInfoWindow {
//            return DeviceInfoWindow(context)
//        }
//
//    }
//
//}
