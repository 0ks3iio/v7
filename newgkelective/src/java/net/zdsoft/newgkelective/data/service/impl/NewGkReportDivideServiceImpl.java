package net.zdsoft.newgkelective.data.service.impl;

import java.util.Arrays;
import java.util.List;

import net.zdsoft.newgkelective.data.utils.SplitUtils;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.echarts.common.StringUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkReportDivideDao;
import net.zdsoft.newgkelective.data.entity.NewGkReport;
import net.zdsoft.newgkelective.data.entity.NewGkReportDivide;
import net.zdsoft.newgkelective.data.service.NewGkReportDivideService;
import net.zdsoft.newgkelective.data.service.NewGkReportService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("newGkReportDivideService")
public class NewGkReportDivideServiceImpl extends BaseServiceImpl<NewGkReportDivide, String> implements NewGkReportDivideService{
	
	@Autowired
	private NewGkReportDivideDao newGkReportDivideDao;
	
	@Autowired
	private NewGkReportService newGkReportService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkReportDivide, String> getJpaDao() {
		return newGkReportDivideDao;
	}

	@Override
	protected Class<NewGkReportDivide> getEntityClass() {
		return NewGkReportDivide.class;
	}

	@Override
	public List<NewGkReportDivide> findByReportIdAnd(String[] reportIds, String subjectId) {
		if(StringUtils.isNotBlank(subjectId)) {
			return SplitUtils.doSplit(Arrays.asList(reportIds), 
					e->newGkReportDivideDao.findByReportIdInAndSubjectId(e.toArray(new String[0]), subjectId),1000);
		}
		return SplitUtils.doSplit(Arrays.asList(reportIds), 
				e->newGkReportDivideDao.findByReportIdIn(e.toArray(new String[0])),1000);
	}

	@Override
	public void saveAndDelete(NewGkReport report, List<NewGkReportDivide> divideList) {
		newGkReportService.save(report);
		newGkReportDivideDao.deleteByReportId(report.getId());
		if(CollectionUtils.isNotEmpty(divideList)){
			saveAll(divideList.toArray(new NewGkReportDivide[divideList.size()]));
		}
	}

}
