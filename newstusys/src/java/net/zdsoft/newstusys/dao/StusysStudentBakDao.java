package net.zdsoft.newstusys.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newstusys.entity.StusysStudentBak;

/**
 * 
 * @author weixh
 * 2018年9月18日	
 */
public interface StusysStudentBakDao extends BaseJpaRepositoryDao<StusysStudentBak, String> {
	
	@Modifying
	@Query("DELETE FROM StusysStudentBak WHERE acadyear= ?1 AND semester=?2 AND schoolId=?3")
	public void deleteBySchIdSemester(String acadyear, int semester, String schId);
}
