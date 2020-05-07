/*
 * Project: v7
 * Author : shenke
 * @(#) TeachClassStuServiceimpl.java Created on 2016-9-26
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.dao.StudentDao;
import net.zdsoft.basedata.dao.TeachClassDao;
import net.zdsoft.basedata.dao.TeachClassStuDao;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachClassStuService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: TODO
 * @author: shenke
 * @version: 1.0
 * @date: 2016-9-26下午3:07:24
 */
@Service("teachClassStuService")
public class TeachClassStuServiceimpl extends BaseServiceImpl<TeachClassStu, String> implements TeachClassStuService {

    @Autowired
    private TeachClassStuDao teachClassstuDao;
    @Autowired
    private TeachClassDao teachClassDao;
    @Autowired
    private TeachClassService teachClassService;
	@Autowired
	private StudentDao studentDao;
    @Override
    protected BaseJpaRepositoryDao<TeachClassStu, String> getJpaDao() {
        return teachClassstuDao;
    }

    @Override
    protected Class<TeachClassStu> getEntityClass() {
        return TeachClassStu.class;
    }

    @Override
    public void deleteByClassId(String classId) {
        teachClassstuDao.deleteByClassId(classId);
    }

    @Override
    public void deleteByIds(String[] ids) {
        teachClassstuDao.deleteByIds(ids);
    };

    @Override
    public Map<String, List<String>> findMapByClassIds(String[] classIds) {
    	Map<String, List<String>> map = new HashMap<String, List<String>>();
    	if(ArrayUtils.isEmpty(classIds)){
    		return map;
    	}
        List<TeachClassStu> list = teachClassstuDao.findByClassIds(classIds);
        if (CollectionUtils.isNotEmpty(list)) {
            for (TeachClassStu tstu : list) {
                if (!map.containsKey(tstu.getStudentId())) {
                    map.put(tstu.getStudentId(), new ArrayList<String>());
                }
                map.get(tstu.getStudentId()).add(tstu.getClassId());
            }
        }
        return map;
    }

    @Override
    public List<TeachClassStu> findByClassIds(String[] classIds) {
    	if(ArrayUtils.isEmpty(classIds)){
    		return new ArrayList<TeachClassStu>();
    	}
        return teachClassstuDao.findByClassIds(classIds);
    }

    @Override
    public List<TeachClassStu> findByStuIds(String[] stuIds) {
    	if(ArrayUtils.isEmpty(stuIds)){
    		return new ArrayList<TeachClassStu>();
    	}
        return teachClassstuDao.findByStuIds(stuIds);
    }

    @Override
    public List<TeachClassStu> saveAllEntitys(TeachClassStu... teachClassStu) {
        return teachClassstuDao.saveAll(checkSave(teachClassStu));
    }

    @Override
    public List<TeachClass> findByStudentId(String studentId, String acadyear, String semester) {
        return teachClassDao.findByStuIdAndAcadyearAndSemester(studentId, acadyear, semester);
    }

	@Override
	public int delete(String[] teachClassId, String[] studentId) {
		return teachClassstuDao.delete(teachClassId,studentId);
	}

	@Override
	public Map<String, Integer> countByClassIds(String[] classIds) {
		//List<TeachClassStu> list = findByClassIds(classIds);
		List<Student> list  =studentDao.findListBlendClassIds(classIds);
		if(CollectionUtils.isEmpty(list)){
			return new HashMap<String, Integer>();
		}
		Map<String, Integer> returnMap=new HashMap<String, Integer>();
		for(Student stu:list){
			if(returnMap.containsKey(stu.getClassId())){
				returnMap.put(stu.getClassId(), returnMap.get(stu.getClassId())+1);
			}else{
				returnMap.put(stu.getClassId(), 1);
			}
		}
		return returnMap;
	}

	@Override
	public List<TeachClass> findByStudentId2(String studentId, String acadyear, String semester) {
		List<TeachClass> childClassList = teachClassDao.findByStuIdAndAcadyearAndSemester(studentId, acadyear, semester);
		if(CollectionUtils.isNotEmpty(childClassList)){
			Set<String> parentClassIdSet = EntityUtils.getSet(childClassList, "parentId");
			if(CollectionUtils.isNotEmpty(parentClassIdSet)){
				List<TeachClass> parentClassList = teachClassService.findListByIds(parentClassIdSet.toArray(new String[0]));
				childClassList.addAll(parentClassList);
			}
		}
		return childClassList;
	}
	
	 @Override
	 public List<TeachClass> findByStudentIds(String[] studentIds, String acadyear, String semester) {
		 List<TeachClass> teachClassList = new ArrayList<TeachClass>();
		 if(studentIds != null && studentIds.length > 0) {
			 teachClassList = teachClassDao.findByStuIdsAndAcadyearAndSemester(studentIds, acadyear, semester);
		 }
		 return teachClassList;
	 }

	@Override
	public List<TeachClassStu> findStudentByClassIds(String[] classIds) {
		List<String> littleClassIdList = new ArrayList<String>();
		List<String> bigClassIdList = new ArrayList<String>();
		List<TeachClass> teachList = teachClassService.findListByIdIn(classIds);
		for (TeachClass teachClass : teachList) {
			if("1".equals(teachClass.getIsMerge())){
				bigClassIdList.add(teachClass.getId());
			}else {
				littleClassIdList.add(teachClass.getId());
			}
		}
		
		//小班
		List<TeachClassStu> littleStudentList = findByClassIds(littleClassIdList.toArray(new String[littleClassIdList.size()]));
		
		//大班
		List<TeachClass> bigTeachClassList = teachClassService.findByParentIds(bigClassIdList.toArray(new String[bigClassIdList.size()]));
		List<String> idList = EntityUtils.getList(bigTeachClassList, "id");
		Map<String, TeachClass> map = EntityUtils.getMap(bigTeachClassList, "id");
		List<TeachClassStu> bigStudentList = findByClassIds(idList.toArray(new String[idList.size()]));
		for (TeachClassStu teachClassStu : bigStudentList) {
			String classId = teachClassStu.getClassId();
			if(bigClassIdList.contains(classId)){
				littleStudentList.add(teachClassStu);
			}else if(map.containsKey(classId)){
				String parentClassId = map.get(classId).getParentId();
				teachClassStu.setClassId(parentClassId);
				littleStudentList.add(teachClassStu);
			}
		}
		return littleStudentList;
	}

	@Override
	public void deleteByStudentIds(String... studentIds) {
		teachClassstuDao.deleteByStudentIdIn(studentIds);
	}

	@Override
	public void deleteByGradeIds(String... gradeIds) {
		teachClassstuDao.deleteByGradeIds(gradeIds);
	}

	@Override
	public void deleteBySubjectIds(String... subjectIds) {
		teachClassstuDao.deleteBySubjectIds(subjectIds);
	}

	@Override
	public List<TeachClassStu> findByClassIdsWithMaster(String[] classIds) {
        return this.findByClassIds(classIds);
	}
}
