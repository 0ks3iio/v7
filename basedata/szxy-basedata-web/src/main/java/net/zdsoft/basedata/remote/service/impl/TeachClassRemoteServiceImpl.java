package net.zdsoft.basedata.remote.service.impl;


import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teachClassRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TeachClassRemoteServiceImpl extends BaseRemoteServiceImpl<TeachClass, String> implements
        TeachClassRemoteService {

    @Autowired
    private TeachClassService teachClassService;

    @Override
    protected BaseService<TeachClass, String> getBaseService() {
        return teachClassService;
    }

    @Override
    public String findTeachClassList(String unitId, String acadyear, String semester, String courseId) {
        return SUtils.s(teachClassService.findTeachClassList(unitId, acadyear, semester, courseId));
    }

    @Override
    public String findTeachClassListByIds(String[] ids) {
        return SUtils.s(teachClassService.findTeachClassListByIds(ids));
    }
    
    @Override
    public String findTeachClassContainNotUseByIds(String[] ids) {
    	return SUtils.s(teachClassService.findTeachClassContainNotUseByIds(ids));
    }

    @Override
    public String findTeachClassList(String unitId, String acadyear, String semester, String subjectId,
            String[] gradeIds, boolean isFiltration) {
        return SUtils.s(teachClassService.findTeachClassList(unitId, acadyear, semester, subjectId, true,gradeIds,isFiltration));
    }
    
    @Override
	public String findTeachClassListByGradeId(String unitId, String acadyear,
			String semester, String subjectId, String gradeId) {
    	 return SUtils.s(teachClassService.findTeachClassList(unitId, acadyear, semester, subjectId,false,new String[]{gradeId},false));
	}

    @Override
    public String findByCourseIdAndInIds(String courseId, String[] ids) {
        return SUtils.s(teachClassService.findByCourseIdAndInIds(courseId, ids));
    }

    @Override
    public void deleteByIds(String[] ids) {
        teachClassService.deleteByIds(ids);
    }
    @Override
    public void notUsing(String[] ids) {
    	teachClassService.notUsing(ids);
    }
    
    @Override
    public void yesUsing(String[] ids) {
    	teachClassService.yesUsing(ids);
    }

    @Override
    public void saveAll(String teachClasses) {
        TeachClass[] cs = SUtils.dt(teachClasses, new TR<TeachClass[]>() {
        });
        if (ArrayUtils.isNotEmpty(cs)) {
            teachClassService.saveAllEntitys(cs);
        }
    }

    @Override
    public String findListByTeacherId(String teacherId, String acadyear, String semester) {
        return SUtils.s(teachClassService.findListByTeacherId(teacherId, acadyear, semester));
    }

	@Override
	public String findTeachClassList(String unitId, String acadyear, String semester, boolean isGk, String studentId) {
		return SUtils.s(teachClassService.findTeachClassList(unitId,acadyear,semester,isGk,studentId));
	}

	@Override
	public String countNumByIds(String[] ids) {
		return SUtils.s(teachClassService.countNumByIds(ids));
	}

	@Override
	public String findByGradeId(String gradeId) {
		return SUtils.s(teachClassService.findByGradeId(gradeId));
	}

	@Override
	public String findByNames(String unitId, String[] names) {
		return SUtils.s(teachClassService.findByNames(unitId, names));
	}
	
	@Override
	public String findByStuIdAndAcadyearAndSemester(String studentId, String acadyear, String semester){
		return SUtils.s(teachClassService.findByStuIdAndAcadyearAndSemester(studentId,acadyear,semester));
	}
	
	@Override
	public String findByStuIdAndAcadyearAndSemester(String studentId,String schoolId, String acadyear, String semester){
		return SUtils.s(teachClassService.findByStuIdAndAcadyearAndSemester(studentId,schoolId,acadyear,semester));
	}
	
	@Override
	public String findBySearch(String unitId, String acadyear, String semester,String classType,
			String gradeId,String subjectId) {
		return SUtils.s(teachClassService.findBySearch(unitId, acadyear,semester,classType,gradeId,subjectId));
	}

	@Override
	public void deleteStusByClaIds(String[] ids) {
		teachClassService.deleteStusByClaIds(ids);
	}

	@Override
	public String findByParentIds(String[] parentIds) {
		return SUtils.s(teachClassService.findByParentIds(parentIds));
	}

	@Override
	public String findByStuIds(String acadyear, String semester,
			String isBigClass, String[] stuIds) {
		return SUtils.s(teachClassService.findByStuIds(acadyear, semester, isBigClass, stuIds));
	}

	@Override
	public String findbyUnitIdIn(String... unitIds) {
		return SUtils.s(teachClassService.findbyUnitIdIn(unitIds));
	}

	@Override
	public String findClassHasEx(String unitId, String acadyear, String semester, String gradeId,
			String[] classTypes) {
		return SUtils.s(teachClassService.findClassHasEx(unitId, acadyear, semester, gradeId, classTypes));
	}

	@Override
	public String findByUnitIdAndAcadyearAndSemesterAndTeaIds(String unitId, String acadyear, String semester, String[] teacherIds) {
		return SUtils.s(teachClassService.findByUnitIdAndAcadyearAndSemesterAndTeaIds(unitId,acadyear,semester,teacherIds));
	}
}
