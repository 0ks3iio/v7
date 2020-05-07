package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.dao.ClassDao;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@com.alibaba.dubbo.config.annotation.Service
@Service("classRemoteService")
public class ClassRemoteServiceImpl extends BaseRemoteServiceImpl<Clazz,String> implements ClassRemoteService {

    @Autowired
    private ClassService classService;
    
    @Autowired
    private GradeService gradeService;

    @Autowired
    private ClassDao classDao;

    @Override
    protected BaseService<Clazz, String> getBaseService() {
        return classService;
    }
    @Override
    public String findOneById(String id){
    	Clazz clazz=getBaseService().findOne(id);
    	if(clazz!=null){
    		Grade grade=gradeService.findOne(clazz.getGradeId());
    		if(grade!=null){
    			clazz.setClassNameDynamic(grade.getGradeName()+clazz.getClassName());
    		}
    	}
    	return SUtils.s(clazz);
    }
    @Override
    public String findListByIds(String... ids) {
//    	List<Clazz> clalist = classService.findByIds(ids);
//    	if(CollectionUtils.isNotEmpty(clalist)){
//    		Set<String> gids=EntityUtils.getSet(clalist, "gradeId");
//        	Map<String,Grade> gmap = gradeService.findByIdInMap(gids.toArray(new String[0]));
//        	for(Clazz cls : clalist){
//        		cls.setClassNameDynamic(gmap.get(cls.getGradeId()).getGradeName()+cls.getClassName());
//        	}
//    	}
//        return SUtils.s(clalist);
    	return findClassListByIds(ids);
    }

    @Override
    public String findClassList(String schoolId, String courseId, String oepnAcadyear, String acadyear,
            String semester, int section) {
        return SUtils.s(classService.findClassList(schoolId, courseId, oepnAcadyear, acadyear, semester, section));
    }

    @Override
    public String findByTeacherId(String teacherId) {
        return SUtils.s(classService.findByTeacherId(teacherId));
    }

    @Override
    public String findByIdAcadyear(String teacherId, String graduateAcadyear) {
        return SUtils.s(classService.findByIdAcadyear(teacherId, graduateAcadyear));
    }

    @Override
    public String findByteacherIdAcadyear(String teacheId, String graduateAcadyear) {
        return SUtils.s(classService.findByteacherIdAcadyear(teacheId, graduateAcadyear));
    }

    @Override
    public String findByAllSchoolId(String schoolId) {
    	List<Clazz> clsList = classService.findBySchoolId(schoolId);
        Map<String, String> gradeMap = new HashMap<String,String>();
    	List<Grade> gradeList = gradeService.findByUnitId(schoolId);
    	for(Grade grade : gradeList){
    		gradeMap.put(grade.getId(), grade.getGradeName());
    	}
    	for(Clazz cls : clsList){
    		cls.setClassNameDynamic(gradeMap.get(cls.getGradeId())+cls.getClassName());
    	}
        return SUtils.s(clsList);
    }

    @Override
    public String findByOpenAcadyear(String schoolId, String openAcadyear) {
        return SUtils.s(classService.findByOpenAcadyear(schoolId, openAcadyear));
    }

    @Override
    public String findByGradeId(String schoolId, String gradeId, String teacherId) {
        return SUtils.s(classService.findByGradeId(schoolId, gradeId, teacherId));
    }

    @Override
    public String findByIdCurAcadyear(String schoolId, String curAcadyear) {
    	List<Clazz> clsList = classService.findByIdCurAcadyear(schoolId, curAcadyear);
        return SUtils.s(clsList);
    }

    @Override
    public String findByOverSchoolinglen(String schoolId, String curAcadyear) {
        return SUtils.s(classService.findByOverSchoolinglen(schoolId, curAcadyear));
    }

    @Override
    public String findByGraduateyear(String schoolId, String graduateAcadyear) {
        return SUtils.s(classService.findByGraduateyear(schoolId, graduateAcadyear));
    }

    @Override
    public String findByCampusId(String campusId) {
        return SUtils.s(classService.findByCampusId(campusId));
    }

    @Override
    public String findByIdYear(String campusId, String graduateAcadyear) {
        return SUtils.s(classService.findByIdYear(campusId, graduateAcadyear));
    }

    @Override
    public void updateGraduateSign(int sign, Date currentDate, String classId) {
        classService.updateGraduateSign(sign, currentDate, classId);
    }

    @Override
    public String findByIdSectionYearType(String schoolId, int section, String enrollyear, String artScienceType) {
        return SUtils.s(classDao.findByIdSectionYearType(schoolId, section, enrollyear, artScienceType));
    }

