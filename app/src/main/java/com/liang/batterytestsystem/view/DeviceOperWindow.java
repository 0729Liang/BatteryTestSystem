package com.liang.batterytestsystem.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.liang.batterytestsystem.R;
import com.liang.batterytestsystem.device.Customer2;
import com.liang.liangutils.utils.LLogX;

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

        LLogX.e("h = " + this.getHeight());
        LLogX.e("w = " + this.getWidth());
        this.showAsDropDown(view, 0, -this.getHeight());
        //this.showAsDropDown(view, location[0] - this.getWidth(), location[1]);
        //this.show(view);

        return this;
    }

    public DeviceOperWindow addTestPauseClickEvent(Customer2 customer) {
        View view = this.getContentView().findViewById(R.id.mvWindowTestPause);
        view.setOnClickListener(v -> customer.accept());
        return this;
    }

    public DeviceOperWindow addTestStopClickEvent(Customer2 customer) {
        View view = this.getContentView().findViewById(R.id.mvWindowTestStop);
        view.setOnClickListener(v -> customer.accept());
        return this;
    }

    public DeviceOperWindow addDisconnectClickEvent(Customer2 customer) {
        View view = this.getContentView().findViewById(R.id.mvWindowDisconnect);
        view.setOnClickListener(v -> customer.accept());
        return this;
    }
}
