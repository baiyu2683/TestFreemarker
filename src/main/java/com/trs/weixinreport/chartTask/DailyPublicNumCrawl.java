package com.trs.weixinreport.chartTask;

import com.trs.weixinreport.WeixinReportContants;
import com.trs.weixinreport.hybase.HybaseDao;
import com.trs.weixinreport.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * 每日采集文章的微信公众号数
 * Created by zhangheng on 2017/1/7.
 */
public class DailyPublicNumCrawl implements ChartTask, Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(DailyPublicNumCrawl.class);

    private CountDownLatch countDownLatch;

    private String startDate;

    private String endDate;

    public DailyPublicNumCrawl(String startDate, String endDate, CountDownLatch countDownLatch) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        LOGGER.info("===开始获取每日采集文章的微信公众号数对比数据===");
        String className = this.getClass().getSimpleName();
        Calendar[] dateScope = DateUtil.getDateScope(startDate, endDate);
        if(dateScope != null && dateScope.length != 0){
            Calendar start = dateScope[0];
            Calendar end = dateScope[1];
            String main6xAxis = "";
            String procurementWeChatPublicArticalNumber = "";
            String trsWeChatPublicArticalNumber = "";
            while(start.compareTo(end) <= 0){
                String date = DateUtil.date2String(start.getTime(),"yyyy/M/d");
                LOGGER.info("日期:" + date);
                main6xAxis += date + ",";
                String startstr = DateUtil.date2String(start.getTime(),"yyyyMMdd");
//                String swhere0 = "select IR_WEIXINID as data from " + WeixinReportContants.HYBASE_ZICAI_DBNAME_DC +"  where trs('hybase', 'IR_LOADTIME:" + startstr + " AND SOURCE:0')";
                String swhere0 = "IR_LOADTIME:" + startstr + " AND SOURCE:0";
//                trsWeChatPublicArticalNumber += HybaseDao.getHybsCountRecordsBySQLFromDc(swhere0).size() + ",";
                Set<String> tmpSet = HybaseDao.getRecordsSetByFile(swhere0, WeixinReportContants.SOURCE_ZICAI, "IR_WEIXINID", className + "-" + WeixinReportContants.SOURCE_ZICAI + "-" + startstr);
                trsWeChatPublicArticalNumber += tmpSet.size() + ",";
                tmpSet.clear();
                String swhere1 = "IR_LOADTIME:" + startstr;
                tmpSet = HybaseDao.getRecordsSetByFile(swhere1, WeixinReportContants.SOURCE_CAIGOU, "IR_WEIXINID", className + "-" + WeixinReportContants.SOURCE_CAIGOU + "-" + startstr);
                procurementWeChatPublicArticalNumber += tmpSet.size() + ",";
                tmpSet.clear();
                start.add(Calendar.DAY_OF_MONTH, 1);
            }
            if(StringUtils.isNotEmpty(main6xAxis)){
                main6xAxis = main6xAxis.substring(0, main6xAxis.lastIndexOf(","));
            }
            if(StringUtils.isNotEmpty(procurementWeChatPublicArticalNumber)){
                procurementWeChatPublicArticalNumber = procurementWeChatPublicArticalNumber.substring(0, procurementWeChatPublicArticalNumber.lastIndexOf(","));
            }
            if(StringUtils.isNotEmpty(trsWeChatPublicArticalNumber)){
                trsWeChatPublicArticalNumber = trsWeChatPublicArticalNumber.substring(0, trsWeChatPublicArticalNumber.lastIndexOf(","));
            }
            ChartData.data.put("main6xAxis", "\"" + main6xAxis + "\"");
            ChartData.data.put("procurementWeChatPublicArticalNumber", "\"" + procurementWeChatPublicArticalNumber + "\"");
            ChartData.data.put("trsWeChatPublicArticalNumber", "\"" + trsWeChatPublicArticalNumber + "\"");
        }
        LOGGER.info("===开始获取每日采集文章的微信公众号数对比数据===");
        countDownLatch.countDown();
    }
}
