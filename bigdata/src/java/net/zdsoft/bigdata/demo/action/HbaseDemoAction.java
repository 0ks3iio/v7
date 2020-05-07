package net.zdsoft.bigdata.demo.action;

import net.zdsoft.bigdata.frame.data.hbase.HbaseClientService;
import net.zdsoft.bigdata.frame.data.phoenix.PhoenixClientService;
import net.zdsoft.bigdata.frame.data.redis.BgRedisUtils;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/bigdata/common/demo")
public class HbaseDemoAction extends BigdataBaseAction {

    @Autowired
    private HbaseClientService hbaseClientService;
    @Autowired
    private PhoenixClientService phoenixClientService;

    @RequestMapping("/hbase")
    public String map(ModelMap map) throws Exception {
        map.put("resultList", new ArrayList<>());

        String sql = "select * from DWD_APP_STUDENT_TAG_FULLY where ID ='test' ";
//        List<Json> resultList = phoenixClientService.getDataListFromPhoenix(null, sql, new ArrayList<>());
//        hbaseClientService.putData("DWD_APP_STUDENT_TAG_FULLY", "test", datas);
        Json result = hbaseClientService.getOneRowAndMultiColumn("DWD_APP_STUDENT_TAG_FULLY", "81673121184010846049111360805008", null);
//        System.out.print(Json.toJSONString(resultList));
        System.out.print(result.toJSONString());
        Long num = hbaseClientService.getTotalCount("DWD_APP_STUDENT_TAG_FULLY");
        System.out.print(num);


        String sql1 = "SELECT * FROM DWC_STAT_METADATA_DAILY";
        List<Json> resultList1 = phoenixClientService.getDataListFromPhoenix(null, sql1, new ArrayList<>());
        for(Json result1:resultList1){
            System.out.print(result1.toJSONString());
        }
        return "/bigdata/demo/hbaseQuery.ftl";
    }

    @RequestMapping("/hbase1")
    public void hbase1() throws Exception {
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 这里配置zookeeper的地址，可单个，也可多个。可以是域名或者ip
        String url = "jdbc:phoenix:172.16.10.113,172.16.10.115,172.16.10.116:2181";
        Connection conn = DriverManager.getConnection(url);
        Statement statement = conn.createStatement();

//        String sql1="upsert into HBASE_PROPERTY_TEST values('00000000000000000000000000000022','test22')";
//        String sql2="upsert into HBASE_PROPERTY_TEST values('00000000000000000000000000000007','test7')";
//        String sql3="upsert into HBASE_PROPERTY_TEST values('00000000000000000000000000000008','test8')";
//        String sql4="upsert into HBASE_PROPERTY_TEST values('00000000000000000000000000000009','test9')";
//        String sql5="upsert into HBASE_PROPERTY_TEST values('00000000000000000000000000000010','test10')";
//        statement.executeUpdate(sql1);
//        statement.executeUpdate(sql2);
//        statement.executeUpdate(sql3);
//        statement.executeUpdate(sql4);
//        statement.executeUpdate(sql5);
//        conn.commit();

        String sql = "select count(1) from HBASE_PROPERTY_TEST";
        //String sql="select region_code,count(1) from HBASE_TEST_1 group by region_code";
//		sql = "select age,count(1) from DEMO_HBASE_4_HIVE group by age";
        long time = System.currentTimeMillis();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
//			String  host = rs.getString("HOST");
//			String  domain = rs.getString("DOMAIN");
//			System.out.println("host is " + host +" and domain is"+domain);
//			System.out.println("count = " + rs.getString(1));
//			System.out.print("age = "+rs.getString(1));
            System.out.println("count = " + rs.getInt(1));
//			System.out.println("count = " + rs.getString(3));
        }
//		long timeUsed = System.currentTimeMillis() - time;
//		System.out.println("time " + timeUsed + "mm");
        // 关闭连接
        rs.close();
        statement.close();
        conn.close();
    }
}
