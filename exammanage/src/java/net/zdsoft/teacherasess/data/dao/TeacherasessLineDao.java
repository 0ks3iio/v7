package net.zdsoft.teacherasess.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.entity.TeacherAsessLine;

public interface TeacherasessLineDao extends BaseJpaRepositoryDao<TeacherAsessLine, String>{
	@Transactional
	@Modifying
	@Query("delete From TeacherAsessLine where assessId=?1")
	void deleteByAssessId(String teacherAsessId);
	
	@Query("From TeacherAsessLine where unitId=?1 and assessId=?2 and subjectId=?3 order by classId")
	public List<TeacherAsessLine> getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(String unitId,String assessId,String subjectId);
	
	@Query("select distinct(classId) from TeacherAsessLine where unitId=?1 and assessId=?2 and subjectId=?3 and classType=?4")
	public List<String> getListByUnitIdAndAsessIdAndSubjectIdAndClassType(String unitId,String assessId,String subjectId,String classType);

}