    @Override
    public String findByIdSectionYear(String schoolId, int section, String acadyear) {
        return SUtils.s(classDao.findByIdSectionYear(schoolId, section, acadyear));
    }

    @Override
    public String findBySchoolId(String schoolId) {
    	List<Clazz> clsList = classDao.findBySchoolId(schoolId);
    	Map<String, String> gradeMap = new HashMap<String,String>();
    	List<Grade> gradeList = gradeService.findByUnitId(schoolId);
    	for(Grade grade : gradeList){
    		gradeMap.put(grade.getId(), grade.getGradeName());
    	}
    	for(Clazz cls : clsList){
    		cls.setClassNameDynamic(gradeMap.get(cls.getGradeId())+cls.getClassName());
    	}
        return SUtils.s(clsList);
    }

    @Override
    public String findByGradeIdSortAll(String gradeId) {
    	
    	Grade grade = gradeService.findOne(gradeId);
    	List<Clazz> clazzes = classDao.findByGradeIdSortAll(gradeId);
    	if(grade != null){
    		for(Clazz clazz : clazzes){
    			clazz.setClassNameDynamic(grade.getGradeName() + clazz.getClassName());
    		}
    	}
        return SUtils.s(clazzes);
    }

    @Override
    public String findByInGradeIds(String[] gradeIds) {
    	List<Grade> gradeList = gradeService.findListByIds(gradeIds);
    	Map<String, String> gradeNameMap = new HashMap<String, String>();
    	for(Grade grade : gradeList){
    		gradeNameMap.put(grade.getId(), grade.getGradeName());
    	}
    	List<Clazz> clazzes = classDao.findByInGradeIds(gradeIds);
    	for(Clazz clazz : clazzes){
			clazz.setClassNameDynamic(gradeNameMap.get(clazz.getGradeId()) + clazz.getClassName());
		}
        return SUtils.s(classDao.findByInGradeIds(gradeIds));
    }

    @Override
    public String findBySchoolIdInGradeIds(String schoolId, String[] gradeIds) {
        return SUtils.s(classDao.findBySchoolIdInGradeIds(schoolId, gradeIds));
    }

    @Override
    public String findBySchoolIdTeacherIdAll(String schoolId, String teacherId) {
        return SUtils.s(classDao.findBySchoolIdTeacherIdAll(schoolId, teacherId));
    }

    @Override
    public String findBySchoolIdCurAcadyear(String schoolId, String curAcadyear) {
        return SUtils.s(classDao.findBySchoolIdCurAcadyear(schoolId, curAcadyear));
    }

    @Override
    public String findBySchoolIdGraduateAcadyear(String schoolId, String graduateAcadyear) {
        return SUtils.s(classDao.findBySchoolIdGraduateAcadyear(schoolId, graduateAcadyear));
    }

    @Override
    public String findBySchoolIdAcadyear(String schoolId, String acadyear) {
    	List<Clazz> clazzes = classDao.findBySchoolIdAcadyear(schoolId, acadyear);
    	Set<String> gradeIds = EntityUtils.getSet(clazzes, Clazz::getGradeId);
    	Map<String, Grade> gradeNameMap = gradeService.findMapByIdIn(gradeIds.toArray(new String[0]));
    	for(Clazz clazz : clazzes){
			clazz.setClassNameDynamic(gradeNameMap.get(clazz.getGradeId()).getGradeName() + clazz.getClassName());
		}
    	Collections.sort(clazzes, new Comparator<Clazz>() {
			@Override
			public int compare(Clazz arg0, Clazz arg1) {
				//return StringUtils.(arg0.getClassCode(), arg1.getClassCode());
                String str1 = arg0.getClassCode();
                String str2 = arg1.getClassCode();
                if (str1 == str2) {
                    return 0;
                } else if (str1 == null) {
                    return -1;
                } else if (str2 == null) {
                    return 1;
                } else {
                    return str1.compareTo(str2);
                }
			}
		});
        return SUtils.s(clazzes);
    }

    @Override
    public String findBySchoolIdTeacherId(String schoolId, String teacherId) {
        return SUtils.s(classDao.findBySchoolIdTeacherId(schoolId, teacherId));
    }

    @Override
    public String findClassesByGrade(String schoolId, int section, String enrollyear, String sl) {
        return SUtils.s(classDao.findClassesByGrade(schoolId, section, enrollyear, sl));
    }

