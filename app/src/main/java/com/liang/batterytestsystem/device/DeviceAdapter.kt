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

    }
}
