package com.liang.batterytestsystem.module.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.format.DateFormat
import android.view.View
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.base.LAbstractBaseActivity
import com.liang.batterytestsystem.constant.DeviceKey
import com.liang.batterytestsystem.device.DeviceEvent
import com.liang.batterytestsystem.device.DeviceStatus
import com.liang.batterytestsystem.module.item.DeviceItemChannelBean
import com.liang.batterytestsystem.utils.DigitalTrans
import com.liang.liangutils.mgrs.LKVMgr
import com.liang.liangutils.utils.LLogX
import com.liang.liangutils.utils.LResourceX
import com.liang.liangutils.utils.LSizeX
import kotlinx.android.synthetic.main.activity_device_details.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


class DeviceDetails : LAbstractBaseActivity() {

    lateinit var mChannelBean: DeviceItemChannelBean
    val MSG_QUERY_DEVICE = "7B00088F0102007D"
    private val MSG_QUERY_DEVICE2 = byteArrayOf(0x7B, 0x00, 0x08, 0x8F.toByte(), 0x01, 0x02, 0x00, 0x7D)

    // 假数据
    val mEntries: MutableList<Entry> = ArrayList()
    val mList = listOf(3f, 2.2f).toMutableList()
    val mLineDataSet = LineDataSet(mEntries, "电池测试数据表")
    val mLineData = LineData(mLineDataSet)
    val mHandler: MyHandlerCreateData = MyHandlerCreateData(this)
    var mXMax = 0f
    var mYMax = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_details)


        initData()
        initView()
        initLineChart()
        clicEvent()

    }

    override fun initData() {
        mChannelBean = LKVMgr.memory().getObj(DeviceKey.KEY_DETAIL_INFO, DeviceItemChannelBean::class.java)
    }

    override fun initView() {
        val title = "设备" + DigitalTrans.byte2hex(byteArrayOf(mChannelBean.deviceId)) + "-通道" + DigitalTrans.byte2hex(byteArrayOf(mChannelBean.channelId))
        mvDetailTitle.setClickLeftFinish(this)
        mvDetailTitle.setTitle(title)
        mvDetailTitle.lineView.visibility = View.VISIBLE
        mvDetailTitle.setRightText("停止")
        mvDetailStatus.text = mChannelBean.deviceStatus.statusName

        when (mChannelBean.deviceStatus) {
            DeviceStatus.OFFLINE -> mvDetailConnect.text = "连接"
            DeviceStatus.CONNECTING -> mvDetailConnect.text = "连接中"
            else -> mvDetailConnect.text = "断开连接"
        }

        when (mChannelBean.deviceStatus) {
            DeviceStatus.TESTPAUSE -> mvDetailTest.text = "继续测试"
            DeviceStatus.TESTING -> mvDetailTest.text = "暂停测试"
            else -> mvDetailTest.text = "开始测试"
        }
    }

    override fun clicEvent() {
        mvDetailTitle.rightTextView.setOnClickListener {
            mHandler.sendEmptyMessage(1)
        }
        mvDetailConnect.setOnClickListener {

        }
        mvDetailTest.setOnClickListener {

        }
    }

    // 初始化图表
    private fun initLineChart() {
        /*********************** 边界、背景等外观*******************************/
        mvDetailLineChart.setDrawBorders(true)
        mvDetailLineChart.setBorderWidth(LSizeX.dp2px(1f).toFloat())
        mvDetailLineChart.setBorderColor(LResourceX.getColor(this, R.color.text_gray))
        mvDetailLineChart.setBackgroundColor(LResourceX.getColor(this, R.color.white))
        mvDetailLineChart.setGridBackgroundColor(LResourceX.getColor(this, R.color.gray_bg))
        mvDetailLineChart.setVisibleXRangeMaximum(3f)   // 设定x轴最大可见区域范围的大小。设定为3，x轴超过3的值不滑动 chart不可见
        //设置一页最大显示个数为6，超出部分就滑动
        val ratio = mList.size / 6f
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        mvDetailLineChart.zoom(ratio, 1f, 0f, 0f)
        //设置从X轴出来的动画时间
        //mLineChart.animateX(1500);
        //设置XY轴动画
        mvDetailLineChart.animateXY(1500, 1500, Easing.EasingOption.EaseInSine, Easing.EasingOption.EaseInSine)
        //mvDetailLineChart.animateX(2500, Easing.EasingOption.EaseInSine);

        /****************************说明、提示，图例**********************************/
        val description = Description()
        description.text = "电池测试"
        mvDetailLineChart.description = description
        mvDetailLineChart.setNoDataText("暂无数据")
        val legend = mvDetailLineChart.legend   //图例：得到Lengend
        legend.isEnabled = true //显示Lengend

        /*****************************设置数据**************************************/
        //一个LineDataSet就是一条线
        mLineDataSet.lineWidth = 1.6f //线宽度
        mLineDataSet.setDrawCircles(true) //显示圆点
        mLineDataSet.setDrawFilled(true) //设置折线图填充
        mLineDataSet.isHighlightEnabled = true // 选中高亮
        mLineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        mLineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER //线条平滑
        mLineDataSet.color = LResourceX.getColor(this, R.color.colorPrimary) //线颜色
        mLineDataSet.highLightColor = LResourceX.getColor(this, R.color.redPrimary) // 设置点击某个点时，横竖两条线的颜色

        mLineData.setDrawValues(true) //折线图显示数值
        mvDetailLineChart.setNoDataText("暂无数据") //无数据时显示的文字
        mvDetailLineChart.data = mLineData   //设置数据

        /********************坐标轴***********************/
        val xAxis = mvDetailLineChart.xAxis //得到X轴
        xAxis.setDrawGridLines(true)//显示网格线
        xAxis.labelRotationAngle = 45f // 标签倾斜
        xAxis.labelCount = 6//一个界面显示6个Lable
        xAxis.position = XAxis.XAxisPosition.BOTTOM //设置X轴的位置（默认在上方)
        xAxis.axisMinimum = 0f  //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
        //xAxis.axisMaximum = mList.size.toFloat()
        xAxis.granularity = 1f //设置X轴坐标之间的最小间隔
        xAxis.setLabelCount(mList.size, false)   //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），但是可能值导致不均匀，默认（false）
        //设置X轴值为字符串
        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
            val IValue = value.toInt()
            val format = DateFormat.format("MM/dd", System.currentTimeMillis() - (mList.size - IValue).toLong() * 24 * 60 * 60 * 1000)
            val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Date())
