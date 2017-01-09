package com.trs.weixinreport.chartTask;

import com.trs.weixinreport.EsDataQueryService;
import com.trs.weixinreport.WeixinReportContants;
import com.trs.weixinreport.hybase.HybaseDao;
import com.trs.weixinreport.util.BeanUtil;
import com.trs.weixinreport.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

/**
 * TRS和采购一段时间内采集数据异同
 * Created by zhangheng on 2017/1/6.
 */
public class SomeTimeCrawl implements ChartTask, Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(SomeTimeCrawl.class);

    private EsDataQueryService esDataQueryService = (EsDataQueryService) BeanUtil.getBean("esDataQueryService");

    private CountDownLatch countDownLatch;

    private String startDate;

    private String endDate;

    public SomeTimeCrawl(String startDate, String endDate, CountDownLatch countDownLatch) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        LOGGER.info("===开始查询TRS和采购一段时间内采集数据对比===");
        Calendar[] dateScope = DateUtil.getDateScope(startDate, endDate);
        if(dateScope != null && dateScope.length != 0){
            Calendar start = dateScope[0];
            Calendar end = dateScope[1];
            String startstr = DateUtil.date2String(start.getTime(),"yyyyMMdd");
            String endstr = DateUtil.date2String(end.getTime(),"yyyyMMdd");
            String head3 = DateUtil.date2String(start.getTime(),"M/d") + "-" + DateUtil.date2String(end.getTime(),"M/d") + " TRS和采购数据异同";
            String swhere0 = "IR_URLTIME:[" + startstr + " TO " + endstr + "] AND SOURCE:0";
            long lwhere0 = HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI);
            String swhere1 = "IR_URLTIME:[" + startstr + " TO " + endstr + "]";
            long lwhere1 = HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU);
            String swhereAll = "IR_URLTIME:[" + startstr + "000000 TO " + endstr + "235959]";
            long weChatPublicNumberArticalAll = esDataQueryService.getRecordsCount(WeixinReportContants.ES_DBNAME, swhereAll);
            long weiChatPublicNumberArticalIntersection = lwhere0 + lwhere1 - weChatPublicNumberArticalAll;
            if(lwhere0 == 0 || lwhere1 == 0){
                weiChatPublicNumberArticalIntersection = 0;
            }
            weiChatPublicNumberArticalIntersection = weiChatPublicNumberArticalIntersection >= 0?weiChatPublicNumberArticalIntersection:0;
            //计算
            long trsWeChatPublicNumberArticalAlone = (lwhere0 - weiChatPublicNumberArticalIntersection);
            long procurementWeChatPublicNumberArticalAlone = (lwhere1 - weiChatPublicNumberArticalIntersection);

            ChartData.data.put("head3", "\"" + head3 + "\"");
            ChartData.data.put("weiChatPublicNumberArticalIntersection", weiChatPublicNumberArticalIntersection);
            ChartData.data.put("trsWeChatPublicNumberArticalAlone", trsWeChatPublicNumberArticalAlone);
            ChartData.data.put("procurementWeChatPublicNumberArticalAlone", procurementWeChatPublicNumberArticalAlone);
        }
        LOGGER.info("===获取TRS和采购一段时间内采集对比数据结束===");
        countDownLatch.countDown();
    }
}
