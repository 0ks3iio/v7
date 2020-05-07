package net.zdsoft.studevelop.mobile.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.mobile.entity.StuFamilyWishes;

public interface StuFamilyWishesDao extends BaseJpaRepositoryDao<StuFamilyWishes, String>{
	
	@Modifying
	@Query("update StuFamilyWishes set parentContent = ?1, childContent = ?2, modifyTime = ?3 where acadyear = ?4 and semester = ?5 and studentId = ?6")
	void update(String parentContent, String childContent, Date modifyTime, String acadyear, String semester, String studentId);
	
	@Query("From StuFamilyWishes where acadyear = ?1 and semester = ?2 and studentId = ?3")
	StuFamilyWishes findObj(String acadyear, String semester, String studentId);
	
	@Query("From StuFamilyWishes where studentId = ?1")
	List<StuFamilyWishes> findListByStudentId(String studentId);
	@Query("From StuFamilyWishes where acadyear = ?1 and semester = ?2 and studentId in ?3")
	List<StuFamilyWishes>  getStuFamily(String acadyear,String semester , String[] studentIds);
}
