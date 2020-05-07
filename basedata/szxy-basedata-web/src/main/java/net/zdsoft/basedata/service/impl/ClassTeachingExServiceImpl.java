package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.ClassTeachingExDao;
import net.zdsoft.basedata.entity.ClassTeachingEx;
import net.zdsoft.basedata.service.ClassTeachingExService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
@Service("classTeachingExService")
public class ClassTeachingExServiceImpl extends BaseServiceImpl<ClassTeachingEx, String> implements ClassTeachingExService{

	@Autowired
	private ClassTeachingExDao classTeachingExDao;
	
	@Override
	protected BaseJpaRepositoryDao<ClassTeachingEx, String> getJpaDao() {
		return classTeachingExDao;
	}

	@Override
	protected Class<ClassTeachingEx> getEntityClass() {
		return ClassTeachingEx.class;
	}

	@Override
	public List<ClassTeachingEx> findByClassTeachingIdIn(String[] classTeachingIds) {
		if(classTeachingIds==null || classTeachingIds.length==0) {
			return new ArrayList<ClassTeachingEx>();
		}
		List<ClassTeachingEx> exList=new ArrayList<>();
		if (classTeachingIds.length<= 1000) {
			exList=classTeachingExDao.findByClassTeachingIdIn(classTeachingIds);
		} else {
			int cyc = classTeachingIds.length / 1000 + (classTeachingIds.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > classTeachingIds.length)
					max = classTeachingIds.length;
				exList.addAll(classTeachingExDao.findByClassTeachingIdIn(ArrayUtils.subarray(classTeachingIds, i * 1000, max)));
			}
		}
		
		return exList;
	}

	@Override
	public void save(List<ClassTeachingEx> classTeachingExSaveList) {
		classTeachingExDao.saveAll(classTeachingExSaveList);
	}

	@Override
	public void deleteByClassTeachingIdAndTeacherIdIn(String classTeachingId,
			String[] teacherIds) {
		classTeachingExDao.deleteByClassTeachingIdAndTeacherIdIn(classTeachingId,teacherIds);		
	}

	@Override
	public void deleteClassTeachingIdIn(String[] classTeachingIds) {
		if(classTeachingIds==null || classTeachingIds.length==0) {
			return;
		}
		if (classTeachingIds.length <= 1000) {
			classTeachingExDao.deleteByClassTeachingIdIn(classTeachingIds);
		} else {
			int cyc = classTeachingIds.length / 1000 + (classTeachingIds.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > classTeachingIds.length)
					max = classTeachingIds.length;
				
				classTeachingExDao.deleteByClassTeachingIdIn(ArrayUtils.subarray(classTeachingIds, i * 1000, max));
			}
		}
		
	}

	@Override
	public void deleteByTeacherIds(String... teacherIds) {
		classTeachingExDao.deleteByTeacherIds(teacherIds);
	}

	@Override
	public void deleteByClassIds(String... classIds) {
		classTeachingExDao.deleteByClassIds(classIds);
	}

	@Override
	public void deleteBySubjectIds(String... subjectIds) {
		classTeachingExDao.deleteBySubjectIds(subjectIds);
	}

}
