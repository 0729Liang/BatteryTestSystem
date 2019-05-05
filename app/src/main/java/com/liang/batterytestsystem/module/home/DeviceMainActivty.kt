package com.liang.batterytestsystem.module.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.base.LAbstractBaseActivity
import com.liang.batterytestsystem.exts.Router
import com.liang.batterytestsystem.module.data.DeviceDataAnalysisUtils
import com.liang.batterytestsystem.module.device.DeviceStatus
import com.liang.batterytestsystem.module.service.DeviceMgrService
import com.liang.batterytestsystem.module.service.DeviceQueryEvent
import com.liang.batterytestsystem.module.service.DeviceQueryService
import com.liang.batterytestsystem.module.service.DeviceTestEvent
import com.liang.batterytestsystem.utils.DigitalTrans
import com.liang.batterytestsystem.view.DeviceOperWindow
import com.liang.liangutils.utils.LLogX
import kotlinx.android.synthetic.main.activity_device_main_activty.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DeviceMainActivty : LAbstractBaseActivity() {

    val mSendName = "发送线程"
    lateinit var mDeviceItemAdapter: DeviceItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_main_activty)

        Router.startDeviceMgrService(this)
        Router.startDeviceQueryService(this)

        initData()
        initView()
        clicEvent()

        //DeviceDataAnalysisUtils.test()
    }


    override fun initData() {

    }

    override fun initView() {
        mvMain2Title.setTitle("电池测试系统")
        mvMain2Title.setClickLeftFinish(this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mvMain2RecycleView.layoutManager = layoutManager
        mDeviceItemAdapter = DeviceItemAdapter(DeviceMgrService.sDeviceList)
        mvMain2RecycleView.adapter = mDeviceItemAdapter
    }

    override fun clicEvent() {
        mvMain2ConfigTest.setOnClickListener {
            Router.startUdpConfig(this)

        }

        mvMain2TestStart.setOnClickListener {
            val deviceItemBeanList = DeviceMgrService.sDeviceItemBeanList
            val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_START_TEST, true)
            ToastUtils.showShort("开始测试 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
            DeviceCommand.sendCommandList(commandList, mSendName)
            // todo Device.Service 异步查询数据，通道状态
        }

        mvMain2DeviceMore.setOnClickListener {
            DeviceOperWindow.create(this).show(it)
                    .addTestPauseClickEvent {
                        //                        val list = DeviceMgrService.sDeviceTestChannelList
//                        ToastUtils.showShort("发送暂停 测试通道数=" + list.size)
//                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_PAUSE_TEST)
//                        DeviceCommand.sendCommandList(commandList, mSendName)

                        val deviceItemBeanList = DeviceMgrService.sDeviceItemBeanList
                        val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_PAUSE_TEST, true)
                        ToastUtils.showShort("发送暂停 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    .addTestResumeClickEvent {
                        //                        val list = DeviceMgrService.sDeviceTestChannelList
//                        ToastUtils.showShort("发送继续 测试通道数=" + list.size)
//                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_RESUME_TEST)
//                        DeviceCommand.sendCommandList(commandList, mSendName)

                        val deviceItemBeanList = DeviceMgrService.sDeviceItemBeanList
                        val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_RESUME_TEST, true)
                        ToastUtils.showShort("发送继续 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    .addStopClickEvent {
                        //                        val list = DeviceMgrService.sDeviceTestChannelList
//                        ToastUtils.showShort("发送停止 测试通道数=" + list.size)
//                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_PAUSE_TEST)
//                        DeviceCommand.sendCommandList(commandList, mSendName)

                        val deviceItemBeanList = DeviceMgrService.sDeviceItemBeanList
                        val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_STOP_TEST, true)
                        ToastUtils.showShort("发送停止 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    // 通道单独发
                    .addQueryDataClickEvent {
                        //                        val list = DeviceMgrService.sDeviceTestChannelList
//                        ToastUtils.showShort("发送查询数据 测试通道数=" + list.size)
//                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_QUERY_DATA_TEST)
//                        DeviceCommand.sendCommandList(commandList, mSendName)

                        // 查询数据 只管设备号，
                        val deviceItemBeanList = DeviceMgrService.sDeviceItemBeanList
                        val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_QUERY_DATA_TEST, false)
                        ToastUtils.showShort("发送查询数据 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    .addQueryChannelStatusClickEvent {
                        //                        val list = DeviceMgrService.sDeviceTestChannelList
//                        ToastUtils.showShort("发送查询通道状态 测试通道数=" + list.size)
//                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_QUERY_CHANNEL_STATUS_TEST)
//                        DeviceCommand.sendCommandList(commandList, mSendName)

                        // 查询通道状态 只管设备号
                        val deviceItemBeanList = DeviceMgrService.sDeviceItemBeanList
                        val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_QUERY_CHANNEL_STATUS_TEST, false)
                        ToastUtils.showShort("发送查询通道状态 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }

        }
    } // clickevent

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, DeviceMgrService::class.java))
        stopService(Intent(this, DeviceQueryService::class.java))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDeviceQueryEvent(event: DeviceQueryEvent) {
        when (event.msg) {
            DeviceQueryEvent.DEVICE_QUERY_CHANNEL_STATUS_RESULT -> {
                // 发出通知
                mDeviceItemAdapter.notification(event.queryResultByteArray)
            }
            else -> {

            }
        }
    }
}