    @Override
    public String findClassesByGrade(String schoolId, String campusId, int section, String enrollyear, int schoolingLen) {
        return SUtils.s(classDao.findClassesByGrade(schoolId, campusId, section, enrollyear, schoolingLen));
    }

    @Override
    public String findBySchoolIdGradeId(String schoolId, String gradeId) {
    	Grade grade = gradeService.findOne(gradeId);
    	List<Clazz> clazzes = classDao.findBySchoolIdGradeId(schoolId, gradeId);
    	if(grade != null){
    		for(Clazz clazz : clazzes){
    			clazz.setClassNameDynamic(grade.getGradeName() + clazz.getClassName());
    		}
    	}
        return SUtils.s(clazzes);
    }

    @Override
    public String findClassesByKinClass(String schoolId, int section, String enrollyear, int schoolingLen,
            String artScienceType) {
        return SUtils.s(classDao.findClassesByKinClass(schoolId, section, enrollyear, schoolingLen, artScienceType));
    }

    @Override
	public String findByIdsSort(String[] classIds) {
    	List<Clazz> classList = classService.findByIdsSort(classIds);
		return SUtils.s(classList);
	}
    
    @Override
    public String findClassListByIds(String[] ids) {
    	List<Clazz> clalist = classService.findClassListByIds(ids);
//    	上一步已经将 年级+班级 名称装配好了，这一步不需要。
//    	if(CollectionUtils.isNotEmpty(clalist)){
//    		Set<String> gids=EntityUtils.getSet(clalist, "gradeId");
//        	Map<String,Grade> gmap = gradeService.findByIdInMap(gids.toArray(new String[0]));
//        	for(Clazz cls : clalist){
//        		cls.setClassNameDynamic(gmap.get(cls.getGradeId()).getGradeName()+cls.getClassName());
//        	}
//    	}
        return SUtils.s(clalist);
        
        
        
    }

    @Override
    public String saveAllEntitys(String entitys) {
        Clazz[] dt = SUtils.dt(entitys, new TR<Clazz[]>() {
        });
        return SUtils.s(classService.saveAllEntitys(dt));
    }

	@Override
	public String findMapByGradeIdIn(String[] gradeIds) {
		return SUtils.s(classService.findMapByGradeIdIn(gradeIds));
	}
	@Override
	public String findByClassCode(String unitId, String[] classCodes) {
		List<Clazz> clalist = classService.findByClassCode(unitId, classCodes);
		if(CollectionUtils.isNotEmpty(clalist)){
    		Set<String> gids=EntityUtils.getSet(clalist, Clazz::getGradeId);
        	Map<String,Grade> gmap = gradeService.findMapByIdIn(gids.toArray(new String[0]));
        	for(Clazz cls : clalist){
        		cls.setClassNameDynamic(gmap.get(cls.getGradeId()).getGradeName()+cls.getClassName());
        	}
    	}
        return SUtils.s(clalist);
	}
	@Override
	public String findClassList(String unitId, String subjectId,
			String openAcadyear, String acadyear, String semester, int section,
			Integer isTeach) {
		return SUtils.s(classService.findClassList(unitId, subjectId, openAcadyear, acadyear, semester, section,isTeach));
	}
	@Override
	public String findBySchoolIdIn(String[] schoolIds) {
		List<Clazz> clalist = classService.findBySchoolIdIn(schoolIds);
		if(CollectionUtils.isNotEmpty(clalist)){
    		Set<String> gids=EntityUtils.getSet(clalist, Clazz::getGradeId);
        	Map<String,Grade> gmap = gradeService.findMapByIdIn(gids.toArray(new String[0]));
        	for(Clazz cls : clalist){
        		cls.setClassNameDynamic(gmap.get(cls.getGradeId()).getGradeName()+cls.getClassName());
        	}
    	}
        return SUtils.s(clalist);
	}
	@Override
	public String findAllBySchoolIdIn(String[] schoolIds) {
		List<Clazz> clalist = classService.findAllBySchoolIdIn(schoolIds);
		if(CollectionUtils.isNotEmpty(clalist)){
    		Set<String> gids=EntityUtils.getSet(clalist, Clazz::getGradeId);
        	Map<String,Grade> gmap = gradeService.findMapByIdIn(gids.toArray(new String[0]));
        	for(Clazz cls : clalist){
        		cls.setClassNameDynamic(gmap.get(cls.getGradeId()).getGradeName()+cls.getClassName());
        	}
    	}
        return SUtils.s(clalist);
	}

    @Override
    public String findListBlendIds(String[] ids) {
        return SUtils.s(classService.findListBlendIds(ids));
    }
}
