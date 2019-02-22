package com.liang.batterytestsystem.module.connect

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.CheckBox
import android.widget.PopupWindow
import com.blankj.utilcode.util.ToastUtils
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.base.LAbstractBaseActivity
import com.liang.batterytestsystem.device.*
import com.liang.liangutils.mgrs.LKVMgr
import com.liang.liangutils.utils.LResourceX
import kotlinx.android.synthetic.main.activity_device_connect.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DeviceConnect : LAbstractBaseActivity() {

    private val mDataBinding: DeviceDataBinding = DeviceDataBinding()
    private val mAdapter: DeviceConnectAdapter = DeviceConnectAdapter(mDataBinding.mDeviceBeanList)

    private var mFlagChooseAll = true  //是否全部被选中
    lateinit var mDevideInfoWindow: PopupWindow
    lateinit var mTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_connect)

        initData()
        initView()
        clicEvent()


    }

    override fun initData() {
        val list = LKVMgr.memory().getList(DeviceKey.KEY_MAIN_CHECKED_DEVICE, DeviceBean::class.java)
        list?.let {
            mDataBinding.mDeviceBeanList.addAll(list)
        }
        mDataBinding.mDeviceBeanList.forEachIndexed { index, bean ->
            if (bean.deviceStatus == DeviceStatus.OFFLINE) {
                conenctService(bean)
                // 未连接的发送连接通知
                bean.deviceStatus = DeviceStatus.CONNECTING
                DeviceEvent.postStartConnect(bean.deviceSerialNumber)
                // todo 模拟连接成功
                Handler().postDelayed({
                    bean.deviceStatus = DeviceStatus.ONLINE
                    DeviceEvent.postConnectSussessDeviceObj(bean)
                }, (index + 1) * 2000L)
            } else if (bean.deviceStatus != DeviceStatus.CONNECTING) {
                // 已经连接的直接发送
                bean.deviceStatus = DeviceStatus.ONLINE
                DeviceEvent.postConnectSussessDeviceObj(bean)
            }
        }
    }

    override fun initView() {
        mvConnectTitleView.setClickLeftFinish(this)
        mvConnectTitleView.setTitle("在线设备")
        mvConnectTitleView.lineView.visibility = View.VISIBLE

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mvConnectRecycleView.layoutManager = layoutManager
        mvConnectRecycleView.adapter = mAdapter

        initChooseAllBtn(mFlagChooseAll)
    }

    override fun clicEvent() {
        mvConnectChooseAll.setOnClickListener {
            mDataBinding.mDeviceBeanList.forEach {
                it.checkStatus = !mFlagChooseAll
            }
            initChooseAllBtn(!mFlagChooseAll)
        }


//        // chexbox点击
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val checkBox = adapter.getViewByPosition(mvConnectRecycleView, position, R.id.mvItemDeviceCheckbox) as CheckBox
            val bean = mDataBinding.mDeviceBeanList.get(position)
            bean.checkStatus = checkBox.isChecked
            ToastUtils.showShort("This is status = " + bean.checkStatus)

            initChooseAllBtn(mDataBinding.getCheckedDeviceList().size == mDataBinding.mDeviceBeanList.size)
        }

        mAdapter.setOnItemLongClickListener { adapter, view, position ->

            false
        }


    }

    // WebSocket 连接服务器
    private fun conenctService(bean: DeviceBean) {

    }

    // 全部选中
    fun initChooseAllBtn(allChoosed: Boolean) {
        val drawable: Drawable
        mFlagChooseAll = allChoosed
        if (allChoosed) {
            mvConnectChooseAll.text = "取消选择"
            drawable = LResourceX.getDrawable(this, R.drawable.icon_choose_all)
        } else {
            mvConnectChooseAll.text = "选择全部"
            drawable = LResourceX.getDrawable(this, R.drawable.icon_not_choose_all)
        }
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        mvConnectChooseAll.setCompoundDrawables(null, drawable, null, null)
        mAdapter.notifyDataSetChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDeviceEvent(event: DeviceEvent) {
        when (event.msg) {
            DeviceEvent.EVENT_START_CONNECT -> {
                mDataBinding.mDeviceBeanList.forEachIndexed { index, bean ->
                    if (bean.deviceSerialNumber.equals(event.serialNumber)) {
                        bean.deviceStatus = DeviceStatus.CONNECTING
                        mAdapter.notifyItemChanged(index)
                    }
                } // forEach
            }
            DeviceEvent.EVENT_CONNECTED_OBJ -> {
                mDataBinding.mDeviceBeanList.forEachIndexed { index, bean ->
                    if (bean.deviceSerialNumber.equals(event.deviceBean?.deviceSerialNumber)) {
                        bean.deviceStatus = DeviceStatus.ONLINE
                        mAdapter.notifyItemChanged(index)
                    }
                } // forEach

            }
            DeviceEvent.EVENT_HIDE_DEVICE_INFO_WINDOW -> {
                if (mDevideInfoWindow.isShowing) {
                    mDevideInfoWindow.dismiss()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, DeviceConnect::class.java)
            activity.startActivity(intent)
        }
    }


}
