package net.zdsoft.bigdata.frame.data.hbase;

import net.zdsoft.bigdata.daq.data.component.BigSqlAnalyseLogController;
import net.zdsoft.bigdata.daq.data.entity.BigSqlAnalyseLog;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("hbaseClientService")
public class HbaseClientServiceImpl implements HbaseClientService {
    private static Logger logger = LoggerFactory
            .getLogger(HbaseClientServiceImpl.class);

    @Autowired
    private OptionService optionService;

    public static Configuration configuration;

    public static Connection connection;

    public static HBaseAdmin admin;

    @PostConstruct
    public void init() {
        try {
            OptionDto hbaseDto = optionService.getAllOptionParam("hbase");
            if (hbaseDto == null || hbaseDto.getStatus() == 0) {
                logger.error("Hbase服务未初始化");
                return;
            }
            configuration = HBaseConfiguration.create();

            configuration.set("hbase.zookeeper.property.clientPort", hbaseDto.getFrameParamMap()
                    .get("port"));
            configuration.set("hbase.zookeeper.quorum", hbaseDto.getFrameParamMap().get("domain"));
            configuration.set("hbase.master", hbaseDto.getFrameParamMap()
                    .get("master"));

            //configuration.set("hbase.master","ip1:60000");
            //configuration.set("hbase.zookeeper.quorum", "ip1:2181,ip2:2181") ;
            connection = ConnectionFactory.createConnection(configuration);
            admin = new HBaseAdmin(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建表
    public Boolean creatTable(String tableName, String[] familys)
            throws Exception {
        if (admin.tableExists(tableName)) {
            logger.error("table already exists!");
            return false;
        } else {
            //预分区
//            String[] s = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
//            int partition = 16;
//            byte[][] splitKeys = new byte[partition - 1][];
//            for (int i = 1; i < partition; i++) {
//                splitKeys[i - 1] = Bytes.toBytes(s[i - 1]);
//            }
            //createTable(tableName, columnFamilies, splitKeys);

            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            for (int i = 0; i < familys.length; i++) {
                tableDesc.addFamily(new HColumnDescriptor(familys[i]));
            }
            admin.createTable(tableDesc);

            String coprocessClassName = "org.apache.hadoop.hbase.coprocessor.AggregateImplementation";
            admin.disableTable(tableName);
            HTableDescriptor htd = admin.getTableDescriptor(Bytes.toBytes(tableName));
            htd.addCoprocessor(coprocessClassName);
            admin.modifyTable(tableName, htd);
            admin.enableTable(tableName);

            logger.info("create table " + tableName + " success");
            return true;
        }
    }

    //创建表
    public Boolean isExistTable(String tableName)
            throws Exception {
        if (admin.tableExists(tableName)) {
            return true;
        } else {
            return false;
        }
    }

    // 查看已有表
    @Deprecated
    public List<String> listTables() throws IOException {
        List<String> tableList = new ArrayList<>();
        HTableDescriptor hTableDescriptors[] = admin.listTables();
        for (HTableDescriptor hTableDescriptor : hTableDescriptors) {
            System.out.println(hTableDescriptor.getNameAsString());
            tableList.add(hTableDescriptor.getNameAsString());
        }
        return tableList;

    }

    public void putData(String tableName, String key, List<Json> datas) throws IOException {
        HTable table = new HTable(configuration, tableName);
        Put put = new Put(Bytes.toBytes(key));
        for (Json column : datas) {
            put.add(Bytes.toBytes(column.getString("family")), Bytes.toBytes(column.getString("column")), Bytes.toBytes(column.getString("value")));
        }

        table.put(put);
    }

    public void putDatas(String tableName, Map<String, List<Json>> datas) throws IOException {
        HTable table = new HTable(configuration, tableName);
        List<Put> putList = new ArrayList<Put>();
        for (String key : datas.keySet()) {
            Put put = new Put(Bytes.toBytes(key));
            for (Json column : datas.get(key)) {
                put.add(Bytes.toBytes(column.getString("family")), Bytes.toBytes(column.getString("column")), Bytes.toBytes(column.getString("value")));
            }
            putList.add(put);
        }
        table.put(putList);
    }

    /**
     * 最常用的方法，优化查询
     * 查询一行数据，
     *
     * @param tableName
     * @param rowKey
     * @param cols
     * @return
     */
    public Json getOneRowAndMultiColumn(String tableName, String rowKey, List<Json> cols) {
        Json result = null;
        try {
            long startTime=System.currentTimeMillis();
            Table table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(rowKey.getBytes());
            if (CollectionUtils.isNotEmpty(cols)) {
                for (Json col : cols) {
                    get.addColumn(Bytes.toBytes(col.getString("family")), Bytes.toBytes(col.getString("column")));
                }
            }
            Result rsResult = table.get(get);
            result = convertHbaseResult2Json(rsResult);
            long endTime = System.currentTimeMillis();
            long totalTime=endTime-startTime;
            BigSqlAnalyseLog bigSqlAnalyseLog=new BigSqlAnalyseLog();
            bigSqlAnalyseLog.setId(UuidUtils.generateUuid());
            bigSqlAnalyseLog.setBusinessName("");
            bigSqlAnalyseLog.setDbType("hbase");
            bigSqlAnalyseLog.setOperationTime(new Date());
            bigSqlAnalyseLog.setSql("");
            bigSqlAnalyseLog.setDuration(totalTime);
            BigSqlAnalyseLogController.submitBigSqlAnalyseLog(bigSqlAnalyseLog);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Long getTotalCount(String tableName) {
        long rowCount = 0;
        try {
            AggregationClient ac = new AggregationClient(configuration);
            Scan scan = new Scan();
            //scan.addFamily(Bytes.toBytes(family));
            try {
                long startTime=System.currentTimeMillis();
                rowCount = ac.rowCount(TableName.valueOf(tableName), new LongColumnInterpreter(), scan);
                long endTime = System.currentTimeMillis();
                long totalTime=endTime-startTime;
                BigSqlAnalyseLog bigSqlAnalyseLog=new BigSqlAnalyseLog();
                bigSqlAnalyseLog.setId(UuidUtils.generateUuid());
                bigSqlAnalyseLog.setBusinessName("");
                bigSqlAnalyseLog.setDbType("hbase");
                bigSqlAnalyseLog.setOperationTime(new Date());
                bigSqlAnalyseLog.setSql("");
                bigSqlAnalyseLog.setDuration(totalTime);
                BigSqlAnalyseLogController.submitBigSqlAnalyseLog(bigSqlAnalyseLog);
            } catch (Throwable e) {
                logger.info(e.getMessage(), e);
            }

            return rowCount;
        } catch (Exception e) {
            return rowCount;
        }

    }

    @Deprecated
    public List<Result> getRows(String tableName, String rowKeyLike) {
        List<Result> list = null;
        try {
            FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
            Table table = connection.getTable(TableName.valueOf(tableName));
            PrefixFilter filter = new PrefixFilter(rowKeyLike.getBytes());
            SingleColumnValueFilter filter1 = new SingleColumnValueFilter(
                    "order".getBytes(),
                    "order_type".getBytes(),
                    CompareFilter.CompareOp.EQUAL,
                    Bytes.toBytes("1")
            );
            fl.addFilter(filter);
            fl.addFilter(filter1);
            Scan scan = new Scan();
            scan.setFilter(fl);
            ResultScanner scanner = table.getScanner(scan);
            list = new ArrayList<Result>();
            for (Result rs : scanner) {
                list.add(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Deprecated
    public List<Result> getRows(String tableName, String rowKeyLike, String cols[]) {
        List<Result> list = null;
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            PrefixFilter filter = new PrefixFilter(rowKeyLike.getBytes());

            Scan scan = new Scan();
            for (int i = 0; i < cols.length; i++) {
                scan.addColumn("cf".getBytes(), cols[i].getBytes());
            }
            scan.setFilter(filter);
            ResultScanner scanner = table.getScanner(scan);
            list = new ArrayList<Result>();
            for (Result rs : scanner) {
                list.add(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Deprecated
    public List<Result> getRowsByOneKey(String tableName, String rowKeyLike, String cols[]) {
        List<Result> list = null;
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            PrefixFilter filter = new PrefixFilter(rowKeyLike.getBytes());

            Scan scan = new Scan();
            for (int i = 0; i < cols.length; i++) {
                scan.addColumn("cf".getBytes(), cols[i].getBytes());
            }
            scan.setFilter(filter);
            ResultScanner scanner = table.getScanner(scan);
            list = new ArrayList<Result>();
            for (Result rs : scanner) {
                list.add(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 范围查询
     *
     * @param tableName
     * @param startRow
     * @param stopRow
     * @return
     */
    @Deprecated
    public List<Result> getRows(String tableName, String startRow, String stopRow) {
        List<Result> list = null;
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.setStartRow(startRow.getBytes());
            scan.setStopRow(stopRow.getBytes());
            ResultScanner scanner = table.getScanner(scan);
            list = new ArrayList<Result>();
            for (Result rsResult : scanner) {
                list.add(rsResult);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Deprecated
    public void deleteRecords(String tableName, String rowKeyLike) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            PrefixFilter filter = new PrefixFilter(rowKeyLike.getBytes());
            Scan scan = new Scan();
            scan.setFilter(filter);
            ResultScanner scanner = table.getScanner(scan);
            List<Delete> list = new ArrayList<Delete>();
            for (Result rs : scanner) {
                Delete del = new Delete(rs.getRow());
                list.add(del);
            }
            table.delete(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addAFamily(String tableName,String familyName) throws IOException{
    	TableName tablename=TableName.valueOf(tableName);
		admin.disableTable(tablename);
		HTableDescriptor hDescriptor= admin.getTableDescriptor(tablename);
		HColumnDescriptor hColumnDescriptor=new HColumnDescriptor(familyName);
		hDescriptor.addFamily(hColumnDescriptor);
		admin.modifyTable(tablename, hDescriptor);
		admin.enableTable(tablename);
    }
    
    //删除ColumnFamily
    public boolean deleteColumnFamily(String tableName,String columnFamilyName) throws IOException {
    	if (admin.tableExists(tableName)) {
            try {
                admin.deleteColumn(tableName,columnFamilyName);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private Json convertHbaseResult2Json(Result rsResult) {
        Json json = new Json();
        // 输出结果,raw方法返回所有keyvalue数组
        for (KeyValue rowKV : rsResult.raw()) {
//            System.out.println(String.format("row:%s, family:%s, qualifier:%s, qualifiervalue:%s, timestamp:%s.",
//                    Bytes.toString(rowKV.getRow()),
//                    Bytes.toString(rowKV.getFamily()),
//                    Bytes.toString(rowKV.getQualifier()),
//                    Bytes.toString(rowKV.getValue()),
//                    rowKV.getTimestamp()));
        	System.out.println(Bytes.toString(rowKV.getRow()));
            json.put(Bytes.toString(rowKV.getQualifier()), Bytes.toString(rowKV.getValue()));
        }
        return json;
    }
    
}
