package com.trs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by zhangheng on 2017/1/9.
 */
@Component
@PropertySource(value = "classpath:/reportgenerator.properties")
public class ReportGeneratorConfig {

    @Value("${generator.dailyartical}")
    public String dailyartical;

    @Value("${generator.dailyaveragenumofarticles}")
    public String dailyaveragenumofarticles;

    @Value("${generator.dailycrawl}")
    public String dailycrawl;

    @Value("${generator.dailypublicnumcrawl}")
    public String dailypublicnumcrawl;

    @Value("${generator.dailypublicnumcrawlbyurltime}")
    public String dailypublicnumcrawlbyurltime = "123";

    @Value("${generator.dailypublicnumcrawldiffbyurltime}")
    public String dailypublicnumcrawldiffbyurltime;

    @Value("${generator.sometimecrawl}")
    public String sometimecrawl;

    @Value("${generator.sometimepublicnumcrawldiff}")
    public String sometimepublicnumcrawldiff;

    @Value("${generator.timeforcrawl}")
    public String timeforcrawl;

    @Value("${generator.todayandhistoryarticalcrawl}")
    public String todayandhistoryarticalcrawl;

    @Value("${generator.timelinessforcrawl}")
    public String timeliness;

}
