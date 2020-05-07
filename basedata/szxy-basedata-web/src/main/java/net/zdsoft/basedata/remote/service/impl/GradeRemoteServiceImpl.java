package net.zdsoft.basedata.remote.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;

@Service("gradeRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class GradeRemoteServiceImpl extends BaseRemoteServiceImpl<Grade,String> implements GradeRemoteService {

    @Autowired
    private GradeService gradeService;

    @Override
    protected BaseService<Grade, String> getBaseService() {
        return gradeService;
    }

    @Override
    public String findBySchoolId(String schoolId) {
        return SUtils.s(gradeService.findByUnitId(schoolId));
    }

    @Override
    public String findByTeacherId(String teacherId) {
        return SUtils.s(gradeService.findByTeacherId(teacherId));
    }

    @Override
    public void updateGraduate(Date date, String schId, String acadYear, Integer section, Integer schoolingLength) {
        gradeService.updateGraduate(date, schId, acadYear, section, schoolingLength);
    }

    @Override
    public String findBySchidSectionAcadyear(String schoolId, String curAcadyear, Integer[] section) {
        return SUtils.s(gradeService.findBySchidSectionAcadyear(schoolId, curAcadyear, section));
    }

    @Override
    public String findBySchoolId(String schoolId, Integer[] section) {
        return SUtils.s(gradeService.findByUnitIdNotGraduate(schoolId, section));
    }

    @Override
    public String saveAllEntitys(String entitys) {
        Grade[] dt = SUtils.dt(entitys, new TR<Grade[]>() {
        });
        return SUtils.s(gradeService.saveAllEntitys(dt));
    }

	@Override
	public String findBySchoolId(String schoolId, Integer[] section, String openAcadyear,boolean isOnlyNotGraduate) {
		return SUtils.s(gradeService.findGradeList(schoolId,section,openAcadyear,isOnlyNotGraduate));
	}

	@Override
	public String findBySchoolIdMap(String[] unitIds) {
		return SUtils.s(gradeService.findByUnitIdMap(unitIds));
	}

	@Override
	public String findByUnitIdAndGradeCode(String unitId,
			String... gradeCodes) {
		return SUtils.s(gradeService.findByUnitIdAndGradeCode(unitId, gradeCodes));
	}
	
	@Override
	public String findByUnitIdAndCurrentAcadyear(String unitId, String acadyear) {
		return SUtils.s(gradeService.findByUnitIdAndCurrentAcadyear(unitId, acadyear,null,true));
	}
	@Override
	public String findByUnitIdAndGradeCode(String unitId,Integer[] sections, String currentAcadyear){
		return SUtils.s(gradeService.findByUnitIdAndGradeCode(unitId, sections,currentAcadyear));
	}
	public String findByUnitIdsAndCurrentAcadyear(String[] unitIds, String acadyear) {
		return SUtils.s(gradeService.findByUnitIdsAndCurrentAcadyear(unitIds, acadyear));
	}
	
	@Override
	public String findBySchoolIdAndOpenAcadyear(String unitId, String acadyear) {
		return SUtils.s(gradeService.findBySchoolIdAndAcadyear(unitId, acadyear));
	}
	
	@Override
	public String findByIdAndCurrentAcadyear(String unitId, String gradeId,
			String searchAcadyear) {
		return SUtils.s(gradeService.findByIdAndCurrentAcadyear(unitId, gradeId , searchAcadyear));
	}


	@Override
	public String findBySchoolIdAndSection(String schoolId, Integer section) {
		return SUtils.s(gradeService.findBySectionAndAcadyear(schoolId,section,null));
	}

	@Override
	public String findBySchoolIdAndIsGraduate(String unitId, Integer graduated) {
		return SUtils.s(gradeService.findBySchoolIdAndIsGraduate(unitId,graduated));
	}

	@Override
	public String findTimetableMaxRangeBySchoolId(String schoolId,String[] gradIds) {
		return SUtils.s(gradeService.findTimetableMaxRangeBySchoolId(schoolId,gradIds));
	}
	@Override
	public String findByUnitIdsIn(String[] unitIds){
		return SUtils.s(gradeService.findByUnitIdsIn(unitIds));
	}

	@Override
	public String findBySchoolIdsAndOpenAcaday(String[] schoolIds, String openAcadyear) {
		return SUtils.s(gradeService.findBySchoolIdsAndOpenAcaday(schoolIds,openAcadyear));
	}
}
