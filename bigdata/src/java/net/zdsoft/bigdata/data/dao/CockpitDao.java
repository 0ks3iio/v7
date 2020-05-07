package net.zdsoft.bigdata.data.dao;

import java.util.List;

import net.zdsoft.bigdata.data.entity.Cockpit;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author ke_shen@126.com
 * @since 2018/4/13 下午1:34
 */
public interface CockpitDao extends BaseJpaRepositoryDao<Cockpit, String> {

    List<Cockpit>  getCockpitsByUnitId(String unitId);

    @Query(
            value = "select * from (" +
                    "(select * from BG_COCKPIT where UNIT_ID = :unitId )" +
                    " union  " +
                    "(select c.* from BG_COCKPIT c, BG_SUBSCRIBE s " +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId )" +
                    ") order by MODIFY_TIME desc ",
            countQuery = "select count(*) from (" +
                    "(select * from BG_COCKPIT where UNIT_ID = :unitId and CHART_TYPE <> 0)" +
                    " union  " +
                    "(select c.* from BG_COCKPIT c, BG_SUBSCRIBE s" +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId)" +
                    ")",
            nativeQuery = true
    )
    List<Cockpit> getCurrentUserEdit(@Param("unitId") String unitId);

    @Query(
            value = "select * from (" +
                    "(select * from BG_COCKPIT where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) " +
                    "union " +
                    "(select * from BG_COCKPIT where ORDER_TYPE = 3 and UNIT_ID = :unitId ) " +
                    "union " +
                    "(select c.* from BG_COCKPIT c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_COCKPIT c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID " +
                    ") " +
                    ") order by MODIFY_TIME desc ",
            countQuery = "select count(*) from (" +
                    "(select * from BG_COCKPIT where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) " +
                    "union " +
                    "(select * from BG_COCKPIT where ORDER_TYPE = 3 and UNIT_ID = :unitId ) " +
                    "union " +
                    "(select c.* from BG_COCKPIT c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_COCKPIT c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID " +
                    ") " +
                    ")",
            nativeQuery = true
    )
    List<Cockpit> getCurrentUserQuery(@Param("userId") String userId, @Param("unitId") String unitId);

    @Modifying
    @Query(
            value = "delete from Cockpit where id in (?1)"
    )
    void batchDelete(String[] ids);
}
