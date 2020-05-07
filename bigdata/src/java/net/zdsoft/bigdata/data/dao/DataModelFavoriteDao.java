package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.DataModelFavorite;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataModelFavoriteDao extends BaseJpaRepositoryDao<DataModelFavorite, String> {

    @Query(
            value = "select * from (" +
                    "(select * from BG_MODEL_FAVORITE where UNIT_ID = :unitId)" +
                    " union  " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s " +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId)"+
                    ") order by ORDER_ID",
            nativeQuery = true
    )
    List<DataModelFavorite> findAllByUnitIdOrderByOrderId(@Param("unitId")String unitId);

    @Query(
            value = "select * from (" +
                    "(select * from BG_MODEL_FAVORITE where UNIT_ID = :unitId)" +
                    " union  " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s " +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId)" +
                    ") order by ORDER_ID asc",
            countQuery = "select count(*) from (" +
                    "(select * from BG_MODEL_FAVORITE where UNIT_ID = :unitId)" +
                    " union  " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s" +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId)" +
                    ")",
            nativeQuery = true
    )
    Page<DataModelFavorite> getCurrentUserEditReportCharts(@Param("unitId") String unitId, Pageable pageable);

    @Query(
            value = "select * from (" +
                    "(select * from BG_MODEL_FAVORITE where UNIT_ID = :unitId)" +
                    " union  " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s " +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId)" +
                    ") where FAVORITE_NAME like :name order by ORDER_ID asc",
            countQuery = "select count(*) from (" +
                    "(select * from BG_MODEL_FAVORITE where UNIT_ID = :unitId)" +
                    " union  " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s" +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId)" +
                    ") where FAVORITE_NAME like :name",
            nativeQuery = true
    )
    Page<DataModelFavorite> getCurrentUserEditReportCharts(@Param("unitId") String unitId, @Param("name") String name,
                                               Pageable pageable);
    @Query(
            value = "select distinct * from (select ct.* from (" +
                    "(select * from BG_MODEL_FAVORITE where UNIT_ID = :unitId)" +
                    " union  " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s " +
                    "where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId)" +
                    ") ct inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID = ct.ID and tr.TAG_ID in (:tagIds) order by ORDER_ID asc ) where FAVORITE_NAME like :name",
            countQuery = "select count(*) from (select distinct ct.* from (" +
                    "(select * from BG_MODEL_FAVORITE where UNIT_ID = :unitId)" +
                    " union  " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s " +
                    "where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId)" +
                    ") ct inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID = ct.ID and tr.TAG_ID in (:tagIds)) where FAVORITE_NAME like :name",
            nativeQuery = true
    )
    Page<DataModelFavorite> getCurrentUserEditReportCharts(@Param("unitId") String unitId,
                                               @Param("tagIds") String[] tagIds,
                                               @Param("name") String name,
                                               Pageable pageable);

    @Query(
            value = "select * from (select distinct ct.* from (" +
                    "(select * from BG_MODEL_FAVORITE where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) "+
                    "union all " +
                    "(select * from BG_MODEL_FAVORITE where ORDER_TYPE = 3 and UNIT_ID = :unitId) " +
                    "union all " +
                    "(select c.* from BG_MODEL_FAVORITE c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID" +
                    ") " +
                    "union all " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID" +
                    ") " +
                    "union all " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " s.USER_ID=:userId and s.BUSINESS_ID = c.ID" +
                    ") " +
                    ") ct " +
                    "inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID=ct.ID and tr.TAG_ID in (:tagIds) order by ORDER_ID asc ) where FAVORITE_NAME like :name",
            countQuery = "select count(*) from (select distinct ct.* from (" +
                    "(select * from BG_MODEL_FAVORITE where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) "+
                    "union all " +
                    "(select * from BG_MODEL_FAVORITE where ORDER_TYPE = 3 and UNIT_ID = :unitId) " +
                    "union all " +
                    "(select c.* from BG_MODEL_FAVORITE c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID" +
                    ") " +
                    "union all " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID" +
                    ") " +
                    "union all " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID" +
                    ") " +
                    ") ct " +
                    " inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID=ct.ID and tr.TAG_ID in (:tagIds)) where FAVORITE_NAME like :name",
            nativeQuery = true
    )
    Page<DataModelFavorite> getCurrentUserQueryReportChart(@Param("userId") String userId,
                                               @Param("unitId") String unitId,
                                               @Param("tagIds") String[] tagIds,
                                               @Param("name") String name,
                                               Pageable pageable
    );
    @Query(
            value = "select * from (" +
                    "(select * from BG_MODEL_FAVORITE where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) "+
                    "union " +
                    "(select * from BG_MODEL_FAVORITE where ORDER_TYPE = 3 and UNIT_ID = :unitId) " +
                    "union " +
                    "(select c.* from BG_MODEL_FAVORITE c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID " +
                    ") " +
                    "union " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID" +
                    ") " +
                    "union " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID" +
                    ") " +
                    ") where FAVORITE_NAME like :name order by ORDER_ID asc ",
            countQuery = "select count(*) from (" +
                    "(select * from BG_MODEL_FAVORITE where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id)) "+
                    "union " +
                    "(select * from BG_MODEL_FAVORITE where ORDER_TYPE = 3 and UNIT_ID = :unitId) " +
                    "union " +
                    "(select c.* from BG_MODEL_FAVORITE c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID" +
                    ") " +
                    "union " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID" +
                    ") " +
                    "union " +
                    "(select c.* from BG_MODEL_FAVORITE c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID" +
                    ") " +
                    ") where FAVORITE_NAME like :name",
            nativeQuery = true
    )
    Page<DataModelFavorite> getCurrentUserQueryReportChart(@Param("userId") String userId,
                                               @Param("unitId") String unitId,
                                               @Param("name") String name,
                                               Pageable pageable);

    @Query(
            value = "select * from BG_MODEL_FAVORITE a inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID = a.ID " +
                    "and tr.TAG_ID in (:tagIds) and a.user_id= :userId and a.FAVORITE_NAME like :name order by a.order_id asc",
            nativeQuery = true
    )
    List<DataModelFavorite> findAllByUserId(@Param("userId") String userId,
                                            @Param("tagIds") String[] tagIds,
                                            @Param("name") String name);

    @Query(
            value = "select * from BG_MODEL_FAVORITE where user_id= :userId and FAVORITE_NAME like :name order by order_id asc",
            nativeQuery = true
    )
    List<DataModelFavorite> findAllByUserId(@Param("userId") String userId,
                                            @Param("name") String name);
}
