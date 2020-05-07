package net.zdsoft.stuwork.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyAllCheck;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DyAllCheckDao extends BaseJpaRepositoryDao<DyAllCheck, String>{

	@Query("From DyAllCheck  where schoolId=?1 and acadyear=?2 and semester=?3 and week=?4 and classId=?5")
	DyAllCheck getAllCheckBy(String unitId,String acadyear,String semester,int week,String classId);
	
	@Query("From DyAllCheck  where schoolId=?1 and acadyear=?2 and semester=?3 and section =?4")
	List<DyAllCheck> getListBy(String unitId,String acadyear,String semester,int section);
	@Modifying
	@Query("delete From DyAllCheck  where schoolId=?1 and acadyear=?2 and semester=?3 and week =?4")
	void deleteBy(String unitId,String acadyear,String semester,int week);
}
