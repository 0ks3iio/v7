package net.zdsoft.studevelop.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StuDevelopQualityReportSetDao;
import net.zdsoft.studevelop.data.entity.StuDevelopQualityReportSet;
import net.zdsoft.studevelop.data.service.StuDevelopQualityReportSetService;
@Service("stuDevelopQualityReportSetService")
public class StuDevelopQualityReportSetServiceImpl extends BaseServiceImpl<StuDevelopQualityReportSet, String> implements StuDevelopQualityReportSetService{
    @Autowired
	private StuDevelopQualityReportSetDao stuDevelopQualityReportSetDao;
	
	@Override
	protected BaseJpaRepositoryDao<StuDevelopQualityReportSet, String> getJpaDao() {
		return stuDevelopQualityReportSetDao;
	}

	@Override
	protected Class<StuDevelopQualityReportSet> getEntityClass() {
		return StuDevelopQualityReportSet.class;
	}

	@Override
	public StuDevelopQualityReportSet findByAll(String unitId, String acadyear,
			String semester, int section) {
		return stuDevelopQualityReportSetDao.findByAll(unitId, acadyear, semester, section);
	}

}
