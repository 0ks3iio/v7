package net.zdsoft.teacherasess.data.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.teacherasess.data.dao.TeacherasessResultDao;
import net.zdsoft.teacherasess.data.entity.TeacherAsessResult;
import net.zdsoft.teacherasess.data.service.TeacherasessResultService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teacherasessResultService")
public class TeacherasessResultServiceImpl extends BaseServiceImpl<TeacherAsessResult, String> implements TeacherasessResultService{
	@Autowired
	private TeacherasessResultDao teacherasessResultDao;
	@Autowired
	private CourseRemoteService courseRemoteService;

	@Override
	protected BaseJpaRepositoryDao<TeacherAsessResult, String> getJpaDao() {
		return teacherasessResultDao;
	}

	@Override
	public List<TeacherAsessResult> findByUnitIdAndAsessIdAndClassTypeAndSubjectId(
			String unitId, String assessId, String classType, String subjectId) {
		return teacherasessResultDao.findByUnitIdAndAsessIdAndClassTypeAndSubjectId(unitId, assessId, classType, subjectId);
	}

	@Override
	public List<TeacherAsessResult> findByUnitIdAndAsessIdAndSubjectId(
			String unitId, String assessId, String subjectId) {
		return teacherasessResultDao.findByUnitIdAndAsessIdAndSubjectId(unitId, assessId, subjectId);
	}

	@Override
	public List<Course> findByUnitIdAndAsessId(String unitId, String asessId) {
		List<String> subjectIds=teacherasessResultDao.findByUnitIdAndAsessId(unitId, asessId);
		List<Course> courseList = new ArrayList<Course>();
		if(CollectionUtils.isEmpty(subjectIds)){
			return courseList;
		}
		courseList = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIds.toArray(new String[0])),new TR<List<Course>>(){});
		Collections.sort(courseList, new Comparator<Course>(){
			@Override
			public int compare(Course o1, Course o2) {
				return o1.getOrderId()-o2.getOrderId();
			}
			
		});
		return courseList;
	}

	@Override
	public void deleteByAssessId(String unitId, String...assessId) {
		teacherasessResultDao.deleteByAssessId(unitId, assessId);
	}

	@Override
	protected Class<TeacherAsessResult> getEntityClass() {
		return TeacherAsessResult.class;
	}

}
