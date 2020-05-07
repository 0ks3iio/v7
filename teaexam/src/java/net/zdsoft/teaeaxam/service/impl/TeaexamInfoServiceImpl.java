package net.zdsoft.teaeaxam.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teaeaxam.constant.TeaexamConstant;
import net.zdsoft.teaeaxam.dao.TeaexamInfoDao;
import net.zdsoft.teaeaxam.dao.TeaexamSubjectDao;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.service.TeaexamInfoService;

@Service("teaexamInfoService")
public class TeaexamInfoServiceImpl extends BaseServiceImpl<TeaexamInfo,String> implements TeaexamInfoService{
	@Autowired
	private TeaexamInfoDao teaexamInfoDao;
	@Autowired
	private TeaexamSubjectDao teaexamSubjectDao;

	@Override
	protected BaseJpaRepositoryDao<TeaexamInfo, String> getJpaDao() {
		return teaexamInfoDao;
	}

	@Override
	protected Class<TeaexamInfo> getEntityClass() {
		return TeaexamInfo.class;
	}

	@Override
	public List<TeaexamInfo> findByInfoYearType(String unitId, int year, int type) {
		return teaexamInfoDao.findByInfoYearType(unitId, year, type);
	}
	
	public List<TeaexamInfo> findByInfoYearSchoolId(int year, String schoolId){
		if(StringUtils.isEmpty(schoolId)) {
			return new ArrayList<>();
		}
		return teaexamInfoDao.findByInfoYearSchoolId(year, "%"+schoolId+"%");
	}
	
	public List<TeaexamInfo> findByInfoYearTypeSchoolId(int year, int type, String schoolId){
		if(type < 0) {
			return findByInfoYearSchoolId(year, "%"+schoolId+"%");
		}
		return teaexamInfoDao.findByInfoYearTypeSchoolId(year, "%"+schoolId+"%", type);
	}

	@Override
	public void saveTeaexamInfo(TeaexamInfo teaexamInfo,
			List<TeaexamSubject> subjectList) {
		teaexamInfoDao.save(teaexamInfo);
		if (teaexamInfo.getInfoType() == TeaexamConstant.EXAM_INFOTYPE_0) {
			teaexamSubjectDao.deleteSubByExamId(teaexamInfo.getId());
			if (CollectionUtils.isNotEmpty(subjectList)) {
				teaexamSubjectDao.saveAll(subjectList);
			} 
		}
	}

	@Override
	public List<TeaexamInfo> findByUnitId(String unitId) {
		return teaexamInfoDao.findByUnitId(unitId);
	}

	@Override
	public void deleteTeaexamInfo(String examId) {
		teaexamInfoDao.deleteById(examId);
		teaexamSubjectDao.deleteSubByExamId(examId);
	}

	@Override
	public List<TeaexamInfo> findByRegisterTime(String registerTime) {
		return teaexamInfoDao.findByRegisterTime(registerTime);
	}

	@Override
	public List<TeaexamInfo> findByRegisterTimeAndUnitId(String unitId,
			int type, String registerTime) {
		return teaexamInfoDao.findByRegisterTimeAndUnitId(unitId, registerTime, type);
	}

	@Override
	public List<TeaexamInfo> findByRegisterEnd(int year, int type, String registerTime) {
		return teaexamInfoDao.findByRegisterEnd(year, type, registerTime);
	}

	@Override
	public List<TeaexamInfo> findByEndTime(int year, int type,
			String examEndTime, String unitId) {
		return teaexamInfoDao.findByEndTime(unitId, year, type, examEndTime);
	}

}
