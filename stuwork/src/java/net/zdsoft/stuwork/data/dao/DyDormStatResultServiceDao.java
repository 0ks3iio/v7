package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyDormStatResult;

public interface DyDormStatResultServiceDao extends BaseJpaRepositoryDao<DyDormStatResult, String>{
	@Modifying
	@Query("DELETE FROM DyDormStatResult WHERE schoolId= ?1 and acadyear=?2 and semester=?3 and week=?4 and classId in (?5)")
	public void deleteBySth(String schoolId,String acadyear,String semester,int week,String[] classIds);
	
	@Query("FROM DyDormStatResult WHERE schoolId = ?1  and acadyear= ?2 and semester= ?3 and week= ?4 and classId = ?5")
	public List<DyDormStatResult> findListByCon(String schoolId,String acadyear,String semester,int week,String classId);
}
