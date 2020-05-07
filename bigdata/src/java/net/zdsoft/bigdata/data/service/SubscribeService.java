/**
 * FileName: SubscribeService.java
 * Author:   shenke
 * Date:     2018/5/18 下午1:15
 * Descriptor:
 */
package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.Subscribe;

import java.util.List;

/**
 * @author shenke
 * @since 2018/5/18 下午1:15
 */
public interface SubscribeService extends BaseService<Subscribe, String> {

    List<Subscribe> getSubscribeByBusinessId(String [] businessId);

    @Deprecated
    /** 根据用户信息获取所有的订阅关系 */
    List<Subscribe> getSubscribeByUser(String unitId, String userId);

    /** 根据图表Id删除订阅关系 */
    void deleteByBusinessId(String[] businessIds);


    List<Subscribe> getSubscribeUnits(String businessId);

    List<Subscribe> getSubscribeUsers(String businessId);

    List<Subscribe> getSubscribeUsers(String businessId, Integer orderType);

    /**
     * 获取某一个单位指定图表的订阅用户
     * 仅对{@link net.zdsoft.bigdata.data.OrderType#UNIT_ORDER_USER_AUTHORIZATION} 有效
     * @param businessId chartID
     * @param unitId 单位ID
     * @return
     */
    List<Subscribe> getSubscribeUsers(String businessId, String unitId);

    void deleteByIds(String[] ids);

    void deleteOrderUserByUnitId(String unitId, String businessId);

    List<Subscribe> getSubscribeByBusinessIdAndUnitId(String chartId, String currentUnitId);

    /**
     * 批量授权
     * @param dataIds
     * @param orderUnits
     * @param orderUsers
     * @param currentUnitId
     * @param orderType
     */
    void addAuthorization(String[] dataIds , String[] orderUnits, String[] orderUsers, String currentUnitId, int orderType);
}
