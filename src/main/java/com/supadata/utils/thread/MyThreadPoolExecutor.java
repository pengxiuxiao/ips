package com.supadata.utils.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ThreadPoolExecutor
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/21 16:19
 * @Description:
 */
public class MyThreadPoolExecutor {



    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            5, //（线程池的基本大小）：当提交一个任务到线程池时，线程池会创建一个线程来执行任务，
            // 即使其他空闲的基本线程能够执行新任务也会创建线程，等到需要执行的任务数大于线程池基本大小时就不再创建。
            // 如果调用了线程池的prestartAllCoreThreads方法，线程池会提前创建并启动所有基本线程。
            10,//（线程池最大大小）：线程池允许创建的最大线程数。如果队列满了，并且已创建的线程数小于最大线程数，
            // 则线程池会再创建新的线程执行任务。值得注意的是如果使用了无界的任务队列这个参数就没什么效果。
            200, //（线程活动保持时间）：线程池的工作线程空闲后，保持存活的时间。
            // 所以如果任务很多，并且每个任务执行的时间比较短，可以调大这个时间，提高线程的利用率。
            TimeUnit.MILLISECONDS,//（线程活动保持时间的单位）
            new ArrayBlockingQueue<Runnable>(5));//工作队列
}
