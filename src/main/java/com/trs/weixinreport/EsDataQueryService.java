package com.trs.weixinreport;

/**
 * Created by zhangheng on 2016/10/24.
 */
public interface EsDataQueryService {

    /**
     * 根据检索表达式，数据库
     * @param swhere
     * @param dbName
     * @param distinctColumn
     * @return
     */
    long getRecordsCount(String dbName, String swhere, String distinctColumn);

    /**
     * 获取记录数
     * @param dbName
     * @param swhere
     * @return
     */
    long getRecordsCount(String dbName, String swhere);
}
