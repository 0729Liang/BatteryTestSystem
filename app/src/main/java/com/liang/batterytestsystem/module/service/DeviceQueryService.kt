package com.liang.batterytestsystem.module.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.blankj.utilcode.util.ToastUtils
import com.liang.batterytestsystem.base.LBaseService
import com.liang.batterytestsystem.module.home.DeviceCommand
import com.liang.liangutils.utils.LLogX
import java.lang.ref.WeakReference


/**
 * @author : Amarao
 * CreateAt : 13:16 2019/2/28
 * Describe : 设备查询服务类，主要负责轮询查询各个设备状态
 *
 */
class DeviceQueryService : LBaseService() {

    private val mSendName: String = " 轮询服务类： "
    private val mHandler: QueryHandler = QueryHandler(this)


    override fun onCreate() {
        super.onCreate()

        postHandlerQueryAllDeviceChannelStatus()

    }


    // 网络发送查询数据命令
    fun sendQueryDeviceChannelData() {
        // 查询数据 只管设备号，
        val commandList = DeviceCommand.createDeviceTestComposeCommandList(
                DeviceMgrService.sDeviceItemBeanList,
                DeviceCommand.COMMAND_QUERY_DATA_TEST,
                false)
        DeviceCommand.sendCommandList(commandList, mSendName)
    }

    // handler 发送查询所有设备的通道状态命令
    fun postHandlerQueryAllDeviceChannelStatus() {
        if (!mHandler.hasMessages(QUERY_ALL_DEVICE_CHANNEL_STATUS)) {
            mHandler.sendEmptyMessageDelayed(QUERY_ALL_DEVICE_CHANNEL_STATUS, 3000)
        }
    }

    // 网络发送查询所有设备的通道状态命令
    fun sendQueryAllDeviceChannelStatus() {
        val commandList = DeviceCommand.createDeviceTestComposeCommandList(
                DeviceMgrService.sDeviceList,
                DeviceCommand.COMMAND_QUERY_CHANNEL_STATUS_TEST,
                false)
        DeviceCommand.sendCommandList(commandList, mSendName)
    }

    // 静态Handler
    class QueryHandler(service: DeviceQueryService) : Handler() {

        var mWeakReference: WeakReference<DeviceQueryService>

        init {
            mWeakReference = WeakReference(service)
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val service = mWeakReference.get()
            service?.let {
                when (msg.what) {
                    QUERY_ALL_DEVICE_CHANNEL_STATUS -> {
                        service.sendQueryAllDeviceChannelStatus()
                        //service.mHandler.sendEmptyMessageDelayed(QUERY_ALL_DEVICE_CHANNEL_STATUS, 3000)
                        service.postHandlerQueryAllDeviceChannelStatus()
                    }
                    QUERY_CHOOSE_DEVICE_CHANNEL_DATA -> {
                        service.sendQueryDeviceChannelData()
                        service.mHandler.sendEmptyMessageDelayed(QUERY_CHOOSE_DEVICE_CHANNEL_DATA, 3000)
                    }
                    else -> {
                        LLogX.e("未匹配到查询命令")
                    }

                }
            }
        }
    }

    companion object {

        val QUERY_ALL_DEVICE_CHANNEL_STATUS = 0
        val QUERY_CHOOSE_DEVICE_CHANNEL_DATA = 1

        fun startService(context: Context) {
            context.startService(Intent(context, DeviceQueryService::class.java))
        }

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
