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
                            DeviceCommand.CHANNEL_1, DeviceCommand.CHANNEL_2, DeviceCommand.CHANNEL_3, DeviceCommand.CHANNEL_4,
                            DeviceCommand.CHANNEL_5, DeviceCommand.CHANNEL_6, DeviceCommand.CHANNEL_7, DeviceCommand.CHANNEL_8
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

    var stepTime: Float = 0f // 步时间S
    var electric: Float = 0f // 电流A
    var voltage: Float = 0f  // 电压V
    var power: Float = 0f    // 功率W
    var temperture: Float = 0f // 温度℃
    var ampereHour: Float = 0f // 安时Ah

    /**
     * 功能：更新选中设备的数据，并通知adapter更新
     *
     */
    private fun updateDeviceData(byteArray: ByteArray) {
        // 更新所有查询的数据
        LLogX.e("设备号 = " + DigitalTrans.byte2hex(byteArray[4]))
        sDeviceItemBeanList.forEachIndexed { deviceIndex, deviceBean ->

            deviceBean.channeChooselList.forEachIndexed { channelIndex, channelBean ->

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
    private fun updateDeviceChannelStatus(byteArray: ByteArray) {
        // 更新每条数据的tag标签
        sDeviceItemBeanList.forEachIndexed { deviceIndex, deviceBean ->
            if (deviceBean.deviceId == byteArray[4]) {
                deviceBean.channelList.forEachIndexed { channelIndex, channelBean ->
                    // 共计 15 通道;通道信息位 n(0-14) = 6+x*4
                    // min=6;max=2+15*4=62; 步长 4
                    val status = byteArray.get(channelIndex * 4 + 6)
                    when (status) {
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
