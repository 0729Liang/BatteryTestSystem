package com.liang.batterytestsystem.module.config

import com.liang.liangutils.msg.BusEvent

/**
 * @author : Amarao
 * CreateAt : 8:56 2019/3/16
 * Describe : udp 相关的通知
 *
 */
class UdpEvent : BusEvent() {
    companion object {
        val EVENT_CREATE_NEW_UDP_RECV = "EVENT_CREATE_NEW_UDP_RECV"

        @JvmStatic
        fun postCreateNewUdpRecv() {
            val event = UdpEvent()
            event.msg = EVENT_CREATE_NEW_UDP_RECV
            BusEvent.post(event)
        }
    }
}