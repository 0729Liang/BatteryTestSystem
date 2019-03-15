package com.liang.batterytestsystem.module.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.liang.batterytestsystem.module.socket.ReceiveUtils
import com.liang.liangutils.utils.LLogX
import java.lang.ref.WeakReference

/**
 * @author : Amarao
 * CreateAt : 13:16 2019/2/28
 * Describe : 设备管理类
 *
 */
class DeviceService : Service() {

    val mHandler = MyHandler(this)


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ReceiveUtils.receiveMessage()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

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
