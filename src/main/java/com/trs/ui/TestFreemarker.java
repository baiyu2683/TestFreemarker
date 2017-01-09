package com.trs.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangheng on 2017/1/9.
 */
@Controller
@RequestMapping("/testFreemarker")
public class TestFreemarker {

    @RequestMapping(value = "/fm", method = RequestMethod.GET)
    @ResponseBody
    public String test(Map<String, Object> map) {
        Map<String, String> temp = new HashMap<>();
        temp.put("asdf", "asdfasdfasdfasfd");
        map.put("map", temp);
        System.out.println(System.getProperty("user.dir"));
        return "test";
    }

    @RequestMapping(value = "/fm2", method = RequestMethod.GET)
    @ResponseBody
    public String test2() {
        System.out.println("asdfasdf");
        return "weixinReport";
    }
}
