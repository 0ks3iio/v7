package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.TeachClassStu;

public interface TeachClassStuRemoteService extends BaseRemoteService<TeachClassStu,String> {

    /**
     * 根据教学班的id，获取教学班学生，并组装成Map
     * 
     * @param classIds
     * @return Map&lt;studentId, List&lt;teachClassId&gt;&gt;
     */
    public String findMapWithStuIdByClassIds(String[] classIds);

    /**
     * 根据教学班的id，获取教学班学生
     * 
     * @param classIds
     * @return
     */
    public String findByClassIds(String[] classIds);

    /**
     * 根据学生id，获取相关的教学班
     * 
     * @param stuIds
     * @return
     */
    public String findByStuIds(String[] stuIds);

    /**
     * 批量保存
     * TeachClassStu[]
     */
    public void saveAll(String teachClassStues);

    /**
     * 软删
     * 
     * @param array
     */
    public void deleteByIds(String[] array);

    /**
     * 查询指定学生某个学年学期的教学班列表实际教学班
     * 
     * @author dingw
     * @param studentId
     * @param acadyear
     * @param semester
     * @return JSON(List<TeachClass>)
     */
    public String findTeachClassByStudentId(String studentId, String acadyear, String semester);

    /**
     * 查询指定学生某个学年学期的教学班列表 包含合班和小班
     * @param studentId
     * @param acadyear
     * @param semester
     * @return JSON(List<TeachClass>)
     */
    public String findTeachClassByStudentId2(String studentId, String acadyear, String semester);

	public void delete(String[] teachClassId, String[] studentId);

	/**
     * 根据教学班的id，获取教学班学生
     * 如果是大班就转换下返回大班对应下所有小班下的学生
     * @param classIds
     * @return
     */ 
    public String findStudentByClassIds(String[] classIds);
}
