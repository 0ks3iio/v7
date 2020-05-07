package net.zdsoft.studevelop.mobile.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.mobile.entity.StuHonor;

public interface StuHonorDao extends BaseJpaRepositoryDao<StuHonor, String>{
	
	@Modifying
	@Query("update StuHonor set modifyTime = ?1 where id = ?2 ")
	void updateById(Date modifyTime, String id);
	
	@Query("From StuHonor where acadyear = ?1 and semester = ?2 and studentId = ?3 order by creationTime desc")
	List<StuHonor> findList(String acadyear, String semester, String studentId);
	
	@Query("From StuHonor where acadyear = ?1 and semester = ?2 and studentId = ?3 ")
	StuHonor findObj(String acadyear, String semester, String studentId);
	@Query("From StuHonor where acadyear = ?1 and semester = ?2 and studentId in ?3 order by creationTime desc")
	List<StuHonor> getStuHonorList(String acadyear, String semester, String[] studentIds);
}
