package com.trs.weixinreport.chartTask;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.trs.weixinreport.WeixinReportContants;
import com.trs.weixinreport.hybase.HybaseDao;
import com.trs.weixinreport.util.DateUtil;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

/**
 * 每日采集数据当天和历史文章量对比数据
 * Created by zhangheng on 2017/1/7.
 */
public class TodayAndHistoryArticalCrawl implements ChartTask, Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(TodayAndHistoryArticalCrawl.class);

    private CountDownLatch countDownLatch;

    private String startDate;

    private String endDate;

    public TodayAndHistoryArticalCrawl(String startDate, String endDate, CountDownLatch countDownLatch) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        LOGGER.info("===开始获取每日采集数据当天和历史文章量对比数据===");
        try{
            //所有的数据
            //微信采集某日发布数据所需天数对比信息
            Calendar[] dateScope = DateUtil.getDateScope(startDate, endDate);
            String main5xAixs = "";
            String procurementHistory = "";
            String procurementToday = "";
            String trsHistory = "";
            String trsToday = "";
            if(dateScope != null && dateScope.length != 0){
                Calendar start = dateScope[0];
                Calendar end = dateScope[1];
                while(start.compareTo(end) <= 0){
                    String startstr = DateUtil.date2String(start.getTime(),"yyyyMMdd");
                    //TRS4/1\n采购4/1
                    String datestr = DateUtil.date2String(start.getTime(), "M/d");
                    String xAixs = "TRS" + datestr + "\\n采购" + datestr;
                    main5xAixs += xAixs + ",";
                    String procurementHistoryWhere = "IR_LOADTIME:" + startstr + " AND IR_URLDATE:[* TO " + startstr +"}";
                    long source0 = HybaseDao.getHybaseCountRecords(procurementHistoryWhere, WeixinReportContants.SOURCE_CAIGOU);
                    procurementHistory += source0 + ",";
                    String procurementTodayWhere = "IR_LOADTIME:" + startstr + " AND IR_URLDATE:" + startstr;
                    long source1 = HybaseDao.getHybaseCountRecords(procurementTodayWhere, WeixinReportContants.SOURCE_CAIGOU);
                    procurementToday += source1 + ",";
                    String trsHistoryWhere = "IR_LOADTIME:" + startstr + " AND IR_URLDATE:[* TO " + startstr +"}"
                            + " AND SOURCE:0";
                    long source2 = HybaseDao.getHybaseCountRecords(trsHistoryWhere, WeixinReportContants.SOURCE_ZICAI);
                    trsHistory += source2 + ",";
                    String trsTodayWhere = "IR_LOADTIME:" + startstr + " AND IR_URLDATE:" + startstr
                            + " AND SOURCE:0";
                    long source3 = HybaseDao.getHybaseCountRecords(trsTodayWhere, WeixinReportContants.SOURCE_ZICAI);
                    trsToday += source3 + ",";
                    start.add(Calendar.DAY_OF_MONTH, 1);
                }
                if(StringUtils.isNotBlank(main5xAixs)){
                    main5xAixs = main5xAixs.substring(0, main5xAixs.lastIndexOf(","));
                    ChartData.data.put("main5xAixs", "\""+main5xAixs+"\"");
                }
                if(StringUtils.isNotBlank(procurementHistory)){
                    procurementHistory = procurementHistory.substring(0, procurementHistory.lastIndexOf(","));
                    ChartData.data.put("procurementHistory", "\""+procurementHistory+"\"");
                }
                if(StringUtils.isNotBlank(procurementToday)){
                    procurementToday = procurementToday.substring(0, procurementToday.lastIndexOf(","));
                    ChartData.data.put("procurementToday", "\""+procurementToday+"\"");
                }
                if(StringUtils.isNotBlank(trsHistory)){
                    trsHistory = trsHistory.substring(0, trsHistory.lastIndexOf(","));
                    ChartData.data.put("trsHistory", "\""+trsHistory+"\"");
                }
                if(StringUtils.isNotBlank(trsToday)){
                    trsToday = trsToday.substring(0, trsToday.lastIndexOf(","));
                    ChartData.data.put("trsToday", "\""+trsToday+"\"");
                }
            }
        }catch(Exception e){
            LOGGER.error("获取每日采集数据当天和历史文章量对比数据出错：", e);
        }
        LOGGER.info("===获取每日采集数据当天和历史文章量对比数据完成===");
        countDownLatch.countDown();
    }
}
