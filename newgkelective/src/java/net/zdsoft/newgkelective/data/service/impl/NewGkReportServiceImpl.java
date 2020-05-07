package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.echarts.common.StringUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.newgkelective.data.dao.NewGkReportDao;
import net.zdsoft.newgkelective.data.entity.NewGkReport;
import net.zdsoft.newgkelective.data.entity.NewGkReportBase;
import net.zdsoft.newgkelective.data.entity.NewGkReportChose;
import net.zdsoft.newgkelective.data.service.NewGkReportBaseService;
import net.zdsoft.newgkelective.data.service.NewGkReportChoseService;
import net.zdsoft.newgkelective.data.service.NewGkReportService;
import net.zdsoft.newgkelective.data.utils.SplitUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

@Service("newGkReportService")
public class NewGkReportServiceImpl extends BaseServiceImpl<NewGkReport, String> implements NewGkReportService{
	@Autowired
	private NewGkReportDao newGkReportDao;
	@Autowired
	private NewGkReportBaseService newGkReportBaseService;
		@Autowired
    private NewGkReportChoseService newGkReportChoseService;

	@Override
	protected BaseJpaRepositoryDao<NewGkReport, String> getJpaDao() {
		return newGkReportDao;
	}

	@Override
	protected Class<NewGkReport> getEntityClass() {
		return NewGkReport.class;
	}

	@Override
	public List<NewGkReport> findListBy(String[] unitIds, String openAcadyear) {
		if(unitIds==null || unitIds.length==0 || StringUtils.isBlank(openAcadyear))
			return new ArrayList<>();
		return SplitUtils.doSplit(Arrays.asList(unitIds), e->newGkReportDao.findListBy(e.toArray(new String[0]), openAcadyear), 1000);
	}

	@Override
	public void saveAllBaseList(NewGkReport report, List<NewGkReportBase> baseList) {
		
		this.save(report);
		if(CollectionUtils.isNotEmpty(baseList)) {
			newGkReportBaseService.deleteByReportId(report.getId());
			newGkReportBaseService.saveAll(baseList.toArray(new NewGkReportBase[0]));
		}
		
	}

    @Override
    public NewGkReport findOneChoseByGradeId(String unitId, String gradeId) {
        return newGkReportDao.findOneChoseByGradeId(unitId, gradeId);
    }

    @Override
    public NewGkReport findOneByGradeId(String unitId, String gradeId) {
        return newGkReportDao.findOneByGradeId(unitId, gradeId);
    }

    @Override
    public void saveAllChoseList(NewGkReport newGkReport, List<NewGkReportChose> newGkReportChoseList) {
        newGkReportChoseService.deleteByReportId(newGkReport.getId());
	    newGkReportDao.save(newGkReport);
        newGkReportChoseService.saveAll(newGkReportChoseList.toArray(new NewGkReportChose[0]));
    }

	@Override
	public List<String> findAllOpenAcadyear() {
		List<String> returnlist = RedisUtils.getObject("REDIS.NK.REPORT.OPENTIME", RedisUtils.TIME_ONE_WEEK, new TypeReference<List<String>>(){}, new RedisInterface<List<String>>(){
			@Override
			public List<String> queryData() {
				return newGkReportDao.findAllOpenAcadyear();
			}
		});
		
		return returnlist;
	}

	@Override
	public List<NewGkReport> findListByWithMaster(String[] unitIds, String openAcadyear) {
		return findListBy(unitIds, openAcadyear);
	}

}
