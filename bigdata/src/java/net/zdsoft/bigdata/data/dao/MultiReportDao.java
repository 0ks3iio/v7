package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.entity.MultiReport;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MultiReportDao extends BaseJpaRepositoryDao<MultiReport, String> {

    @Query("From MultiReport where unitId = ?1 and type = ?2 order by modifyTime desc ")
    public List<MultiReport> findMultiReportsByUnitIdAndType(String unitId, Integer type);

    @Query("From MultiReport where creatorUserId = ?1 and type = ?2 order by modifyTime desc ")
    public List<MultiReport> findMultiReportsByUserIdAndType(String creatorUserId, Integer type);

    @Query("select max(orderId) from MultiReport where unitId = ?1 and type = ?2")
    public Integer getMaxOrderIdByUnitIdAndType(String unitId, Integer type);

    @Query("select max(orderId) from MultiReport where creatorUserId = ?1 and type = ?2")
    public Integer getMaxOrderIdByUserIdAndType(String creatorUserId, Integer type);

    @Query(
            value = "select * from (" +
                    "(select * from BG_MULTI_REPORT where UNIT_ID = :unitId and TYPE= :type)" +
                    " union  " +
                    "(select c.* from BG_MULTI_REPORT c, BG_SUBSCRIBE s " +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and c.TYPE= :type and s.UNIT_ID = :unitId" +
                    ")) order by MODIFY_TIME DESC",
            nativeQuery = true
    )
    List<MultiReport> findAllByUnitIdAndType(@Param("unitId") String unitId, @Param("type") Integer type);

    @Query(
            value = "select distinct * from (select ct.* from (" +
                    "(select * from BG_MULTI_REPORT where UNIT_ID = :unitId  and TYPE = :type)" +
                    " union  " +
                    "(select c.* from BG_MULTI_REPORT c, BG_SUBSCRIBE s " +
                    "where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and TYPE = :type)" +
                    ") ct) where name like :name order by MODIFY_TIME DESC",
            nativeQuery = true
    )
    List<MultiReport> getCurrentUserEditMultiReport(@Param("unitId") String unitId, @Param("type") Integer type, @Param("name") String name);

    @Query(
            value = "select distinct * from (select ct.* from (" +
                    "(select * from BG_MULTI_REPORT where UNIT_ID = :unitId  and TYPE = :type)" +
                    " union  " +
                    "(select c.* from BG_MULTI_REPORT c, BG_SUBSCRIBE s " +
                    "where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and TYPE = :type)" +
                    ") ct inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID = ct.ID and tr.TAG_ID in (:tagIds)) where name like :name order by MODIFY_TIME DESC",
            nativeQuery = true
    )
    List<MultiReport> getCurrentUserEditMultiReport(@Param("unitId") String unitId, @Param("tagIds") String[] tagIds, @Param("type") Integer type, @Param("name") String name);


    @Query(
            value = "select * from (" +
                    "(select * from BG_MULTI_REPORT where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and TYPE = :type ) " +
                    "union " +
                    "(select * from BG_MULTI_REPORT where ORDER_TYPE = 3 and UNIT_ID = :unitId and TYPE = :type) " +
                    "union " +
                    "(select c.* from BG_MULTI_REPORT c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and TYPE = :type" +
                    ") " +
                    "union " +
                    "(select c.* from BG_MULTI_REPORT c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and TYPE = :type" +
                    ") " +
                    "union " +
                    "(select c.* from BG_MULTI_REPORT c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and TYPE = :type" +
                    ") " +
                    ") where name like :name order by MODIFY_TIME desc",
            countQuery = "select count(*) from (" +
                    "(select * from BG_MULTI_REPORT where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and TYPE = :type) " +
                    "union " +
                    "(select * from BG_MULTI_REPORT where ORDER_TYPE = 3 and UNIT_ID = :unitId and TYPE = :type) " +
                    "union " +
                    "(select c.* from BG_MULTI_REPORT c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and TYPE = :type" +
                    ") " +
                    "union " +
                    "(select c.* from BG_MULTI_REPORT c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and TYPE = :type" +
                    ") " +
                    "union " +
                    "(select c.* from BG_MULTI_REPORT c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and TYPE = :type" +
                    ") " +
                    ") where name like :name",
            nativeQuery = true
    )
    Page<MultiReport> getCurrentUserQueryReportChart(@Param("unitId") String unitId,
                                                     @Param("userId") String userId,
                                                     @Param("name") String name,
                                                     @Param("type") Integer type,
                                                     Pageable pageable);
}
