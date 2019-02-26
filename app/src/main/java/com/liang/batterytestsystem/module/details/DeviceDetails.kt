package com.liang.batterytestsystem.module.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.liang.batterytestsystem.R
import com.liang.batterytestsystem.base.LAbstractBaseActivity
import com.liang.batterytestsystem.constant.DeviceKey
import com.liang.batterytestsystem.device.DeviceBean
import com.liang.batterytestsystem.device.DeviceStatus
import com.liang.liangutils.mgrs.LKVMgr
import com.liang.liangutils.utils.LResourceX
import com.liang.liangutils.utils.LSizeX
import kotlinx.android.synthetic.main.activity_device_details.*
import java.text.SimpleDateFormat
import java.util.*


class DeviceDetails : LAbstractBaseActivity() {

    lateinit var deviceBean: DeviceBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_details)

        initData()
        initView()
        clicEvent()
    }

    override fun initData() {
        deviceBean = LKVMgr.memory().getObj(DeviceKey.KEY_DETAIL_INFO, DeviceBean::class.java)
    }

    override fun initView() {
        mvDetailTitle.setClickLeftFinish(this)
        mvDetailTitle.setTitle(deviceBean.deviceSerialNumber)
        mvDetailTitle.lineView.visibility = View.VISIBLE
        mvDetailTitle.setRightText("停止")

        when (deviceBean.deviceStatus) {
            DeviceStatus.OFFLINE -> mvDetailConnect.text = "连接"
            DeviceStatus.CONNECTING -> {
                mvDetailConnect.text = "连接中"
            }
            else -> {
                mvDetailConnect.text = "断开连接"
            }
        }

        if (deviceBean.deviceStatus == DeviceStatus.TESTPAUSE) {
            mvDetailTest.text = "继续测试"
        } else if (deviceBean.deviceStatus == DeviceStatus.TESTING) {
            mvDetailTest.text = "暂停测试"
        } else {
            mvDetailTest.text = "开始测试"
        }

        mvDetailLineChart.setBackgroundColor(LResourceX.getColor(this, R.color.white))
        mvDetailLineChart.setGridBackgroundColor(LResourceX.getColor(this, R.color.gray_bg))
        val description = Description()
        description.text = "电池测试"
        mvDetailLineChart.description = description
        mvDetailLineChart.setNoDataText("暂无数据")
        mvDetailLineChart.setDrawBorders(true)
        mvDetailLineChart.setBorderColor(LResourceX.getColor(this, R.color.text_gray))
        mvDetailLineChart.setBorderWidth(LSizeX.dp2px(1f).toFloat())

        val list = listOf<Int>(1, 2, 3)

        //设置数据
        val entries: MutableList<Entry> = ArrayList()
        var i = 0
        while (i < list.size) {
            entries.add(Entry(i.toFloat(), list[i].toFloat()))
            i++
        }
        //一个LineDataSet就是一条线
        val lineDataSet = LineDataSet(entries, "")
        lineDataSet.color = LResourceX.getColor(this, R.color.colorPrimary) //线颜色
        lineDataSet.lineWidth = 1.6f //线宽度
        lineDataSet.setDrawCircles(true) //显示圆点
        lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER //线条平滑
        lineDataSet.setDrawFilled(true) //设置折线图填充
        val data = LineData(lineDataSet)
        mvDetailLineChart.setNoDataText("暂无数据") //无数据时显示的文字
        data.setDrawValues(true) //折线图显示数值

        /********************坐标轴***********************/
        val xAxis = mvDetailLineChart.xAxis //得到X轴
        xAxis.position = XAxis.XAxisPosition.BOTTOM //设置X轴的位置（默认在上方)
        xAxis.granularity = 1f //设置X轴坐标之间的最小间隔
        //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），但是可能值导致不均匀，默认（6，false）
        xAxis.setLabelCount(list.size, false)
        //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
        xAxis.axisMinimum = 0f
        xAxis.axisMaximum = list.size.toFloat()
        xAxis.setDrawGridLines(true)//显示网格线
        xAxis.labelRotationAngle = 45f // 标签倾斜
        //设置X轴值为字符串
        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
            val IValue = value.toInt()
            val format = DateFormat.format("MM/dd", System.currentTimeMillis() - (list.size - IValue).toLong() * 24 * 60 * 60 * 1000)
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Date())
            value.toString()
        }

        //得到Y轴
        val yAxis = mvDetailLineChart.axisLeft
        val rightYAxis = mvDetailLineChart.axisRight
        rightYAxis.isEnabled = false //右侧Y轴不显示
        yAxis.setDrawGridLines(true)//显示网格线
        yAxis.granularity = 1f //设置Y轴坐标之间的最小间隔
        //设置y轴的刻度数量
        //+2：最大值n就有n+1个刻度，在加上y轴多一个单位长度，为了好看，so+2
        yAxis.setLabelCount(Collections.max(list) + 2, false)
        yAxis.axisMinimum = 0f //设置从Y轴值
        yAxis.axisMaximum = (Collections.max(list) + 1).toFloat() //+1:y轴多一个单位长度，为了好看
        //y轴
        yAxis.valueFormatter = IAxisValueFormatter { value, axis ->
            val IValue = value.toInt()
            IValue.toString()
        }

        //图例：得到Lengend
        val legend = mvDetailLineChart.legend
        //隐藏Lengend
        legend.isEnabled = false
        //设置数据
        mvDetailLineChart.data = data
        //图标刷新
        mvDetailLineChart.invalidate()


    }

    override fun clicEvent() {
    }

    companion object {
        @JvmStatic
        fun startActivity(context: Context, bean: DeviceBean) {
            val intent = Intent(context, DeviceDetails::class.java)
            LKVMgr.memory().putObj(DeviceKey.KEY_DETAIL_INFO, bean)
            context.startActivity(intent)
        }
    }
}
