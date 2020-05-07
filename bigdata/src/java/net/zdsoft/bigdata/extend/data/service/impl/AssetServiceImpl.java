package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.AssetDao;
import net.zdsoft.bigdata.extend.data.entity.Asset;
import net.zdsoft.bigdata.extend.data.service.AssetService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class AssetServiceImpl extends BaseServiceImpl<Asset, String> implements AssetService {

    @Resource
    private AssetDao assetDao;

    @Override
    protected BaseJpaRepositoryDao<Asset, String> getJpaDao() {
        return assetDao;
    }

    @Override
    protected Class<Asset> getEntityClass() {
        return Asset.class;
    }
}
