package com.liang.batterytestsystem.module.item

import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.utils.DigitalTrans

/**
 * @author : Amarao
 * CreateAt : 13:55 2019/3/15
 * Describe : 设备某一通道详情的适配器
 *
 */
class DeviceItemChannelAdapter(data: List<DeviceItemChannelBean>?)
    : BaseQuickAdapter<DeviceItemChannelBean, BaseViewHolder>(R.layout.item_device_channel, data) {

    override fun convert(helper: BaseViewHolder, item: DeviceItemChannelBean) {
        val checkBox = helper.getView<CheckBox>(R.id.mvItemDeviceChannelCheckbox)

        // 状态同步
        item.checkState = checkBox.isChecked

        // 数据赋值
        helper.setText(R.id.mvItemDeviceChannelId, "通道号: " + DigitalTrans.byte2hex(byteArrayOf(item.channelId)))
        helper.setText(R.id.mvItemDeviceChannelStepTime, "步时间: " + item.stepTime.toString() + " S")
        helper.setText(R.id.mvItemDeviceChannelElectric, "电流: " + item.electric.toString() + " A")
        helper.setText(R.id.mvItemDeviceChannelVoltage, "电压: " + item.voltage.toString() + " V")
        helper.setText(R.id.mvItemDeviceChannelPower, "功率: " + item.power.toString() + " W")
        helper.setText(R.id.mvItemDeviceChannelTemp, "温度: " + item.temperture.toString() + " ℃")
        helper.setText(R.id.mvItemDeviceChannelAh, "安时:" + item.ampereHour.toString() + " Ah")

    }


}