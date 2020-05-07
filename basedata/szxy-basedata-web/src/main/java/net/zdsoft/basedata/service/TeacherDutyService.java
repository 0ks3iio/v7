package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.TeacherDuty;

public interface TeacherDutyService extends BaseService<TeacherDuty, String> {
    /**
     * 根据职务code及教师ids获取教师职务列表
     * 
     * @param dutyCode
     * @param teacherIds
     * @return
     */
    public List<TeacherDuty> findByTeacherIds(String dutyCode, String[] teacherIds);

    /**
     * 根据教师id获取教师对应的职务
     * 
     * @param teacherIds
     * @return
     */
    public List<TeacherDuty> findByTeacherIds(String[] teacherIds);

}
