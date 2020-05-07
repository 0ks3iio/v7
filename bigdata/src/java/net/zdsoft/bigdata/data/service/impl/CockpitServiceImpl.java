package net.zdsoft.bigdata.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.CockpitChartDao;
import net.zdsoft.bigdata.data.dao.CockpitDao;
import net.zdsoft.bigdata.data.dto.DashboardCloneDto;
import net.zdsoft.bigdata.data.dto.DashboardSaveDto;
import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.entity.Cockpit;
import net.zdsoft.bigdata.data.entity.CockpitChart;
import net.zdsoft.bigdata.data.entity.CockpitChartStyle;
import net.zdsoft.bigdata.data.service.ChartService;
import net.zdsoft.bigdata.data.service.CockpitChartService;
import net.zdsoft.bigdata.data.service.CockpitChartStyleService;
import net.zdsoft.bigdata.data.service.CockpitService;
import net.zdsoft.bigdata.data.service.CockpitStyleService;
import net.zdsoft.bigdata.data.service.SubscribeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ke_shen@126.com
 * @since 2018/4/13 下午1:35
 */
@Service("cockpitService")
public class CockpitServiceImpl extends BaseServiceImpl<Cockpit, String> implements CockpitService {

    @Resource
    private CockpitDao cockpitDao;
    @Resource
    private CockpitChartDao cockpitChartDao;
    @Resource
    private CockpitStyleService cockpitStyleService;
    @Resource
    private CockpitChartStyleService cockpitChartStyleService;
    @Resource
    private CockpitChartService cockpitChartService;
    @Resource
    private SubscribeService subscribeService;
    @Resource
    private ChartService chartService;

    @Override
    public List<Cockpit> getCockpitsByUnitId(String unitId) {
        return cockpitDao.getCockpitsByUnitId(unitId);
    }

    @Override
    public void deleteCockpit(String id) {
        cockpitChartDao.deleteByCockpitId(id);
        cockpitDao.deleteById(id);
    }

    @Override
    public void updateCockpitTemplate(String template, String cockpitId) {
        Cockpit cockpit = findOne(cockpitId);
        cockpit.setTemplate(template);
        cockpit.setModifyTime(new Date());
        cockpitChartService.deleteCockpitChart(cockpitId);
        save(cockpit);
    }

    @Transactional(
            transactionManager = "txManagerJap",
            rollbackFor = Throwable.class
    )
    @Override
    public void batchDelete(String[] ids) {
        subscribeService.deleteByBusinessId(ids);
        cockpitDao.batchDelete(ids);
    }

    @Override
    public void doClone(DashboardCloneDto dashboardCloneDto) {
        save(dashboardCloneDto.getCockpit());
        if (dashboardCloneDto.getCockpitStyle() != null) {
            cockpitStyleService.save(dashboardCloneDto.getCockpitStyle());
        }
        if (dashboardCloneDto.getCockpitCharts() != null
                && !dashboardCloneDto.getCockpitCharts().isEmpty()) {
            cockpitChartService.saveAll(dashboardCloneDto.getCockpitCharts().toArray(new CockpitChart[0]));
        }
        if (dashboardCloneDto.getCockpitChartStyles() != null
                && !dashboardCloneDto.getCockpitChartStyles().isEmpty()) {
            cockpitChartStyleService.saveAll(dashboardCloneDto.getCockpitChartStyles().toArray(new CockpitChartStyle[0]));
        }
        if (dashboardCloneDto.getCloneCharts() != null
                && !dashboardCloneDto.getCloneCharts().isEmpty()) {
            chartService.saveAll(dashboardCloneDto.getCloneCharts().toArray(new Chart[0]));
        }
    }

    @Override
    public List<Cockpit> getCurrentUserCockpits(String userId, String unitId, boolean editPage) {
        if (editPage) {
            return cockpitDao.getCurrentUserEdit(unitId);
        }
        return cockpitDao.getCurrentUserQuery(userId, unitId);
    }

    @Override
    public void saveCockpit(DashboardSaveDto dashboardSaveDto) {
        save(dashboardSaveDto.getCockpit());
        cockpitStyleService.save(dashboardSaveDto.getCockpitStyle());


        List<CockpitChart> origins = cockpitChartService.getCockpitChartsByCockpitId(dashboardSaveDto.getCockpit().getId());
        Set<String> toSaveCockpitChartIdSet = dashboardSaveDto.getCockpitCharts().stream()
                .map(CockpitChart::getId).collect(Collectors.toSet());
        String[] deleteCockpitChartIds = origins.stream()
                .filter(cockpitChart -> !toSaveCockpitChartIdSet.contains(cockpitChart.getId()))
                .map(CockpitChart::getId).toArray(String[]::new);
        cockpitChartService.deleteByIds(deleteCockpitChartIds);
        cockpitChartService.saveAll(dashboardSaveDto.getCockpitCharts().toArray(new CockpitChart[0]));

        String[] toSaveCockpitChartIds = toSaveCockpitChartIdSet.toArray(new String[0]);
        cockpitChartStyleService.deleteStyleByCockpitChartIds(toSaveCockpitChartIds);
        cockpitChartStyleService.saveAll(dashboardSaveDto.getCockpitChartStyles().toArray(new CockpitChartStyle[0]));

        //被删除的cockpitChart引用的Chart的Id
        String[] deleteCockpitChartQuoteChartIds = origins.stream()
                .filter(cockpitChart -> !toSaveCockpitChartIdSet.contains(cockpitChart.getId()))
                .map(CockpitChart::getChartId).toArray(String[]::new);

        List<Chart> deleteTextCharts = chartService.getTextChartByIds(deleteCockpitChartQuoteChartIds);
        List<Chart> deleteOtherCharts = chartService.getOtherChartByIds(deleteCockpitChartQuoteChartIds);
        List<Chart> deleteCharts = new ArrayList<>(deleteOtherCharts.size() + deleteTextCharts.size());
        if (!deleteTextCharts.isEmpty()) {
            deleteCharts.addAll(deleteTextCharts);
        }
        if (!deleteOtherCharts.isEmpty()) {
            deleteCharts.addAll(deleteOtherCharts);
        }
        if (!deleteCharts.isEmpty()) {
            chartService.batchDelete(deleteCharts.stream().map(Chart::getId).toArray(String[]::new));
        }

        List<Chart> addCharts = new ArrayList<>(dashboardSaveDto.getCharts().size() + dashboardSaveDto.getOtherCharts().size());
        addCharts.addAll(dashboardSaveDto.getCharts());
        addCharts.addAll(dashboardSaveDto.getOtherCharts());
        chartService.saveAll(addCharts.toArray(new Chart[0]));
    }

    @Override
    protected BaseJpaRepositoryDao<Cockpit, String> getJpaDao() {
        return cockpitDao;
    }

    @Override
    protected Class<Cockpit> getEntityClass() {
        return Cockpit.class;
    }
}
