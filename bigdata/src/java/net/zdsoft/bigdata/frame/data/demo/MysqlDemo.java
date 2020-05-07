package net.zdsoft.bigdata.frame.data.demo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import net.zdsoft.bigdata.frame.data.kafka.KafkaProducerAdapterTest;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;

import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import com.mysql.jdbc.Driver;

public class MysqlDemo {

	public void createProc() throws Exception {

		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager
				.getConnection(
						"jdbc:mysql://192.168.20.51:3306/bigdata?useUnicode=true&characterEncoding=utf-8",
						"root", "zdsoft");
		String sql = "{CALL pro_num_user(?,?)}"; // 调用存储过程
		CallableStatement cstm = connection.prepareCall(sql); // 实例化对象cstm
		cstm.setString(1, "myd"); // 存储过程输入参数
		cstm.setString(2, ""); // 存储过程输入参数
		cstm.execute(); // 执行存储过程
		cstm.close();
		connection.close();

	}
	
	public void createDatas() throws Exception {
		Driver driver = (Driver) Class.forName("com.mysql.jdbc.Driver")
				.newInstance();
		Properties info = new Properties();
		info.put("user", "root");
		info.put("password", "zdsoft");
		Connection conn = driver
				.connect(
						"jdbc:mysql://192.168.20.51:3306/bigdata?useUnicode=true&characterEncoding=utf-8",
						info);
		String sql = "select 学员id,学员姓名,地区省,所属机构名称,事业部,学生来源名称,注册时间 from trans_khw_register where  注册时间 >= '2018-07-01 00:00:00'";
		// String sql =
		// "select 学员id,学员姓名,地区省,所属机构名称,事业部,学生来源名称,注册时间 from trans_khw_register where  学员id = 208624938";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Json result = new Json();

			result.put("org_name", rs.getString("所属机构名称"));
			result.put("org_province", rs.getString("地区省"));
			result.put("business_dept", rs.getString("事业部"));
			result.put("student_source", rs.getString("学生来源名称"));
			result.put("student_id", "" + rs.getInt("学员id"));
			result.put("student_name", rs.getString("学员姓名"));
			Timestamp originalDate = rs.getTimestamp("注册时间");
			java.util.Date convertedDate = new java.util.Date(
					originalDate.getTime());
			DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(
					"yyyy-MM-dd HH:mm:ss").withChronology(
					ISOChronology.getInstance(DateTimeZone
							.forID("Asia/Shanghai")));
			String realDate = dateTimeFormatter.parseDateTime(
					DateUtils.date2String(convertedDate, "yyyy-MM-dd")
							+ " 08:00:00").toString();
			result.put("timestamp", realDate);
			try {
				KafkaProducerAdapterTest producer = KafkaProducerAdapterTest
						.getInstance();
				producer.send("kafka_khw", result.toString());
				System.out.println(result.toString());
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ps.close();
		conn.close();
	}

	@Test
	public void buyCourse() throws Exception {
		Driver driver = (Driver) Class.forName("com.mysql.jdbc.Driver")
				.newInstance();
		Properties info = new Properties();
		info.put("user", "root");
		info.put("password", "zdsoft");
		Connection conn = driver
				.connect(
						"jdbc:mysql://192.168.20.51:3306/bigdata?useUnicode=true&characterEncoding=utf-8",
						info);
		String sql = "select 学员id,学员姓名,地区省,所属机构名称,事业部,学生来源,支付时间,课程数 from trans_khw_order where  学生来源 = '自注册'";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Json result = new Json();
			result.put("student_id", "" + rs.getInt("学员id"));
			result.put("student_name", rs.getString("学员姓名"));
			result.put("org_province", rs.getString("地区省"));
			result.put("org_name", rs.getString("所属机构名称"));
			result.put("business_dept", rs.getString("事业部"));
			result.put("student_source", rs.getString("学生来源"));
			Timestamp originalDate = rs.getTimestamp("支付时间");
			result.put("course_num", rs.getInt("课程数"));

			java.util.Date convertedDate = new java.util.Date(
					originalDate.getTime());
			DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(
					"yyyy-MM-dd HH:mm:ss").withChronology(
					ISOChronology.getInstance(DateTimeZone
							.forID("Asia/Shanghai")));
			String realDate = dateTimeFormatter.parseDateTime(
					DateUtils.date2String(convertedDate, "yyyy-MM-dd")
							+ " 08:00:00").toString();
			result.put("timestamp", realDate);
			try {
				KafkaProducerAdapterTest producer = KafkaProducerAdapterTest
						.getInstance();
				producer.send("kafka_khw", result.toString());
				System.out.println(result.toString());
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ps.close();
		conn.close();
	}
	
	public static void main(String[] args) {
//		DateTimeFormatter dtFormatter=DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");    
//		/**输出结果 2016-08-29T22:58:20.000Z*/  
//		String result = dtFormatter.parseDateTime("2016-08-29 22:58:20").withZone(DateTimeZone.UTC).toString();  
//		System.out.println(result);
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(
				"yyyy-MM-dd HH:mm:ss").withChronology(
				ISOChronology.getInstance(DateTimeZone
						.forID("Asia/Shanghai")));
		String realDate = dateTimeFormatter.parseDateTime(
				DateUtils.date2String(new Date(), "yyyy-MM-dd")
						+ " 08:00:00").toString();
		System.out.println(realDate);
	}

}
