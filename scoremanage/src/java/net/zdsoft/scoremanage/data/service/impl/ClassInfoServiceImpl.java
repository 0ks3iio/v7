package net.zdsoft.scoremanage.data.service.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.dao.ClassInfoDao;
import net.zdsoft.scoremanage.data.entity.ClassInfo;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ClassInfoService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;

@Service("classInfoService")
public class ClassInfoServiceImpl extends BaseServiceImpl<ClassInfo, String> implements ClassInfoService{

	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private ClassInfoDao classInfoDao;
	
	@Override
	protected BaseJpaRepositoryDao<ClassInfo, String> getJpaDao() {
		return classInfoDao;
	}

	@Override
	protected Class<ClassInfo> getEntityClass() {
		return ClassInfo.class;
	}

	@Override
	public List<ClassInfo> findList(String unitId, String classType,
			String classIdSearch, String[] courseInfoIds) {
		List<ClassInfo> returnList=new ArrayList<ClassInfo>();
		if(StringUtils.isNotBlank(classIdSearch)){
			returnList=classInfoDao.findList(unitId,classType,classIdSearch,courseInfoIds);
		}else{
			returnList=classInfoDao.findList(unitId,classType,courseInfoIds);
		}
		return returnList;
	}

	@Override
	public List<ClassInfo> findList(String unitId, String... courseInfoIds) {
		return classInfoDao.findList(unitId,courseInfoIds);
	}

	@Override
	public List<ClassInfo> findByCourseInfoIdIn(String... subjectInfoIds) {
		return classInfoDao.findBySubjectInfoIdIn(subjectInfoIds);
	}

	@Override
	public List<ClassInfo> findBySchoolIdAndSubjectInfoIdIn(String schoolId, String... subjectInfoIds) {
		return classInfoDao.findBySchoolIdAndSubjectInfoIdIn(schoolId,subjectInfoIds);
	}

	@Override
	public List<ClassInfo> findByExamInfoId(String examInfoId) {
		List<SubjectInfo> cifList = subjectInfoService.findByExamIdAndCourseId(examInfoId, null);
		Set<String> cifIds = new HashSet<String>();
		for (SubjectInfo courseInfo : cifList) {
			cifIds.add(courseInfo.getId());
		}
		return classInfoDao.findByExamInfoId(cifIds.toArray(new String[0]));
	}

	@Override
	public void deleteBySchoolId(String schooolId, String... courseInfoIds) {
		if(courseInfoIds!=null && courseInfoIds.length>0)
			classInfoDao.deleteBySchoolId(schooolId,courseInfoIds);
	}

	@Override
	public void deleteAllByIds(String... id) {
		if(id!=null && id.length>0)
			classInfoDao.deleteAllByIds(id);
	}

	@Override
	public List<ClassInfo> saveAllEntitys(ClassInfo... classInfo) {
		if(classInfo!=null&&classInfo.length>0){
			for (ClassInfo classInfo2 : classInfo) {
				if(classInfo2.getIsLock()==null){
					classInfo2.setIsLock("0");
				}
			}
		}
		return classInfoDao.saveAll(checkSave(classInfo));
	}

	@Override
	public List<ClassInfo> findByAll(String unitId, String classType, String[] subjectInfoIds) {
		return classInfoDao.findBySchoolIdAndClassTypeAndSubjectInfoIdIn(unitId, classType, subjectInfoIds);
	}
	@Override
	public ClassInfo findByAll(String unitId, String classType,
			String classId, String subjectInfoId) {
		return classInfoDao.findOneBySchoolIdAndClassTypeAndClassIdAndSubjectInfoId(unitId,classType,classId,subjectInfoId);
	}

	@Override
	public List<ClassInfo> findBySchoolIdAndClassIdIn(String unitId, String[] classIds) {
		return classInfoDao.findBySchoolIdAndClassIdIn(unitId,classIds);
	}

}
