package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.CockpitChartStyleDao;
import net.zdsoft.bigdata.data.entity.CockpitChartStyle;
import net.zdsoft.bigdata.data.service.CockpitChartStyleService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author shenke
 * @since 2018/8/7 上午11:17
 */
@Service
public class CockpitChartStyleServiceImpl extends BaseServiceImpl<CockpitChartStyle, String> implements CockpitChartStyleService {

    @Resource
    private CockpitChartStyleDao cockpitChartStyleDao;

    @Override
    protected BaseJpaRepositoryDao<CockpitChartStyle, String> getJpaDao() {
        return cockpitChartStyleDao;
    }

    @Override
    protected Class<CockpitChartStyle> getEntityClass() {
        return CockpitChartStyle.class;
    }

    @Override
    public void deleteStyleByCockpitChartIds(String[] cockpitChartIds) {
        if (ArrayUtils.isNotEmpty(cockpitChartIds)) {
            cockpitChartStyleDao.deleteByCockpitChartIdIn(cockpitChartIds);
        }
    }

    @Override
    public List<CockpitChartStyle> getCockpitChartStylesByCockpitChartIds(String[] cockpitChartIds) {
        if (ArrayUtils.isEmpty(cockpitChartIds)) {
            return Collections.emptyList();
        }
        return cockpitChartStyleDao.getByCockpitChartIdIn(cockpitChartIds);
    }
}
