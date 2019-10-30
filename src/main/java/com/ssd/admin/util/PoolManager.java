package com.ssd.admin.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 线程池管理器
 */
public class PoolManager {


    public static ExecutorService statisticPool;

    static {
        statisticPool = new ThreadPoolExecutor(
                100,
                200,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10000));
    }
}
