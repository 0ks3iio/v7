package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.OpenTeachingSearchDto;
import net.zdsoft.basedata.dto.TeachClassSearchDto;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.GradeTeachingService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentSelectSubjectService;
import net.zdsoft.basedata.service.SyncBasedataService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachGroupService;
import net.zdsoft.basedata.service.TipsayExService;

@Service("syncCourseService")
public class SyncCourseServiceImpl extends SyncBasedataService<Course, String> {
	
	@Autowired
	private TeachGroupService teachGroupService;
	@Autowired
	private StudentSelectSubjectService studentSelectSubjectService;
	@Autowired
	private GradeTeachingService gradeTeachingService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private TipsayExService tipsayExService;
	@Autowired
	private SemesterService semesterService;
	
	@Override
	protected void add(Course... t) {
		
	}
	
	@Override
	protected void update(Course... t) {
		
	}
	
	@Override
	protected void delete(String... subjectIds) {
		//教研组删除关联课程
		teachGroupService.deleteBySubjectIds(subjectIds);
		//学生选课信息删除
		studentSelectSubjectService.deleteBySubjectIds(subjectIds);
		//年级和班级课程开设计划删除
		gradeTeachingService.deleteBySubjectIds(subjectIds);
		//课程表删除
		courseScheduleService.deleteBySubjectIds(subjectIds);
		//教学班删除
		teachClassService.deleteBySubjectIds(subjectIds);
		//调代课删除 还需完善
		tipsayExService.deleteBySubjectIds(subjectIds);
		
	}
	
	@Override
	protected String preDelete(String... ids) {
		Semester semester = semesterService.getCurrentSemester(2);
		for(String k :ids){
			List<TeachGroup> list =teachGroupService.findListBy(new String[]{"subjectId","isDeleted"}, new Object[]{k,0});
			if(CollectionUtils.isNotEmpty(list)){
				return "教研组中已经引用该科目不能删除！";
			}
			if(semester!=null){
				OpenTeachingSearchDto dto=new OpenTeachingSearchDto();
				dto.setSubjectIds(new String[]{k});
				dto.setAcadyear(semester.getAcadyear());
				dto.setSemester(semester.getSemester()+"");
				dto.setIsDeleted(0);
				List<GradeTeaching> gtlist = gradeTeachingService.findBySearch(dto);
				if(CollectionUtils.isNotEmpty(gtlist)){
					return "当前学年年级中已经开设该科目不能删除！";
				}			
				
				List<TeachClass>tclist= teachClassService.findListBy(new String[]{"acadyear","semester","courseId","isDeleted","isUsing"}, new Object[]{semester.getAcadyear(),semester.getSemester()+"",k,0,"1"});
				if(CollectionUtils.isNotEmpty(tclist)){
					return "当前学年教学班中已经开设该科目班级不能删除！";
				}
				TeachClassSearchDto tcs =new TeachClassSearchDto();
				tcs.setRelaCourseId(k);
				tcs.setAcadyearSearch(semester.getAcadyear());
				tcs.setSemesterSearch(semester.getSemester()+"");
				tcs.setIsUsing(BaseConstants.ONE_STR);
				List<TeachClass>tcslist= teachClassService.findListByDtoWithMaster(tcs);
				if(CollectionUtils.isNotEmpty(tcslist)){
					return "当前学年教学班中已经引用虚拟课程不能删除！";
				}
			}
		}
		return null;
	}
	
	@Override
	protected String preUpdate(Course t) {
		return null;
	}
	
	@Override
	protected String preAdd(Course t) {
		return null;
	}
	

}
