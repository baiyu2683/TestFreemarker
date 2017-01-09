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
 * TRS和采购微信公众号每日数据
 * Created by zhangheng on 2017/1/7.
 */
public class DailyPublicNumCrawlByUrlTime implements ChartTask, Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(DailyPublicNumCrawlByUrlTime.class);

    private CountDownLatch countDownLatch;

    private String startDate;

    private String endDate;

    public DailyPublicNumCrawlByUrlTime(String startDate, String endDate, CountDownLatch countDownLatch) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        LOGGER.info("===开始获取TRS和采购微信公众号对比数据===");
        String className = this.getClass().getSimpleName();
        Calendar[] dateScope = DateUtil.getDateScope(startDate, endDate);
        if(dateScope != null && dateScope.length != 0){
            Calendar start = dateScope[0];
            Calendar end = dateScope[1];
            String main7xAxis = "";
            String procurementWeChatPublicNumber = "";
            String trsWeChatPublicNumber = "";
            String head7 = " TRS和采购微信公众号对比";
            String datestr = DateUtil.date2String(start.getTime(),"M/d") + "-" + DateUtil.date2String(end.getTime(),"M/d");
            head7 = datestr + head7;
            while(start.compareTo(end) <= 0){
                String date = DateUtil.date2String(start.getTime(),"M/d");
                LOGGER.info("日期:" + date);
                main7xAxis += date + ",";
                String startstr = DateUtil.date2String(start.getTime(),"yyyyMMdd");
//                String swhere0 = "select IR_WEIXINID as data from " + WeixinReportContants.HYBASE_ZICAI_DBNAME_DC +" where trs('hybase', 'IR_URLTIME:" + startstr + " AND SOURCE:0')";
                String swhere0 = "IR_URLTIME:" + startstr + " AND SOURCE:0";
                Set<String> tmpSet = HybaseDao.getRecordsSetByFile(swhere0, WeixinReportContants.SOURCE_ZICAI, "IR_WEIXINID", className + "-" + WeixinReportContants.SOURCE_ZICAI + "-" + startstr);
                trsWeChatPublicNumber += tmpSet.size() + ",";
                tmpSet.clear();
                String swhere1 = "IR_URLTIME:" + startstr;
                tmpSet = HybaseDao.getRecordsSetByFile(swhere1, WeixinReportContants.SOURCE_CAIGOU, "IR_WEIXINID", className + "-" + WeixinReportContants.SOURCE_CAIGOU + "-" + startstr);
                procurementWeChatPublicNumber += tmpSet.size() + ",";
                tmpSet.clear();
                start.add(Calendar.DAY_OF_MONTH, 1);
            }
            if(StringUtils.isNotEmpty(main7xAxis)){
                main7xAxis = main7xAxis.substring(0, main7xAxis.lastIndexOf(","));
            }
            if(StringUtils.isNotEmpty(procurementWeChatPublicNumber)){
                procurementWeChatPublicNumber = procurementWeChatPublicNumber.substring(0, procurementWeChatPublicNumber.lastIndexOf(","));
            }
            if(StringUtils.isNotEmpty(trsWeChatPublicNumber)){
                trsWeChatPublicNumber = trsWeChatPublicNumber.substring(0, trsWeChatPublicNumber.lastIndexOf(","));
            }
            ChartData.data.put("head7", "\"" + head7 + "\"");
            ChartData.data.put("main7xAxis", "\"" + main7xAxis + "\"");
            ChartData.data.put("procurementWeChatPublicNumber", "\"" + procurementWeChatPublicNumber + "\"");
            ChartData.data.put("trsWeChatPublicNumber", "\"" + trsWeChatPublicNumber + "\"");
        }
        LOGGER.info("===获取TRS和采购微信公众号对比数据结束===");
        countDownLatch.countDown();
    }
}
