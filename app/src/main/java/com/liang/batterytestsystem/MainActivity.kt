package com.liang.batterytestsystem

import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.liang.batterytestsystem.base.LAbstractBaseActivity
import com.liang.batterytestsystem.device.DeviceAdapter
import com.liang.batterytestsystem.device.DeviceBean
import com.liang.batterytestsystem.device.DeviceStatus
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : LAbstractBaseActivity() {

    private val mList = ArrayList<DeviceBean>()
    private val mAdapter: DeviceAdapter = DeviceAdapter(mList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

        addDevice("ox00-01", DeviceStatus.OFFLINE)
        addDevice("ox00-02", DeviceStatus.OFFLINE)
        addDevice("ox00-03", DeviceStatus.OFFLINE)
        addDevice("ox00-04", DeviceStatus.OFFLINE)
        addDevice("ox00-05", DeviceStatus.OFFLINE)

    }

    override fun initView() {
        mvMainTitleView.setClickLeftFinish(this)
        mvMainTitleView.setTitle("电池测试系统")
        mvMainTitleView.lineView.visibility = View.VISIBLE

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mvMainRecycleView.layoutManager = layoutManager
        mvMainRecycleView.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position ->
            ToastUtils.showShort("position = " + position)
        }
    }

    override fun clicEvent() {
        mvMainConnectBtn.setOnClickListener { }
    }

    fun addDevice(bean: DeviceBean) {
        mList.add(bean)
        mAdapter.notifyItemInserted(mList.size)
    }

    fun addDevice(deviceSerialNumber: String, status: DeviceStatus) {
        val bean = DeviceBean(deviceSerialNumber)
        bean.deviceConnectStatus = status
        mList.add(bean)
        mAdapter.notifyItemInserted(mList.size)
    }

}
