package net.zdsoft.bigdata.data.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.CockpitChartDao;
import net.zdsoft.bigdata.data.entity.Cockpit;
import net.zdsoft.bigdata.data.entity.CockpitChart;
import net.zdsoft.bigdata.data.service.CockpitChartService;
import net.zdsoft.bigdata.data.service.CockpitService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 下午6:19
 */
@Service(value = "cockpitChartService")
public class CockpitChartServiceImpl extends BaseServiceImpl<CockpitChart, String> implements CockpitChartService {

    @Resource
    private CockpitChartDao cockpitChartDao;
    @Resource
    private CockpitService cockpitService;

    @Override
    public List<CockpitChart> getCockpitChartsByCockpitId(String cockpitId) {
        return cockpitChartDao.getCockpitChartsByCockpitId(cockpitId);
    }

    @Override
    public Optional<CockpitChart> getCockpitChartsByCockpitIdAndDivId(String cockpitId, String divId) {
        return Optional.ofNullable(cockpitChartDao.getCockpitChartByCockpitIdAndDivId(cockpitId, divId));
    }

    @Override
    public void deleteCockpitChart(String cockpitId, String divId) {
        Cockpit cockpit = cockpitService.findOne(cockpitId);
        cockpit.setModifyTime(new Date());
        cockpitService.save(cockpit);
        cockpitChartDao.deleteByCockpitIdAndDivId(cockpitId, divId);
    }

    @Override
    public void deleteCockpitChart(String cockpitId) {
        cockpitChartDao.deleteByCockpitId(cockpitId);
    }

    @Override
    public void deleteCockpitChartByChartId(String[] chartId) {
        cockpitChartDao.deleteByChartIdIn(chartId);
    }

    @Override
    public void saveCockpitChart(CockpitChart cockpitChart) {
        Cockpit cockpit = cockpitService.findOne(cockpitChart.getCockpitId());
        cockpit.setModifyTime(new Date());
        cockpitService.save(cockpit);
        this.save(cockpitChart);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ArrayUtils.isEmpty(ids)) {
            return;
        }
        cockpitChartDao.deleteByIdIn(ids);
    }

    @Override
    protected BaseJpaRepositoryDao<CockpitChart, String> getJpaDao() {
        return this.cockpitChartDao;
    }

    @Override
    protected Class<CockpitChart> getEntityClass() {
        return CockpitChart.class;
    }
}
