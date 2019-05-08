package com.liang.batterytestsystem.module.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.base.LAbstractBaseActivity
import com.liang.batterytestsystem.exts.Router
import com.liang.batterytestsystem.module.device.DeviceStatus
import com.liang.batterytestsystem.module.service.DeviceMgrService
import com.liang.batterytestsystem.module.service.DeviceQueryEvent
import com.liang.batterytestsystem.module.service.DeviceQueryService
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
                        val deviceItemBeanList = DeviceMgrService.sDeviceItemBeanList
                        val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_PAUSE_TEST, true)
                        ToastUtils.showShort("发送暂停 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    .addTestResumeClickEvent {
                        val deviceItemBeanList = DeviceMgrService.sDeviceItemBeanList
                        val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_RESUME_TEST, true)
                        ToastUtils.showShort("发送继续 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    .addStopClickEvent {

                        val deviceItemBeanList = DeviceMgrService.sDeviceItemBeanList
                        val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_STOP_TEST, true)
                        ToastUtils.showShort("发送停止 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    // 通道单独发
                    .addQueryDataClickEvent {

                        // 查询数据 只管设备号，
                        val deviceItemBeanList = DeviceMgrService.sDeviceItemBeanList
                        val commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.COMMAND_QUERY_DATA_TEST, false)
                        ToastUtils.showShort("发送查询数据 设备数 =" + deviceItemBeanList.size + " 命令数 = " + commandList.size)
                        DeviceCommand.sendCommandList(commandList, mSendName)
                    }
                    .addQueryChannelStatusClickEvent {
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
            DeviceQueryEvent.DEVICE_DATA_UPDATE_CHANNEL_STATUS_NOTIFICATION -> {
                // 更新指定设备
                DeviceMgrService.sDeviceItemBeanList.forEachIndexed { deviceIndex, deviceBean ->
                    if (deviceBean.deviceId == event.deviceId) { // 同一设备
                        deviceBean.channelList.forEachIndexed { channelIndex, channelBean ->
                            if (channelBean.channelId == event.channelId) { // 同一通道
                                deviceBean.channelAdapter.notifyItemChanged(channelIndex)
                            }// if
                        }
                    }//if
                }
            }

            DeviceQueryEvent.DEVICE_DATA_UPDATE_DATA_NOTIFICATION -> {
                DeviceMgrService.sDeviceItemBeanList.forEachIndexed { deviceIndex, deviceBean ->
                    if (deviceBean.deviceId == event.deviceId) { // 同一设备
                        deviceBean.channelList.forEachIndexed { channelIndex, channelBean ->
                            if (channelBean.channelId == event.channelId) { // 同一通道
                                //LLogX.e("channelIndex = " + channelIndex + " id = " + DigitalTrans.byte2hex(event.channelId) + " toIndex = " + DigitalTrans.byte2hex(DeviceMgrService.getChannelIndex(event.channelId)) + " toInt " + DeviceMgrService.getChannelIndex(event.channelId).toInt())

                                if (channelBean.deviceStatus == DeviceStatus.ONLINE) {
                                    deviceBean.channelAdapter.notifyItemChanged(channelIndex)

                                }

                            }// if
                        }
                    }// if
                }
            }
            else -> {
            }
        }// when
    } // onDeviceQueryEvent
}
