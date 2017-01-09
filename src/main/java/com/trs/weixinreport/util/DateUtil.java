package com.trs.weixinreport.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhangheng on 2017/1/7.
 */
public class DateUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    //获得一个时间数组
    public static Calendar[] getDateScope(String start, String end){
        Calendar[] dateArr = new Calendar[2];
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calstart = Calendar.getInstance();
            calstart.setTime(df.parse(start));
            Calendar calend = Calendar.getInstance();
            calend.setTime(df.parse(end));
            dateArr[0] = calstart;
            dateArr[1] = calend;
        } catch (ParseException e) {
            LOGGER.error("获得时间范围错误：", e);
        }
        return dateArr;
    }

    //日期格式化
    public static String date2String(Date date, String format){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        }catch(Exception e){
            LOGGER.error("日期格式化失败：", e);
        }
        return "";
    }

    public static Date string2Date(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date);
        }catch(ParseException e){
            LOGGER.error("转换日期失败", e);
        }
        return null;
    }

    //c1和c2间隔天数
    public static int dateInterval(Calendar c1, Calendar c2){
        return (int) ((c2.getTimeInMillis()-c1.getTimeInMillis())/(1000*60*60*24));
    }

    public static long dataInterval(Date d1, Date d2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(d1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(d2);
        return (calendar2.getTimeInMillis()-calendar1.getTimeInMillis());
    }

}
