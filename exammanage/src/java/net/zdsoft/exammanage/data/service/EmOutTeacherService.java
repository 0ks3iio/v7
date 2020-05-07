package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmOutTeacher;

import java.util.List;

public interface EmOutTeacherService extends BaseService<EmOutTeacher, String> {


    /**
     * 通过考试Id和学校Id查找所有的外编老师
     *
     * @param examId
     * @return
     */
    public List<EmOutTeacher> findByExamIdAndSchoolId(String examId, String unitId);

    /**
     * 查找所有的外编老师
     *
     * @param unitId
     * @return
     */
    public List<EmOutTeacher> findBySchoolId(String unitId);

    /**
     * 新增外编老师
     *
     * @param outTeacher
     */
    public void saveOne(EmOutTeacher outTeacher);

    /**
     * 批量新增外编老师
     *
     * @param insertList
     */
    public void saveAll(List<EmOutTeacher> insertList);

}
