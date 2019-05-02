package com.liang.batterytestsystem.module.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.liang.batterytestsystem.R;
import com.liang.batterytestsystem.utils.LColor;
import com.liang.liangutils.utils.LLogX;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewDeviceDetails extends AppCompatActivity implements View.OnClickListener {

    public static final int MSG_ADD_DATASET = 1;
    private Button mBtnClick;


    private LineChart mLineChart; // 折线表，存线集合
    private LineData mLineData; // 线集合，所有折现以数组的形式存到此集合中
    private LineDataSet mLineDataSet; // 点集合,即一条线

    private XAxis mXAxis; //X轴
    private YAxis mLeftYAxis; //左侧Y轴
    private YAxis mRightYAxis; //右侧Y轴
    private Legend mLegend; //图例
    private LimitLine mLimitline; //限制线

    //  private MyMarkerView markerView; //标记视图 即点击xy轴交点时弹出展示信息的View 需自定义

    private DetailHandler mDetailHandler = new DetailHandler(NewDeviceDetails.this);

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, NewDeviceDetails.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp_test);
        mBtnClick = findViewById(R.id.btn_click);
        mLineChart = findViewById(R.id.chart);
        mBtnClick.setOnClickListener(this);

        initChart(mLineChart);

        List<Float> list = new ArrayList<>();
        list.add(1f);
        list.add(3f);
        list.add(5f);
        list.add(2f);
        list.add(7f);
        list.add(1.1f);
        showLineChart(list, "我的", Color.CYAN);
        setMarkerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click:
//                Random random = new Random();
//                List<Float> list = new ArrayList<>();
//                for (int i = 0; i < 5; i++) {
//                    float data = random.nextFloat() * 10;
//                    list.add(data);
//                }
//                addLine(list, "www", LColor.getRandomColor());
//                LLogX.e("随机颜色 ——:" + LColor.getRandomColor());


                mDetailHandler.sendEmptyMessageDelayed(MSG_ADD_DATASET, 1000);

                break;
            default:
        }
    }

    /**
     * 初始化图表
     */
    private void initChart(LineChart lineChart) {
        /***图表设置***/
        lineChart.setDrawGridBackground(false); //是否展示网格线
        lineChart.setDrawBorders(true); //是否显示边界
        lineChart.setDragEnabled(true); //是否可以拖动
        lineChart.setScaleEnabled(true); // 是否可以缩放
        lineChart.setTouchEnabled(true); //是否有触摸事件
        //lineChart.setVisibleXRangeMaximum(3); // 设置在曲线图中X轴显示的最大数量;
        //lineChart.setVisibleYRangeMaximum(6, YAxis.AxisDependency.RIGHT);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);

        /***XY轴的设置***/
        mXAxis = lineChart.getXAxis(); // x轴
        mLeftYAxis = lineChart.getAxisLeft(); // 左侧Y轴
        mRightYAxis = lineChart.getAxisRight(); // 右侧Y轴
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //X轴设置显示位置在底部
        mXAxis.setAxisMinimum(0f); // 设置X轴的最小值
        mXAxis.setAxisMaximum(200f); // 设置X轴的最大值
        mXAxis.setLabelCount(200, false); // 设置X轴的刻度数量，第二个参数表示是否平均分配
        mXAxis.setGranularity(1f); // 设置X轴坐标之间的最小间隔
        mLineChart.setVisibleXRangeMaximum(6);// 当前统计图表中最多在x轴坐标线上显示的总量
        //保证Y轴从0开始，不然会上移一点
        mLeftYAxis.setAxisMinimum(0f);
        mRightYAxis.setAxisMinimum(0f);
        mLeftYAxis.setAxisMaximum(200f);
        mRightYAxis.setAxisMaximum(200f);
        mLeftYAxis.setGranularity(1f);
        mRightYAxis.setGranularity(1f);
        mLeftYAxis.setLabelCount(200);
        mLineChart.setVisibleYRangeMaximum(10, YAxis.AxisDependency.LEFT);// 当前统计图表中最多在Y轴坐标线上显示的总量
        mLineChart.setVisibleYRangeMaximum(20, YAxis.AxisDependency.RIGHT);// 当前统计图表中最多在Y轴坐标线上显示的总量



        /***折线图例 标签 设置***/
        mLegend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        mLegend.setForm(Legend.LegendForm.LINE);
        mLegend.setTextSize(12f);
        //显示位置 左下方
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        mLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        mLegend.setDrawInside(false);
    }

    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color); // 设置曲线颜色
        lineDataSet.setCircleColor(color);  // 设置数据点圆形的颜色
        lineDataSet.setDrawCircleHole(false);// 设置曲线值的圆点是否是空心
        lineDataSet.setLineWidth(1f); // 设置折线宽度
        lineDataSet.setCircleRadius(3f); // 设置折现点圆点半径
        lineDataSet.setValueTextSize(10f);

        lineDataSet.setDrawFilled(true); //设置折线图填充
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

    /**
     * 展示曲线
     *
     * @param dataList 数据集合
     * @param name     曲线名称
     * @param color    曲线颜色
     */
    public void showLineChart(List<Float> dataList, String name, int color) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            float data = dataList.get(i);
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            Entry entry = new Entry(i, data);
            entries.add(entry);
        }
        // 每一个LineDataSet代表一条线
        mLineDataSet = new LineDataSet(entries, name);
        initLineDataSet(mLineDataSet, color, LineDataSet.Mode.CUBIC_BEZIER);
        mLineData = new LineData(mLineDataSet);
        mLineChart.setData(mLineData);
    }

    /**
     * 添加曲线
     */
    private void addLine(List<Float> dataList, String name, int color) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            Entry entry = new Entry(i, dataList.get(i));
            entries.add(entry);
        }
        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, name);
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
        mLineChart.getLineData().addDataSet(lineDataSet);
        mLineChart.invalidate();
    }

    /**
     * 动态添加数据（一条折线图）
     *
     * @param number
     */
    public void addEntry(float number) { //最开始的时候才添加 lineDataSet（一个lineDataSet 代表一条线）
        int count = mLineDataSet.getEntryCount();
        LLogX.e("data = " + number + " count == " + count + "-" + mLineData.getEntryCount() + "-" + mLineData.getDataSetCount());

        Entry entry = new Entry(count, number);
        mLineData.addEntry(entry, 0);
        //通知数据已经改变
        mLineData.notifyDataChanged();
        mLineChart.notifyDataSetChanged();

        //mLineChart.setVisibleXRangeMaximum(6);// 当前统计图表中最多在x轴坐标线上显示的总量
        mLineChart.moveViewToX(count - 6); //移到某个位置
        //mLineChart.invalidate();//图标刷新

    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    public void setMarkerView() {
        LineChartMarkView mv = new LineChartMarkView(this, mXAxis.getValueFormatter());
        mv.setChartView(mLineChart);
        mLineChart.setMarker(mv);
        mLineChart.invalidate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailHandler.removeCallbacksAndMessages(null);
        mDetailHandler = null;
    }

    private static class DetailHandler extends Handler {
        WeakReference<NewDeviceDetails> mReference;

        DetailHandler(NewDeviceDetails activity) {
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NewDeviceDetails newDeviceDetails = mReference.get();
            if (newDeviceDetails == null) {
                return;
            }
            switch (msg.what) {
                case MSG_ADD_DATASET:
                    Random random = new Random();
                    newDeviceDetails.addEntry(random.nextFloat() * 10);
                    newDeviceDetails.mDetailHandler.sendEmptyMessageDelayed(MSG_ADD_DATASET, 1000);
                    break;
                default:
            }
        }
    }
}