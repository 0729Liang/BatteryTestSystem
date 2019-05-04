package com.liang.batterytestsystem.module.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.base.LAbstractBaseActivity
import com.liang.batterytestsystem.exts.Router
import com.liang.batterytestsystem.module.service.DeviceService
import com.liang.batterytestsystem.view.DeviceOperWindow
import kotlinx.android.synthetic.main.activity_device_main_activty.*

class DeviceMainActivty : LAbstractBaseActivity() {

    val mDeviceList: MutableList<DeviceItemBean> = ArrayList()
    val mAdapter = DeviceItemAdapter(mDeviceList)
    val mSendName = "发送线程"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_main_activty)

        Router.startDeviceMgrService(this)
        initData()
        initView()
        clicEvent()


    }


    override fun initData() {
        //创建设备
        val device1 = DeviceCreateFactory.createDevice(DeviceCommand.DEVICE_1)
        val device2 = DeviceCreateFactory.createDevice(DeviceCommand.DEVICE_2)
        val device3 = DeviceCreateFactory.createDevice(DeviceCommand.DEVICE_3)
        val device4 = DeviceCreateFactory.createDevice(DeviceCommand.DEVICE_4)
        // 设备列表生成
        addDeviceList(device1, device2, device3, device4)
    }


    fun addDeviceList(vararg deviceList: DeviceItemBean) {
        mDeviceList.addAll(deviceList)
        mDeviceList.forEach {
            it.addChannelList(
                    // 为每台设备添加测试通道
                    DeviceCreateFactory.createDeviceChannelList(it,
                            DeviceCommand.CHANNEL_1, DeviceCommand.CHANNEL_2, DeviceCommand.CHANNEL_3, DeviceCommand.CHANNEL_4,
                            DeviceCommand.CHANNEL_5, DeviceCommand.CHANNEL_6, DeviceCommand.CHANNEL_7, DeviceCommand.CHANNEL_8
//                            DeviceCommand.CHANNEL_9, DeviceCommand.CHANNEL_10, DeviceCommand.CHANNEL_11, DeviceCommand.CHANNEL_12,
//                            DeviceCommand.CHANNEL_13, DeviceCommand.CHANNEL_14, DeviceCommand.CHANNEL_15, DeviceCommand.CHANNEL_16
                    ))
        }
    }


    override fun initView() {
        mvMain2Title.setTitle("电池测试系统")
        mvMain2Title.setClickLeftFinish(this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mvMain2RecycleView.layoutManager = layoutManager
        mvMain2RecycleView.adapter = mAdapter
    }

    override fun clicEvent() {
        mvMain2ConfigTest.setOnClickListener {
            Router.startUdpConfig(this)

        }

        mvMain2TestStart.setOnClickListener {
            val deviceItemBeanList = DeviceService.sDeviceItemBeanList
            val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_START_TEST)
            ToastUtils.showShort("开始测试 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
            DeviceCommand.sendCommandList(commandList, mSendName)
            // todo Device.Service 异步查询数据，通道状态

        }

        mvMain2DeviceMore.setOnClickListener {
            DeviceOperWindow.create(this).show(it)
                    .addTestPauseClickEvent {
                        //                        val list = DeviceService.sDeviceTestChannelList
//                        ToastUtils.showShort("发送暂停 测试通道数=" + list.size)
//                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_PAUSE_TEST)
//                        DeviceCommand.sendCommandList(commandList, mSendName)

                        val deviceItemBeanList = DeviceService.sDeviceItemBeanList
                        val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_PAUSE_TEST)
                        ToastUtils.showShort("发送暂停 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    .addTestResumeClickEvent {
                        //                        val list = DeviceService.sDeviceTestChannelList
//                        ToastUtils.showShort("发送继续 测试通道数=" + list.size)
//                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_RESUME_TEST)
//                        DeviceCommand.sendCommandList(commandList, mSendName)

                        val deviceItemBeanList = DeviceService.sDeviceItemBeanList
                        val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_RESUME_TEST)
                        ToastUtils.showShort("发送继续 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    .addStopClickEvent {
                        //                        val list = DeviceService.sDeviceTestChannelList
//                        ToastUtils.showShort("发送停止 测试通道数=" + list.size)
//                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_PAUSE_TEST)
//                        DeviceCommand.sendCommandList(commandList, mSendName)

                        val deviceItemBeanList = DeviceService.sDeviceItemBeanList
                        val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_STOP_TEST)
                        ToastUtils.showShort("发送停止 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    // 通道单独发
                    .addQueryDataClickEvent {
                        val list = DeviceService.sDeviceTestChannelList
                        ToastUtils.showShort("发送查询数据 测试通道数=" + list.size)
                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_QUERY_DATA_TEST)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    .addQueryChannelStatusClickEvent {
                        val list = DeviceService.sDeviceTestChannelList
                        ToastUtils.showShort("发送查询通道状态 测试通道数=" + list.size)
                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_QUERY_CHANNEL_STATUS_TEST)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }

        }
    } // clickevent

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, DeviceService::class.java))
    }

}
