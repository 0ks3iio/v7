/**
 * FileName: SubscribeDao.java
 * Author:   shenke
 * Date:     2018/5/18 下午1:16
 * Descriptor:
 */
package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.Subscribe;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author shenke
 * @since 2018/5/18 下午1:16
 */
public interface SubscribeDao extends BaseJpaRepositoryDao<Subscribe, String> {

    List<Subscribe> getAllByBusinessIdIn(String [] businessId);

    List<Subscribe> getByUnitIdOrUserId(String unitId, String userId);

    @Modifying
    void deleteAllByBusinessIdIn(String[] businessIds);

    @Query(
            value = "from Subscribe where orderType>=4 and businessId=?1 and userId is null "
    )
    List<Subscribe> getSubscribeUnits(String businessId);

    @Query(
            value = "from Subscribe where orderType=5 and businessId=?1 and userId is not null "
    )
    List<Subscribe> getSubscribeUsers(String businessId);

    @Query(
            value = "from Subscribe where orderType=5 and businessId=?1 and " +
                    " userId is not null and unitId=?2"
    )
    List<Subscribe> getSubscribeUsers(String businessId, String unitId);

    void deleteAllByIdIn(String[] ids);

    @Query(
            value = "delete from Subscribe where unitId=?1 and businessId=?2 and" +
                    " userId is not null"
    )
    @Modifying
    void deleteOrderUserByUnitId(String unitId, String businessId);

    List<Subscribe> getSubscribeByBusinessIdAndAndUnitIdAndUserIdIsNull(String businessId, String unitId);

    @Query(
            value = "from Subscribe where orderType=?2 and businessId=?1 and userId is not null "
    )
    List<Subscribe> getSubscribeUsersByOrderType(String businessId, Integer orderType);
}
