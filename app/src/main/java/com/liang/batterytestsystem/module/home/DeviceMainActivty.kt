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
import com.liang.liangutils.utils.LLogX
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

        val a = "7B0000710105060708017B01012515000001250000004E00000195000001BE0000001BC35EC392000D0053005D002B000003A800000385000002A40000006A00000000000001B1000000000000016C0389000003AB0000024C000000000000067D007D"
        val s = "7B0000710205060708017B01010C1400000107000000D4000000AE000000DB000002B1C3A0C38F001A00510003003C000000510000029F0000028D0000022900000000000001C5000000000000000E010B0000011D000000BC000000000000067D007D"
        LLogX.e("length = " + s.length / 2) // lenght1 = 99 1419
        val s2 = "1234"
        LLogX.e("lenght2 = " + s2.length / 2)// lenght2 = 2

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
                    DeviceCreateFactory.createDeviceChannelList(
                            DeviceCommand.CHANNEL_1,
                            DeviceCommand.CHANNEL_2,
                            DeviceCommand.CHANNEL_3,
                            DeviceCommand.CHANNEL_4))
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
            val list = DeviceService.mDeviceTestChannelList
            ToastUtils.showShort("开始测试 测试通道数=" + list.size)
            val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_START_TEST)
            DeviceCommand.sendCommandList(commandList, mSendName)
        }

        mvMain2DeviceMore.setOnClickListener {
            DeviceOperWindow.create(this).show(it)
                    .addTestPauseClickEvent {
                        val list = DeviceService.mDeviceTestChannelList
                        ToastUtils.showShort("发送暂停 测试通道数=" + list.size)
                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_PAUSE_TEST)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    .addTestResumeClickEvent {
                        val list = DeviceService.mDeviceTestChannelList
                        ToastUtils.showShort("发送继续 测试通道数=" + list.size)
                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_RESUME_TEST)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    .addQueryClickEvent {
                        val list = DeviceService.mDeviceTestChannelList
                        ToastUtils.showShort("发送查询 测试通道数=" + list.size)
                        val commandList = DeviceCommand.createDeviceTestCommandList(list, DeviceCommand.COMMAND_QUERY_TEST)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }

        }
    } // clickevent

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, DeviceService::class.java))
    }

}
