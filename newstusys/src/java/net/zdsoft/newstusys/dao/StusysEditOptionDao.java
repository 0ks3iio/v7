package net.zdsoft.newstusys.dao;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newstusys.entity.StusysEditOption;

/**
 * 
 * @author weixh
 * 2019年9月4日	
 */
public interface StusysEditOptionDao extends BaseJpaRepositoryDao<StusysEditOption, String> {
	@Query("FROM StusysEditOption WHERE unitId=?1")
	public StusysEditOption findByUnitId(String unitId);
}
