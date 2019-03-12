package com.liang.batterytestsystem.module.connect

import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.view.MotionEvent
import android.widget.CheckBox
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.device.DeviceBean
import com.liang.batterytestsystem.device.DeviceStatus
import com.liang.batterytestsystem.exts.Router
import com.liang.batterytestsystem.view.DeviceInfoWindow

/**
 * @author : Amarao
 * CreateAt : 14:21 2019/2/20
 * Describe :
 */
class DeviceConnectAdapter(data: List<DeviceBean>?) : BaseQuickAdapter<DeviceBean, BaseViewHolder>(R.layout.item_device, data) {

    override fun convert(helper: BaseViewHolder, item: DeviceBean) {
        val imageView = helper.getView<ImageView>(R.id.mvItemDeviceIcon)
        val checkBox = helper.getView<CheckBox>(R.id.mvItemDeviceCheckbox)

        checkBox.isChecked = item.checkStatus

        helper.setText(R.id.mvItemDeviceNumber, item.deviceSerialNumber)
        when (item.deviceStatus) {
            DeviceStatus.OFFLINE -> {
                helper.setText(R.id.mvItemDeviceStatusTag, DeviceStatus.OFFLINE.statusName)
                helper.setImageResource(R.id.mvItemDeviceIcon, R.drawable.icon_offline_device)
            }
            DeviceStatus.ONLINE -> {
                helper.setText(R.id.mvItemDeviceStatusTag, DeviceStatus.ONLINE.statusName)
                helper.setImageResource(R.id.mvItemDeviceIcon, R.drawable.icon_online_device)
            }
            DeviceStatus.CONNECTING -> {
                helper.setText(R.id.mvItemDeviceStatusTag, DeviceStatus.CONNECTING.statusName)
                imageView.setImageResource(R.drawable.anim_device_connect)
                helper.setImageResource(R.id.mvItemDeviceIcon, R.drawable.anim_device_connect)
                val anim = imageView.drawable as AnimationDrawable
                anim.start()
            }
            DeviceStatus.TESTING -> {
                helper.setText(R.id.mvItemDeviceStatusTag, DeviceStatus.TESTING.statusName)
                helper.setImageResource(R.id.mvItemDeviceIcon, R.drawable.icon_online_device)
            }
            DeviceStatus.TESTPAUSE -> {
                helper.setText(R.id.mvItemDeviceStatusTag, DeviceStatus.TESTPAUSE.statusName)
                helper.setImageResource(R.id.mvItemDeviceIcon, R.drawable.icon_online_device)
            }
        }// when

        helper.addOnClickListener(R.id.mvItemDeviceCheckbox)
        helper.itemView.setOnClickListener { v ->
            Router.startDeviceDetail(mContext, item)
        }

        displayInfo(helper, item)

    }

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
