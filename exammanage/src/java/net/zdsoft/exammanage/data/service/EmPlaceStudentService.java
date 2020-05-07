package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.dto.EmTicketDto;
import net.zdsoft.exammanage.data.entity.EmExamNum;
import net.zdsoft.exammanage.data.entity.EmPlaceStudent;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmPlaceStudentService extends BaseService<EmPlaceStudent, String> {

    public List<EmPlaceStudent> saveAllEntitys(EmPlaceStudent... emPlaceStudent);

    public List<EmPlaceStudent> findByNewExamPlaceIdAndGroupId(String groupId, String... examPlaceId);

    public List<EmPlaceStudent> findByExamPlaceIdAndGroupId(String groupId, String... examPlaceId);

    public List<EmPlaceStudent> findByExamIdAndSchoolIdAndGroupId(String examId, String schoolId, String groupId);

    public List<EmPlaceStudent> findByExamIdAndSchoolIdAndGroupIdIn(String examId, String schoolId, String[] groupIds);

    public List<EmPlaceStudent> findByExamIdAndPlaceId(String examId, String placeId);

    public List<EmPlaceStudent> findByExamIdAndPlaceIdAndGroupId(String examId, String placeId, String groupId);

    public List<EmPlaceStudent> findByExamIdStuIds(String examId, String[] stuids);

    /**
     * 获取examId 对应考场学生信息
     *
     * @param examIds
     * @return
     */
    public Map<String, List<EmPlaceStudent>> findMapByExamIds(String[] examIds);

    public Set<String> getExamIds(String[] examIds);

    public Set<String> getPlaceIdSet(String examId);

    public void insertList(Set<String> emPlaceIds,
                           List<EmPlaceStudent> emStudentList, List<EmExamNum> examNumList);

    /**
     * 清空某单位某次考试考场学生安排
     *
     * @param examId
     */
    public void deleteByExamId(String examId, String schoolId);

    public void deleteByExamId(String examId);

    public void deleteByEmPlaceIds(String... emPlaceIds);

    public List<EmPlaceStudent> findByExamId(String examId);

    public List<EmTicketDto> findByExamIdStuIds(String examId, List<Student> stus);

    public Set<String> getClsSchMap(String examId);

    public Map<String, Integer> getAllCountMap(String[] examIds);

    public String autoByExamNum(String unitId, String examId, String isgkExamType, String chooseType);

    public List<EmPlaceStudent> findByExamPlaceId(String examPlaceId);

    public List<EmPlaceStudent> findByExamPlaceIds(String[] examPlaceIds);

    public Map<String, Integer> getCountMapByPlaceIds(String examId, String groupId, Set<String> emPlaceIds);

    public boolean hasStudent(String examId, String unitId);

    public List<EmPlaceStudent> findByExamIdAndGroupId(String examId, String emSubGroupId);

    public Map<String, String> findByExamIdAndStuIds(String examId, String[] stuIds);

}
