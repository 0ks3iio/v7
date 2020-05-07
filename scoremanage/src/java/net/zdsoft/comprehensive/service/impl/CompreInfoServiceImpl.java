package net.zdsoft.comprehensive.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.comprehensive.dao.CompreInfoDao;
import net.zdsoft.comprehensive.dao.CompreScoreDao;
import net.zdsoft.comprehensive.dao.CompreSetupDao;
import net.zdsoft.comprehensive.entity.CompreInfo;
import net.zdsoft.comprehensive.service.CompreInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("compreInfoService")
public class CompreInfoServiceImpl extends BaseServiceImpl<CompreInfo, String> implements CompreInfoService{

	@Autowired
	private CompreInfoDao compreInfoDao;
	@Autowired
	private CompreScoreDao compreScoreDao;
	@Autowired
	private CompreSetupDao compreSetupDao;
	
	@Override
	public void deleteAll(String id) {
		compreInfoDao.deleteById(id);
		compreScoreDao.deleteByExamId(id);
		compreSetupDao.deleteByInfo(id);
	}
	
	@Override
	public List<CompreInfo> findByUnitIdAndAcadyearAndSemester(String unitId,
			String searchAcadyear, String searchSemester) {
		return compreInfoDao.findByUnitIdAndAcadyearAndSemester(unitId,searchAcadyear,searchSemester);
	}
	
	@Override
	protected BaseJpaRepositoryDao<CompreInfo, String> getJpaDao() {
		return compreInfoDao;
	}

	@Override
	protected Class<CompreInfo> getEntityClass() {
		return CompreInfo.class;
	}

	@Override
	public CompreInfo findByGradeId(String gradeId, String acadyear,
			String semester) {
		return compreInfoDao.findByGradeId(gradeId,acadyear,semester);
	}

//	@Override
//	public void deleteAndsaveByInfoAndRel(String compreInfoId,CompreInfo compreInfo,
//			List<CompreRelationship> compreRelList,List<String> delSubList) {
//		if (StringUtils.isNotBlank(compreInfoId)) {
//			compreInfoDao.deleteById(compreInfoId);
//			for (String str : delSubList) {
//				compreSetupDao.deleteByInfoIdAndSubId(compreInfoId,str);
//			}
//		}
//		compreInfoDao.save(compreInfo);
//	}

	@Override
	public List<CompreInfo> findByUnitIdAndGradeIds(String unitId, String[] gradeIds) {
		return compreInfoDao.findByUnitIdAndGradeIdIn(unitId, gradeIds);
	}

    @Override
    public CompreInfo findOneByGradeIdAndGradeCode(String unitId, String gradeId, String gradeCode) {
        return compreInfoDao.findOneByGradeIdAndGradeCode(unitId, gradeId, gradeCode);
    }


}
