package net.zdsoft.bigdata.data.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.BiFavoriteDao;
import net.zdsoft.bigdata.data.entity.BiFavorite;
import net.zdsoft.bigdata.data.entity.BiShare;
import net.zdsoft.bigdata.data.service.BiFavoriteService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.UuidUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("biFavoriteService")
public class BiFavoriteServiceImpl extends BaseServiceImpl<BiFavorite, String>
        implements BiFavoriteService {

    @Resource
    private BiFavoriteDao biFavoriteDao;

    @Override
    protected BaseJpaRepositoryDao<BiFavorite, String> getJpaDao() {
        return biFavoriteDao;
    }

    @Override
    protected Class<BiFavorite> getEntityClass() {
        return BiFavorite.class;
    }

    @Override
    public List<BiFavorite> findBiFavoriteListByUserId(String userId,
                                                       Pagination pagination) {
        if (pagination == null)
            return biFavoriteDao.findBiFavoriteListByUserId(userId);
        else {
            Integer count = biFavoriteDao.findBiFavoriteListByUserId(userId).size();
            pagination.setMaxRowCount(count == null ? 0 : count.intValue());
            return biFavoriteDao.findBiFavoriteListByUserId(userId, pagination.toPageable());
        }
    }

    @Override
    public List<BiFavorite> findBiFavoriteListByUserIdAndBusinessId(
            String userId, String businessId) {
        return biFavoriteDao.findBiFavoriteListByUserIdAndBusinessId(userId,
                businessId);
    }

    @Override
    public void deleteBiFavoriteByUserIdAndBusinessId(String userId,
                                                      String businessId) {
        biFavoriteDao.deleteBiFavoriteByUserIdAndBusinessId(userId, businessId);
    }

    @Override
    public void addBiFavorite(BiFavorite favorite) {
        favorite.setId(UuidUtils.generateUuid());
        favorite.setCreationTime(new Date());
        save(favorite);
    }

    @Override
    public void addBiFavorite(String businessId, String businessType,
                              String businessName, String userId) {
        BiFavorite favorite = new BiFavorite();
        favorite.setId(UuidUtils.generateUuid());
        favorite.setBusinessId(businessId);
        favorite.setBusinessType(businessType);
        favorite.setBusinessType(businessType);
        favorite.setUserId(userId);
        favorite.setCreationTime(new Date());
    }

    @Override
    public Integer findAllByUserId(String userId) {
        return biFavoriteDao.countAllByUserId(userId);
    }

}
