package com.liang.batterytestsystem.device

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
        helper.setText(R.id.mvItemDeviceNumber, item.deviceSerialNumber)

        when (item.deviceConnectStatus) {
            DeviceStatus.OFFLINE -> helper.setText(R.id.mvItemDeviceStatusTag, DeviceStatus.OFFLINE.statusName)
            DeviceStatus.ONLINE -> helper.setText(R.id.mvItemDeviceStatusTag, DeviceStatus.ONLINE.statusName)
            DeviceStatus.CONNECTING -> helper.setText(R.id.mvItemDeviceStatusTag, DeviceStatus.CONNECTING.statusName)
            DeviceStatus.TESTING -> helper.setText(R.id.mvItemDeviceStatusTag, DeviceStatus.TESTING.statusName)
            DeviceStatus.TESTPAUSE -> helper.setText(R.id.mvItemDeviceStatusTag, DeviceStatus.TESTPAUSE.statusName)
        }
    }
}
