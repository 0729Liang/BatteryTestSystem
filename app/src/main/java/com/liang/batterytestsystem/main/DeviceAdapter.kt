package com.liang.batterytestsystem.main

import android.os.Handler
import android.view.MotionEvent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.device.DeviceBean
import com.liang.batterytestsystem.view.DeviceInfoWindow

/**
 * @author : Amarao
 * CreateAt : 14:21 2019/2/20
 * Describe :
 */
class DeviceAdapter(data: List<DeviceBean>?) : BaseQuickAdapter<DeviceBean, BaseViewHolder>(R.layout.item_device_channel, data) {

    // 数据 状态 界面 点击事件 特殊
    override fun convert(helper: BaseViewHolder, item: DeviceBean) {
//        val imageView = helper.getView<ImageView>(R.id.mvItemDeviceChannelIcon)
//        val checkBox = helper.getView<CheckBox>(R.id.mvItemDeviceChannelCheckbox)
//
//        // 状态
//        checkBox.isChecked = item.checkStatus
//
//        helper.setText(R.id.mvItemDeviceChannelNumber, item.deviceSerialNumber)
//        when (item.deviceStatus) {
//            DeviceStatus.OFFLINE -> {
//                helper.setText(R.id.mvItemDeviceChannelStatusTag, DeviceStatus.OFFLINE.statusName)
//                helper.setImageResource(R.id.mvItemDeviceChannelIcon, R.drawable.icon_offline_device)
//            }
//            DeviceStatus.ONLINE -> {
//                helper.setText(R.id.mvItemDeviceChannelStatusTag, DeviceStatus.ONLINE.statusName)
//                helper.setImageResource(R.id.mvItemDeviceChannelIcon, R.drawable.icon_online_device)
//            }
//            DeviceStatus.CONNECTING -> {
//                helper.setText(R.id.mvItemDeviceChannelStatusTag, DeviceStatus.CONNECTING.statusName)
//                imageView.setImageResource(R.drawable.anim_device_connect)
//                helper.setImageResource(R.id.mvItemDeviceChannelIcon, R.drawable.anim_device_connect)
//                val anim = imageView.drawable as AnimationDrawable
//                anim.start()
//            }
//            DeviceStatus.TESTING -> {
//                helper.setText(R.id.mvItemDeviceChannelStatusTag, DeviceStatus.TESTING.statusName)
//                helper.setImageResource(R.id.mvItemDeviceChannelIcon, R.drawable.icon_online_device)
//            }
//            DeviceStatus.TESTPAUSE -> {
//                helper.setText(R.id.mvItemDeviceChannelStatusTag, DeviceStatus.TESTPAUSE.statusName)
//                helper.setImageResource(R.id.mvItemDeviceChannelIcon, R.drawable.icon_online_device)
//            }
//        }// when
//
//        // 点击事件
//        helper.addOnClickListener(R.id.mvItemDeviceChannelCheckbox)
//        helper.itemView.setOnClickListener { v ->
//            Router.startDeviceDetail(mContext, item)
//        }
//
//        // 特殊
//        displayInfo(helper, item)

    }

    /* 设备信息展示弹窗*/
    fun displayInfo(helper: BaseViewHolder, item: DeviceBean) {
        val window = DeviceInfoWindow.create(mContext)
        var x = 0
        var y = 0
        helper.itemView.setOnTouchListener { v, event ->
            x = event.x.toInt()
            y = event.y.toInt() - v.height
            if (event.action == MotionEvent.ACTION_UP) {
                Handler().postDelayed({ window.hide() }, 2000)
            }
            false
        }

        helper.itemView.setOnLongClickListener {
            window.show(it, x, y, item)
            true
        }
    }
}
