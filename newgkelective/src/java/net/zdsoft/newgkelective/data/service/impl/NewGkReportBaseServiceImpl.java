
package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.newgkelective.data.entity.NewGkReportBase;
import net.zdsoft.newgkelective.data.service.NewGkReportBaseService;
import net.zdsoft.newgkelective.data.utils.SplitUtils;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkReportBaseDao;

@Service("NewGkReportBaseService")
public class NewGkReportBaseServiceImpl extends BaseServiceImpl<NewGkReportBase, String> implements NewGkReportBaseService{

	@Autowired
	private NewGkReportBaseDao newGkReportBaseDao;
	@Override
	protected BaseJpaRepositoryDao<NewGkReportBase, String> getJpaDao() {
		return newGkReportBaseDao;
	}

	@Override
	protected Class<NewGkReportBase> getEntityClass() {
		return NewGkReportBase.class;
	}

	@Override
	public List<NewGkReportBase> findByReportIdAndType(String[] reportIds, String[] types) {
		if(reportIds==null || reportIds.length==0)
			return new ArrayList<>();
		
		if(types == null || types.length==0) {
			return SplitUtils.doSplit(Arrays.asList(reportIds), 
					e->newGkReportBaseDao.findByReportIdIn(e.toArray(new String[0])), 1000);
		}
		else {
			return SplitUtils.doSplit(Arrays.asList(reportIds), 
					e->newGkReportBaseDao.findByReportIdAndType(e.toArray(new String[0]),types), 1000);
		}
	}

	@Override
	public void deleteByReportId(String reportId) {
		newGkReportBaseDao.deleteByReportId(reportId);
	}

	@Override
	public List<NewGkReportBase> findByReportIdAndTypeWithMaster(String[] reportIds, String[] types) {
		return findByReportIdAndType(reportIds, types);
	}

}
