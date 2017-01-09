package com.trs.weixinreport.chartTask;

import com.trs.hybase.client.TRSConnection;
import com.trs.hybase.client.TRSException;
import com.trs.hybase.client.TRSResultSet;
import com.trs.hybase.client.params.SearchParams;
import com.trs.weixinreport.WeixinReportContants;
import org.junit.Test;

/**
 * Created by zhangheng on 2017/1/9.
 */
public class TestHybaseSearch {

    @Test
    public void test1() throws TRSException {
        String key = "http://192.168.203.200:5555;import;import2dev3;dh_weixin_bykj_2016";
        String swhere = "IR_LOADTIME:20161201";
        String where = "IR_LOADTIME:20161201 AND IR_URLDATE:[* TO 20161201}";
        String[] infoArr = key.split(";");
        TRSConnection tRSConnection = new TRSConnection(infoArr[0], infoArr[1], infoArr[2], null);
        TRSResultSet trsRs = null;
        SearchParams searchParams = new SearchParams();
//            trsRs = tRSConnection.executeSelect(WeixinReportContants.HYBASE_ZICAI_DBNAME_DC, swhere, 0, 0, searchParams);
        trsRs = tRSConnection.executeSelect(infoArr[3], swhere, 0, 0, searchParams);
        System.out.println(trsRs.getNumFound());
    }
}
