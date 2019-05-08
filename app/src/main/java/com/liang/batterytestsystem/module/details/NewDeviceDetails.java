package com.liang.batterytestsystem.module.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.blankj.utilcode.util.ToastUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.jobs.MoveViewJob;
import com.liang.batterytestsystem.R;
import com.liang.batterytestsystem.base.LAbstractBaseActivity;
import com.liang.batterytestsystem.constant.DeviceKey;
import com.liang.batterytestsystem.module.home.DeviceCommand;
import com.liang.batterytestsystem.module.home.DeviceItemBean;
import com.liang.batterytestsystem.module.item.DeviceItemChannelBean;
import com.liang.batterytestsystem.module.service.DeviceMgrService;
import com.liang.batterytestsystem.module.service.DeviceQueryEvent;
import com.liang.batterytestsystem.utils.DigitalTrans;
import com.liang.batterytestsystem.utils.LColor;
import com.liang.liangutils.mgrs.LKVMgr;
import com.liang.liangutils.utils.LLogX;
import com.liang.liangutils.view.LTitleView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewDeviceDetails extends LAbstractBaseActivity implements View.OnClickListener {

    public static final int MSG_ADD_DATASET = 1;
    public static final int FLAG_HIDDEN_ALL = -1;
    //  数据链表
    List<Float> mStepTimeList = new ArrayList<>();
    List<Float> mElectricistList = new ArrayList<>();
    List<Float> mVoltageList = new ArrayList<>();
    private Button mBtnClick;
    List<Float> mPowerList = new ArrayList<>();
    List<Float> mTempertureList = new ArrayList<>();
    List<Float> mAmpereHourList = new ArrayList<>();
    // Chart需要的点数据链表
    List<Entry> mStepTimeEntries = new ArrayList<>();
    List<Entry> mElectricistEntries = new ArrayList<>();
    List<Entry> mVoltageEntries = new ArrayList<>();
    List<Entry> mPowerEntries = new ArrayList<>();
    List<Entry> mTempertureEntries = new ArrayList<>();

    private LineChart mLineChart; // 折线表，存线集合
    List<Entry> mAmpereHourEntries = new ArrayList<>();

    private XAxis mXAxis; //X轴
    private YAxis mLeftYAxis; //左侧Y轴
    private YAxis mRightYAxis; //右侧Y轴
    private Legend mLegend; //图例
    private LimitLine mLimitline; //限制线
    private Random random = new Random();
    private DeviceItemChannelBean mChannelBean;
    private LTitleView mTitleView;
    private CheckBox mRadioButtonStepTime;
    private CheckBox mRadioButtonElectricist;
    private CheckBox mRadioButtonVoltage;
    private CheckBox mRadioButtonPower;
    private CheckBox mRadioButtonTemperture;
    private CheckBox mRadioButtonAmpereHour;
    private List<CheckBox> mCheckBoxList = new ArrayList<>();
    private LineData mLineData = null; // 线集合，所有折现以数组的形式存到此集合中
    // LineDataSet:点集合,即一条线
    private LineDataSet mStepTimeDataSet = new LineDataSet(mStepTimeEntries, "步时间"); //步时间
    private LineDataSet mElectricLineDataSet = new LineDataSet(mElectricistEntries, "电流"); // 电流
    private LineDataSet mVoltageLineDataSet = new LineDataSet(mVoltageEntries, "电压"); // 电压
    private LineDataSet mPowerLineDataSet = new LineDataSet(mPowerEntries, "功率"); // 功率
    private LineDataSet mTempertureDataSet = new LineDataSet(mTempertureEntries, "温度"); // 温度
    private LineDataSet mAmpereHourDataSet = new LineDataSet(mAmpereHourEntries, "安时"); // 安时

    public static void startActivity(Context context, DeviceItemChannelBean bean) {
        LKVMgr.memory().putObj(DeviceKey.KEY_DETAIL_INFO, bean);
        context.startActivity(new Intent(context, NewDeviceDetails.class));
    }


    private DetailHandler mDetailHandler = new DetailHandler(NewDeviceDetails.this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp_test);

        initData();
        initView();
        initChart(mLineChart);

    }


    public Float getRandom(Float seed) {
        return random.nextFloat() * seed;
    }

    @Override
    public void initData() {
        mChannelBean = LKVMgr.memory().getObj(DeviceKey.KEY_DETAIL_INFO, DeviceItemChannelBean.class);
    }

    @Override
    public void initView() {
        mTitleView = findViewById(R.id.mDeviceDetailTitle);
        mTitleView.setClickLeftFinish(this);
        if (mChannelBean != null) {
            mTitleView.setTitle("设备" + DigitalTrans.byte2hex(mChannelBean.getDeviceId()) + "-通道" + DigitalTrans.byte2hex(mChannelBean.getChannelId()));
        } else {
            mTitleView.setTitle("详情页");
        }

        mLineChart = findViewById(R.id.mDeviceDetailChart);
        mBtnClick = findViewById(R.id.mTestBtn1);
        mBtnClick.setOnClickListener(this);
        mBtnClick = findViewById(R.id.mTestBtn2);
        mBtnClick.setOnClickListener(this);
        mRadioButtonStepTime = findViewById(R.id.mDeviceDetailStepTime);
        mRadioButtonElectricist = findViewById(R.id.mDeviceDetailElectric);
        mRadioButtonVoltage = findViewById(R.id.mDeviceDetailVoltage);
        mRadioButtonPower = findViewById(R.id.mDeviceDetailPower);
        mRadioButtonTemperture = findViewById(R.id.mDeviceDetailTemperture);
        mRadioButtonAmpereHour = findViewById(R.id.mDeviceDetailAmpereHour);
        mCheckBoxList.add(mRadioButtonStepTime);
        mCheckBoxList.add(mRadioButtonElectricist);
        mCheckBoxList.add(mRadioButtonVoltage);
        mCheckBoxList.add(mRadioButtonPower);
        mCheckBoxList.add(mRadioButtonTemperture);
        mCheckBoxList.add(mRadioButtonAmpereHour);

        mRadioButtonStepTime.setOnClickListener(this);
        mRadioButtonElectricist.setOnClickListener(this);
        mRadioButtonVoltage.setOnClickListener(this);
        mRadioButtonPower.setOnClickListener(this);
        mRadioButtonTemperture.setOnClickListener(this);
        mRadioButtonAmpereHour.setOnClickListener(this);

    }


    @Override
    public void clicEvent() {

    }

    List<DeviceItemBean> mDeviceList;

    /**
     * 初始化图表
     */
    private void initChart(LineChart lineChart) {
        // 设置图标基本属性
        setChartBasicAttr(lineChart);

        // 设置XY轴
        setXYAxis(lineChart);

        // 添加线条
        createLine();

        // 设置图例
        createLegend();

        // 设置MarkerView
        //setMarkerView();
    }

    List<DeviceItemChannelBean> mChannelList;
    private DeviceItemBean mDeviceItemBean;

    void createLegend() {
        /***折线图例 标签 设置***/
        mLegend = mLineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        mLegend.setForm(Legend.LegendForm.CIRCLE);
        mLegend.setTextSize(12f);
        //显示位置 左下方
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        mLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        mLegend.setDrawInside(false);
        mLegend.setEnabled(true);
    }

    private void createLine() {
        //showLineChart(mElectricistList, "电流", Color.CYAN);
        addLine(mStepTimeList, mStepTimeEntries, mStepTimeDataSet, LColor.Colors.RED.getColor());
        addLine(mElectricistList, mElectricistEntries, mElectricLineDataSet, LColor.Colors.ORANGE.getColor());
        addLine(mVoltageList, mVoltageEntries, mVoltageLineDataSet, LColor.Colors.YELLOW.getColor());
        addLine(mPowerList, mPowerEntries, mPowerLineDataSet, LColor.Colors.GREEN.getColor());
        addLine(mTempertureList, mTempertureEntries, mTempertureDataSet, LColor.Colors.PURPLE.getColor());
        addLine(mAmpereHourList, mAmpereHourEntries, mAmpereHourDataSet, LColor.Colors.PINK.getColor());
        for (int i = 0; i < 6; i++) {
            addEntry(0, i);
            mLineChart.getLineData().getDataSets().get(i).setVisible(false);
        }
        showLine(1);
    }

    public void showLine(int index) {
        mLineChart
                .getLineData()
                .getDataSets()
                .get(index)
                .setVisible(mCheckBoxList.get(index).isChecked());
        mLineChart.invalidate();
    }

    private DeviceItemChannelBean mChannelItemBean;


    /**
     * 添加曲线
     */
    private void addLine(List<Float> dataList, List<Entry> entries, LineDataSet lineDataSet, int color) {
        for (int i = 0; i < dataList.size(); i++) {
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            Entry entry = new Entry(i, dataList.get(i));// Entry(x,y)
            entries.add(entry);
        }

        initLineDataSet(lineDataSet, color, LineDataSet.Mode.CUBIC_BEZIER);
        if (mLineData == null) {
            mLineData = new LineData();
            mLineData.addDataSet(lineDataSet);
            mLineChart.setData(mLineData);
        } else {
            mLineChart.getLineData().addDataSet(lineDataSet);
        }
        mLineChart.invalidate();
    }

    /**
     * 动态添加数据（一条折线图）
     *
     * @param xValues
     * @param yValues
     */
    public void addEntry(float xValues, float yValues, int index) {

        int count = mLineData.getEntryCount();
        LLogX.e("data = " + yValues + " count == " + count + "-" + mLineData.getDataSetCount());

        Entry entry = new Entry(count, yValues);
        mLineData.addEntry(entry, index);

        //通知数据已经改变
        mLineData.notifyDataChanged();
        mLineChart.notifyDataSetChanged();

        // TODO: 2019/5/4 内存泄漏，异步 待修复
        //移到某个位置
        mLineChart.moveViewToX(count - 6);

    }

    /**
     * 动态添加数据（一条折线图）
     *
     * @param yValues
     */
    public void addEntry(float yValues, int index) {

        int count = mLineData.getDataSetByIndex(index).getEntryCount();
        //LLogX.e("data = " + yValues + " 线：" + index + " 点数：" + count + " 总共点数：" + mLineData.getEntryCount() + " 线条数：" + mLineData.getDataSetCount());

        Entry entry = new Entry(count, yValues);
        mLineData.addEntry(entry, index);

        //通知数据已经改变
        mLineData.notifyDataChanged();
        mLineChart.notifyDataSetChanged();

        //移到某个位置
        mLineChart.moveViewToX(count - 5);// 异步的，会内存泄漏
        mLineChart.invalidate();
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

        LLogX.e(" x1 = " + mLineChart.getJobs().size());
        mLineChart.clearAllViewportJobs();
        mLineChart.removeAllViewsInLayout();
        mLineChart.removeAllViews();
        LLogX.e(" x2 = " + mLineChart.getJobs().size());

//        mLineData.clearValues();
//        mLineChart.clear();
//        mLineChart.clearAllViewportJobs();
//        mLineChart.removeAllViews();
//        mLineData = null;
//        mLineChart = null;
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
                    for (int i = 0; i < 6; i++) {
                        newDeviceDetails.addEntry(newDeviceDetails.getRandom(10f), i);
                    }
                    if (newDeviceDetails.mLineData.getDataSetByIndex(0).getEntryCount() <= 5) {
                        newDeviceDetails.mDetailHandler.sendEmptyMessageDelayed(MSG_ADD_DATASET, 1000);
                    }

//                    newDeviceDetails.addEntry(newDeviceDetails.getRandom(10f));
//                    newDeviceDetails.mDetailHandler.sendEmptyMessageDelayed(MSG_ADD_DATASET, 1000);
                    break;
                default:
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTestBtn1:
                //mDetailHandler.sendEmptyMessageDelayed(MSG_ADD_DATASET, 1000);

                // 查询数据 只管设备号，
                List<DeviceItemBean> deviceItemBeanList = DeviceMgrService.Companion.getSDeviceItemBeanList();

                List<byte[]> commandList = DeviceCommand.createDeviceTestComposeCommandList(deviceItemBeanList, DeviceCommand.Companion.getCOMMAND_QUERY_DATA_TEST(), false);
                ToastUtils.showShort("发送查询数据 设备数 =" + deviceItemBeanList.size() + " 命令数 = " + commandList.size());
                DeviceCommand.Companion.sendCommandList(commandList, "详情页");
                break;
            case R.id.mTestBtn2:
                //LLogX.e("共 " + mLineData.getDataSetCount());
                break;
            case R.id.mDeviceDetailStepTime:
                showLine(0);

                //mLineChart.getLineData().getDataSets().get(0).setVisible(true);
                //mStepTimeDataSet.setVisible(true);
                //mLineChart.invalidate();
                break;
            case R.id.mDeviceDetailElectric:
                showLine(1);
                break;
            case R.id.mDeviceDetailVoltage:
                showLine(2);
                break;
            case R.id.mDeviceDetailPower:
                showLine(3);
                break;
            case R.id.mDeviceDetailTemperture:
                showLine(4);
                break;
            case R.id.mDeviceDetailAmpereHour:
                showLine(5);
                break;
            default:
        }
    }

    private void setChartBasicAttr(LineChart lineChart) {
        /***图表设置***/
        lineChart.setDrawGridBackground(false); //是否展示网格线
        lineChart.setDrawBorders(true); //是否显示边界
        lineChart.setDragEnabled(true); //是否可以拖动
        lineChart.setScaleEnabled(true); // 是否可以缩放
        lineChart.setTouchEnabled(true); //是否有触摸事件

        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);
    }

    private void setXYAxis(LineChart lineChart) {
        /***XY轴的设置***/
        mXAxis = lineChart.getXAxis(); // x轴
        mLeftYAxis = lineChart.getAxisLeft(); // 左侧Y轴
        mRightYAxis = lineChart.getAxisRight(); // 右侧Y轴
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //X轴设置显示位置在底部
        mXAxis.setAxisMinimum(0f); // 设置X轴的最小值
        mXAxis.setAxisMaximum(500f); // 设置X轴的最大值
        mXAxis.setLabelCount(500, false); // 设置X轴的刻度数量，第二个参数表示是否平均分配
        mXAxis.setGranularity(1f); // 设置X轴坐标之间的最小间隔
        mLineChart.setVisibleXRangeMaximum(6);// 当前统计图表中最多在x轴坐标线上显示的总量
        //保证Y轴从0开始，不然会上移一点
        mLeftYAxis.setAxisMinimum(0f);
        mRightYAxis.setAxisMinimum(0f);
        mLeftYAxis.setAxisMaximum(500f);
        mRightYAxis.setAxisMaximum(500f);
        mLeftYAxis.setGranularity(1f);
        mRightYAxis.setGranularity(1f);
        mLeftYAxis.setLabelCount(500);
        mLineChart.setVisibleYRangeMaximum(10, YAxis.AxisDependency.LEFT);// 当前统计图表中最多在Y轴坐标线上显示的总量
        mLineChart.setVisibleYRangeMaximum(20, YAxis.AxisDependency.RIGHT);// 当前统计图表中最多在Y轴坐标线上显示的总量
//        mLeftYAxis.setCenterAxisLabels(true);// 将轴标记居中
//        mLeftYAxis.setDrawZeroLine(true); // 原点处绘制 一条线
//        mLeftYAxis.setZeroLineColor(Color.RED);
//        mLeftYAxis.setZeroLineWidth(1f);

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

        lineDataSet.setDrawFilled(false); //设置折线图填充
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceQueryEvent(DeviceQueryEvent event) {

        if (event.msg.equals(DeviceQueryEvent.Companion.getDEVICE_DATA_UPDATE_DATA_NOTIFICATION())) {

            mDeviceList = DeviceMgrService.Companion.getSDeviceList();
            for (int i = 0; i < mDeviceList.size(); i++) {
                mDeviceItemBean = mDeviceList.get(i);
                // 同一设备
                if (event.getDeviceId() == mDeviceItemBean.getDeviceId()) {

                    mChannelList = mDeviceItemBean.getChannelList();

                    for (int channelIndex = 0; channelIndex < mChannelList.size(); channelIndex++) {
                        mChannelItemBean = mChannelList.get(i);
                        // 同一通道
                        if (event.getChannelId() == mChannelItemBean.getChannelId()) {
                            LLogX.e(" dID = " + DigitalTrans.byte2hex(mDeviceItemBean.getDeviceId()) + " cId = " + DigitalTrans.byte2hex(mChannelItemBean.getChannelId()));
                            return;
                        }
                    }
                }
            }
        }

    } // onDeviceQueryEvent

    /**
     * 展示曲线
     *
     * @param dataList 数据集合
     * @param name     曲线名称
     * @param color    曲线颜色
     */
//    public void showLineChart(List<Float> dataList, String name, int color) {
//        List<Entry> entries = new ArrayList<>();
//        for (int i = 0; i < dataList.size(); i++) {
//            /**
//             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
//             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
//             */
//            Entry entry = new Entry(i, dataList.get(i));
//            entries.add(entry);
//        }
//        // 每一个LineDataSet代表一条线
//        mElectricLineDataSet = new LineDataSet(entries, name);
//        initLineDataSet(mElectricLineDataSet, color, LineDataSet.Mode.CUBIC_BEZIER);
//        mLineData = new LineData(mElectricLineDataSet);
//        mLineChart.setData(mLineData);
//    }

}