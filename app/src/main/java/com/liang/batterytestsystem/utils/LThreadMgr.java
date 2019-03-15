package com.liang.batterytestsystem.utils;

import android.support.annotation.NonNull;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : Amarao
 * CreateAt : 17:07 2019/3/15
 * Describe :
 */
public class LThreadMgr {

//    public ThreadPoolExecutor(
//                             int corePoolSize,线程池的核心线程数，默认情况下， 核心线程会在线程池中一直存活， 即使处于闲置状态. 但如果将allowCoreThreadTimeOut设置为true的话, 那么核心线程也会有超时机制， 在keepAliveTime设置的时间过后， 核心线程也会被终止.
//                             int maximumPoolSize,最大的线程数， 包括核心线程， 也包括非核心线程， 在线程数达到这个值后，新来的任务将会被阻塞.
//                             long keepAliveTime,超时的时间， 闲置的非核心线程超过这个时长，讲会被销毁回收， 当allowCoreThreadTimeOut为true时，这个值也作用于核心线程.
//                             TimeUnit unit,超时时间的时间单位.
//                             BlockingQueue<Runnable> workQueue,线程池的任务队列， 通过execute方法提交的runnable对象会存储在这个队列中.
//                             ThreadFactory threadFactory,线程工厂, 为线程池提供创建新线程的功能.
//                             RejectedExecutionHandler handler() {});任务无法执行时，回调handler的rejectedExecution方法来通知调用者.


    private static int                            sCorePoolSize    = 0; // 核心线程池的数量，同时能够执行的线程数量
    private static int                            sMaximumPoolSize = 10;// 最大线程池数量，表示当缓冲队列满的时候能继续容纳的等待任务的数量
    private static long                           sKeepAliveTime   = 10;// 存活时间
    private static TimeUnit                       sTimeUnit        = TimeUnit.MINUTES;
    private static LinkedBlockingQueue<Runnable>  sWorkQueue       = new LinkedBlockingQueue<>();
    private static ThreadPoolExecutor.AbortPolicy sHandler         = new ThreadPoolExecutor.AbortPolicy();
    /**
     * 创建线程工厂
     */
    private static ThreadFactory                  sFactory         = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "Liang Thread:" + mCount.getAndIncrement());
        }
    };

    public static ThreadPoolExecutor createThread() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                sCorePoolSize,
                sMaximumPoolSize,
                sKeepAliveTime,
                sTimeUnit,
                sWorkQueue,
                sFactory,
                sHandler);
        //threadPoolExecutor.allowCoreThreadTimeOut(true);// 设置核心线程也会超时
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread("");
            }
        };
        return threadPoolExecutor;
    }
}
