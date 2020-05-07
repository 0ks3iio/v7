/*
 * Project: v7
 * Author : shenke
 * @(#) TeachClassStuService.java Created on 2016-9-26
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;

/**
 * @description: 教学班学生
 * @author: shenke
 * @version: 1.0
 * @date: 2016-9-26下午3:06:34
 */
public interface TeachClassStuService extends BaseService<TeachClassStu, String> {

    /**
     * 根据教学班Id删除学生
     * 
     * @param classId
     */
    public void deleteByClassId(String classId);

    /**
     * 学生id,教学班ids
     * 
     * @param classIds
     * @return
     */

    public Map<String, List<String>> findMapByClassIds(String[] classIds);

    /**
     * 根据条件获取数据
     * 
     * @param classIds
     * @return
     */
    public List<TeachClassStu> findByClassIds(String[] classIds);
    public List<TeachClassStu> findByClassIdsWithMaster(String[] classIds);
    
    public List<TeachClassStu> findByStuIds(String[] stuIds);

    public void deleteByIds(String[] ids);

    public List<TeachClassStu> saveAllEntitys(TeachClassStu... teachClassStu);

    /**
     * 查询指定学生某个学年学期的教学班信息
     * 
     * @author dingw
     * @param studentId
     * @param acadyear
     * @param semester
     * @return
     */
    List<TeachClass> findByStudentId(String studentId, String acadyear, String semester);
    /**
	 * 删除某个教学班下的学生
	 * @param teachClassId
	 * @param studentId
	 * @return 执行条数
	 */
	public int delete(String[] teachClassId, String[] studentId);
	
	/**
	 * key:classId value:学生人数
	 * @param classIds
	 * @return
	 */
    public Map<String,Integer> countByClassIds(String[] classIds);

    /**
     * 查询指定学生某个学年学期的教学班信息 包含合班和小班
     * 
     * @param studentId
     * @param acadyear
     * @param semester
     * @return
     */
	public List<TeachClass> findByStudentId2(String studentId, String acadyear, String semester);
	
	public List<TeachClass> findByStudentIds(String[] studentIds, String acadyear, String semester);
	
	/**
     * 根据教学班的id，获取教学班学生
     * 如果是大班就转换下返回大班对应下所有小班下的学生
     * @param classIds
     * @return
     */ 
    public List<TeachClassStu> findStudentByClassIds(String[] classIds);

    /**
     * 删除学生后删除教学班中的该学生
     * @param studentIds
     */
	public void deleteByStudentIds(String... studentIds);

	/**
	 * 删除年级后删除年级下所有教学班中的学生
	 * @param gradeIds
	 */
	public void deleteByGradeIds(String... gradeIds);

	/**
	 * 删除课程后删除课程相关教学班中的学生
	 * @param subjectIds
	 */
	public void deleteBySubjectIds(String... subjectIds);
	
}
