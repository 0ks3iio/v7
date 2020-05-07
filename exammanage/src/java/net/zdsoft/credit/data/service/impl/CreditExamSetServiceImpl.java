package net.zdsoft.credit.data.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.credit.data.constant.CreditConstants;
import net.zdsoft.credit.data.dao.CreditExamSetDao;
import net.zdsoft.credit.data.entity.CreditExamSet;
import net.zdsoft.credit.data.entity.CreditSet;
import net.zdsoft.credit.data.service.CreditExamSetService;
import net.zdsoft.credit.data.service.CreditModuleInfoService;
import net.zdsoft.credit.data.service.CreditSetService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("creditExamSetService")
public class CreditExamSetServiceImpl extends BaseServiceImpl<CreditExamSet, String> implements CreditExamSetService {
	@Autowired
	private CreditExamSetDao creditExamSetDao;
	@Autowired
	private CreditSetService creditSetService;
	@Autowired
	private CreditModuleInfoService creditModuleInfoService;
	
	@Override
	public void saveUsualSet(String unitId, String userId, String setId, String gradeId, String classId,
			String classType, String subjectId, int maxRow) {
		CreditSet set = creditSetService.findOne(setId);
		String acadyear = set.getAcadyear();
		String semester = set.getSemester();
		List<CreditExamSet> usualList = findByUsualSet(setId, acadyear, semester, subjectId, classId, classType);
		Set<String> deleteIds = new HashSet<>();
		Set<String> allInts = new HashSet<>();
		for (CreditExamSet usual : usualList) {
			int nameInt = NumberUtils.toInt(usual.getName().trim());
			if(nameInt>maxRow) {
				deleteIds.add(usual.getId());
				continue;
			}
			allInts.add(usual.getName().trim());
		}
		List<CreditExamSet> addList=  new ArrayList<>();
		CreditExamSet uu;
		for(int i=1;i<=maxRow;i++) {
			if(!allInts.contains(i+"")) {
				uu = new CreditExamSet();
				uu.setAcadyear(acadyear);
				uu.setSemester(semester);
				uu.setUnitId(unitId);
				uu.setOperator(userId);
				uu.setClassId(classId);
				uu.setClassType(classType);
				uu.setName(i+"");
				uu.setGradeId(gradeId);
				uu.setSetId(setId);
				uu.setSubjectId(subjectId);
				uu.setType(CreditConstants.CREDIT_EXAM_TYPE_1);
				addList.add(uu);
			}
		}
		if(deleteIds.size() > 0) {
			creditExamSetDao.deleteByIds(deleteIds.toArray(new String[0]));
			creditModuleInfoService.deleteBySetExamIds(deleteIds.toArray(new String[0]));
		}
		if(addList.size() > 0) {
			checkSave(addList.toArray(new CreditExamSet[0]));
			saveAll(addList.toArray(new CreditExamSet[0]));
		}
	}
	
	@Override
	public List<CreditExamSet> findBySetIdAndAcadyearAndSemesterAndGradeIdAndType(String setId, String acadyear,
			String semester, String gradeId, String type) {
		return creditExamSetDao.findBySetIdAndAcadyearAndSemesterAndGradeIdAndType(setId,acadyear,semester,gradeId,type);
	}
	
	@Override
	public List<CreditExamSet> findByUsualSet(String setId, String acadyear, String semester, String subjectId,
			String classId, String classType) {
		return creditExamSetDao.findByUsualSet(setId,acadyear,semester,subjectId,classId,classType);
	}
	
	@Override
	protected BaseJpaRepositoryDao<CreditExamSet, String> getJpaDao() {
		return creditExamSetDao;
	}

    @Override
    protected Class<CreditExamSet> getEntityClass() {
        return CreditExamSet.class;
    }

}
