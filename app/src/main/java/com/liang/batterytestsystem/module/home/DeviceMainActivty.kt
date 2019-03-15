package com.liang.batterytestsystem.module.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.base.LAbstractBaseActivity
import com.liang.batterytestsystem.constant.DeviceCommand
import com.liang.batterytestsystem.exts.Router
import com.liang.batterytestsystem.module.item.DeviceItemChannelBean
import com.liang.batterytestsystem.module.service.DeviceService
import com.liang.batterytestsystem.module.socket.SendUtils
import com.liang.batterytestsystem.view.DeviceOperWindow
import kotlinx.android.synthetic.main.activity_device_main_activty.*

class DeviceMainActivty : LAbstractBaseActivity() {

    val mDeviceList: MutableList<DeviceItemBean> = ArrayList()
    val mDeviceChannelList: MutableList<DeviceItemChannelBean> = ArrayList()
    val mAdapter = DeviceItemAdapter(mDeviceList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_main_activty)

        Router.startDeviceMgrService(this)


        initData()
        initView()
        clicEvent()


    }

    override fun initData() {
        // 创建通道
        val channel1 = DeviceCreateFactory.createDeviceChannel(DeviceCommand.CHANNEL_1)
        val channel2 = DeviceCreateFactory.createDeviceChannel(DeviceCommand.CHANNEL_2)
        val channel3 = DeviceCreateFactory.createDeviceChannel(DeviceCommand.CHANNEL_3)
        val channel4 = DeviceCreateFactory.createDeviceChannel(DeviceCommand.CHANNEL_4)
        //创建设备
        val device1 = DeviceCreateFactory.createDevice(DeviceCommand.DEVICE_1)
        val device2 = DeviceCreateFactory.createDevice(DeviceCommand.DEVICE_2)
        val device3 = DeviceCreateFactory.createDevice(DeviceCommand.DEVICE_3)
        val device4 = DeviceCreateFactory.createDevice(DeviceCommand.DEVICE_4)
        // 通道列表生成
        mDeviceChannelList.add(channel1)
        mDeviceChannelList.add(channel2)
        mDeviceChannelList.add(channel3)
        mDeviceChannelList.add(channel4)
        // 设备添加通道
        device1.addChannelList(mDeviceChannelList)
        device2.addChannelList(mDeviceChannelList)
        device3.addChannelList(mDeviceChannelList)
        device4.addChannelList(mDeviceChannelList)
        // 设备列表生成
        mDeviceList.add(device1)
        mDeviceList.add(device2)
        mDeviceList.add(device3)
        mDeviceList.add(device4)
    }

    override fun initView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mvMain2RecycleView.layoutManager = layoutManager
        mvMain2RecycleView.adapter = mAdapter
    }

    override fun clicEvent() {
        // chexbox点击
        mAdapter.setOnItemChildClickListener { adapter, view, position ->

        }

        // 全部/取消 选择按钮
        mvMain2ChooseAll.setOnClickListener {

        }

        // 连接按钮
        mvMain2ConnectBtn.setOnClickListener {


        }

        mvMain2ConfigTest.setOnClickListener {
            Router.startUdpConfig(this)
        }

        mvMain2TestStart.setOnClickListener {
            ToastUtils.showShort("开始 测试")
            SendUtils.sendCommand(DeviceCommand.createCommandStartTest(DeviceCommand.DEVICE_1, DeviceCommand.CHANNEL_1))
        }

        mvMain2DeviceMore.setOnClickListener {
            DeviceOperWindow.create(this).show(it)
                    .addTestPauseClickEvent {
                        ToastUtils.showShort("发送 暂停")
                        SendUtils.sendCommand(DeviceCommand.createCommandPauseTest(DeviceCommand.DEVICE_1, DeviceCommand.CHANNEL_1))

                    }
                    .addTestStopClickEvent {
                        ToastUtils.showShort("发送 继续")
                        SendUtils.sendCommand(DeviceCommand.createCommandResumeTest(DeviceCommand.DEVICE_1, DeviceCommand.CHANNEL_1))
                    }
                    .addDisconnectClickEvent {
                        ToastUtils.showShort("发送 查询")
                        val msg = byteArrayOf(0x7B, 0x00, 0x08, 0x71.toByte(), 0x01, 0x02, 0x00, 0x7D)

                        SendUtils.sendCommand(msg)
                    }

        }
    } // clickevent

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, DeviceService::class.java))
    }

}
