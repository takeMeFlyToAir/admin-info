package com.ssd.admin.web.controller;

import com.ssd.admin.util.PoolManager;
import com.ssd.admin.util.excel.ExcelDataUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zhaozhirong on 2019/9/23.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(100);
                    Thread.sleep(100);
                    countDownLatch.countDown();
                    System.out.println("100----------");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(1000);
                    Thread.sleep(1000);
                    countDownLatch.countDown();
                    System.out.println("1000----------");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(20000);
                    Thread.sleep(20000);
                    countDownLatch.countDown();
                    System.out.println("20000----------");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println("===========");
        countDownLatch.await();
        System.out.println("======end=====");
    }

}
