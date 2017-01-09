package com.trs.weixinreport.chartTask;

import com.trs.weixinreport.WeixinReportContants;
import com.trs.weixinreport.hybase.HybaseDao;
import com.trs.weixinreport.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 每日文章采集量对比
 * Created by zhangheng on 2017/1/6.
 */
public class DailyArtical implements ChartTask, Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(DailyArtical.class);

    private CountDownLatch countDownLatch;

    private String startDate;

    private String endDate;

    public DailyArtical(String startDate, String endDate, CountDownLatch countDownLatch) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        LOGGER.info("===开始查询每日文章采集量对比数据===");
        try{
            DateFormat screendf = new SimpleDateFormat("yyyy/M/d");
            Calendar calstart = Calendar.getInstance();
            calstart.setTime(DateUtil.string2Date(startDate,"yyyy-MM-dd"));
            Calendar calend = Calendar.getInstance();
            calend.setTime(DateUtil.string2Date(endDate,"yyyy-MM-dd"));
            //生成自采和采购信息
            List<String> dateScope = new ArrayList<String>();
            List<Long> zicailist = new ArrayList<Long>();
            List<Long> caigoulist = new ArrayList<Long>();
            String head1 = DateUtil.date2String(calstart.getTime(),"M/d") + "-" + DateUtil.date2String(calend.getTime(),"M/d") + " TRS与采购文章数对比";
            while(calstart.compareTo(calend) <= 0){
                //时间
                dateScope.add("\""+screendf.format(calstart.getTime())+"\"");
                String date = DateUtil.date2String(calstart.getTime(),"yyyyMMdd");
                LOGGER.info("日期:" + date);
                // 在数据库里检索记录
                String swhere0 = "IR_LOADTIME:" + date + " AND SOURCE:0";
                long source0 = HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI);
                String swhere1 = "IR_LOADTIME:" + date;
                long source1 = HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU);
                calstart.add(Calendar.DAY_OF_MONTH, 1);
                zicailist.add(source0);
                caigoulist.add(source1);
            }
            ChartData.data.put("main1xAixs", dateScope);
            ChartData.data.put("zicailist", zicailist);
            ChartData.data.put("caigoulist", caigoulist);
            ChartData.data.put("head1","\"" + head1 + "\"");
        }catch(Exception e){
            LOGGER.error("生成每日文章采集量报表出错：", e);
        }
        LOGGER.info("获取每日文章采集量对比数据完成");
        countDownLatch.countDown();
    }
}
