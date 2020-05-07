package net.zdsoft.comprehensive.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.comprehensive.dao.CompreSetupDao;
import net.zdsoft.comprehensive.entity.CompreSetup;
import net.zdsoft.comprehensive.service.CompreSetupService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("compreSetupService")
public class CompreSetupServiceImpl extends BaseServiceImpl<CompreSetup, String> implements CompreSetupService{
	@Autowired
	private CompreSetupDao compreSetupDao;
	
	@Override
	public void deleteByInfoIdAndSubId(String compreInfoId,String subjectId) {
		compreSetupDao.deleteByInfoIdAndSubId(compreInfoId,subjectId);
	}
	
	@Override
	protected BaseJpaRepositoryDao<CompreSetup, String> getJpaDao() {
		return compreSetupDao;
	}

	@Override
	protected Class<CompreSetup> getEntityClass() {
		return CompreSetup.class;
	}

	@Override
	public CompreSetup findByAll(String unitId, String examId,
			String subjectId, String infoId) {
		return compreSetupDao.findByAll(unitId, examId, subjectId, infoId);
	}

	@Override
	public List<CompreSetup> findByUnitIdAndSubIdAndInfoId(String unitId,
			String subjectId, String comInfoId) {
		return compreSetupDao.findByUnitIdAndSubIdAndInfoId(unitId,subjectId,comInfoId);
	}

	@Override
	public void saveAllSetup(String unitId, String subjectId, String comInfoId,
			List<CompreSetup> compreSetups) {
		for (CompreSetup compreSetup : compreSetups) {
			compreSetup.setId(UuidUtils.generateUuid());
			compreSetup.setSubjectId(subjectId);
			compreSetup.setUnitId(unitId);
		}
		if (CollectionUtils.isNotEmpty(compreSetups)) {
			for (CompreSetup compreSetup : compreSetups) {
				compreSetupDao.save(compreSetup);
			}
		}
	}

	@Override
	public void deleteByInfo(String compreInfoId) {
		compreSetupDao.deleteByInfo(compreInfoId);
	}

	@Override
	public void deleteAndSave(String comInfoId, String unitId,
			String subjectId, List<CompreSetup> compreSetups) {
		compreSetupDao.deleteByInfoIdAndSubId(comInfoId, subjectId);
		saveAllSetup(unitId,subjectId,comInfoId,compreSetups);
	}

	@Override
	public List<CompreSetup> findByUnitIdAndInfoId(String unitId, String infoId) {
		return compreSetupDao.findByUnitIdAndCompreInfoId(unitId, infoId);
	}

	@Override
	public List<CompreSetup> findByUnitIdAndInfoIdAndType(String unitId, String infoId, String type) {
		return compreSetupDao.findByUnitIdAndCompreInfoIdAndType(unitId, infoId, type);
	}

	@Override
	public void deleteByInfoIdAndType(String unitId, String infoId, String type) {
		compreSetupDao.deleteByInfoIdAndType(unitId, infoId, type);
	}

}
