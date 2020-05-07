package net.zdsoft.syncdata.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConnectionUtil {
		private String driver;
	    private String url;
	    private String user;
	    private String password;
	    public DataBaseConnectionUtil(String driver,String url,String user,String password){
	    	this.driver = driver;
	    	this.url = url;
	    	this.user = user;
	    	this.password = password;
	    }
		public Connection getConnection() throws SQLException, ClassNotFoundException {
			Connection conn = null;
	        try {
	        	Class.forName(driver);
	        	conn = (Connection) DriverManager.getConnection(url, user, password);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		    return conn;
	    }
		
	    public static void closeAll(Connection conn,Statement stmt,ResultSet rs) throws SQLException {
	        if(rs!=null) {
	            rs.close();
	        }
	        if(stmt!=null) {
	            stmt.close();
	        }
	        if(conn!=null) {
	            conn.close();
	        }
	    }
	    public int executeSQL(String preparedSql, Object[] param) throws ClassNotFoundException {
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        /* 处理SQL,执行SQL */
	        try {
	            conn = getConnection(); // 得到数据库连接
	            pstmt = (PreparedStatement) conn.prepareStatement(preparedSql); // 得到PreparedStatement对象
	            if (param != null) {
	                for (int i = 0; i < param.length; i++) {
	                    pstmt.setObject(i + 1, param[i]); // 为预编译sql设置参数
	                }
	            }
	        ResultSet num = pstmt.executeQuery(); // 执行SQL语句
	        } catch (SQLException e) {
	            e.printStackTrace(); // 处理SQLException异常
	        } finally {
	            try {
	            	DataBaseConnectionUtil.closeAll(conn, pstmt, null);
	            } catch (SQLException e) {    
	                e.printStackTrace();
	            }
	        }
	        return 0;
	    }
}
