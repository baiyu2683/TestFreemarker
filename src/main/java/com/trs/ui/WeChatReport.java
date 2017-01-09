package com.trs.ui;

import com.trs.redis.JedisDao;
import com.trs.weixinreport.WeixinReportExportService;
import com.trs.weixinreport.WeixinReportExportServiceImpl;
import com.trs.weixinreport.chartTask.ChartData;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangheng on 2016/7/27.
 */
@Controller
@RequestMapping(value ="/wechatreport")
public class WeChatReport {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatReport.class);

    @Autowired
    private WeixinReportExportService weixinReportExportService;

    @Autowired
    private JedisDao jedisDao;

    @RequestMapping(value="/export", method = RequestMethod.GET)
    @ResponseBody
    public String list() throws Exception {
        weixinReportExportService.exportReport("2016-12-01", "2016-12-31");
        return "完成了";
    }

    @RequestMapping(value="/getReport", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView list2(Map<String,Object> map) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("weixinReport");
        String result = jedisDao.hget("weixinreport", "2016-12-01;2016-12-31");
        ObjectMapper om = new ObjectMapper();
        Map<String, String> temp = om.readValue(result, Map.class);
//        map.put("map", temp);
        modelAndView.addObject("map", temp);
        return modelAndView;
    }

}
