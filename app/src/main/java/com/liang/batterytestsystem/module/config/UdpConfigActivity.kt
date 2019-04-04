package com.liang.batterytestsystem.module.config

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.base.LAbstractBaseActivity
import com.liang.batterytestsystem.exts.Router
import com.liang.batterytestsystem.module.service.DeviceService
import com.liang.batterytestsystem.utils.LNetwork
import com.liang.liangutils.init.LCommon
import kotlinx.android.synthetic.main.activity_udp_config.*

class UdpConfigActivity : LAbstractBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_udp_config)

        initData()
        initView()
        clicEvent()
    }

    override fun initData() {
    }


    override fun initView() {
        val ipAddress = LNetwork.getIPAddress(LCommon.getContext())
        //var substring = ipAddress.substring(0, ipAddress.lastIndexOf(".") + 1)

        mvConfigClientIpEt.setText(ipAddress)
        mvConfigSendPortEt.setText(UdpInfoStorage.getClientSendPort().toString())
        mvConfigServerIpEt.setText(ipAddress)
        mvConfigListenPortEt.setText(UdpInfoStorage.getClientListenPort().toString())
        mvConfigServerIpEt.requestFocus() // 申请焦点


        mvConfigTitle.setTitle("一键配置")
        mvConfigTitle.setClickLeftFinish(this)
        mvConfigTitle.setRightText("保存")

        mvConfigTitle.rightTextView.setOnClickListener {
            storageUdpInfo(mvConfigClientIpEt.text.toString(),
                    Integer.valueOf(mvConfigSendPortEt.text.toString()),
                    mvConfigServerIpEt.text.toString(),
                    Integer.valueOf(mvConfigListenPortEt.text.toString()))
            ToastUtils.showShort("保存成功")
            UdpEvent.postCreateNewUdpRecv()
            //this.finish()
            Router.startDeviceDetail(this, DeviceService.mDeviceTestChannelList.get(0))
        }
    }

    override fun clicEvent() {

    }

    private fun storageUdpInfo(clientIp: String?, clientSendPort: Int, serverIp: String, clienListentPort: Int) {
        UdpInfoStorage.setClientIp(clientIp)
        UdpInfoStorage.setClientSendPort(clientSendPort)
        UdpInfoStorage.setServerIp(serverIp)
        UdpInfoStorage.setClientListenPort(clienListentPort)
    }

    companion object {
        @JvmStatic
        fun startActivity(context: Context) {
            val intent = Intent(context, UdpConfigActivity::class.java)
            context.startActivity(intent)
        }
    }
}
