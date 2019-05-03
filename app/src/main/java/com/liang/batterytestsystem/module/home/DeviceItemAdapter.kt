package com.liang.batterytestsystem.module.home

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.module.item.DeviceItemChannelAdapter
import com.liang.batterytestsystem.utils.DigitalTrans

/**
 * @author : Amarao
 * CreateAt : 14:27 2019/3/15
 * Describe : 设备适配器
 *
 */
class DeviceItemAdapter(data: List<DeviceItemBean>?)
    : BaseQuickAdapter<DeviceItemBean, BaseViewHolder>(R.layout.item_device, data) {

    override fun convert(helper: BaseViewHolder, item: DeviceItemBean) {
        val checkBox = helper.getView<CheckBox>(R.id.mvItemDeviceCheckbox)

        // 状态同步
        item.checkState = checkBox.isChecked

        //数据赋值
        helper.setText(R.id.mvItemDeviceId, "设备号: " + DigitalTrans.byte2hex(byteArrayOf(item.deviceId)))

        val mRecyclerView = helper.getView<RecyclerView>(R.id.mvItemDeviceRecycleView)
        val mChannelAdpter = DeviceItemChannelAdapter(item.channelList)
        val mLayoutManager = LinearLayoutManager(mContext)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mChannelAdpter


    }

}