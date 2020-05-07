package net.zdsoft.studevelop.mobile.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.mobile.entity.StuIntroduction;

public interface StuIntroductionDao extends BaseJpaRepositoryDao<StuIntroduction, String>{
	
	@Modifying
	@Query("update StuIntroduction set speciality = ?1, content = ?2, modifyTime = ?3, hasRelease = ?4 where acadyear = ?5 and semester = ?6 and studentId = ?7")
	void update(String speciality, String content, Date modifyTime, Integer hasRelease, String acadyear, String semester, String studentId);
	
	@Query("From StuIntroduction where acadyear = ?1 and semester = ?2 and studentId = ?3 ")
	StuIntroduction findObj(String acadyear, String semester, String studentId);

	@Query("From StuIntroduction where acadyear = ?1 and semester = ?2 and studentId in ?3 ")
	List<StuIntroduction> findStudentsIntro(String acadyear, String semester, String[] studentIds);
	
	@Query("From StuIntroduction where studentId = ?1 and hasRelease = ?2 order by acadyear,semester desc")
	List<StuIntroduction>  findListByStudentId(String studentId,Integer hasRelease);
	
	@Query("From StuIntroduction where studentId = ?1 order by acadyear desc,semester desc")
	List<StuIntroduction>  findListByStudentId(String studentId);
}
