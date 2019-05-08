package com.liang.batterytestsystem.module.service

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.widget.TextView
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.base.LBaseService
import com.liang.batterytestsystem.module.config.UdpEvent
import com.liang.batterytestsystem.module.data.DeviceDataAnalysisUtils
import com.liang.batterytestsystem.module.device.DeviceStatus
import com.liang.batterytestsystem.module.home.DeviceCommand
import com.liang.batterytestsystem.module.home.DeviceCreateFactory
import com.liang.batterytestsystem.module.home.DeviceItemBean
import com.liang.batterytestsystem.module.item.DeviceItemChannelBean
import com.liang.batterytestsystem.module.socket.ReceiveUtils
import com.liang.batterytestsystem.utils.DigitalTrans
import com.liang.liangutils.utils.LLogX
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference
import java.lang.reflect.Array

/**
 * @author : Amarao
 * CreateAt : 13:16 2019/2/28
 * Describe : 设备管理服务类
 *
 */
class DeviceMgrService : LBaseService() {

    val mHandler = MyHandler(this)
    val mRecvName = "接收线程"


    override fun onCreate() {
        super.onCreate()
        createDevice()
        ReceiveUtils.receiveMessage(mRecvName)
    }

    fun createDevice() {
        //创建设备
        val device1 = DeviceCreateFactory.createDevice(DeviceCommand.DEVICE_1)
        val device2 = DeviceCreateFactory.createDevice(DeviceCommand.DEVICE_2)
        val device3 = DeviceCreateFactory.createDevice(DeviceCommand.DEVICE_3)
        val device4 = DeviceCreateFactory.createDevice(DeviceCommand.DEVICE_4)
        // 设备列表生成
        addDeviceList(device1, device2, device3, device4)

    }


