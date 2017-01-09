package com.trs.weixinreport.hybase;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.trs.hybase.client.*;
import com.trs.hybase.client.params.SearchParams;
import com.trs.weixinreport.WeixinReportContants;
import com.trs.weixinreport.util.BeanUtil;
import com.trs.weixinreport.util.DateUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HybaseTimeLinessDao {

    private static final Logger logger = LoggerFactory.getLogger(HybaseDao.class);

    private static HybaseDaoKeyedPool hybaseDaoKeyedPool;

    private static HybaseJdbcDaoKeyedPool hybaseJdbcDaoKeyedPool;

    private static String path;

    private static int pageSize = 10000;

    static {
        String home = System.getProperty("user.dir");
        path = home.substring(0, home.indexOf(":") + 1) + "\\weixinReportTemp\\";
        hybaseDaoKeyedPool = (HybaseDaoKeyedPool) BeanUtil.getBean("hybaseDaoKeyedPool");
        hybaseJdbcDaoKeyedPool = (HybaseJdbcDaoKeyedPool) BeanUtil.getBean("hybaseJdbcDaoKeyedPool");
        logger.info("数据文件位置:" + path);
    }

    private static Set<String> readFile(String distinctColumn, String filePrefix) throws IOException {
        String urlTimeTag = "IR_URLTIME";
        String lastTimeTag = "IR_LASTTIME";
        Set<String> result = new HashSet<>();
        String[] paths = filePrefix.split("-");
        File dir = new File(path + "/" + paths[0] + "/" + paths[1] + "/" + paths[2]);
        File[] trsFiles = dir.listFiles();
        FileInputStream fis = null;
        BufferedReader br = null;
        int count = 0;
        String urlTime = "";
        String lastTime = "";
        List<String> interval = new ArrayList<>();
        for(int i = 0; i < trsFiles.length; i++) {
            File currentFile = trsFiles[i];
            if (currentFile.getName().indexOf("trs") != -1) {
                try {
                    fis = new FileInputStream(currentFile);
                    br = new BufferedReader(new InputStreamReader(fis, WeixinReportContants.ENCODING_TRSFILE));
                    String content = null;
                    while ((content = br.readLine()) != null) {
                        if(StringUtils.isBlank(content)) continue;
                        try {
                            if (content.contains("REC")) {
                                count++;
                            }
                            lastTime = br.readLine().split("=")[1];
                            urlTime = br.readLine().split("=")[1];
                            interval.add(String.valueOf(DateUtil.dataInterval(DateUtil.string2Date(urlTime, "yyyy/MM/dd HH:mm:ss"), DateUtil.string2Date(lastTime, "yyyy/MM/dd HH:mm:ss"))));
                        }catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("解析出错");
                        }
                    }
                } finally {
                    if(br != null)
                        br.close();
                    if(fis != null)
                        fis.close();
                }
            }
        }
        File intervalFile = new File(path + "/" + paths[0] + "/" + paths[1] + "/" + paths[2] + "/interval.txt");
        if(!intervalFile.exists())
            intervalFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(intervalFile);
        IOUtils.writeLines(interval, System.lineSeparator(),fos);
        return result;
    }

    public static boolean exportFile(String swhere, String source, String filePrefix) throws IOException {
        TRSConnection conn = null;
        try {
            if (WeixinReportContants.SOURCE_ZICAI.equals(source)) {
                conn = hybaseDaoKeyedPool.borrowObject(WeixinReportContants.HYBASE_DC_CONNINFO);
            } else if (WeixinReportContants.SOURCE_CAIGOU.equals(source)) {
                conn = hybaseDaoKeyedPool.borrowObject(WeixinReportContants.HYBASE_DH_CONNINFO);
            }
        } catch (Exception e) {
            logger.error("获取hybase链接失败!", e);
            return false;
        }
        TRSResultSet trsRs = null;
        SearchParams searchParams = new SearchParams();
        searchParams.setReadColumns("IR_URLTIME;IR_LASTTIME");
        TRSExport export;
        try {
            if (WeixinReportContants.SOURCE_ZICAI.equals(source)) {
                trsRs = conn.executeSelect(WeixinReportContants.HYBASE_ZICAI_DBNAME_DC, swhere, 0, 0, searchParams);
            } else if (WeixinReportContants.SOURCE_CAIGOU.equals(source)) {
                trsRs = conn.executeSelect(WeixinReportContants.HYBASE_CAIGOU_DBNAME_DH, swhere, 0, 0, searchParams);
            }
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            //1, 读取增量文件获得index
            int incrementIndex = getIncrement(filePrefix);

            //2, 调用hybase的export接口导出文件，写完后更新增量文件
            long start = incrementIndex + pageSize;
            String[] paths = filePrefix.split("-");
            while (start < trsRs.getNumFound()) {
                try {
                    //覆盖写
                    export = new TRSExport(conn, path + "/" + paths[0] + "/" + paths[1] + "/" + paths[2] + "/" + start + ".trs", false);
                    if (WeixinReportContants.SOURCE_ZICAI.equals(source)) {
                        export.export(WeixinReportContants.HYBASE_ZICAI_DBNAME_DC, swhere, start, pageSize, searchParams);
                    } else if (WeixinReportContants.SOURCE_CAIGOU.equals(source)) {
                        export.export(WeixinReportContants.HYBASE_CAIGOU_DBNAME_DH, swhere, start, pageSize, searchParams);
                    }
                } catch (Exception e) {
                    logger.error("导出出错:" +filePrefix + "-" + start, e);
                } finally {
                    //更新增量文件
                    writeIncrementFile(paths, start);
                    start += pageSize;
                }
            }
        } catch (Exception e) {
            logger.error(filePrefix + ", 获取数据出错!", e);
        } finally {
            if (trsRs != null)
                trsRs.close();
            if (WeixinReportContants.SOURCE_ZICAI.equals(source)) {
                hybaseDaoKeyedPool.returnObject(WeixinReportContants.HYBASE_DC_CONNINFO, conn);
            } else if (WeixinReportContants.SOURCE_CAIGOU.equals(source)) {
                hybaseDaoKeyedPool.returnObject(WeixinReportContants.HYBASE_DH_CONNINFO, conn);
            }
        }
        readFile(null, filePrefix);
        return true;
    }

    private static void writeIncrementFile(String[] paths, long index) throws IOException {
        File increment = new File(path + "/" + paths[0] + "/" + paths[1] + "/" + paths[2] + "/" +"index.txt");
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(increment);
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(String.valueOf(index));
            bw.flush();
            fos.flush();
        } finally {
            if(bw != null)
                bw.close();
            if(fos != null)
                fos.close();
        }
    }

    private static int getIncrement(String filePrefix) throws IOException {
        int incrementIndex = 0;
        String[] paths = filePrefix.split("-");

        File dir = new File(path + "/" + paths[0] + "/" + paths[1] + "/" + paths[2]);
        if(!dir.exists())
            dir.mkdirs();

        File increment = new File(dir.getPath() + "/" +"index.txt");
        if (!increment.exists())
            increment.createNewFile();
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(increment);
            br = new BufferedReader(new InputStreamReader(fis));
            String tmp = br.readLine();
            if (tmp != null) {
                incrementIndex = Integer.valueOf(tmp);
            }
        } finally {
            if(br != null)
                br.close();
            if(fis != null)
                fis.close();
        }
        return incrementIndex;
    }

    /**
     * 根据海贝检索表达式查询数量
     *
     * @param swhere
     * @return
     */
    public static long getHybaseCountRecords(String swhere, String source) {
        TRSConnection conn = null;
        try {
            if (WeixinReportContants.SOURCE_ZICAI.equals(source)) {
                conn = hybaseDaoKeyedPool.borrowObject(WeixinReportContants.HYBASE_DC_CONNINFO);
            } else if (WeixinReportContants.SOURCE_CAIGOU.equals(source)) {
                conn = hybaseDaoKeyedPool.borrowObject(WeixinReportContants.HYBASE_DH_CONNINFO);
            }
        } catch (Exception e) {
            logger.error("获取hybase链接失败!");
            return 0;
        }
        TRSResultSet trsRs = null;
        SearchParams searchParams = new SearchParams();
        try {
            if (WeixinReportContants.SOURCE_ZICAI.equals(source)) {
                trsRs = conn.executeSelect(WeixinReportContants.HYBASE_ZICAI_DBNAME_DC, swhere, 0, 0, searchParams);
            } else if (WeixinReportContants.SOURCE_CAIGOU.equals(source)) {
                trsRs = conn.executeSelect(WeixinReportContants.HYBASE_CAIGOU_DBNAME_DH, swhere, 0, 0, searchParams);
            }
            return trsRs.getNumFound();
        } catch (Exception e) {
            logger.error("查询出错!", e);
        } finally {
            if (trsRs != null)
                trsRs.close();
            if (conn != null) {
                if (WeixinReportContants.SOURCE_ZICAI.equals(source)) {
                    hybaseDaoKeyedPool.returnObject(WeixinReportContants.HYBASE_DC_CONNINFO, conn);
                } else if (WeixinReportContants.SOURCE_CAIGOU.equals(source)) {
                    hybaseDaoKeyedPool.returnObject(WeixinReportContants.HYBASE_DH_CONNINFO, conn);
                }
            }
        }
        return 0;
    }


    /**
     * 根据sparksql查询统计数量
     *
     * @param sqlwhere
     * @return
     */
    public static Set<String> getHybsCountRecordsBySQLFromDc(String sqlwhere) {
        Statement stmt = null;
        ResultSet res = null;
        Connection connection = null;
        Set<String> data = new HashSet<>();
        try {
            connection = hybaseJdbcDaoKeyedPool.borrowObject(WeixinReportContants.HYBASE_DC_JDBC_CONNINFO);
            stmt = connection.createStatement();
            res = stmt.executeQuery(sqlwhere);
            long count = 0;
            while (res.next()) {
                data.add(res.getString("data"));
            }
            return data;
        } catch (Exception e) {
            logger.error("查询失败!", e);
        } finally {
            if (res != null)
                try {
                    res.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null)
                hybaseJdbcDaoKeyedPool.returnObject(WeixinReportContants.HYBASE_DC_JDBC_CONNINFO, connection);
        }
        return data;
    }
}
