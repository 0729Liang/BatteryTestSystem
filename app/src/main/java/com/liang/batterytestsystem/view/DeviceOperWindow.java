package com.liang.batterytestsystem.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.liang.batterytestsystem.R;
import com.liang.batterytestsystem.module.device.CustomerNoParameters;
import com.liang.liangutils.utils.LSizeX;

/**
 * @author : Amarao
 * CreateAt : 16:22 2019/3/4
 * Describe : 更多操作window
 */
public class DeviceOperWindow extends PopupWindow {
    private DeviceOperWindow(Context context) {
        super(context);
    }

    public static DeviceOperWindow create(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.window_device_oper, null);
        DeviceOperWindow window = new DeviceOperWindow(context);
        window.setContentView(view); // 设置内容
        //window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        return window;
    }

    public DeviceOperWindow show(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        this.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);// 需要先测量，才可以得到宽高getMeasuredWidth
        // 默认显示在view的正下方
        this.showAsDropDown(view, 0 - this.getContentView().getMeasuredWidth() / 4, -this.getContentView().getMeasuredHeight() - view.getHeight() - LSizeX.dp2px(5));
        return this;
    }

    public DeviceOperWindow addTestPauseClickEvent(CustomerNoParameters customer) {
        View view = this.getContentView().findViewById(R.id.mvWindowTestPause);
        view.setOnClickListener(v -> customer.accept());
        return this;
    }

    public DeviceOperWindow addTestResumeClickEvent(CustomerNoParameters customer) {
        View view = this.getContentView().findViewById(R.id.mvWindowTestResume);
        view.setOnClickListener(v -> customer.accept());
        return this;
    }

    public DeviceOperWindow addStopClickEvent(CustomerNoParameters customer) {
        View view = this.getContentView().findViewById(R.id.mvWindowStop);
        view.setOnClickListener(v -> customer.accept());
        return this;
    }

    public DeviceOperWindow addQueryDataClickEvent(CustomerNoParameters customer) {
        View view = this.getContentView().findViewById(R.id.mvWindowQueryData);
        view.setOnClickListener(v -> customer.accept());
        return this;
    }

    public DeviceOperWindow addQueryChannelStatusClickEvent(CustomerNoParameters customer) {
        View view = this.getContentView().findViewById(R.id.mvWindowQueryChannelStatus);
        view.setOnClickListener(v -> customer.accept());
        return this;
    }
}
