package com.liang.batterytestsystem.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import com.blankj.utilcode.util.ToastUtils
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.base.LAbstractBaseActivity
import com.liang.batterytestsystem.demo.FastSort
import com.liang.batterytestsystem.demo.ResverString
import com.liang.batterytestsystem.device.DeviceDataBinding
import com.liang.batterytestsystem.device.DeviceEvent
import com.liang.batterytestsystem.device.DeviceStatus
import com.liang.batterytestsystem.exts.Router
import com.liang.batterytestsystem.module.home.DeviceCommand
import com.liang.batterytestsystem.module.service.DeviceService
import com.liang.batterytestsystem.module.socket.SendUtils
import com.liang.batterytestsystem.utils.LTime
import com.liang.batterytestsystem.view.DeviceInfoWindow
import com.liang.batterytestsystem.view.DeviceOperWindow
import com.liang.liangutils.utils.LLogX
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
        //ReceiveUtils.receiveMessage()
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
        // chexbox点击
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val checkBox = adapter.getViewByPosition(mvMainRecycleView, position, R.id.mvItemDeviceChannelCheckbox) as CheckBox
            val bean = mDataBinding.mDeviceBeanList.get(position)

            bean.checkStatus = checkBox.isChecked
            ToastUtils.showShort("This is status = " + bean.checkStatus)

            initChooseAllBtn(mDataBinding.getCheckedDeviceList().size == mDataBinding.mDeviceBeanList.size)

        }

        // 全部/取消 选择按钮
        mvMainChooseAll.setOnClickListener {
            mDataBinding.mDeviceBeanList.forEach {
                it.checkStatus = !mFlagChooseAll
            }
            initChooseAllBtn(!mFlagChooseAll)
        }

        // 连接按钮
        mvMainConnectBtn.setOnClickListener {
            //            LKVMgr.memory().putList(DeviceKey.KEY_ONLINE_DEVICE, mDataBinding.getOnlineDeviceList())
//            Router.startDeviceConnect(this)

        }

        mvMainConfigTest.setOnClickListener {
            Router.startUdpConfig(this)
        }

        mvMainTestStart.setOnClickListener {
            ToastUtils.showShort("开始测试")
            SendUtils.sendCommand(DeviceCommand.createCommandStartTest(DeviceCommand.DEVICE_1, DeviceCommand.CHANNEL_1))
        }

        mvMainDeviceMore.setOnClickListener {
            DeviceOperWindow.create(this).show(it)
                    .addTestPauseClickEvent {
                        ToastUtils.showShort("发送 暂停")
                        SendUtils.sendCommand(DeviceCommand.createCommandPauseTest(DeviceCommand.DEVICE_1, DeviceCommand.CHANNEL_1))

                    }
                    .addTestResumeClickEvent {
                        ToastUtils.showShort("发送继续")
                        SendUtils.sendCommand(DeviceCommand.createCommandResumeTest(DeviceCommand.DEVICE_1, DeviceCommand.CHANNEL_1))
                    }
                    .addQueryClickEvent {
                        ToastUtils.showShort("发送查询")
                        //SendUtils.sendCommand(DeviceCommand.createCommandQueryTest(DeviceCommand.DEVICE_1,DeviceCommand.CHANNEL_1))
                        val msg = byteArrayOf(0x7B, 0x00, 0x08, 0x71.toByte(), 0x01, 0x02, 0x00, 0x7D)

                        SendUtils.sendCommand(msg)
                    }

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
            DeviceEvent.EVENT_RECV_MSG -> {
                val msg = LTime.convertStampToTime(System.currentTimeMillis()) + " : " + event.recvMsg
                mvMainTestText.text = msg
            }
        } // when
    }// onDeviceEvent


    private fun test() {
        var x = 0
        var y = 0
        val window = DeviceInfoWindow.create(this)
        mvMainTestBtn.setOnTouchListener { v, event ->
            x = event.x.toInt()
            y = event.y.toInt() - v.height
            if (event.action == MotionEvent.ACTION_UP) {
                Handler().postDelayed({ window.hide() }, 2000)
            }
            false
        }

        mvMainTestBtn.setOnLongClickListener {
            window.show(it, x, y, mDataBinding.mDeviceBeanList.get(0))
            true
        }

        // mvMainTestBtn.setOnClickListener { Router.startDeviceDetailTest(this, mDataBinding.mDeviceBeanList.get(0)) }
    }

    private fun test2() {

        // 字符串翻转
        val s = "ABCDEFG"
        LLogX.e("-------------字符串翻转-----------------")
        LLogX.e("二分法翻转字符：" + ResverString.resverByDichotomy(s))
        LLogX.e("CharAt 拼接：" + ResverString.reverseByCharAt(s))

        // 快排
        val num = intArrayOf(1, 5, 2, 4, 3, 3, 7)
        LLogX.e("-------------快排-----------------" + num.size)
        //FastSort.quickSort(num,0,num.size-1)
        FastSort.sort(num, 0, num.size - 1)
        LLogX.e("快排：" + FastSort.arrayToString(num))
    }
}
