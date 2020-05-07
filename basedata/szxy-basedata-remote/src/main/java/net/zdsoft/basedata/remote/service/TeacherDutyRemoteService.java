package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.TeacherDuty;

public interface TeacherDutyRemoteService extends BaseRemoteService<TeacherDuty,String> {

    /**
     * 根据职务code及教师ids获取教师职务列表
     *
     * @param dutyCode
     * @param teacherIds
     * @return
     */
    public String findByTeacherIds(String dutyCode, String[] teacherIds);

    /**
     * 根据教师id获取教师对应的职务
     *
     * @param teacherIds
     * @return
     */
    public String findByTeacherIds(String[] teacherIds);

    /**
     * 根据单位id获取职务列表
     *
     * @param unitId
     * @return
     */
    public String findDutysByUnitId(String unitId);

    /**
     * 查询单位职务用户
     * 
     * @param unitId
     * @param dutyCode
     * @return
     */
    public String findDutysByUnitIdAndDuty(String unitId, String dutyCode);

}
