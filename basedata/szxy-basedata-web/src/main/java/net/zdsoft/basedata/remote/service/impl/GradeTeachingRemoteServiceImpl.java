package net.zdsoft.basedata.remote.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.remote.service.GradeTeachingRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.GradeTeachingService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("gradeTeachingRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class GradeTeachingRemoteServiceImpl extends BaseRemoteServiceImpl<GradeTeaching,String> implements GradeTeachingRemoteService {
	@Autowired
	private GradeTeachingService gradeTeachingService;
	@Autowired
	private ClassTeachingService classTeachingService;
	@Autowired
	private ClassService classService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private CourseService courseService;

	@Override
	public String findBySearchMap(String unitId, String acadyear,String semester,String gradeId) {
		return SUtils.s(gradeTeachingService.findBySearchMap(unitId, acadyear, semester,gradeId));
	}

	@Override
	protected BaseService<GradeTeaching, String> getBaseService() {
		return gradeTeachingService;
	}

	@Override
	public String findBySearchList(String unitId, String acadyear,
			String semester, String gradeId,Integer subjectType) {
		
		return SUtils.s(gradeTeachingService.findBySearchList(unitId,acadyear,semester,gradeId,subjectType==null?"":subjectType+""));
	}

	@Override
	public String findCourseListByGradeId(String unitId,String acadyear,String semester,String... gradeId) {
		List<GradeTeaching> gradeTeachingList = gradeTeachingService.findGradeTeachingList(acadyear, semester, gradeId, unitId, Constant.IS_DELETED_FALSE, null, null);
		Set<String> subjectIds = new HashSet<String>();
		//取年级课程开设
		if(CollectionUtils.isNotEmpty(gradeTeachingList)){
			subjectIds.addAll(EntityUtils.getSet(gradeTeachingList, GradeTeaching::getSubjectId));
		}else{//如果年级开设没有，取班级课程开设
			List<Clazz> clazzList = classService.findByGradeIdIn(gradeId);
			if(CollectionUtils.isNotEmpty(clazzList)){
				List<ClassTeaching> classTeachingList = classTeachingService.findBySearchForList(acadyear, semester, EntityUtils.getList(clazzList, Clazz::getId).toArray(new String[0]));
				if(CollectionUtils.isNotEmpty(classTeachingList)){
					subjectIds.addAll(EntityUtils.getSet(classTeachingList, ClassTeaching::getSubjectId));
				}
			}
		}
		//教学班课程
		List<TeachClass> teachClassList = teachClassService.findBySearch(unitId, acadyear, semester, gradeId);
		if(CollectionUtils.isNotEmpty(teachClassList)){
			subjectIds.addAll(EntityUtils.getSet(teachClassList, TeachClass::getCourseId));
		}
		return SUtils.s(courseService.findListByIds(subjectIds.toArray(new String[0])));
	}
	
}
