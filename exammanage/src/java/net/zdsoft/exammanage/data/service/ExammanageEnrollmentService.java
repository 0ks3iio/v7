package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.ExammanageEnrollment;

import java.util.List;

public interface ExammanageEnrollmentService extends BaseService<ExammanageEnrollment, String> {

    public List<ExammanageEnrollment> findListByParams(String acadyear, String semester, String examId, String unitId);

}
