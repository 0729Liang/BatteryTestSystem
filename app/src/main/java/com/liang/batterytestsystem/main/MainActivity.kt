package com.liang.batterytestsystem.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.CheckBox
import com.blankj.utilcode.util.ToastUtils
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.base.LAbstractBaseActivity
import com.liang.batterytestsystem.constant.DeviceKey
import com.liang.batterytestsystem.device.DeviceDataBinding
import com.liang.batterytestsystem.device.DeviceEvent
import com.liang.batterytestsystem.device.DeviceStatus
import com.liang.batterytestsystem.exts.Router
import com.liang.batterytestsystem.module.service.DeviceService
import com.liang.batterytestsystem.view.DeviceOperWindow
import com.liang.liangutils.mgrs.LKVMgr
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @author : Amarao
 * CreateAt : 10:59 2019/2/21
 * Describe : 主界面，负责显示与连接设备
 */
class MainActivity : LAbstractBaseActivity() {

    private val mDataBinding: DeviceDataBinding = DeviceDataBinding()
    private val mAdapter: DeviceAdapter = DeviceAdapter(mDataBinding.mDeviceBeanList)

    private var mFlagChooseAll = false  //是否全部被选中

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Router.startDeviceMgrService(this)

        initData()
        initView()
        clicEvent()
    }

    override fun initData() {
        mDataBinding.addDevice("ox00-01", DeviceStatus.OFFLINE, mAdapter)
        mDataBinding.addDevice("ox00-02", DeviceStatus.OFFLINE, mAdapter)
        mDataBinding.addDevice("ox00-03", DeviceStatus.OFFLINE, mAdapter)
        mDataBinding.addDevice("ox00-04", DeviceStatus.OFFLINE, mAdapter)
        mDataBinding.addDevice("ox00-05", DeviceStatus.OFFLINE, mAdapter)
    }

    override fun initView() {
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
            mvMainChooseAll.text = "取消选择"
        } else {
            mvMainChooseAll.text = "选择全部"
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun clicEvent() {
        // 全部/取消 选择按钮
        mvMainChooseAll.setOnClickListener {
            mDataBinding.mDeviceBeanList.forEach {
                if (it.deviceStatus == DeviceStatus.OFFLINE) {
                    it.checkStatus = !mFlagChooseAll
                }
            }
            initChooseAllBtn(!mFlagChooseAll)
        }
        // 连接按钮
        mvMainConnectBtn.setOnClickListener {
            LKVMgr.memory().putList(DeviceKey.KEY_ONLINE_DEVICE, mDataBinding.getOnlineDeviceList())
            Router.startDeviceConnect(this)
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
        mvMainTestBtn.setOnClickListener {
            DeviceOperWindow.create(this).show(it).addTestPauseClickEvent { ToastUtils.showShort("暂停") }.addTestStopClickEvent { ToastUtils.showShort("结束") }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, DeviceService::class.java))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDeviceEvent(event: DeviceEvent) {
        when (event.msg) {
            DeviceEvent.EVENT_START_CONNECT -> {
                mDataBinding.mDeviceBeanList.forEachIndexed { index, bean ->
                    if (bean.deviceSerialNumber.equals(event.serialNumber)) {
                        bean.deviceStatus = DeviceStatus.CONNECTING
                        mAdapter.notifyItemChanged(index)
                    }
                } // forEach
            }
            DeviceEvent.EVENT_CONNECTED_OBJ -> {
                mDataBinding.mDeviceBeanList.forEachIndexed { index, bean ->
                    if (bean.deviceSerialNumber.equals(event.serialNumber)) {
                        bean.deviceStatus = DeviceStatus.ONLINE
                        mAdapter.notifyItemChanged(index)
                    }
                } // forEach
            }
            DeviceEvent.EVENT_START_DISCONNECT -> {
                mDataBinding.mDeviceBeanList.forEachIndexed { index, bean ->
                    if (bean.deviceSerialNumber.equals(event.serialNumber)) {
                        bean.deviceStatus = DeviceStatus.OFFLINE
                        mAdapter.notifyItemChanged(index)
                    }
                } // forEach
            }
        } // when
    }// onDeviceEvent
}
