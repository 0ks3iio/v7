package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.TeacherSubjectDao;
import net.zdsoft.basedata.entity.TeacherSubject;
import net.zdsoft.basedata.service.TeacherSubjectService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("teacherSubjectService")
public class TeacherSubjectServiceImpl extends BaseServiceImpl<TeacherSubject, String> implements TeacherSubjectService{

	@Autowired
	private TeacherSubjectDao teacherSubjectDao;
	@Override
	protected BaseJpaRepositoryDao<TeacherSubject, String> getJpaDao() {
		return teacherSubjectDao;
	}

	@Override
	protected Class<TeacherSubject> getEntityClass() {
		return TeacherSubject.class;
	}

	@Override
	public List<TeacherSubject> findListByTeacherIdIn(String[] teacherIds) {
		if(teacherIds==null || teacherIds.length==0) {
			return new ArrayList<TeacherSubject>();
		}
		List<TeacherSubject> exList=new ArrayList<>();
		if (teacherIds.length<= 1000) {
			exList=teacherSubjectDao.findListByTeacherIdIn(teacherIds);
		} else {
			int cyc = teacherIds.length / 1000 + (teacherIds.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > teacherIds.length)
					max = teacherIds.length;
				exList.addAll(teacherSubjectDao.findListByTeacherIdIn(ArrayUtils.subarray(teacherIds, i * 1000, max)));
			}
		}
		
		return exList;
	}

}
