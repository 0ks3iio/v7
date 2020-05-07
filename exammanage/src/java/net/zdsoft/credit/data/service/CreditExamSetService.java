package net.zdsoft.credit.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.credit.data.entity.CreditExamSet;

public interface CreditExamSetService extends BaseService<CreditExamSet, String> {

	List<CreditExamSet> findBySetIdAndAcadyearAndSemesterAndGradeIdAndType(String setId, String acadyear, String semester,
			String gradeId, String type);

	List<CreditExamSet> findByUsualSet(String setId, String acadyear, String semester, String subjectId, String classId,
			String classType);

	void saveUsualSet(String unitId, String userId, String setId, String gradeId, String classId, String classType,
			String subjectId, int maxRow);

}
