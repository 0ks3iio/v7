package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.SyncBasedataService;
import net.zdsoft.basedata.service.TipsayExService;

@Service("syncClazzService")
public class SyncClazzServiceImpl extends SyncBasedataService<Clazz, String> {
	
	@Autowired
	private ClassTeachingService classTeachingService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private TipsayExService tipsayExService;
	@Autowired
	private SemesterService semesterService;
	@Override
	protected void add(Clazz... t) {
		
	}
	
	@Override
	protected void update(Clazz... t) {
		
	}
	
	@Override
	protected void delete(String... classIds) {
		//课程开设计划删除
		classTeachingService.deleteByClassIds(classIds);
		//课程表删除
		courseScheduleService.deleteByClassIds(classIds);
		//调代课删除
		tipsayExService.deleteByClassIds(classIds);
	}
	
	@Override
	protected String preDelete(String... k) {
		for(String key:k){
			Semester semester = semesterService.getCurrentSemester(2);
			List<ClassTeaching> list = classTeachingService.findBySearchForList(semester.getAcadyear(),semester.getSemester()+"",new String[]{key});
			if(CollectionUtils.isNotEmpty(list)){
				return "当前班级计划中已经开课不能删除！";
			}
		}
		
		return null;
	}
	
	@Override
	protected String preUpdate(Clazz t) {
		return null;
	}
	
	@Override
	protected String preAdd(Clazz t) {
		return null;
	}
	

}
