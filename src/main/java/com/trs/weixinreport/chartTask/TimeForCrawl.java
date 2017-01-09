package com.trs.weixinreport.chartTask;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trs.weixinreport.WeixinReportContants;
import com.trs.weixinreport.hybase.HybaseDao;
import com.trs.weixinreport.util.DateUtil;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 每日发布数据从发布到入库差异
 * Created by zhangheng on 2017/1/7.
 */
public class TimeForCrawl implements ChartTask, Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(TimeForCrawl.class);

    private CountDownLatch countDownLatch;

    private String startDate;

    private String endDate;

    public TimeForCrawl(String startDate, String endDate, CountDownLatch countDownLatch) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        LOGGER.info("===开始查询微信采集某日发布数据所需天数数据===");
        Map<String,String> dataMap = new HashMap<String,String>();
        Calendar[] dateScope = DateUtil.getDateScope(startDate, endDate);
        String trs0 = "";String trs1 = "";String trs2 = "";String trs3 = "";String trs4 = "";String trs5 = "";
        String trs6 = "";String trs7 = "";String trs8 = "";String trs9 = "";String trs10 = "";
        String procurement0 = "";String procurement1 = "";String procurement2 = "";String procurement3 = "";String procurement4 = "";String procurement5 = "";
        String procurement6 = "";String procurement7 = "";String procurement8 = "";String procurement9 = "";String procurement10 = "";
        String main4xAxis = "";
        try{
            if(dateScope != null && dateScope.length != 0){
                Calendar start = dateScope[0];
                Calendar end = dateScope[1];
                while(start.compareTo(end) <= 0){
                    String startstr = DateUtil.date2String(start.getTime(), "yyyyMMdd");
                    Calendar startTmp = Calendar.getInstance();
                    startTmp.setTime(start.getTime());
                    Calendar endTmp = Calendar.getInstance();
                    endTmp.setTime(start.getTime());
                    endTmp.add(Calendar.DAY_OF_MONTH, 10);
                    String datestr = DateUtil.date2String(start.getTime(),"M/d");
                    main4xAxis += datestr + "TRS\\n"+ datestr +"采购,";
                    //获取
                    while(startTmp.compareTo(endTmp) <= 0){
                        String startTmpstr = DateUtil.date2String(startTmp.getTime(), "yyyyMMdd");
                        LOGGER.info("URLDATE:" + startstr +",LOADTIME:" + startTmpstr);
                        String swhere0 = "IR_LOADTIME:" + startTmpstr + " AND IR_URLDATE:" + startstr + " AND SOURCE:0";
                        String swhere1 = "IR_LOADTIME:" + startTmpstr + " AND IR_URLDATE:" + startstr;
                        int interval = DateUtil.dateInterval(start, startTmp);
                        switch(interval){
                            case 0:{
                                trs0 += HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI) + ",";
                                procurement0 += HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU) + ",";
                            };
                            break;
                            case 1:{
                                trs1 += HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI) + ",";
                                procurement1 += HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU) + ",";
                            };
                            break;
                            case 2:{
                                trs2 += HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI) + ",";
                                procurement2 += HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU) + ",";
                            };
                            break;
                            case 3:{
                                trs3 += HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI) + ",";
                                procurement3 += HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU) + ",";
                            };
                            break;
                            case 4:{
                                trs4 += HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI) + ",";
                                procurement4 += HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU) + ",";
                            };
                            break;
                            case 5:{
                                trs5 += HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI) + ",";
                                procurement5 += HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU) + ",";
                            };
                            break;
                            case 6:{
                                trs6 += HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI) + ",";
                                procurement6 += HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU) + ",";
                            };
                            break;
                            case 7:{
                                trs7 += HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI) + ",";
                                procurement7 += HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU) + ",";
                            };
                            break;
                            case 8:{
                                trs8 += HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI) + ",";
                                procurement8 += HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU) + ",";
                            };
                            break;
                            case 9:{
                                trs9 += HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI) + ",";
                                procurement9 += HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU) + ",";
                            };
                            break;
                            case 10:{
                                swhere0 = "IR_LOADTIME:[" + startTmpstr + " TO *} AND IR_URLDATE:" + startstr + " AND SOURCE:0";
                                swhere1 = "IR_LOADTIME:[" + startTmpstr + " TO *} AND IR_URLDATE:" + startstr;
                                trs10 += HybaseDao.getHybaseCountRecords(swhere0, WeixinReportContants.SOURCE_ZICAI) + ",";
                                procurement10 += HybaseDao.getHybaseCountRecords(swhere1, WeixinReportContants.SOURCE_CAIGOU) + ",";
                            };
                        }
                        startTmp.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    start.add(Calendar.DAY_OF_MONTH, 1);
                }
                dataMap.put("trs0", trs0);dataMap.put("trs1", trs1);dataMap.put("trs2", trs2);dataMap.put("trs3", trs3);dataMap.put("trs4", trs4);dataMap.put("trs5", trs5);
                dataMap.put("trs6", trs6);dataMap.put("trs7", trs7);dataMap.put("trs8", trs8);dataMap.put("trs9", trs9);dataMap.put("trs10", trs10);
                dataMap.put("procurement0", procurement0);dataMap.put("procurement1", procurement1);dataMap.put("procurement2", procurement2);
                dataMap.put("procurement3", procurement3);dataMap.put("procurement4", procurement4);dataMap.put("procurement5", procurement5);
                dataMap.put("procurement6", procurement6);dataMap.put("procurement7", procurement7);dataMap.put("procurement8", procurement8);
                dataMap.put("procurement9", procurement9);dataMap.put("procurement10", procurement10);
                if(dataMap != null){
                    for(Map.Entry<String,String> entry : dataMap.entrySet()){
                        String value = entry.getValue();
                        if(StringUtils.isNotEmpty(value)){
                            value = value.substring(0,value.lastIndexOf(","));
                            ChartData.data.put(entry.getKey(), "\"" + value + "\"");
                        }
                    }
                }
                ChartData.data.put("main4xAxis", "\"" + main4xAxis + "\"");
            }
        }catch(Exception e){
            LOGGER.error("获取微信采集某日发布数据所需天数数据出错", e);
        }
        LOGGER.info("===获取微信采集某日发布数据所需天数数据结束===");
        countDownLatch.countDown();
    }
}
