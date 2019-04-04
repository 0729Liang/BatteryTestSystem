//package com.liang.batterytestsystem.module.details;
//
//import android.content.Context;
//import android.util.AttributeSet;
//
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.components.Legend;
//import com.liang.batterytestsystem.R;
//import com.liang.liangutils.utils.LLogX;
//
///**
// * @author : Amarao
// * CreateAt : 20:19 2019/3/17
// * Describe :
// */
//public class MyLineChart extends LineChart {
//
//    public MyLineChart(Context context) {
//        super(context);
//    }
//
//    public MyLineChart(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        LLogX.e("自定义");
//    }
//
//    //设置简单的属性
//    private void setChartProperties() {
//        //设置描述文本不显示
//        this.getDescription().setEnabled(false);
//        //设置是否显示表格背景
//        this.setDrawGridBackground(true);
//        //设置是否可以触摸
//        this.setTouchEnabled(true);
//        this.setDragDecelerationFrictionCoef(0.9f);
//        //设置是否可以拖拽
//        this.setDragEnabled(true);
//        //设置是否可以缩放
//        this.setScaleEnabled(false);
//        this.setDrawGridBackground(false);
//        this.setHighlightPerDragEnabled(true);
//        this.setPinchZoom(true);
//        //设置背景颜色
//        //this.setBackgroundColor(ColorAndImgUtils.CHART_BACKGROUND_COLOR);
//        //设置一页最大显示个数为6，超出部分就滑动
//        float ratio = (float) xValueList.size() / (float) 6;
//        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
//        this.zoom(ratio, 1f, 0, 0);
//        //设置从X轴出来的动画时间
//        //this.animateX(1500);
//        //设置XY轴动画
//        this.animateXY(1500, 1500, Easing.EasingOption.EaseInSine, Easing.EasingOption.EaseInSine);
//    }
//
//    //设置图例是否显示，这里我们可以隐藏图例，使用ListView或者RecyclerView实现我们需要实现的简答的效果
//    private void setChartLegend() {
//        //获取图例对象
//        Legend legend = this.getLegend();
//        //设置图例不显示
//        legend.setEnabled(false);
//    }
//
//   // 设置MarkView，实现我们想要的效果
//    private void setChartMarkView() {
//        CustomMarkView mv = new CustomMarkView(getActivity(), R.layout.line_chart_custom_marker_view);
//        // For bounds control
//        mv.setChartView(this);
//        // Set the marker to the chart
//        this.setMarker(mv);
//    }
//
//
//    // 设置X轴
//    private void setChartXAxis(int position) {
//        ArrayList<String> list = getList(position);
//        //自定义设置横坐标
//        IAxisValueFormatter xValueFormatter = new FastBrowserXValueFormatter(list);
//        //X轴
//        XAxis xAxis = this.getXAxis();
//        //设置线为虚线
//        //xAxis.enableGridDashedLine(10f, 10f, 0f);
//        //设置字体大小10sp
//        xAxis.setTextSize(10f);
//        //设置X轴字体颜色
//        xAxis.setTextColor(ColorAndImgUtils.FAST_BW_TEXT_COLOR);
//        //设置从X轴发出横线
//        xAxis.setDrawGridLines(true);
//        xAxis.setGridColor(ColorAndImgUtils.GRID_COLOR);
//        //设置网格线宽度
//        xAxis.setGridLineWidth(1);
//        //设置显示X轴
//        xAxis.setDrawAxisLine(true);
//        //设置X轴显示的位置
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        //设置自定义X轴值
//        xAxis.setValueFormatter(xValueFormatter);
//        //一个界面显示6个Lable，那么这里要设置11个
//        xAxis.setLabelCount(6);
//        //设置最小间隔，防止当放大时出现重复标签
//        xAxis.setGranularity(1f);
//        //设置为true当一个页面显示条目过多，X轴值隔一个显示一个
//        xAxis.setGranularityEnabled(true);
//        //设置X轴的颜色
//        xAxis.setAxisLineColor(ColorAndImgUtils.GRID_COLOR);
//        //设置X轴的宽度
//        xAxis.setAxisLineWidth(1f);
//        this.invalidate();
//    }
//
//    // 设置Y轴
//    private void setChartYAxis() {
//        YAxis leftAxis = this.getAxisLeft();
//        //设置从Y轴发出横向直线(网格线)
//        leftAxis.setDrawGridLines(true);
//        //设置网格线的颜色
//        leftAxis.setGridColor(ColorAndImgUtils.GRID_COLOR);
//        //设置网格线宽度
//        leftAxis.setGridLineWidth(1);
//        //设置Y轴最小值是0，从0开始
//        leftAxis.setAxisMinimum(0f);
//        leftAxis.setGranularityEnabled(true);
//        //设置最小间隔，防止当放大时出现重复标签
//        leftAxis.setGranularity(1f);
//        //如果沿着轴线的线应该被绘制，则将其设置为true,隐藏Y轴
//        leftAxis.setDrawAxisLine(false);
//        leftAxis.setDrawZeroLine(false);
//        leftAxis.setTextSize(10f);
//        leftAxis.setTextColor(ColorAndImgUtils.FAST_BW_TEXT_COLOR);
//        //设置左边X轴显示
//        leftAxis.setEnabled(true);
//        //设置Y轴的颜色
//        leftAxis.setAxisLineColor(ColorAndImgUtils.GRID_COLOR);
//        //设置Y轴的宽度
//        leftAxis.setAxisLineWidth(1f);
//
//        YAxis rightAxis = this.getAxisRight();
//        //设置右边Y轴不显示
//        rightAxis.setEnabled(false);
//    }
//
//    //设置整个图表的Y值，Y轴的值，和X轴Y轴对应的值
//    private void setData(ArrayList<String> xValueList, ArrayList<Integer> yValueList) {
//        ArrayList<Entry> yValues = new ArrayList<>();
//        for (int i = 0; i < xValueList.size(); i++) {
//            yValues.add(new Entry(i, yValueList.get(i)));
//        }
//
//        LineDataSet set;
//        if (this.getMLineData() != null && this.getMLineData().getDataSetCount() > 0) {
//            set = (LineDataSet) this.getData().getDataSetByIndex(0);
//            set.setValues(yValues);
//            this.getData().notifyDataChanged();
//            this.notifyDataSetChanged();
//        } else {
//            //设置值给图表
//            set = new LineDataSet(yValues, "");
//            //设置图标不显示
//            set.setDrawIcons(false);
//            //设置Y值使用左边Y轴的坐标值
//            set.setAxisDependency(YAxis.AxisDependency.LEFT);
//            //设置线的颜色
//            set.setColors(ColorAndImgUtils.ONE_COLOR);
//            //设置数据点圆形的颜色
//            set.setCircleColor(ColorAndImgUtils.ONE_COLOR);
//            //设置填充圆形中间的颜色
//            set.setCircleColorHole(ColorAndImgUtils.ONE_COLOR);
//            //设置折线宽度
//            set.setLineWidth(1f);
//            //设置折现点圆点半径
//            set.setCircleRadius(4f);
//            //设置十字线颜色
//            //set.setHighLightColor(Color.parseColor("#47DBCC"));
//            //设置显示十字线，必须显示十字线，否则MarkerView不生效
//            set.setHighlightEnabled(true);
//            //设置是否在数据点中间显示一个孔
//            set.setDrawCircleHole(false);
//
//            //设置填充
//            //设置允许填充
//            set.setDrawFilled(true);
//            set.setFillAlpha(50);
//            //设置背景渐变
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                //设置渐变
//                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.line_chart_gradient);
//                set.setFillDrawable(drawable);
//            } else {
//                set.setFillColor(ColorAndImgUtils.ONE_COLOR);
//            }
//
//            LineData data = new LineData(set);
//            //设置不显示数据点的值
//            data.setDrawValues(false);
//
//            this.setData(data);
//            this.invalidate();
//        }
//    }
//
//
//}
