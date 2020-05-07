package net.zdsoft.datareport.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.datareport.data.dao.DataReportColumnDao;
import net.zdsoft.datareport.data.entity.DataReportColumn;
import net.zdsoft.datareport.data.service.DataReportColumnService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dataReportColumnService")
public class DataReportColumnServiceImpl extends BaseServiceImpl<DataReportColumn,String> implements DataReportColumnService{

	@Autowired
	private DataReportColumnDao dataReportColumnDao;
	
	@Override
	public List<DataReportColumn> findByReportId(String reportId) {
		return dataReportColumnDao.findByReportId(reportId);
	}
	
	@Override
	public List<DataReportColumn> findByIdAndType(String reportId, Integer type) {
		return dataReportColumnDao.findByIdAndType(reportId,type);
	}
	
	@Override
	public void deleteByReportId(String reportId) {
		dataReportColumnDao.deleteByReportId(reportId);
	}
	
	@Override
	protected BaseJpaRepositoryDao<DataReportColumn, String> getJpaDao() {
		return dataReportColumnDao;
	}

	@Override
	protected Class<DataReportColumn> getEntityClass() {
		return DataReportColumn.class;
	}
}
