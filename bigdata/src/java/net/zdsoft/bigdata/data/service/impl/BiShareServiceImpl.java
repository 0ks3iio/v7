package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.BiShareDao;
import net.zdsoft.bigdata.data.entity.BiShare;
import net.zdsoft.bigdata.data.service.BiShareService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("biShareService")
public class BiShareServiceImpl extends BaseServiceImpl<BiShare, String>
        implements BiShareService {

    @Resource
    private BiShareDao biShareDao;

    @Resource
    private UserRemoteService userRemoteService;

    @Override
    protected BaseJpaRepositoryDao<BiShare, String> getJpaDao() {
        return biShareDao;
    }

    @Override
    protected Class<BiShare> getEntityClass() {
        return BiShare.class;
    }

    @Override
    public List<BiShare> findBiFavoriteListByShareUserId(String shareUserId,
                                                         Pagination pagination) {
        List<BiShare> shareList = new ArrayList<BiShare>();
        if (pagination == null)
            shareList = biShareDao.findBiFavoriteListByShareUserId(shareUserId);
        else {
            Integer count = biShareDao.findBiFavoriteListByShareUserId(shareUserId).size();
            pagination.setMaxRowCount(count == null ? 0 : count.intValue());
            shareList = biShareDao.findBiFavoriteListByShareUserId(shareUserId, pagination.toPageable());
        }
        List<BiShare> resultList = new ArrayList<BiShare>();
        Map<String, List<BiShare>> shareMap = new HashMap<String, List<BiShare>>();
        Set<String> userIds = new HashSet<String>();
        for (BiShare share : shareList) {
            userIds.add(share.getBeSharedUserId());
            List<BiShare> tempList = shareMap.get(share.getBusinessId());
            if (CollectionUtils.isEmpty(tempList)) {
                tempList = new ArrayList<BiShare>();
                resultList.add(share);
            }
            tempList.add(share);
            shareMap.put(share.getBusinessId(), tempList);
        }
        List<User> userList = User.dt(userRemoteService.findListByIds(userIds
                .toArray(new String[0])));
        Map<String, User> userMap = new HashMap<String, User>();
        for (User user : userList) {
            userMap.put(user.getId(), user);
        }
        for (BiShare share : resultList) {
            List<BiShare> tempList = shareMap.get(share.getBusinessId());
            StringBuffer userNamesBuffer = new StringBuffer();
            for (BiShare detail : tempList) {
                if (userMap.containsKey(detail.getBeSharedUserId())) {
                    userNamesBuffer.append(userMap.get(
                            detail.getBeSharedUserId()).getRealName());
                    userNamesBuffer.append(",");
                }
            }
            if (userNamesBuffer.length() > 0)
                share.setUserNames(userNamesBuffer.toString().substring(0,
                        userNamesBuffer.toString().length() - 1));
        }
        return resultList;
    }

    @Override
    public List<BiShare> findBiFavoriteListByBeShareUserId(
            String beSharedUserId, Pagination pagination) {
        List<BiShare> shareList = new ArrayList<BiShare>();
        if (pagination == null)
            shareList = biShareDao
                    .findBiFavoriteListByBeShareUserId(beSharedUserId);
        else {
            Integer count = biShareDao.findBiFavoriteListByBeShareUserId(beSharedUserId).size();
            pagination.setMaxRowCount(count == null ? 0 : count.intValue());
            shareList = biShareDao.findBiFavoriteListByBeShareUserId(beSharedUserId, pagination.toPageable());
        }
        Set<String> userIds = new HashSet<String>();
        for (BiShare share : shareList) {
            userIds.add(share.getShareUserId());
        }
        List<User> userList = User.dt(userRemoteService.findListByIds(userIds
                .toArray(new String[0])));
        Map<String, User> userMap = new HashMap<String, User>();
        for (User user : userList) {
            userMap.put(user.getId(), user);
        }
        for (BiShare share : shareList) {
            if (userMap.containsKey(share.getShareUserId())) {
                share.setUserNames(userMap.get(share.getShareUserId())
                        .getRealName());
            }
        }
        return shareList;
    }

    @Override
    public void deleteBiShareByShardUserIdAndBusinessId(String userId,
                                                        String businessId) {
        biShareDao.deleteBiShareByShardUserIdAndBusinessId(userId, businessId);
    }

    @Override
    public void addBiShares(List<BiShare> bishareList) {
        for (BiShare bishare : bishareList) {
            bishare.setId(UuidUtils.generateUuid());
            bishare.setCreationTime(new Date());
            save(bishare);
        }
    }

    @Override
    public void addBiShares(String businessId, String businessType,
                            String businessName, String userId, String[] beSharedUserIdArray) {
        deleteBiShareByShardUserIdAndBusinessId(userId, businessId);
        List<BiShare> shareList = new ArrayList<BiShare>();
        for (String _beSharedUserId : beSharedUserIdArray) {
            BiShare bishare = new BiShare();
            bishare.setId(UuidUtils.generateUuid());
            bishare.setBusinessId(businessId);
            bishare.setBusinessType(businessType);
            bishare.setBusinessName(businessName);
            bishare.setShareUserId(userId);
            bishare.setBeSharedUserId(_beSharedUserId);
            bishare.setCreationTime(new Date());
            shareList.add(bishare);
        }
        saveAll(shareList.toArray(new BiShare[0]));
    }

    @Override
    public Integer findMyShareByUserId(String userId) {
        return biShareDao.countMyShareByUserId(userId);
    }

    @Override
    public Integer findBeShareByUserId(String userId) {
        return biShareDao.countBeSharedByUserId(userId);
    }

}
