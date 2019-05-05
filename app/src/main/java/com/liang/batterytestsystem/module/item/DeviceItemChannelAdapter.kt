package com.liang.batterytestsystem.module.item

import android.widget.CheckBox
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.exts.Router
import com.liang.batterytestsystem.module.device.DeviceStatus
import com.liang.batterytestsystem.module.home.DeviceCommand
import com.liang.batterytestsystem.module.service.DeviceTestEvent
import com.liang.batterytestsystem.utils.DigitalTrans
import com.liang.liangutils.utils.LLogX
import org.jetbrains.annotations.NotNull

/**
 * @author : Amarao
 * CreateAt : 13:55 2019/3/15
 * Describe : 设备某一通道详情的适配器
 *
 */
class DeviceItemChannelAdapter(data: List<DeviceItemChannelBean>?)
    : BaseQuickAdapter<DeviceItemChannelBean, BaseViewHolder>(R.layout.item_device_channel, data) {

    override fun convert(helper: BaseViewHolder, item: DeviceItemChannelBean) {
        val checkBox = helper.getView(R.id.mvItemDeviceChannelCheckbox) as CheckBox

        LLogX.e(" a = " + helper.adapterPosition)
        // 状态同步
        checkBox.isChecked = item.checkState

        // 数据赋值
        helper.setText(R.id.mvItemDeviceChannelId, "通道号: " + DigitalTrans.byte2hex(byteArrayOf(item.channelId)))
        helper.setText(R.id.mvItemDeviceChannelStepTime, "步时间: " + item.stepTime.toString() + " S")
        helper.setText(R.id.mvItemDeviceChannelElectric, "电流: " + item.electric.toString() + " A")
        helper.setText(R.id.mvItemDeviceChannelVoltage, "电压: " + item.voltage.toString() + " V")
        helper.setText(R.id.mvItemDeviceChannelPower, "功率: " + item.power.toString() + " W")
        helper.setText(R.id.mvItemDeviceChannelTemp, "温度: " + item.temperture.toString() + " ℃")
        helper.setText(R.id.mvItemDeviceChannelAh, "安时: " + item.ampereHour.toString() + " Ah")

        checkBox.setOnClickListener {
            val view = it as CheckBox
            item.checkState = view.isChecked
            if (item.checkState) {
                DeviceTestEvent.postAddDeviceTestChannel(item)
            } else {
                DeviceTestEvent.postRemoveDeviceTestChannel(item)
            }
        }

        helper.itemView.setOnClickListener {
            Router.startDeviceDetailTest(mContext, item)
        }

    }

    override fun remove(position: Int) {
        super.remove(position)
        LLogX.e(" r = " + position)
    }


    // 更新数据状态
    fun updateStatus(@NotNull byteArray: ByteArray) {

        /**
         *
         * 1字节总帧头 7B   -> 0
         * 1字节命令（0x80） -> 3
         * 1字节设备号  -> 4
         */
        if (byteArray[0] == DeviceCommand.FRAME_HEADER && byteArray[3] == DeviceCommand.COMMAND_QUERY_CHANNEL_STATUS_TEST) {
            LLogX.e("设备号 = " + DigitalTrans.byte2hex(byteArray[4]))

            // 更新每条数据的tag标签
            mData.forEachIndexed { index, bean ->
                // 共计 15 通道;通道信息位 n(0-14) = 6+x*4
                // min=6;max=2+15*4=62; 步长 4
                val status = byteArray.get(index * 4 + 6)
                val deviceStatusTagTv = getViewByPosition(recyclerView, index, R.id.mvItemDeviceChannelStepTime)

                if (deviceStatusTagTv == null) {
                    LLogX.e("null")
                    return
                }
                deviceStatusTagTv as TextView

                LLogX.e("通道" + index + " STATUS = " + DigitalTrans.byte2hex(status))

                when (status) {
                    DeviceStatus.OFFLINE.statusKey -> {
                        deviceStatusTagTv.text = DeviceStatus.OFFLINE.statusName
                        bean.deviceStatus = DeviceStatus.OFFLINE
                    }
                    DeviceStatus.ONLINE.statusKey -> {
                        deviceStatusTagTv.text = DeviceStatus.ONLINE.statusName
                        bean.deviceStatus = DeviceStatus.ONLINE
                    }
                    DeviceStatus.TESTPAUSE.statusKey -> {
                        deviceStatusTagTv.text = DeviceStatus.TESTPAUSE.statusName
                        bean.deviceStatus = DeviceStatus.TESTPAUSE
                    }
                    DeviceStatus.STOP.statusKey -> {
                        deviceStatusTagTv.text = DeviceStatus.STOP.statusName
                        bean.deviceStatus = DeviceStatus.STOP
                    }
                }
                notifyItemChanged(index)
            }

        }


    }

}