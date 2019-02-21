package com.liang.batterytestsystem.main

import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.CheckBox
import com.blankj.utilcode.util.ToastUtils
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.base.LBaseActivity
import com.liang.batterytestsystem.device.DeviceAdapter
import com.liang.batterytestsystem.device.DeviceDataBinding
import com.liang.batterytestsystem.device.DeviceStatus
import com.liang.liangutils.utils.LLogX
import kotlinx.android.synthetic.main.activity_main.*


/**
 * @author : Amarao
 * CreateAt : 10:59 2019/2/21
 * Describe : 主界面，负责显示与连接设备
 */
class MainActivity : LBaseActivity() {

    private val mDataBinding: DeviceDataBinding = DeviceDataBinding()
    private val mAdapter: DeviceAdapter = DeviceAdapter(mDataBinding.mDeviceBeanList)

    private var mFlagChooseAll = false  //是否全部被选中

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initView()
        clicEvent()

    }

    //override
    fun initData() {
        mDataBinding.addDevice("ox00-01", DeviceStatus.OFFLINE, mAdapter)
        mDataBinding.addDevice("ox00-02", DeviceStatus.OFFLINE, mAdapter)
        mDataBinding.addDevice("ox00-03", DeviceStatus.OFFLINE, mAdapter)
        mDataBinding.addDevice("ox00-04", DeviceStatus.OFFLINE, mAdapter)
        mDataBinding.addDevice("ox00-05", DeviceStatus.OFFLINE, mAdapter)
    }


    //override
    fun initView() {
        mvMainTitleView.setClickLeftFinish(this)
        mvMainTitleView.setTitle("电池测试系统")
        mvMainTitleView.lineView.visibility = View.VISIBLE

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mvMainRecycleView.layoutManager = layoutManager
        mvMainRecycleView.adapter = mAdapter

        initChooseAllBtn(mFlagChooseAll)
    }

    fun initChooseAllBtn(allChoosed: Boolean) {
        mFlagChooseAll = allChoosed
        if (allChoosed) {
            mvMainChooseAllBtn.text = "取消选择"
        } else {
            mvMainChooseAllBtn.text = "选择全部"
        }
        mAdapter.notifyDataSetChanged()
    }

    //override
    fun clicEvent() {

        // 全部/取消 选择按钮
        mvMainChooseAllBtn.setOnClickListener {
            mDataBinding.mDeviceBeanList.forEach {
                if (it.deviceStatus == DeviceStatus.OFFLINE) {
                    it.checkStatus = !mFlagChooseAll
                }
            }
            initChooseAllBtn(!mFlagChooseAll)
        }

        // 连接按钮
        mvMainConnectBtn.setOnClickListener {
            mDataBinding.getCheckedDeviceList().forEach {
                // websocket连接操作
                LLogX.e("设备" + it.deviceSerialNumber + "尝试连接")
                it.deviceStatus = DeviceStatus.CONNECTING
                mAdapter.notifyDataSetChanged()
            }
        }

        // item点击
        mAdapter.setOnItemClickListener { adapter, view, position ->
            ToastUtils.showShort("position = " + position + " number = " + mDataBinding.mDeviceBeanList.get(position).deviceSerialNumber)
        }

        // chexbox点击
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val checkBox = adapter.getViewByPosition(mvMainRecycleView, position, R.id.mvItemDeviceCheckbox) as CheckBox
            val bean = mDataBinding.mDeviceBeanList.get(position)

            if (bean.deviceStatus == DeviceStatus.OFFLINE) {
                bean.checkStatus = checkBox.isChecked
                ToastUtils.showShort("This is status = " + bean.checkStatus)
            } else {
                ToastUtils.showShort(bean.deviceStatus.statusName)
            }

            initChooseAllBtn(mDataBinding.getCheckedDeviceList().size == mDataBinding.mDeviceBeanList.size)

        }
    }
}