//            LLogX.e(time + " value = " + value)
            value.toString()
        }

        //得到Y轴
        val yAxis = mvDetailLineChart.axisLeft
        val rightYAxis = mvDetailLineChart.axisRight
        rightYAxis.isEnabled = false //右侧Y轴不显示
        yAxis.setDrawGridLines(true)//显示网格线
        yAxis.axisMinimum = 0f //设置从Y轴值
        //yAxis.axisMaximum = (Collections.max(mList) + 1) //+1:y轴多一个单位长度，为了好看
        yAxis.granularity = 1f //设置Y轴坐标之间的最小间隔
        yAxis.setLabelCount(Collections.max(mList).toInt() + 2, false)   //设置y轴的刻度数量,+2：最大值n就有n+1个刻度，在加上y轴多一个单位长度，为了好看，so+2
        //设置Y轴值为字符串
        yAxis.valueFormatter = IAxisValueFormatter { value, axis ->
            value.toString()
        }

        var i = 0
        while (i < mList.size) {
            val entry = Entry(i.toFloat(), mList[i])
            mEntries.add(entry)
            mLineDataSet.addEntry(entry)
            i++
            //图标刷新
            mvDetailLineChart.notifyDataSetChanged() //通知数据已经改变
            mvDetailLineChart.invalidate()   // 刷新
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    // 静态Handler
    class MyHandlerCreateData(details: DeviceDetails) : Handler() {

        internal var mWeakReference: WeakReference<DeviceDetails>
        val random = Random(7)

        init {
            mWeakReference = WeakReference(details)
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val details = mWeakReference.get()

            details?.let {
                val data = random.nextFloat() * 10
                LLogX.e("随机数 random = " + data)
                it.addEntry(data)
                sendEmptyMessageDelayed(1, 500)
            }
        }
    }

    fun addEntry(number: Float) {
        //最开始的时候才添加lineDataSet（一个lineDataSet代表一条线）
        if (mLineDataSet.entryCount == 0) {
            mLineData.addDataSet(mLineDataSet)
        }

        mvDetailLineChart.data = mLineData
        mLineData.addEntry(Entry(mLineDataSet.entryCount - 1f, number), 0)// 添加新数据
        //通知数据已经改变
        mLineData.notifyDataChanged() //通知数据已经改变
        mvDetailLineChart.notifyDataSetChanged() // 刷新
        //设置在曲线中显示的最大数据量
        mvDetailLineChart.setVisibleXRangeMaximum(30f)
        mvDetailLineChart.moveViewToX(mLineData.entryCount - 5f)         //移到某个位置

//                //设置在曲线中显示的最大数据量
//                it.mvDetailLineChart.setVisibleXRangeMaximum(5f);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDeviceEvent(event: DeviceEvent) {
//        if (!mChannelBean.deviceId.equals(event.serialNumber)) {
//            return
//        }
        when (event.msg) {
            DeviceEvent.EVENT_START_DISCONNECT -> {
                mChannelBean.deviceStatus = DeviceStatus.OFFLINE
            }
            DeviceEvent.EVENT_START_CONNECT -> {
                mChannelBean.deviceStatus = DeviceStatus.CONNECTING
            }
            DeviceEvent.EVENT_CONNECTED_OBJ -> {
                mChannelBean.deviceStatus = DeviceStatus.ONLINE
            }
            DeviceEvent.EVENT_START_TEST -> {
                mChannelBean.deviceStatus = DeviceStatus.TESTING
            }
            DeviceEvent.EVENT_PAUSE_TEST -> {
                mChannelBean.deviceStatus = DeviceStatus.TESTPAUSE
            }
            DeviceEvent.EVENT_STOP_TEST -> {
            }
        }// when
        initView()
    }

    companion object {
        @JvmStatic
        fun startActivity(context: Context, bean: DeviceItemChannelBean) {
            val intent = Intent(context, DeviceDetails::class.java)
            LKVMgr.memory().putObj(DeviceKey.KEY_DETAIL_INFO, bean)
            context.startActivity(intent)
        }
    }
}
