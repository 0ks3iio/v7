package net.zdsoft.syncdata.custom.yaohai.connect;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.zdsoft.syncdata.custom.yaohai.constant.YhBaseConstant;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class MysqlConnect {
	
	private static Connection connection;
	
    private static String driver=YhBaseConstant.MYSQL_CONNECTION_DRIVE;
    private static String url=YhBaseConstant.MYSQL_CONNECTION_URL;
    private static String user=YhBaseConstant.MYSQL_CONNECTION_USERNAME;
    private static String password=YhBaseConstant.MYSQL_CONNECTION_PASSWORD;
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
        
    public static void setConnection(Connection connection) {
    	MysqlConnect.connection = connection;
	}

	public static Connection getConnection() throws SQLException {
		return (Connection) DriverManager.getConnection(url, user, password);
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
            	MysqlConnect.closeAll(conn, pstmt, null);
            } catch (SQLException e) {    
                e.printStackTrace();
            }
        }
        return 0;
    }
    
}