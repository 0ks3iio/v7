package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.dto.EmHouseDto;
import net.zdsoft.exammanage.data.entity.ExammanageHouseRegister;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;


public interface ExammanageHouseRegisterService extends BaseService<ExammanageHouseRegister, String> {

    public List<ExammanageHouseRegister> getExammanageHouseRegisterByAcadyearAndSemesterAndExamId(String acadyear, String semester, String examId, Pagination page);
    
    public List<ExammanageHouseRegister> getExammanageHouseRegisterByExamIdAndStudentId(String examId, String studentId);

    public List<EmHouseDto> getExammanageHouseRegistersByAcadyearAndSemesterAndExamId(String acadyear, String semester, String examId);

}
