package net.zdsoft.syncdata.custom.laibin.service.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.syncdata.custom.laibin.constant.LaiBinConstant;
import net.zdsoft.syncdata.custom.laibin.service.LaiBinSyncService;
import net.zdsoft.syncdata.custom.yaohai.constant.YhBaseConstant;
import net.zdsoft.syncdata.util.DataBaseConnectionUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("laiBinSyncService")
public class LaiBinSyncServiceImpl implements LaiBinSyncService{
	private Logger log = Logger.getLogger(LaiBinSyncServiceImpl.class);
	private DataBaseConnectionUtil dataBaseConnectionUtil;
	private Connection conn;
	private ResultSet rs = null;
	private Statement statement = null;
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	@Override
	public void updateStuCardNum() {
		if(dataBaseConnectionUtil == null )
		     dataBaseConnectionUtil = getBaseConnectionUtil();
		System.out.println(dataBaseConnectionUtil.hashCode()+"------------------");
		try {
			int Count =  getCount(LaiBinConstant.SQLSERVER_STUDENT_TABLE_NAME);
			for (int k = 1; k < LaiBinConstant.LAIBIN_INDEX_DEFAULT; k++) {
				List<CardUserInfo> cardUserInfos  = getBaseDate (LaiBinConstant.SQLSERVER_STUDENT_TABLE_NAME,null,
						LaiBinConstant.SQLSERVER_STUDENT_MODIFT_TIME_NAME,CardUserInfo.class, k, LaiBinConstant.SQLSERVER_STUDENT_ORDER_NAME);
				if(CollectionUtils.isEmpty(cardUserInfos) || Count < YhBaseConstant.YAOHAI_PAGESIZE_DEFAULT * k){
					log.info("------- 学生分页:" + (k) + "获取不到数据");
					System.out.println("------- 学生分页:" + (k) + "数据已经超过总的数量");
					break;
				}
				if(CollectionUtils.isNotEmpty(cardUserInfos)) {
					System.out.println("更新学生卡号数据为：------------------" + cardUserInfos.size());
					log.info("更新学生卡号数据为----"+ cardUserInfos.size());
					Set<String> stuidSet = new HashSet<String>();
					cardUserInfos.forEach(c->{
						stuidSet.add(c.getStudent_Id());
					});
					List<Student> updateStudents = SUtils.dt(studentRemoteService.findListByIds(
							stuidSet.toArray(new String[stuidSet.size()])), Student.class);
				 	Map<String, Student> stuidMap = EntityUtils.getMap(updateStudents, Student::getId);
					List<Student> updaList = new ArrayList<>();
			        for (CardUserInfo info : cardUserInfos) {
			        	if(stuidMap.get(info.getStudent_Id()) != null){
							Student student = stuidMap.get(info.getStudent_Id());
							student.setCardNumber(String.valueOf(info.getFactoryFixId()));
							student.setModifyTime(new Date());
							updaList.add(student);
						}
					}
			        studentRemoteService.saveAll(updaList.toArray(new Student[updaList.size()]));
				}
			}
		} catch (Exception e) {
			log.error("更新学生卡号接口失败："+ e);
		} finally {
			try {
				DataBaseConnectionUtil.closeAll(conn, statement, rs);
			} catch (SQLException e) {
				log.error("关闭jdbc连接失败："+ e);
			}
		}
	}


	class CardUserInfo{
		private String student_Id;
		private long factoryFixId;
		public String getStudent_Id() {
			return student_Id;
		}
		public void setStudent_Id(String student_Id) {
			this.student_Id = student_Id;
		}
		public long getFactoryFixId() {
			return factoryFixId;
		}
		public void setFactoryFixId(long factoryFixId) {
			this.factoryFixId = factoryFixId;
		}
	}
	
	
	private DataBaseConnectionUtil getBaseConnectionUtil(){
		return dataBaseConnectionUtil = new DataBaseConnectionUtil(LaiBinConstant.SQLSERVER_CONNECTION_DRIVE, LaiBinConstant.SQLSERVER_CONNECTION_URL,
				LaiBinConstant.SQLSERVER_CONNECTION_USERNAME, LaiBinConstant.SQLSERVER_CONNECTION_PASSWORD);
	}
	
    private int getCount(String tableName) {
    	try {
			int count = 0;
			conn = getConnection();
			statement = (Statement) conn.createStatement();
			//要执行的SQL语句
			StringBuilder sql = new StringBuilder();
			sql.append("select count(*) from ");
			sql.append(tableName);
			rs = statement.executeQuery(sql.toString());
			if(rs != null){
			 	while (rs.next()) {
			 		count = (int) rs.getObject("count(*)");
			 	}
			 }
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("连接中间库，获取总的数量报错 ："+ e);
		}
    	return 0;
	}
	
	private <T>  List<T> getBaseDate(String tableName, String modifyTime, String modifyTimeName, Class<T> clazz, Integer page, String orderName) {
		try {
			System.out.println(dataBaseConnectionUtil.hashCode()+"------------------");
			conn = getConnection();
			statement = (Statement) conn.createStatement();
            //要执行的SQL语句
			StringBuilder sql = new StringBuilder();
			sql.append("select * from ");
			if(page != null){
				int a = YhBaseConstant.YAOHAI_PAGESIZE_DEFAULT * page;
				sql.append(" ( select top " + YhBaseConstant.YAOHAI_PAGESIZE_DEFAULT );
				sql.append(" * from ( select top  "  + a);
				sql.append(" * from ");
				sql.append(tableName);
				sql.append(" where Student_Id is not null");
				sql.append(" order by  ");
				sql.append( orderName + " asc ) as temp_sum_table ");
				sql.append(" order by  ");
				sql.append( orderName + " desc ) as temp_order ");
				sql.append(" order by  ");
				sql.append( orderName + " asc ");
			}else{
				sql.append(tableName);
			}
            //3.ResultSet类，用来存放获取的结果集！！
			System.out.println("请求的sql 语句是：-----------" + sql.toString() );
            rs = statement.executeQuery(sql.toString());
            ArrayList<Object> saveTs = new ArrayList<>();
            if(rs != null){
            	while (rs.next()) {
            		Object object = new CardUserInfo() ;
            		Field[] fields = clazz.getDeclaredFields(); 
            		for (int i = 0; i < fields.length; i++) {
            			Field field = fields[i];
            			if("this$0".equalsIgnoreCase(field.getName())){
            				continue;
            			}
            			String columnName = field.getName();
            			field.setAccessible(true);
            			field.set(object, rs.getObject(columnName));
            		}
            		saveTs.add(object);
            	}
            }
			return (ArrayList<T>) saveTs;
		}  catch (Exception e) {
			log.error("连接中间库，获取基础数据失败："+ e);
			return null;
	   }
	}
	
	
	
	
	
	public  Connection getConnection() {
		Connection conn = null; 
		try {
			conn = dataBaseConnectionUtil.getConnection();
			if(conn.isClosed()){
				System.out.println("連接以及關閉了------------------");
				conn = getBaseConnectionUtil().getConnection();
			}else{
				conn =  dataBaseConnectionUtil.getConnection();
			}
		} catch (Exception e) {e.printStackTrace();}
		return conn;

   }
}