    private fun addDeviceList(vararg deviceList: DeviceItemBean) {
        sDeviceList.addAll(deviceList)
        sDeviceList.forEach {
            it.addChannelList(
                    // 为每台设备添加测试通道
                    DeviceCreateFactory.createDeviceChannelList(it,
                            DeviceCommand.Companion.CHANNEL.CHANNEL_1.channelId,
                            DeviceCommand.Companion.CHANNEL.CHANNEL_2.channelId,
                            DeviceCommand.Companion.CHANNEL.CHANNEL_3.channelId,
                            DeviceCommand.Companion.CHANNEL.CHANNEL_4.channelId,
                            DeviceCommand.Companion.CHANNEL.CHANNEL_5.channelId,
                            DeviceCommand.Companion.CHANNEL.CHANNEL_6.channelId,
                            DeviceCommand.Companion.CHANNEL.CHANNEL_7.channelId,
                            DeviceCommand.Companion.CHANNEL.CHANNEL_8.channelId
                    ))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUdpEvent(event: UdpEvent) {
        when (event.msg) {
            UdpEvent.EVENT_CREATE_NEW_UDP_RECV -> {
                ReceiveUtils.receiveMessage(mRecvName)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDeviceTestEvent(event: DeviceTestEvent) {
        val bean: DeviceItemChannelBean = event.mDeviceItemChannelBean
        when (event.msg) {
            DeviceTestEvent.EVENT_ADD_DEVICE_TEST_CHANNEL -> {

                //添加到设备的内部连接通道List表中
                if (!bean.deviceItemBean.channeChooselList.contains(bean)) {
                    bean.deviceItemBean.channeChooselList.add(bean)
                }

                // 保存设备链表
                if (!sDeviceItemBeanList.contains(bean.deviceItemBean)) {
                    sDeviceItemBeanList.add(bean.deviceItemBean)
                }

                sDeviceTestChannelList.add(bean) // 保存通道链表
                DeviceTestEvent.showDeviceInfo(true, bean)
            }
            DeviceTestEvent.EVENT_REMOVE_DEVICE_TEST_CHANNEL -> {

                // 移除选中设备表
                var count = 0

                bean.deviceItemBean.channeChooselList.forEachIndexed { index, it ->
                    if (it.deviceId == bean.deviceId) {
                        count++
                    }
                }

                if (count == 1) {
                    sDeviceItemBeanList.remove(bean.deviceItemBean)
                }

                //移除设备的内部连接通道List表中的数据
                if (bean.deviceItemBean.channeChooselList.contains(bean)) {
                    bean.deviceItemBean.channeChooselList.remove(bean)
                }

                // 移除选中通道表
                if (sDeviceTestChannelList.contains(bean)) {
                    sDeviceTestChannelList.remove(bean)
                    DeviceTestEvent.showDeviceInfo(false, bean)
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDeviceQueryEvent(event: DeviceQueryEvent) {
        when (event.msg) {
            DeviceQueryEvent.DEVICE_QUERY_CHANNEL_STATUS_RESULT -> {
                val byteArray = event.queryResultByteArray

                // 确定帧头
                if (byteArray!![0] != DeviceCommand.FRAME_HEADER) {
                    return
                }

                // 根据相应的命令，执行不同的操作
                when (byteArray[3]) {
                    DeviceCommand.COMMAND_QUERY_CHANNEL_STATUS_TEST -> updateDeviceChannelStatus(event.queryResultByteArray!!)
                    DeviceCommand.COMMAND_QUERY_DATA_TEST -> {
                        updateDeviceData(event.queryResultByteArray!!)
                    }
                }


            }
            DeviceQueryEvent.TEST -> {
                LLogX.e("xxxxx = " + event.count + " yyyy = " + DigitalTrans.byte2hex(event.queryResultByteArray!![0]))
            }
            else -> {

            }
        }
    }


    /**
     * 功能：更新选中设备的数据，并通知adapter更新
     * 一条通道字节：174/2=87
     * 一条通道命令模板：7B***7D
     * 最多16通道
     * 数据范围：
     *  通道1：10-96，
     *  通道2：97-183、
     *  通道3：184-270
     *  *
     *  *
     *  *
     *  通道x：10+87*x+offset  ~ 96+87*x+offset
     *
     * 通用公式：n(0-14)=10+87*x+offset
     * offset=数据索引-基数索引，范围10-96，基数索引是10，如isSave的索引是11，则offset=11-10=1
     *
     * 常用数据偏移量如下
     * 步时间：offset=53 位数：8位   单位 S    索引：63-70
     * 电流：offset=9   位数：4位   单位：A    索引：19-22
     * 电压：offset=5   位数：4位   单位：V    索引：15-18
     * 功率：offset=13  位数：4位   单位：W    索引：23-26
     * 温度：offset=25  位数：2位   单位：℃    索引：35-36
     * 安时：offset=17  位数：4位   单位：Ah   索引：27-30
     */
    var stepTime: Float = -1f // 步时间S
    var electric: Float = -1f // 电流A
    var voltage: Float = -1f  // 电压V
    var power: Float = -1f    // 功率W
    var temperture: Float = -1f // 温度℃
    var ampereHour: Float = -1f // 安时Ah
    private fun updateDeviceData(byteArray: ByteArray) {
        // 更新所有查询的数据
        LLogX.e("设备号 = " + DigitalTrans.byte2hex(byteArray[4]))
        sDeviceItemBeanList.forEachIndexed { deviceIndex, deviceBean ->
            deviceBean.channeChooselList.forEachIndexed { channelIndex, channelBean ->
                //stepTime = byteArray.get(DeviceDataAnalysisUtils.calcStepTimeIndex(channelIndex))
                byteArray.get(DeviceDataAnalysisUtils.calcStepTimeIndex(channelIndex))

            }
        }
    }


    /**
     * 功能：更新选中设备的状态，并通知adapter更新
     *
     * 1字节命令（0x80） -> 3
     * 1字节设备号  -> 4
     *
     */
    var channelStatus: Byte = -1
    private fun updateDeviceChannelStatus(byteArray: ByteArray) {
        // 更新每条数据的tag标签
        sDeviceItemBeanList.forEachIndexed { deviceIndex, deviceBean ->
            if (deviceBean.deviceId == byteArray[4]) {
                deviceBean.channelList.forEachIndexed { channelIndex, channelBean ->
                    // 共计 15 通道;通道信息位 n(0-14) = 6+x*4
                    // min=6;max=2+15*4=62; 步长 4
                    channelStatus = byteArray.get(DeviceDataAnalysisUtils.calcChannelStatusIndex(channelIndex))

                    when (channelStatus) {
                        DeviceStatus.OFFLINE.statusKey -> {
                            channelBean.deviceStatus = DeviceStatus.OFFLINE
                        }
                        DeviceStatus.ONLINE.statusKey -> {
                            channelBean.deviceStatus = DeviceStatus.ONLINE
                        }
                        DeviceStatus.TESTPAUSE.statusKey -> {
                            channelBean.deviceStatus = DeviceStatus.TESTPAUSE
                        }
                        DeviceStatus.STOP.statusKey -> {
                            channelBean.deviceStatus = DeviceStatus.STOP
                        }
                    }

                    // 更新一台设备数据
                    DeviceQueryEvent.postUpdateNotification(deviceBean.deviceId, channelBean.channelId)
                }
                return
            } // deviceBean.channelList
        } // sDeviceList.forEachIndexed

    }

    // 静态Handler
    class MyHandler(service: DeviceMgrService) : Handler() {

        internal var mWeakReference: WeakReference<DeviceMgrService>

        init {
            mWeakReference = WeakReference(service)
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val service = mWeakReference.get()
            service?.let {
                LLogX.e("1111")
                //service.mHandler.sendEmptyMessageDelayed(1, 1000)
                when (msg.what) {
                }
            }
        }
    }


    companion object {

        val sDeviceList: MutableList<DeviceItemBean> = ArrayList() // 所有的设备
        val sDeviceItemBeanList: MutableList<DeviceItemBean> = ArrayList() // 选中设备
        val sDeviceTestChannelList: MutableList<DeviceItemChannelBean> = ArrayList() // 选中通道

        fun startService(context: Context) {
            context.startService(Intent(context, DeviceMgrService::class.java))
        }

    }


    override fun onBind(intent: Intent): IBinder {
        LLogX.e("onBind")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
