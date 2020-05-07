package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.newgkelective.data.entity.NewGkReportChose;
import net.zdsoft.newgkelective.data.service.NewGkReportChoseService;
import net.zdsoft.newgkelective.data.utils.SplitUtils;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkReportChoseDao;

@Service("newGkReportChoseService")
public class NewGkReportChoseServiceImpl extends BaseServiceImpl<NewGkReportChose, String> implements NewGkReportChoseService{

	@Autowired
	private NewGkReportChoseDao newGkReportChoseDao;
	@Override
	protected BaseJpaRepositoryDao<NewGkReportChose, String> getJpaDao() {
		return newGkReportChoseDao;
	}

	@Override
	protected Class<NewGkReportChose> getEntityClass() {
		return NewGkReportChose.class;
	}

	@Override
	public List<NewGkReportChose> findByReportIdAndType(String[] reportIds, String dataType, String dataKeys) {
		if(reportIds == null || reportIds.length==0 || StringUtils.isBlank(dataType))
			return new ArrayList<>();
		
		if(StringUtils.isBlank(dataKeys)) {
			return SplitUtils.doSplit(Arrays.asList(reportIds),
					e->newGkReportChoseDao.findByReportIdInAndDataType(e.toArray(new String[0]), dataType), 1000);
		}
		
		return SplitUtils.doSplit(Arrays.asList(reportIds), 
				e->newGkReportChoseDao.findByReportIdAndType(e.toArray(new String[0]), dataType, dataKeys), 1000);
	}

    @Override
    public List<NewGkReportChose> findByReportIdAndTypeWithMaster(String[] reportIds, String dataType, String dataKeys) {
        return findByReportIdAndType(reportIds, dataType, dataKeys);
    }


    @Override
    public void deleteByReportId(String reportId) {
        newGkReportChoseDao.deleteByReportId(reportId);
    }

}