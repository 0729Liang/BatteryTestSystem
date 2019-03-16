package com.liang.batterytestsystem.module.service

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.liang.batterytestsystem.base.LBaseService
import com.liang.batterytestsystem.module.config.UdpEvent
import com.liang.batterytestsystem.module.socket.ReceiveUtils
import com.liang.liangutils.utils.LLogX
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

/**
 * @author : Amarao
 * CreateAt : 13:16 2019/2/28
 * Describe : 设备管理类
 *
 */
class DeviceService : LBaseService() {

    val mHandler = MyHandler(this)
    val mRecvName = "接收线程"

    override fun onCreate() {
        super.onCreate()
        ReceiveUtils.receiveMessage(mRecvName)
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

    // 静态Handler
    class MyHandler(service: DeviceService) : Handler() {

        internal var mWeakReference: WeakReference<DeviceService>

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
        val device = DeviceMgr
        fun startService(context: Context) {
            context.startService(Intent(context, DeviceService::class.java))
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        LLogX.e("onBind")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
