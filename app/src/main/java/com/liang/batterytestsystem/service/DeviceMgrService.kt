package com.liang.batterytestsystem.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.liang.liangutils.utils.LLogX
import java.lang.ref.WeakReference

class DeviceMgrService : Service() {
    companion object {
        fun startService(context: Context) {
            context.startService(Intent(context, DeviceMgrService::class.java))
        }
    }

    val mHandler = MyHandler(this)

    override fun onBind(intent: Intent?): IBinder {
        LLogX.e("onBind")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        LLogX.e("onCreate")
        mHandler.sendEmptyMessageDelayed(1, 1000)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LLogX.e("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        LLogX.e("destory")
        mHandler.removeCallbacksAndMessages(null)
    }


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
                service.mHandler.sendEmptyMessageDelayed(1, 1000)
                when (msg.what) {
                }
            }
        }
    }

}
