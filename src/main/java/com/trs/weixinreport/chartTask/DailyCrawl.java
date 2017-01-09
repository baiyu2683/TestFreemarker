package com.trs.weixinreport.chartTask;

import com.trs.weixinreport.EsDataQueryService;
import com.trs.weixinreport.WeixinReportContants;
import com.trs.weixinreport.hybase.HybaseDao;
import com.trs.weixinreport.util.BeanUtil;
import com.trs.weixinreport.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

/**
 * TRS和采购每日采集数据异同
 * 改为从两个hybase分别读采购和自采，并从es里读总数之后，由于es入库时间和hybase不一样，会少一些数据
 * 导致按loadtime查得到es里总数量小于两个Hybase的单独的数量，会后负数
 * 改为按urltime查
 * 此图相应需要改为trs和采购采集某天数据的异同
 */
public class DailyCrawl implements ChartTask, Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(DailyCrawl.class);

    private EsDataQueryService esDataQueryService = (EsDataQueryService) BeanUtil.getBean("esDataQueryService");

    private CountDownLatch countDownLatch;

    private String startDate;

    private String endDate;

    public DailyCrawl(String startDate, String endDate, CountDownLatch countDownLatch) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        LOGGER.info("===开始查询TRS和采购每日采集数据对比数据===");
        Calendar[] dateScope = DateUtil.getDateScope(startDate, endDate);
        if(dateScope != null && dateScope.length != 0){
            Calendar start = dateScope[0];
            Calendar end = dateScope[1];
            String main2xAxis = "";
            String procurementWeChatPublicNumberArticalTodayAlone = "";
            String trsWeChatPublicNumberArticalTodayAlone = "";
            String weiChatPublicNumberArticalIntersectionToday = "";
            while(start.compareTo(end) <= 0){
                String date = DateUtil.date2String(start.getTime(),"yyyy/M/d");
                LOGGER.info("日期:" + date);
                main2xAxis += date + ",";
                String startstr = DateUtil.date2String(start.getTime(),"yyyyMMdd");
                String swhere0 = "IR_URLTIME:" + startstr + " AND SOURCE:0";
                long lwhere0 = HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI);
                String swhere1 = "IR_URLTIME:" + startstr;
                long lwhere1 = HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU);
                //TODO 从es里查询所有的记录
                String swhereAll = "IR_URLTIME:[" + startstr + "000000 TO " + startstr + "235959]";
                long weChatPublicNumberAll = esDataQueryService.getRecordsCount(WeixinReportContants.ES_DBNAME, swhereAll);
                long weiChatPublicNumberArticalIntersection = lwhere0 + lwhere1 - weChatPublicNumberAll;
                weiChatPublicNumberArticalIntersection = weiChatPublicNumberArticalIntersection >= 0? weiChatPublicNumberArticalIntersection: 0;
                weiChatPublicNumberArticalIntersectionToday += weiChatPublicNumberArticalIntersection + ",";
                trsWeChatPublicNumberArticalTodayAlone += (lwhere0 - weiChatPublicNumberArticalIntersection) + ",";
                procurementWeChatPublicNumberArticalTodayAlone += (lwhere1 - weiChatPublicNumberArticalIntersection) + ",";
                //计算当天的
                start.add(Calendar.DAY_OF_MONTH, 1);
            }
            if(StringUtils.isNotEmpty(main2xAxis)){
                main2xAxis = main2xAxis.substring(0, main2xAxis.lastIndexOf(","));
            }
            if(StringUtils.isNotEmpty(weiChatPublicNumberArticalIntersectionToday)){
                weiChatPublicNumberArticalIntersectionToday = weiChatPublicNumberArticalIntersectionToday.substring(0, weiChatPublicNumberArticalIntersectionToday.lastIndexOf(","));
            }
            if(StringUtils.isNotEmpty(trsWeChatPublicNumberArticalTodayAlone)){
                trsWeChatPublicNumberArticalTodayAlone = trsWeChatPublicNumberArticalTodayAlone.substring(0, trsWeChatPublicNumberArticalTodayAlone.lastIndexOf(","));
            }
            if(StringUtils.isNotEmpty(procurementWeChatPublicNumberArticalTodayAlone)){
                procurementWeChatPublicNumberArticalTodayAlone = procurementWeChatPublicNumberArticalTodayAlone.substring(0, procurementWeChatPublicNumberArticalTodayAlone.lastIndexOf(","));
            }
            ChartData.data.put("main2xAxis", "\"" + main2xAxis + "\"");
            ChartData.data.put("weiChatPublicNumberArticalIntersectionToday", "\"" + weiChatPublicNumberArticalIntersectionToday + "\"");
            ChartData.data.put("trsWeChatPublicNumberArticalTodayAlone", "\"" + trsWeChatPublicNumberArticalTodayAlone + "\"");
            ChartData.data.put("procurementWeChatPublicNumberArticalTodayAlone", "\"" + procurementWeChatPublicNumberArticalTodayAlone + "\"");
        }
        LOGGER.info("===获取TRS和采购每日采集数据对比数据结束===");
        countDownLatch.countDown();
    }
}
