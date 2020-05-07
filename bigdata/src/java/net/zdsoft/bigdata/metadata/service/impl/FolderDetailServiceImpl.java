package net.zdsoft.bigdata.metadata.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.metadata.dao.FolderDetailDao;
import net.zdsoft.bigdata.metadata.entity.FolderDetail;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wangdongdong on 2019/1/7 14:16.
 */
@Service
public class FolderDetailServiceImpl extends BaseServiceImpl<FolderDetail, String> implements FolderDetailService {

    @Resource
    private FolderDetailDao folderDetailDao;

    @Override
    protected BaseJpaRepositoryDao<FolderDetail, String> getJpaDao() {
        return folderDetailDao;
    }

    @Override
    protected Class<FolderDetail> getEntityClass() {
        return FolderDetail.class;
    }

    @Override
    public List<FolderDetail> findAllAuthorityFolderDetail(String unitId, String userId, String[] folderIds) {
        if (folderIds != null) {
            return folderDetailDao.findAllAuthorityFolderDetailByFolderId(unitId, userId, folderIds);
        }
        return folderDetailDao.findAllAuthorityFolderDetail(unitId, userId);
    }

    @Override
    public List<FolderDetail> findAllAuthorityFolderDetailByBusinessName(String unitId, String userId, String businessName) {
        if (StringUtils.isBlank(businessName)) {
            return Lists.newArrayList();
        }

        return folderDetailDao.findAllAuthorityFolderDetailByBusinessName(unitId, userId, "%" + businessName + "%");
    }

    @Override
    public void deleteByBusinessId(String businessId) {
        folderDetailDao.deleteByBusinessId(businessId);
    }

    @Override
    public void deleteByBusinessIdIn(String[] businessIds) {
        folderDetailDao.deleteByBusinessIdIn(businessIds);
    }

    @Override
    public Integer getMaxOrderIdByFolderId(String folderId) {
        Integer maxOrderId = folderDetailDao.getMaxOrderIdByFolderId(folderId);
        return maxOrderId == null ? 0 : maxOrderId;
    }

    @Override
    public List<FolderDetail> findAllFolderDetailByFolderId(String[] folderIds) {
        return folderDetailDao.findAllFolderDetailByFolderId(folderIds);
    }

    @Override
    public long countAllAuthorityFolderDetail(String unitId, String userId, Integer businessType) {
        return folderDetailDao.countAllAuthorityFolderDetail(unitId, userId, businessType.toString());
    }

    @Override
    public List<FolderDetail> findAllFolderDetailByBusinessName(String businessName) {
        if (StringUtils.isBlank(businessName)) {
            return Lists.newArrayList();
        }

        return folderDetailDao.findAllFolderDetailByBusinessName("%" + businessName + "%");
    }

    @Override
    public List<FolderDetail> findRecentAuthorityFolderDetail(String unitId, String userId) {
        return folderDetailDao.findRecentAuthorityFolderDetail(unitId, userId);
    }

    @Override
    public List<FolderDetail> findRecentFolderDetail() {
        return folderDetailDao.findRecentFolderDetail();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public List<String> getCharts(String unitId, String userId) {
        return folderDetailDao.getCharts(unitId, userId).map(FolderDetail::getBusinessId).collect(Collectors.toList());
    }
}
