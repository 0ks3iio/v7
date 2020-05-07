package net.zdsoft.newstusys.businessimport.dao.impl;

import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BasicDAO;
import net.zdsoft.newstusys.businessimport.dao.StudentimportDao;

/**
 * 
 * @author weixh
 * 2019年2月22日	
 */
@Repository
public class StudentimportDaoImpl extends BasicDAO implements StudentimportDao{

	@Override
	public int batchUpdate(String[] sqls) {
		if (null == sqls || sqls.length == 0)
            return 0;
	    
		int[] rtn = getJdbcTemplate().batchUpdate(sqls);
    	int count = 0;
    	for (int i : rtn) {
    		if(i>0)
			count += i;
		}
    	return count;
	}
	
}
