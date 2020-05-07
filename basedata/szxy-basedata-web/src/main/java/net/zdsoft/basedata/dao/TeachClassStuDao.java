/*
* Project: v7
* Author : shenke
* @(#) TeachClassStu.java Created on 2016-9-26
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @description: 教学班学生
 * @author: shenke
 * @version: 1.0
 * @date: 2016-9-26下午3:04:12
 */
@Repository
public interface TeachClassStuDao extends BaseJpaRepositoryDao<TeachClassStu,String>{

	@Modifying
	@Query("update TeachClassStu set isDeleted=1,modifyTime=sysdate where classId = ?1")
	void deleteByClassId(String classId);
	
	@Query("From TeachClassStu where isDeleted=0 and classId in (?1)")
	public List<TeachClassStu> findByClassIds(String[] classIds);

	
	@Modifying
	@Query("update TeachClassStu set isDeleted=1,modifyTime=sysdate where classId in (?1)")
	public void deleteByClassIds(String[] classIds);

	
	@Query("From TeachClassStu where isDeleted=0 and studentId in (?1)")
	public List<TeachClassStu> findByStuIds(String[] stuIds);
	
	@Modifying
	@Query("update TeachClassStu set isDeleted=1,modifyTime=sysdate where id in (?1)")
	void deleteByIds(String[] ids);
	
	@Modifying
	@Query("update TeachClassStu set isDeleted=1,modifyTime=sysdate where classId in (?1) and studentId in (?2)")
	int delete(String[] teachClassId, String[] studentId);

	void deleteByStudentIdIn(String... studentIds);

	@Modifying
	@Query("update TeachClassStu s set isDeleted=1,modifyTime=sysdate where exists(select 1 from TeachClass tc where tc.id=s.classId and tc.gradeId in (?1))")
	void deleteByGradeIds(String... gradeIds);

	@Modifying
	@Query("update TeachClassStu s set isDeleted=1,modifyTime=sysdate where exists(select 1 from TeachClass tc where tc.id=s.classId and tc.courseId in (?1))")
	void deleteBySubjectIds(String... subjectIds);
}
