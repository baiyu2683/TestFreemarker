package com.trs.weixinreport.chartTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trs.weixinreport.WeixinReportContants;
import com.trs.weixinreport.hybase.HybaseDao;
import com.trs.weixinreport.util.DateUtil;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * 一段时间内TRS与采购的微信公众号异同
 * Created by zhangheng on 2017/1/7.
 */
public class SomeTimePublicNumCrawlDiff implements ChartTask, Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(SomeTimePublicNumCrawlDiff.class);

    private CountDownLatch countDownLatch;

    private String startDate;

    private String endDate;

    public SomeTimePublicNumCrawlDiff(String startDate, String endDate, CountDownLatch countDownLatch) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        LOGGER.info("===开始获取一段时间内TRS与采购的微信公众号异同数据===");
        String className = this.getClass().getSimpleName();
        Calendar[] dateScope = DateUtil.getDateScope(startDate, endDate);
        if(dateScope != null && dateScope.length != 0){
            Calendar start = dateScope[0];
            Calendar end = dateScope[1];
            String head9 = " TRS和采购微信公众号异同";
            String datestr = DateUtil.date2String(start.getTime(),"M/d") + "-" + DateUtil.date2String(end.getTime(),"M/d");
            head9 = datestr + head9;
            String startstr = DateUtil.date2String(start.getTime(),"yyyyMMdd");
            String endstr = DateUtil.date2String(end.getTime(),"yyyyMMdd");
//            String swhere0 = "select IR_WEIXINID as data from dc_weixin_20141029 where trs('hybase', 'IR_LOADTIME:[" + startstr + " TO " + endstr + "] AND SOURCE:0')";
            String swhere0 = "IR_LOADTIME:[" + startstr + " TO " + endstr + "] AND SOURCE:0";
            Set<String> weixinZICAI = HybaseDao.getRecordsSetByFile(swhere0, WeixinReportContants.SOURCE_ZICAI, "IR_WEIXINID", className + "-" + WeixinReportContants.SOURCE_ZICAI + "-" + startstr + "TO" + endstr);
            String swhere1 = "IR_LOADTIME:[" + startstr + " TO " + endstr + "]";
            Set<String> weixinCAIGOU = HybaseDao.getRecordsSetByFile(swhere1, WeixinReportContants.SOURCE_CAIGOU, "IR_WEIXINID", className + "-" + WeixinReportContants.SOURCE_CAIGOU + "-" + startstr + "TO" + endstr);

            long lwhere0 = weixinZICAI.size();
            long lwhere1 = weixinCAIGOU.size();
            weixinZICAI.addAll(weixinCAIGOU);
            long allWeChatPublicNumber = weixinZICAI.size();

            weixinCAIGOU.clear();
            weixinZICAI.clear();

            long weChatPublicNumberIntersection = lwhere0 + lwhere1 - allWeChatPublicNumber;
            if(lwhere0 == 0 || lwhere1 == 0){
                weChatPublicNumberIntersection = 0l;
            }
            weChatPublicNumberIntersection = weChatPublicNumberIntersection >= 0? weChatPublicNumberIntersection: 0;
            Long trsWeCharPublicNumberAlone = lwhere0 - weChatPublicNumberIntersection;
            Long procurementWeChatPublicNumberAlone = lwhere1 - weChatPublicNumberIntersection;
            ChartData.data.put("head9", "\"" + head9 + "\"");
            ChartData.data.put("trsWeCharPublicNumberAlone", trsWeCharPublicNumberAlone);
            ChartData.data.put("procurementWeChatPublicNumberAlone", procurementWeChatPublicNumberAlone);
            ChartData.data.put("weChatPublicNumberIntersection", weChatPublicNumberIntersection);
        }
        LOGGER.info("===获取一段时间内TRS与采购的微信公众号异同数据结束===");
        countDownLatch.countDown();
    }
}
