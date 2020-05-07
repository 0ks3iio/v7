/**
 * FileName: SubscribeServiceImpl.java
 * Author:   shenke
 * Date:     2018/5/18 下午1:16
 * Descriptor:
 */
package net.zdsoft.bigdata.data.service.impl;

import com.google.common.collect.Sets;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.dao.SubscribeDao;
import net.zdsoft.bigdata.data.entity.Subscribe;
import net.zdsoft.bigdata.data.service.SubscribeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author shenke
 * @since 2018/5/18 下午1:16
 */
@Service("subscribeService")
public class SubscribeServiceImpl extends BaseServiceImpl<Subscribe, String> implements SubscribeService {

    @Resource
    private SubscribeDao subscribeDao;

    @Override
    protected BaseJpaRepositoryDao<Subscribe, String> getJpaDao() {
        return subscribeDao;
    }

    @Override
    protected Class<Subscribe> getEntityClass() {
        return Subscribe.class;
    }

    @Override
    public List<Subscribe> getSubscribeByBusinessId(String [] businessId) {
        return subscribeDao.getAllByBusinessIdIn(businessId);
    }

    @Override
    public List<Subscribe> getSubscribeByUser(String unitId, String userId) {
        return subscribeDao.getByUnitIdOrUserId(unitId, userId);
    }

    @Override
    public void deleteByBusinessId(String[] businessIds) {
        subscribeDao.deleteAllByBusinessIdIn(businessIds);
    }

    @Override
    public List<Subscribe> getSubscribeUnits(String businessId) {
        return subscribeDao.getSubscribeUnits(businessId);
    }

    @Override
    public List<Subscribe> getSubscribeUsers(String businessId) {
        return subscribeDao.getSubscribeUsers(businessId);
    }

    @Override
    public List<Subscribe> getSubscribeUsers(String businessId, Integer orderType) {
        return subscribeDao.getSubscribeUsersByOrderType(businessId, orderType);
    }

    @Override
    public List<Subscribe> getSubscribeUsers(String businessId, String unitId) {
        return subscribeDao.getSubscribeUsers(businessId, unitId);
    }

    @Override
    public void deleteByIds(String[] ids) {
        subscribeDao.deleteAllByIdIn(ids);
    }

    @Override
    public void deleteOrderUserByUnitId(String unitId, String businessId) {
        subscribeDao.deleteOrderUserByUnitId(unitId, businessId);
    }

    @Override
    public List<Subscribe> getSubscribeByBusinessIdAndUnitId(String chartId, String currentUnitId) {
        return subscribeDao.getSubscribeByBusinessIdAndAndUnitIdAndUserIdIsNull(chartId, currentUnitId);
    }

    @Override
    public void addAuthorization(String[] dataIds, String[] orderUnits, String[] orderUsers, String currentUnitId, int orderType) {
        List<Subscribe> subscribes = new ArrayList<>();
        //删除原订阅关系
        List<Subscribe> older = this.getSubscribeByBusinessId(dataIds);
        List<String> orderUnitList = Arrays.asList(orderUnits != null ? orderUnits : new String[]{});
        // 删除的订阅关系
        Set<String> deleteSet = Sets.newHashSet();
        older.forEach(e -> {
            if (!orderUnitList.contains(e.getUnitId())) {
                deleteSet.add(e.getId());
            }
        });
        this.deleteByIds(deleteSet.toArray(new String[deleteSet.size()]));

        // 新增的订阅关系
        Set<String> newOrderUnitSet = Sets.newHashSet();
        orderUnitList.forEach(e -> {
            if (!older.contains(e)) {
                newOrderUnitSet.add(e);
            }
        });
        orderUnits = newOrderUnitSet.toArray(new String[newOrderUnitSet.size()]);

        //删除当前单位订阅的用户关系
        for (String businessId : dataIds) {
            this.deleteOrderUserByUnitId(currentUnitId, businessId);
        }

        if (OrderType.UNIT_ORDER.getOrderType() > orderType) {
            return;
        }
        if (orderType >= OrderType.UNIT_ORDER.getOrderType()
                && orderUnits != null && orderType != OrderType.USER_AUTHORIZATION.getOrderType()) {
            for (String chartId : dataIds) {
                for (String unitId : orderUnits) {
                    List<Subscribe> subscribeList = this.getSubscribeByBusinessIdAndUnitId(chartId, unitId);
                    if (subscribeList.size() > 0) {
                        continue;
                    }
                    Subscribe subscribe = new Subscribe();
                    subscribe.setOrderType(orderType);
                    subscribe.setId(UuidUtils.generateUuid());
                    subscribe.setBusinessId(chartId);
                    subscribe.setUnitId(unitId);
                    subscribe.setCreationTime(new Date());
                    subscribes.add(subscribe);
                }
                List<Subscribe> subscribeList = this.getSubscribeByBusinessIdAndUnitId(chartId, currentUnitId);
                if (subscribeList.size() == 0 && orderUsers != null && orderUsers.length != 0) {
                    Subscribe subscribe = new Subscribe();
                    subscribe.setOrderType(orderType);
                    subscribe.setId(UuidUtils.generateUuid());
                    subscribe.setBusinessId(chartId);
                    subscribe.setUnitId(currentUnitId);
                    subscribe.setCreationTime(new Date());
                    subscribes.add(subscribe);
                }
            }
        }
        if ((OrderType.UNIT_ORDER_USER_AUTHORIZATION.getOrderType() == orderType || OrderType.USER_AUTHORIZATION.getOrderType()== orderType)
                && orderUsers != null) {
            for (String chartId : dataIds) {
                for (String userId : orderUsers) {
                    Subscribe subscribe = new Subscribe();
                    subscribe.setOrderType(orderType);
                    subscribe.setId(UuidUtils.generateUuid());
                    subscribe.setBusinessId(chartId);
                    subscribe.setUserId(userId);
                    subscribe.setCreationTime(new Date());
                    subscribe.setUnitId(currentUnitId);
                    subscribes.add(subscribe);
                }
            }
        }

        if (!subscribes.isEmpty()) {
            this.saveAll(subscribes.toArray(new Subscribe[0]));
        }
    }
}
