package com.liang.batterytestsystem.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.liang.batterytestsystem.R;

import java.lang.ref.WeakReference;

public class DemoActivity extends Activity {

    public static final int HANDLER_DEMO = 1;
    private WeakReference<DemoActivity> mWeakReference;
    private MyHandler                   myHandler;
    private TextView                    mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.mvMainTestBtn);

        mWeakReference = new WeakReference<>(this);
        myHandler = new MyHandler(mWeakReference);
        myHandler.sendEmptyMessage(HANDLER_DEMO);
    }

    static class MyHandler extends Handler {
        private DemoActivity activity;

        MyHandler(WeakReference<DemoActivity> ref) {
            this.activity = ref.get();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //需要做判空操作
                    if (activity != null) {
                        activity.mTextView.setText("new Value");
                    }
                    break;
                default:
            }
        }
    }
}
