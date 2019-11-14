package com.ssd.admin.web.controller;

import com.ssd.admin.business.vo.Cons;
import com.ssd.admin.util.BigDecimalUtil;
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
    public static void main(String[] args) throws InterruptedException, IllegalAccessException {

        /**
         * 1/9*9/1000
         */
        Double tcRate = BigDecimalUtil.div(9, 1000, Cons.TC_RATE);
        System.out.println(tcRate);
        double div = BigDecimalUtil.div(1, 9, Cons.CONTRIBUTION_RATE);
        System.out.println(div);
        Double contributionRate = BigDecimalUtil.mul(div,tcRate, Cons.CONTRIBUTION_RATE);
        System.out.println(contributionRate);
    }

}
