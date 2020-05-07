package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.dto.OpenTeachingSearchDto;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.ClassTeachingService;
import net.zdsoft.framework.utils.SUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("classTeachingRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class ClassTeachingRemoteServiceImpl extends BaseRemoteServiceImpl<ClassTeaching,String> implements
        ClassTeachingRemoteService {

    @Autowired
    private ClassTeachingService classTeachingService;

    @Override
    protected BaseService<ClassTeaching, String> getBaseService() {
        return classTeachingService;
    }

    @Override
    public String findByCourseIdsMap(String unitId, String acadyear, String semester, String... subjectIds) {
        return SUtils.s(classTeachingService.findByCourseIdsMap(unitId, acadyear, semester, subjectIds));
    }

	@Override
	public String findClassTeachingListByTeacherId(String unitId, String... teaherIds) {
		return SUtils.s(classTeachingService.findClassTeachingListByTeacherId(unitId, teaherIds));
	}

	@Override
	public String findClassTeachingList(String unitId, String acadyear, String semester, String... teaherIds) {
		return SUtils.s(classTeachingService.findClassTeachingList(unitId,acadyear,semester, teaherIds));
	}
	
	public String findClassTeachingListByClassIds(String acadyear, String semester, String... classIds){
		return SUtils.s(classTeachingService.findBySearchForList(acadyear, semester, classIds));
	}
	@Override
	public String findBySearch(String unitId,String acadyear,String semester,String[] classIds,Integer isDeleted,Integer isTeaCls){
		OpenTeachingSearchDto classTeachDto=new OpenTeachingSearchDto();
		classTeachDto.setUnitId(unitId);
		classTeachDto.setAcadyear(acadyear);
		classTeachDto.setSemester(semester);
		classTeachDto.setIsDeleted(isDeleted);
		classTeachDto.setIsTeaCls(isTeaCls);
		classTeachDto.setClassIds(classIds);
		return SUtils.s(classTeachingService.findBySearch(classTeachDto, null));
	}
	@Override
	public String  findByClassIdsSubjectIds(String unitId,
			String acadyear, String semester, String[] classIds,
			String[] subjectIds, boolean flag) {
		return SUtils.s(classTeachingService.findClassTeachingList(acadyear, semester, classIds, unitId, 0, subjectIds, flag));
	}

	@Override
	public String findByUnitIdAndAcadyearAndSemesterAndSubjectType(String unitId, String acadyear, String semester, Integer subjectType) {
		return SUtils.s(classTeachingService.findByUnitIdAndAcadyearAndSemesterAndSubjectType(unitId, acadyear, semester, subjectType+""));
	}

	@Override
	public String findBySubidTeacherList(String acadyear,
			String semester, String unitId,String gradeId, String subjectId) {
		return SUtils.s(classTeachingService.findBySubidTeacherList(acadyear, semester, unitId,gradeId, subjectId));
	}

	@Override
	public void deleteCurrentClassIds(String unitId, String acadyear,
			String semester, String[] classIds) {
		classTeachingService.deleteCurrentClassIds(unitId, acadyear, semester, classIds);
	}

	@Override
	public String findBySearchMap(String unitId, String acadyear,
			String semester,String[] classIds) {
		return SUtils.s(classTeachingService.findBySearchMap(unitId, acadyear, semester,classIds));
	}

	@Override
	public String findListByGradeId(String acadyear, String semester, String unitId, String gradeId) {
		return SUtils.s(classTeachingService.findListByGradeId(acadyear, semester, unitId, gradeId));
	}
	
	@Override
	public String findListByGradeIdAndSubId(String acadyear, String semester, String unitId, String gradeId,
			String subjectId) {
		return SUtils.s(classTeachingService.findListByGradeIdAndSubId(acadyear, semester, unitId, gradeId,subjectId));
	}

    @Override
    public String findClassTeachingListHistoryByTeacherId(String unitId, String[] teaherIds) {
		return SUtils.s(classTeachingService.findClassTeachingListHistoryByTeacherId( unitId,teaherIds));
    }

	@Override
	public String findClassTeachingListByBlendTeacherIds(String unitId, String[] teaherIds) {
		return SUtils.s(classTeachingService.findClassTeachingListByBlendTeacherIds( unitId,teaherIds));
	}

	@Override
	public String findClassTeachingListByBlendTeacherIds(String unitId, String acadyear, String semester, String[] teaherIds) {
		return SUtils.s(classTeachingService.findClassTeachingListByBlendTeacherIds(unitId,acadyear,semester,teaherIds));
	}

}
