package com.trs.weixinreport;

import com.trs.config.ReportGeneratorConfig;
import com.trs.redis.JedisDao;
import com.trs.weixinreport.chartTask.ChartData;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component("weixinReportExportService")
public class WeixinReportExportServiceImpl implements WeixinReportExportService {

	private static final Logger LOGGER = Logger
			.getLogger(WeixinReportExportServiceImpl.class);

	@Autowired
	private ReportGeneratorConfig reportGeneratorConfig;

	@Autowired
	private JedisDao jedisDao;

	public void exportReport(String startDate, String endDate) {
		Field[] fields = reportGeneratorConfig.getClass().getDeclaredFields();
		String jsonStr = "";
		CountDownLatch countDownLatch = new CountDownLatch(fields.length);
		ExecutorService executorService = Executors.newFixedThreadPool(fields.length);
		try {
			for(Field field : fields) {
				String className = (String) field.get(reportGeneratorConfig);
				Class<?> chartTask = Class.forName(className);
				Runnable instance = (Runnable) chartTask.getConstructor(new Class[]{String.class, String.class, CountDownLatch.class})
						.newInstance(new Object[]{startDate, endDate, countDownLatch});
				executorService.execute(instance);
			}
			countDownLatch.await();
			LOGGER.info("所有任务执行完毕...");
			ObjectMapper om = new ObjectMapper();
			jsonStr = om.writeValueAsString(ChartData.data);
			jedisDao.hset("weixinreport", startDate + ";" + endDate, jsonStr);
			LOGGER.info("报表数据:" + jsonStr);
		}catch(Exception e){
			LOGGER.error("生成报表出错", e);
		}finally{
			executorService.shutdown();
		}
	}
}
