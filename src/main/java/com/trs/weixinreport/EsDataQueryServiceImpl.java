package com.trs.weixinreport;

import com.alibaba.dubbo.rpc.RpcException;
import com.trs.dc.entity.TRSEsRecordSet;
import com.trs.dc.entity.TRSEsSearchParams;
import com.trs.dc.entity.TRSException;
import com.trs.dc.entity.TRSStatisticParams;
import com.trs.dc.openservice.IEsSearchOpenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhangheng on 2016/10/24.
 */
@Component("esDataQueryService")
public class EsDataQueryServiceImpl implements EsDataQueryService {

    @Autowired
    private IEsSearchOpenService esSearchService;

    @Override
    public long getRecordsCount(String dbName, String swhere, String distinctColumn) {
        TRSStatisticParams trsStatisticParams = new TRSStatisticParams();
        trsStatisticParams.setQuery(swhere);
        if(StringUtils.isNotBlank(distinctColumn))
            trsStatisticParams.setGroupTerm(distinctColumn);
        try {
            TRSEsRecordSet recordSet = esSearchService.getTermCount(trsStatisticParams, dbName);
            return recordSet.getNumFound();
        } catch (TRSException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long getRecordsCount(String dbName, String swhere){
        TRSEsSearchParams trsEsSearchParams = new TRSEsSearchParams();
        trsEsSearchParams.setQuery(swhere);
        TRSEsRecordSet recordSet = null;
        try {
            recordSet = esSearchService.querySearch(trsEsSearchParams, dbName);
            return recordSet.getNumFound();
        } catch (TRSException e) {
            e.printStackTrace();
        } catch (RpcException e){
            e.printStackTrace();
        }
        return 0;
    }
}
