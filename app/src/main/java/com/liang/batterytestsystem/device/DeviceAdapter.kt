package com.liang.batterytestsystem.device

import android.graphics.drawable.AnimationDrawable
import android.widget.CheckBox
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.liang.batterytestsystem.R

/**
 * @author : Amarao
 * CreateAt : 14:21 2019/2/20
 * Describe :
 */
class DeviceAdapter(data: List<DeviceBean>?) : BaseQuickAdapter<DeviceBean, BaseViewHolder>(R.layout.item_device, data) {

    override fun convert(helper: BaseViewHolder, item: DeviceBean) {
        val imageView = helper.getView<ImageView>(R.id.mvItemDeviceIcon)
        val checkBox = helper.getView<CheckBox>(R.id.mvItemDeviceCheckbox)

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
        }

        checkBox.isChecked = item.checkStatus

//
//        LLogX.e("位置1 = "+helper.adapterPosition
//                +" ;位置2 ="+helper.position
//                +" ;位置3 ="+helper.layoutPosition
//                +" ;位置4 ="+helper.oldPosition
//                +" ;更新 "+item.checkStatus)

        helper.addOnClickListener(R.id.mvItemDeviceCheckbox)
        if (item.deviceStatus != DeviceStatus.OFFLINE) {
            checkBox.isEnabled = false
        }
    }
}
