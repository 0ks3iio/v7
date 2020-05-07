/**
 * FileName: ChartDetailServiceImpl.java
 * Author:   shenke
 * Date:     2018/6/27 下午4:38
 * Descriptor:
 */
package net.zdsoft.bigdata.data.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.ChartDetailDao;
import net.zdsoft.bigdata.data.entity.ChartDetail;
import net.zdsoft.bigdata.data.service.ChartDetailService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.stereotype.Service;

/**
 * @author shenke
 * @since 2018/6/27 下午4:38
 */
@Service
public class ChartDetailServiceImpl extends BaseServiceImpl<ChartDetail, String> implements ChartDetailService {

    @Resource
    private ChartDetailDao chartDetailDao;

    @Override
    protected BaseJpaRepositoryDao<ChartDetail, String> getJpaDao() {
        return chartDetailDao;
    }

    @Override
    protected Class<ChartDetail> getEntityClass() {
        return ChartDetail.class;
    }

    @Override
    public List<ChartDetail> getByChartId(String chartId) {
        return chartDetailDao.getByChartId(chartId);
    }
}
