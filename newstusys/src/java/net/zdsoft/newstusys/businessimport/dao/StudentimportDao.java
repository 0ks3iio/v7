package net.zdsoft.newstusys.businessimport.dao;

import org.springframework.stereotype.Repository;

/**
 * 
 * @author weixh
 * 2019年2月22日	
 */
@Repository
public interface StudentimportDao {
	/**
     * 批量更新sql语句
     * 
     * @param sqls 要执行的SQL语句
     * @return
     */
    public int batchUpdate(String[] sqls);
}
