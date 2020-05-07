package net.zdsoft.bigdata.extend.data.dao;

public interface WarningResultJdbcDao {
	 /**
     * 执行语法
     * 
     * @param sql
     * @param objs
     *            对应sql中的参数
     */
    public void execSql(String sql, Object[] objs);

}
