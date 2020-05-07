package net.zdsoft.exammanage.data.dao;


import net.zdsoft.exammanage.data.entity.ExammanageHouseRegister;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExammanageHouseRegisterDao extends BaseJpaRepositoryDao<ExammanageHouseRegister, String> {

	@Query("From ExammanageHouseRegister where examId=?1 and studentId=?2")
	public List<ExammanageHouseRegister> getExammanageHouseRegistersByExamIdAndStudentId(String examId,String studentId);
	
	@Query("From ExammanageHouseRegister where acadyear=?1 and semester=?2 and examId=?3 ")
	public List<ExammanageHouseRegister> getExammanageHouseRegisterByAcadyearAndSemesterAndExamId(String acadyear, String semester, String examId);
}
