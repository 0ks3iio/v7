package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.dto.EmAimsStudentDto;
import net.zdsoft.exammanage.data.entity.EmAimsStudent;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;

public interface EmAimsStudentService extends BaseService<EmAimsStudent, String> {

    List<EmAimsStudent> findListBySchoolId(String examId,String aimsId, String schoolId, String field, String keyWord,
                                           Pagination page);

    List<EmAimsStudentDto> findListByStuId(String schoolId, String stuId);

    EmAimsStudentDto findByAimsIdAndStuId(String aimsId, String stuId);

    void saveAimsStu(EmAimsStudent aimsStu);


}
