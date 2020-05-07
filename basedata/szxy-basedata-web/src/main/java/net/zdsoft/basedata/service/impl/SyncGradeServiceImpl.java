package net.zdsoft.basedata.service.impl;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.service.GradeTeachingService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.SyncBasedataService;
import net.zdsoft.basedata.service.TeachClassService;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("syncGradeService")
public class SyncGradeServiceImpl extends SyncBasedataService<Grade, String> {
	
	@Autowired
	private GradeTeachingService gradeTeachingService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private SemesterService semesterService;
	
	@Override
	protected void add(Grade... t) {
		
	}
	
	@Override
	protected void update(Grade... t) {
		
	}
	
	@Override
	protected void delete(String... gradeId) {
		//删除年级课设开设计划
		gradeTeachingService.deleteByGradeIds(gradeId);
		//删除年级下所有教学班、教学班学生及教学班课程表数据
		teachClassService.deleteByGradeIds(gradeId);
	}
	
	@Override
	protected String preDelete(String... ids) {
		Semester semester = semesterService.getCurrentSemester(2);
		for(String k :ids){
			if(semester!=null){
				List<GradeTeaching> gtlist = gradeTeachingService.findListBy(new String[]{"acadyear","semester","gradeId","isDeleted"}, new Object[]{semester.getAcadyear(),semester.getSemester()+"",k,0});
				if(CollectionUtils.isNotEmpty(gtlist)){
					return "当前年级计划中已经开课不能删除！";
				}
				
				List<TeachClass>tclist= teachClassService.findListBy(new String[]{"acadyear","semester","gradeId","isDeleted","isUsing"}, new Object[]{semester.getAcadyear(),semester.getSemester()+"",k,0,"1"});
				if(CollectionUtils.isNotEmpty(tclist)){
					return "当前学年该年级下已经开设教学班不能删除！";
				}
			}
		}
		
		
		return null;
	}
	
	@Override
	protected String preUpdate(Grade t) {
		return null;
	}
	
	@Override
	protected String preAdd(Grade t) {
		return null;
	}
	
}
