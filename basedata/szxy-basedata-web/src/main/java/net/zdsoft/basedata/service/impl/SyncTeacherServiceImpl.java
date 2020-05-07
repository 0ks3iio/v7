package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.SyncBasedataService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachGroupExService;
import net.zdsoft.basedata.service.TipsayExService;
@Service("syncTeacherService")
public class SyncTeacherServiceImpl extends SyncBasedataService<Teacher, String> {
	
	@Autowired
	private TeachGroupExService teachGroupExService;
	@Autowired
	private ClassTeachingService classTeachingService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private TipsayExService tipsayExService;
	@Autowired
	private SemesterService semesterService;
	
	@Override
	protected void add(Teacher... t) {
		
	}
	
	@Override
	protected void update(Teacher... t) {
		
	}
	
	@Override
	protected void delete(String... teacherIds) {
		//教研组成员删除
		teachGroupExService.deleteByTeacherIds(teacherIds);
		//课程开设任课教师删除
		classTeachingService.deleteByTeacherIds(teacherIds);
		//课程表删除
		courseScheduleService.deleteByTeacherIds(teacherIds);
		//调代课删除
		tipsayExService.deleteByTeacherIds(teacherIds);
	}
	
	@Override
	protected String preDelete(String... ids) {
		Semester semester = semesterService.getCurrentSemester(2);
		if(semester!=null){
			for(String teacherId:ids){
				List<ClassTeaching> list = classTeachingService.findListBy(new String[]{"acadyear","semester","teacherId","isDeleted"},new Object[]{semester.getAcadyear(),semester.getSemester()+"",teacherId,0});
				if(CollectionUtils.isNotEmpty(list)){
					return "当前班级计划中已安排该老师不能删除！";
				}
				
				List<TeachClass>tclist= teachClassService.findListBy(new String[]{"acadyear","semester","teacherId","isDeleted","isUsing"}, new Object[]{semester.getAcadyear(),semester.getSemester()+"",teacherId,0,"1"});
				if(CollectionUtils.isNotEmpty(tclist)){
					return "当前学年教学班中已经存在老师上课不能删除！";
				}
			}
			
		}
		return null;
	}
	
	@Override
	protected String preUpdate(Teacher t) {
		return null;
	}
	
	@Override
	protected String preAdd(Teacher t) {
		return null;
	}


}
