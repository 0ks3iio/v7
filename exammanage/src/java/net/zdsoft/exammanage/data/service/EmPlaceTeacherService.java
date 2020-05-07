package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmPlaceTeacher;

import java.util.List;

public interface EmPlaceTeacherService extends BaseService<EmPlaceTeacher, String> {

    /**
     * 通过考试Id和类型查找所有的巡考或监考老师
     *
     * @param examId
     * @param i
     * @return
     */
    public List<EmPlaceTeacher> findByExamIdAndType(String examId, String type);

    /**
     * 保存巡考或监考老师设置
     *
     * @param emPlaceTeacher
     * @param emPlaceTeacherId
     */
    public void saveAndDel(EmPlaceTeacher emPlaceTeacher, String emPlaceTeacherId);

    /**
     * 查询某门考试某科目的监考或巡考老师
     *
     * @param examId
     * @param subjectId
     * @param tEACHER_TYPE1
     * @return
     */
    public List<EmPlaceTeacher> findByExamIdAndSubjectIdAndType(String examId, String subjectId, String tEACHER_TYPE1);

    public void deleteByEmPlaceIds(String... emPlaceIds);

    /**
     * 批量保存监考或巡考老师
     *
     * @param examId
     * @param invigilateList
     */
    public void saveAllAndDel(String examId, List<EmPlaceTeacher> invigilateList, String tEACHER_TYPE);

    /**
     * 查询所有的监考和巡考老师
     *
     * @param examId
     * @return
     */
    public List<EmPlaceTeacher> findByExamId(String examId);

    /**
     * 查询某单位在某段时间内的所有巡考或监考老师情况
     *
     * @param unitId
     * @param nowTime
     * @param tEACHER_TYPE2
     * @param tEACHER_TYPE22
     * @return
     */
    public List<EmPlaceTeacher> findByUnitIdAndStartTimeAndType(String unitId, String startTime, String endTime, String tEACHER_TYPE);

    public void deleteAllPlaceTeacher(String subjectId, String examId, String unitId);

    /**
     * 查询校内老师监考巡考数据
     *
     * @param unitId
     * @param examId
     * @param teacherId
     * @return
     */
    public List<EmPlaceTeacher> findByTeacherIn(String unitId, String examId,
                                                String teacherId);

    /**
     * 查询监考老师数据
     *
     * @param examId
     * @param placeId
     * @param subjectId
     * @param type
     * @return
     */
    public List<EmPlaceTeacher> findByExamIdAndPlaceIdAndSubjectId(String examId, String placeId, String subjectId, String type);

}
