package net.zdsoft.system.service.server.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.dao.SysPlatformModelDao;
import net.zdsoft.system.entity.SysPlatformModel;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.service.server.SysPlatformModelService;

/**
 * created by shenke 2017/3/7 16:09
 */
@Service("SysPlatformModelService")
public class SysPlatformModelServiceImpl extends BaseServiceImpl<SysPlatformModel, Integer> implements
        SysPlatformModelService {

    @Autowired
    private SysPlatformModelDao SysPlatformModelDao;

    @Override
    protected BaseJpaRepositoryDao<SysPlatformModel, Integer> getJpaDao() {
        return this.SysPlatformModelDao;
    }

    @Override
    protected Class<SysPlatformModel> getEntityClass() {
        return SysPlatformModel.class;
    }

    @Override
    public List<SysPlatformModel> findByPlatform(Integer platform) {
        return SysPlatformModelDao.findByPlatform(platform);
    }

    @Override
    public List<Model> findModelsByPlatform(Integer platform) {
        List<Model> modelList = new ArrayList<Model>();

        List<SysPlatformModel> sysPlatformModelList = SysPlatformModelDao.findByPlatform(platform);
        if (CollectionUtils.isNotEmpty(sysPlatformModelList)) {
            modelList.addAll(SysPlatformModel.turn2Models(sysPlatformModelList));
        }
        return modelList;
    }
}
