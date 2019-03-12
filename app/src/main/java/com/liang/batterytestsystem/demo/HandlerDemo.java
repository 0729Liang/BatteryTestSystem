package com.liang.batterytestsystem.demo;


import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @author : Amarao
 * CreateAt : 12:33 2019/3/9
 * Describe : csdn 博客Demo
 */
public class HandlerDemo {

    public static final int HANDLER_DMEO = 1;

    private MyHandler mMyHandler = null;

    public HandlerDemo() {
        mMyHandler = new MyHandler(this);
        mMyHandler.sendEmptyMessage(HANDLER_DMEO);
    }

    public void updateData() {

    }

    // 静态内部类
    public static class MyHandler extends Handler {

        //弱引用
        WeakReference<HandlerDemo> mWeakReference;

        MyHandler(HandlerDemo handlerDemo) {
            mWeakReference = new WeakReference<>(handlerDemo);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HandlerDemo handlerDemo = mWeakReference.get();
            if (handlerDemo == null) {
                return;
            }
            switch (msg.what) {
                case HANDLER_DMEO:
                    // 通过弱引用的方式对外部类操作
                    handlerDemo.updateData();
                    break;
                default:
            }

        }
    }
}
