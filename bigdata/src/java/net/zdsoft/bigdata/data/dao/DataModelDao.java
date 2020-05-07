package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.DataModel;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataModelDao extends BaseJpaRepositoryDao<DataModel, String> {

    @Query(
            value = "select * from (" +
                    "(select * from BG_MODEL where UNIT_ID = :unitId)" +
                    " union  " +
                    "(select c.* from BG_MODEL c, BG_SUBSCRIBE s " +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId" +
                    ")) order by ORDER_ID",
            nativeQuery = true
    )
    List<DataModel> findAllByUnitIdOrderByOrderId(@Param("unitId")String unitId);

    @Query(
            value = "select * from (" +
                    "(select * from BG_MODEL where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) "+
                    "union " +
                    "(select * from BG_MODEL where ORDER_TYPE = 3 and UNIT_ID = :unitId) " +
                    "union " +
                    "(select c.* from BG_MODEL c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID" +
                    ") " +
                    "union " +
                    "(select c.* from BG_MODEL c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID" +
                    ")" +
                    "union " +
                    "(select c.* from BG_MODEL c , BG_SUBSCRIBE s where c.ORDER_TYPE = 6 " +
                    " and s.USER_ID = :userId and s.BUSINESS_ID = c.ID" +
                    ") " +
                    ") order by ORDER_ID",
            nativeQuery = true
    )
    List<DataModel> getCurrentUserDataModel(@Param("userId") String userId,
                                               @Param("unitId") String unitId);
}
