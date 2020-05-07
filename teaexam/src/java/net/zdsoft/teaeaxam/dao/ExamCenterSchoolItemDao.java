package net.zdsoft.teaeaxam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teaeaxam.entity.ExamCenterSchoolItem;

/**
 * 
 * @author weixh
 * 2019年7月19日	
 */
@Repository("examCenterSchoolItemDao")
public interface ExamCenterSchoolItemDao extends BaseJpaRepositoryDao<ExamCenterSchoolItem, String> {
	
	@Query("FROM ExamCenterSchoolItem WHERE centerSchoolId IN (?1)")
	public List<ExamCenterSchoolItem> findItemByCenterSchIds(String[] centerSchIds);
	
	@Query("FROM ExamCenterSchoolItem WHERE unitId in (?1)")
	public List<ExamCenterSchoolItem> findItemByUnitIds(String[] unitId);
}
