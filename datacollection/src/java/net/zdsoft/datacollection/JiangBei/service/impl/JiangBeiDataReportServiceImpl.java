package net.zdsoft.datacollection.JiangBei.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.datacollection.JiangBei.dao.JiangBeiDataReportDao;
import net.zdsoft.datacollection.JiangBei.entity.JiangBeiDataReport;
import net.zdsoft.datacollection.JiangBei.service.JiangBeiDataReportService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service
public class JiangBeiDataReportServiceImpl extends BaseServiceImpl<JiangBeiDataReport, String>
		implements JiangBeiDataReportService {
	
	@Autowired
	private JiangBeiDataReportDao jiangBeiDataReportDao;

	@Override
	protected BaseJpaRepositoryDao<JiangBeiDataReport, String> getJpaDao() {
		return jiangBeiDataReportDao;
	}

	@Override
	protected Class<JiangBeiDataReport> getEntityClass() {
		return JiangBeiDataReport.class;
	}
	
	

}
