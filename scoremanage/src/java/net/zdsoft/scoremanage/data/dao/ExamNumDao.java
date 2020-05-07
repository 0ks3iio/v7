package net.zdsoft.scoremanage.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.ExamNum;

public interface ExamNumDao extends BaseJpaRepositoryDao<ExamNum, String>{

	public List<ExamNum> findByExamId(String examId);
	
	public List<ExamNum> findByExamIdAndSchoolId(String examId,String schoolId);

	@Modifying
	@Query("delete ExamNum where examId= ?1 and studentId in (?2)")
	public void deleteByexamIdStudent(String examId, String[] stuIds);
	@Modifying
	@Query("delete ExamNum where schoolId= ?1 and examId= ?2 ")
	public void deleteBySchoolIdExamId(String schoolId, String examId);

}
