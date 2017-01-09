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
 * TRS和采购公众号平均每天发文数量
 * Created by zhangheng on 2017/1/7.
 */
public class DailyAverageNumOfArticles implements ChartTask, Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(DailyAverageNumOfArticles.class);

    private CountDownLatch countDownLatch;

    private String startDate;

    private String endDate;

    public DailyAverageNumOfArticles(String startDate, String endDate, CountDownLatch countDownLatch) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        LOGGER.info("===开始获取TRS和采购公众号平均每天发文数量对比===");
        String className = this.getClass().getSimpleName();
        Calendar[] dateScope = DateUtil.getDateScope(startDate, endDate);
        if(dateScope != null && dateScope.length != 0){
            Calendar start = dateScope[0];
            Calendar end = dateScope[1];
            String main10xAxis = "";
            String procurementArticalAverage = "";
            String trsArticalAverage = "";
            String head10 = " TRS和采购公众号平均每天发文数量对比";
            String datestr = DateUtil.date2String(start.getTime(),"M/d") + "-" + DateUtil.date2String(end.getTime(),"M/d");
            head10 = datestr + head10;
            while(start.compareTo(end) <= 0){
                String date = DateUtil.date2String(start.getTime(),"M/d");
                LOGGER.info("日期:" + date);
                main10xAxis += date + ",";
                String startstr = DateUtil.date2String(start.getTime(),"yyyyMMdd");
                //trs微信公众号数量
//                String sWhereTRSPublicNumber = "select IR_WEIXINID as data from " + WeixinReportContants.HYBASE_ZICAI_DBNAME_DC +" where trs('hybase', 'IR_URLDATE:" + startstr + " AND SOURCE:0')";
                String sWhereTRSPublicNumber = "IR_URLDATE:" + startstr + " AND SOURCE:0";
                String sWhereTRSArtical = "IR_URLDATE:" + startstr + " AND SOURCE:0";
//                long trsPublicNumber = HybaseDao.getHybsCountRecordsBySQLFromDc(sWhereTRSPublicNumber).size();
                Set<String> tmpSet = HybaseDao.getRecordsSetByFile(sWhereTRSPublicNumber, WeixinReportContants.SOURCE_ZICAI, "IR_WEIXINID", className + "-" + WeixinReportContants.SOURCE_ZICAI + "-" + startstr);
                long trsPublicNumber = tmpSet.size();
                tmpSet.clear();
                long trsArtical = HybaseDao.getHybaseCountRecords(sWhereTRSArtical, WeixinReportContants.SOURCE_ZICAI);
                if(trsArtical > 0){
                    float average = (float) Math.round(((float)trsArtical/trsPublicNumber)*100)/100;
                    trsArticalAverage += average + ",";
                }else{
                    trsArticalAverage += 0 + ",";
                }
                //采购微信公众号数量
                String sWhereProcurementPublicNumber = "IR_URLDATE:" + startstr;
                String sWhereProcurementArtical = "IR_URLDATE:" + startstr;
                tmpSet = HybaseDao.getRecordsSetByFile(sWhereProcurementPublicNumber, WeixinReportContants.SOURCE_CAIGOU, "IR_WEIXINID", className + "-" + WeixinReportContants.SOURCE_CAIGOU + "-" + startstr);
                long procurementPublicNumber = tmpSet.size();
                tmpSet.clear();
                long procurementArtical = HybaseDao.getHybaseCountRecords(sWhereProcurementArtical, WeixinReportContants.SOURCE_CAIGOU);
                if(procurementArtical > 0){
                    float average = (float) Math.round(((float)procurementArtical/procurementPublicNumber)*100)/100;
                    procurementArticalAverage += average + ",";
                }else{
                    procurementArticalAverage += 0 + ",";
                }
                //计算当天的
                start.add(Calendar.DAY_OF_MONTH, 1);
            }
            if(StringUtils.isNotEmpty(main10xAxis)){
                main10xAxis = main10xAxis.substring(0, main10xAxis.lastIndexOf(","));
            }
            if(StringUtils.isNotEmpty(trsArticalAverage)){
                trsArticalAverage = trsArticalAverage.substring(0, trsArticalAverage.lastIndexOf(","));
            }
            if(StringUtils.isNotEmpty(procurementArticalAverage)){
                procurementArticalAverage = procurementArticalAverage.substring(0, procurementArticalAverage.lastIndexOf(","));
            }
            ChartData.data.put("head10", "\"" + head10 + "\"");
            ChartData.data.put("main10xAxis", "\"" + main10xAxis + "\"");
            ChartData.data.put("trsArticalAverage", "\"" + trsArticalAverage + "\"");
            ChartData.data.put("procurementArticalAverage", "\"" + procurementArticalAverage + "\"");
        }
        LOGGER.info("===获取TRS和采购公众号平均每天发文数量对比数据结束===");
        countDownLatch.countDown();
    }
}
