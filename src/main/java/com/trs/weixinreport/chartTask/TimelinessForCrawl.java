package com.trs.weixinreport.chartTask;

import com.trs.weixinreport.WeixinReportContants;
import com.trs.weixinreport.hybase.HybaseDao;
import com.trs.weixinreport.hybase.HybaseTimeLinessDao;
import com.trs.weixinreport.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * 每日数据从发布到采集差异
 * Created by zhangheng on 2017/1/9.
 */
public class TimelinessForCrawl implements ChartTask, Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(TimelinessForCrawl.class);

    private CountDownLatch countDownLatch;

    private String startDate;

    private String endDate;

    public TimelinessForCrawl(String startDate, String endDate, CountDownLatch countDownLatch) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            String className = this.getClass().getSimpleName();
            String startstr = "20161201";
            LOGGER.info("===开始查询微信采集某日发布数据从发布到采集所需时间===");
            String swhere0 = "IR_LOADTIME:" + startstr + " AND SOURCE:0";
            HybaseTimeLinessDao.exportFile(swhere0, WeixinReportContants.SOURCE_ZICAI, className + "-" + WeixinReportContants.SOURCE_ZICAI + "-" + startstr);
            String swhere1 = "IR_LOADTIME:" + startstr;
            HybaseTimeLinessDao.exportFile(swhere1, WeixinReportContants.SOURCE_CAIGOU, className + "-" + WeixinReportContants.SOURCE_CAIGOU + "-" + startstr);
            LOGGER.info("===获取微信采集某日发布数据所需天数从发布到采集所需时间===");
            countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
