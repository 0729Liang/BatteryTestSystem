package com.liang.batterytestsystem.base

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.liang.liangutils.msg.Exts

/**
 * @author : Amarao
 * CreateAt : 9:18 2019/3/16
 * Describe :
 *
 */
open class LBaseService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        Exts.registerEvent(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Exts.unRegisterEvent(this)
    }

}