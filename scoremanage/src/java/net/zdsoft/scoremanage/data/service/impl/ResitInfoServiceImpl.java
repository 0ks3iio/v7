package net.zdsoft.scoremanage.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.dao.ResitInfoDao;
import net.zdsoft.scoremanage.data.entity.ResitInfo;
import net.zdsoft.scoremanage.data.service.ResitInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("resitInfoService")
public class ResitInfoServiceImpl extends BaseServiceImpl<ResitInfo, String> implements ResitInfoService{
    @Autowired
	private ResitInfoDao resitInfoDao;
	
	@Override
	protected BaseJpaRepositoryDao<ResitInfo, String> getJpaDao() {
		return resitInfoDao;
	}

	@Override
	protected Class<ResitInfo> getEntityClass() {
		return ResitInfo.class;
	}

	@Override
	public void deleteResitInfoBy(String unitId, String acadyear,
			String semester, String examId, String gradeId) {
		resitInfoDao.deleteResitInfoBy(unitId, acadyear, semester, examId, gradeId);
	}

	@Override
	public List<ResitInfo> listResitInfoBy(String unitId, String acadyear,
			String semester, String examId, String gradeId) {
		return resitInfoDao.listResitInfoBy(unitId, acadyear, semester, examId, gradeId);
	}
}
