package net.zdsoft.credit.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.credit.data.entity.CreditPatchStudent;

import java.util.List;

public interface CreditPatchStudentService extends BaseService<CreditPatchStudent, String> {
    List<CreditPatchStudent> findListByParam(String year,String semester,String studentId,String gradeId,String classId,String subjectId);

}
