package net.zdsoft.datareport.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.datareport.data.dao.DataReportTitleDao;
import net.zdsoft.datareport.data.entity.DataReportTitle;
import net.zdsoft.datareport.data.service.DataReportTitleService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dataReportTitleService")
public class DataReportTitleServiceImpl extends BaseServiceImpl<DataReportTitle,String> implements DataReportTitleService{

	@Autowired
	private DataReportTitleDao dataReportTitleDao;
	
	@Override
	public List<DataReportTitle> findByReportId(String infoId) {
		return dataReportTitleDao.findByReportId(infoId);
	}
	
	@Override
	public void deleteByReportId(String reportId) {
		dataReportTitleDao.deleteByReportId(reportId);
	}
	
	@Override
	public DataReportTitle findByTypeAndId(Integer type, String reportId) {
		return dataReportTitleDao.findByTypeAndId(type,reportId);
	}
	
	@Override
	protected BaseJpaRepositoryDao<DataReportTitle, String> getJpaDao() {
		return dataReportTitleDao;
	}

	@Override
	protected Class<DataReportTitle> getEntityClass() {
		return DataReportTitle.class;
	}
}
