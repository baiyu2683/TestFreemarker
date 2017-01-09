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
 * TRS和采购微信公众号差异
 * Created by zhangheng on 2017/1/7.
 */
public class DailyPublicNumCrawlDiffByUrlTime implements ChartTask, Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(DailyPublicNumCrawlDiffByUrlTime.class);


    private CountDownLatch countDownLatch;

    private String startDate;

    private String endDate;

    public DailyPublicNumCrawlDiffByUrlTime(String startDate, String endDate, CountDownLatch countDownLatch) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        LOGGER.info("===开始获取TRS和采购微信公众号差异对比数据===");
        String className = this.getClass().getSimpleName();
        Calendar[] dateScope = DateUtil.getDateScope(startDate, endDate);
        if(dateScope != null && dateScope.length != 0){
            Calendar start = dateScope[0];
            Calendar end = dateScope[1];
            String main8xAxis = "";
            String procurementWeChatPublicNumberTodayAlone = "";
            String trsWeChatPublicNumberTodayAlone = "";
            String weiChatPublicNumberIntersectionToday = "";
            String head8 = " TRS和采购微信公众号差异对比";
            String datestr = DateUtil.date2String(start.getTime(),"M/d") + "-" + DateUtil.date2String(end.getTime(),"M/d");
            head8 = datestr + head8;
            while(start.compareTo(end) <= 0){
                String date = DateUtil.date2String(start.getTime(),"M/d");
                LOGGER.info("日期:" + date);
                main8xAxis += date + ",";
                String startstr = DateUtil.date2String(start.getTime(),"yyyyMMdd");
                String swhere1 = "IR_URLTIME:" + startstr;
                Set<String> weixinidCaiGou = HybaseDao.getRecordsSetByFile(swhere1, WeixinReportContants.SOURCE_CAIGOU, "IR_WEIXINID", className + "-" + WeixinReportContants.SOURCE_CAIGOU + "-" + startstr);
//                String swhere0 = "select IR_WEIXINID as data from " + WeixinReportContants.HYBASE_ZICAI_DBNAME_DC + " where trs('hybase', 'IR_URLTIME:" + startstr + " AND SOURCE:0')";
                String swhere0 = "IR_URLTIME:" + startstr + " AND SOURCE:0";
                Set<String> weixinIdZiCai = HybaseDao.getRecordsSetByFile(swhere1, WeixinReportContants.SOURCE_ZICAI, "IR_WEIXINID", className + "-" + WeixinReportContants.SOURCE_ZICAI + "-" + startstr);
                long lwhere1 = weixinidCaiGou.size();
                long lwhere0 = weixinIdZiCai.size();
                weixinidCaiGou.addAll(weixinIdZiCai);
                long allToday = weixinidCaiGou.size();
                //清空
                weixinidCaiGou.clear();
                weixinIdZiCai.clear();
                //计算当天的
                long intersection = lwhere1 + lwhere0 - allToday;
                if(lwhere0 == 0 || lwhere1 == 0){
                    intersection = 0l;
                }
                intersection = intersection >= 0? intersection: 0;
                procurementWeChatPublicNumberTodayAlone += lwhere1 - intersection + ",";
                trsWeChatPublicNumberTodayAlone += lwhere0 - intersection + ",";
                weiChatPublicNumberIntersectionToday += intersection + ",";
                start.add(Calendar.DAY_OF_MONTH, 1);
            }
            if(StringUtils.isNotEmpty(main8xAxis)){
                main8xAxis = main8xAxis.substring(0, main8xAxis.lastIndexOf(","));
            }
            if(StringUtils.isNotEmpty(procurementWeChatPublicNumberTodayAlone)){
                procurementWeChatPublicNumberTodayAlone = procurementWeChatPublicNumberTodayAlone.substring(0, procurementWeChatPublicNumberTodayAlone.lastIndexOf(","));
            }
            if(StringUtils.isNotEmpty(trsWeChatPublicNumberTodayAlone)){
                trsWeChatPublicNumberTodayAlone = trsWeChatPublicNumberTodayAlone.substring(0, trsWeChatPublicNumberTodayAlone.lastIndexOf(","));
            }
            if(StringUtils.isNotEmpty(weiChatPublicNumberIntersectionToday)){
                weiChatPublicNumberIntersectionToday = weiChatPublicNumberIntersectionToday.substring(0, weiChatPublicNumberIntersectionToday.lastIndexOf(","));
            }
            ChartData.data.put("head8", "\"" + head8 + "\"");
            ChartData.data.put("main8xAxis", "\"" + main8xAxis + "\"");
            ChartData.data.put("procurementWeChatPublicNumberTodayAlone", "\"" + procurementWeChatPublicNumberTodayAlone + "\"");
            ChartData.data.put("trsWeChatPublicNumberTodayAlone", "\"" + trsWeChatPublicNumberTodayAlone + "\"");
            ChartData.data.put("weiChatPublicNumberIntersectionToday", "\"" + weiChatPublicNumberIntersectionToday + "\"");
        }
        LOGGER.info("获取TRS和采购微信公众号差异对比数据结束。。");
        countDownLatch.countDown();
    }
}
