package net.zdsoft.bigdata.data.dao;

import java.util.List;

import net.zdsoft.bigdata.data.code.ChartType;
import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author ke_shen@126.com
 * @since 2018/4/17 下午3:48
 */
public interface ChartDao extends BaseJpaRepositoryDao<Chart, String> {

    /**
     * 计算使用特定Api的chart数量
     */
    long countByApiId(String apiId);

    /**
     * 计算使用特定database的chart的数量
     */
    long countByDatabaseId(String databaseId);

    /**
     * 根据单位Id查询所有的chart
     */
    List<Chart> getChartsByUnitId(String unitId);

    /**
     * 根据单位Id查询所有的chart
     */
    List<Chart> getChartsByUnitIdAndChartType(String unitId, Integer chartType);

    Chart getChartByIdAndBusinessType(String id, int businessType);

    /**
     * 根据id和指定的businessType获取数据
     */
    List<Chart> getChartsByIdInAndBusinessType(String[] ids, int businessType);

    /**
     * 获取某单位下指定businessType的图表
     * 可能是基本图表也可能是大屏使用的图表
     *
     * @see net.zdsoft.bigdata.data.ChartBusinessType
     */
    List<Chart> getChartsByUnitIdAndBusinessType(String unitId, int businessType, Sort sort);

    Page<Chart> getChartsByUnitIdAndBusinessTypeOrderByModifyTime(String unitId, int businessType, Pageable pageable);

    Page<Chart> getChartsByIdInOrderByModifyTime(String[] ids, Pageable pageable);

    Page<Chart> getAllByIdIn(String[] ids, Pageable pageable);

    @Query(
            value = "select * from (" +
                    "(select * from BG_CHART where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and CHART_TYPE <> 0) "+
                    "union " +
                    "(select * from BG_CHART where ORDER_TYPE = 3 and UNIT_ID = :unitId and CHART_TYPE <> 0) " +
                    "union " +
                    "(select c.* from BG_CHART c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    ") where name like :name and nvl(is_for_cockpit, 0) <> 1 and business_type<>4 order by MODIFY_TIME desc ",
            countQuery = "select count(*) from (" +
                    "(select * from BG_CHART where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and CHART_TYPE <> 0) "+
                    "union " +
                    "(select * from BG_CHART where ORDER_TYPE = 3 and UNIT_ID = :unitId and CHART_TYPE <> 0) " +
                    "union " +
                    "(select c.* from BG_CHART c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    ") where name like :name and nvl(is_for_cockpit, 0) <> 1 and business_type<>4",
            nativeQuery = true
    )
    Page<Chart> getCurrentUserQueryGraphCharts(@Param("userId") String userId, @Param("unitId") String unitId, @Param("name") String name, Pageable pageable);

    @Query(
            value = "select * from (select distinct ct.* from (" +
                    "(select * from BG_CHART where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and CHART_TYPE <> 0) "+
                    "union all " +
                    "(select * from BG_CHART where ORDER_TYPE = 3 and UNIT_ID = :unitId and CHART_TYPE <> 0 ) " +
                    "union all " +
                    "(select c.* from BG_CHART c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union all " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union all " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    ") ct " +
                    "join BG_TAG_RELAITON tr on tr.BUSINESS_ID=ct.ID and tr.TAG_ID in (:tagIds) order by MODIFY_TIME desc )  where name like :name and nvl(is_for_cockpit, 0) <> 1 and business_type<>4 and business_type<>4",
            countQuery = "select count(*) from (select distinct ct.* from (" +
                    "(select * from BG_CHART where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and CHART_TYPE <> 0) "+
                    "union all " +
                    "(select * from BG_CHART where ORDER_TYPE = 3 and UNIT_ID = :unitId and CHART_TYPE <> 0) " +
                    "union all " +
                    "(select c.* from BG_CHART c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union all " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union all " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    ") ct " +
                    "join BG_TAG_RELAITON tr on tr.BUSINESS_ID=ct.ID and tr.TAG_ID in (:tagIds))  where name like :name and nvl(is_for_cockpit, 0) <> 1 and business_type<>4",
            nativeQuery = true
    )
    Page<Chart> getCurrentUserQueryGraphCharts(@Param("userId") String userId,
                                               @Param("unitId") String unitId,
                                               @Param("tagIds") String[] tagIds,
                                               @Param("name") String name,
                                               Pageable pageable);

    @Query(
            value = "select * from (" +
                    "(select * from BG_CHART where UNIT_ID = :unitId and CHART_TYPE <> 0 )" +
                    " union  " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s " +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and CHART_TYPE <> 0 )" +
                    ") where name like :name and business_type<>4 and nvl(is_for_cockpit, 0) = :forCockpit order by MODIFY_TIME desc",
            countQuery = "select count(*) from (" +
                    "(select * from BG_CHART where UNIT_ID = :unitId and CHART_TYPE <> 0)" +
                    " union  " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s" +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and CHART_TYPE <> 0)" +
                    ") where name like :name and business_type<>4 and nvl(is_for_cockpit, 0) = :forCockpit",
            nativeQuery = true
    )
    Page<Chart> getCurrentUserEditGraphCharts(@Param("unitId") String unitId,
                                              @Param("name") String name,
                                              @Param("forCockpit") int forCockpit,
                                              Pageable pageable);
    @Query(
            value = "select * from (" +
                    "(select * from BG_CHART where UNIT_ID = :unitId and CHART_TYPE <> 0 )" +
                    " union  " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s " +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and CHART_TYPE <> 0 )" +
                    ") where name like :name and business_type<>4 order by MODIFY_TIME desc",
            countQuery = "select count(*) from (" +
                    "(select * from BG_CHART where UNIT_ID = :unitId and CHART_TYPE <> 0)" +
                    " union  " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s" +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and CHART_TYPE <> 0)" +
                    ") where name like :name and business_type<>4",
            nativeQuery = true
    )
    Page<Chart> getCurrentUserEditGraphCharts(@Param("unitId") String unitId,
                                              @Param("name") String name,
                                              Pageable pageable);


    @Query(
            value = "select distinct * from (select ct.* from (" +
                    "(select * from BG_CHART where UNIT_ID = :unitId and ORDER_TYPE <> 5 and CHART_TYPE <> 0)" +
                    " union  " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s " +
                    "where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and CHART_TYPE <> 0)" +
                    ") ct inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID = ct.ID and tr.TAG_ID in (:tagIds) order by MODIFY_TIME desc )" +
                    " where name like :name and business_type<>4 and nvl(is_for_cockpit, 0) = :forCockpit",
            countQuery = "select count(*) from (select distinct ct.* from (" +
                    "(select * from BG_CHART where UNIT_ID = :unitId and ORDER_TYPE <> 5 and CHART_TYPE <> 0)" +
                    " union  " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s " +
                    "where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and CHART_TYPE <> 0)" +
                    ") ct inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID = ct.ID and tr.TAG_ID in (:tagIds)) " +
                    "where name like :name and business_type<>4 and nvl(is_for_cockpit, 0) = :forCockpit",
            nativeQuery = true
    )
    Page<Chart> getCurrentUserEditGraphCharts(@Param("unitId") String unitId,
                                              @Param("tagIds") String[] tagIds,
                                              @Param("name") String name,
                                              @Param("forCockpit") int forCockpit,
                                              Pageable pageable);
    @Query(
            value = "select distinct * from (select ct.* from (" +
                    "(select * from BG_CHART where UNIT_ID = :unitId and ORDER_TYPE <> 5 and CHART_TYPE <> 0)" +
                    " union  " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s " +
                    "where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and CHART_TYPE <> 0)" +
                    ") ct inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID = ct.ID and tr.TAG_ID in (:tagIds) order by MODIFY_TIME desc )" +
                    " where name like :name and business_type<>4",
            countQuery = "select count(*) from (select distinct ct.* from (" +
                    "(select * from BG_CHART where UNIT_ID = :unitId and ORDER_TYPE <> 5 and CHART_TYPE <> 0)" +
                    " union  " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s " +
                    "where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and CHART_TYPE <> 0)" +
                    ") ct inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID = ct.ID and tr.TAG_ID in (:tagIds)) " +
                    "where name like :name and business_type<>4 ",
            nativeQuery = true
    )
    Page<Chart> getCurrentUserEditGraphCharts(@Param("unitId") String unitId,
                                              @Param("tagIds") String[] tagIds,
                                              @Param("name") String name,
                                              Pageable pageable);


    @Query(
            value = "select * from (" +
                    "(select * from BG_CHART where UNIT_ID = :unitId  and CHART_TYPE = 0)" +
                    " union  " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s " +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and CHART_TYPE = 0)" +
                    ") where name like :name order by MODIFY_TIME desc",
            countQuery = "select count(*) from (" +
                    "(select * from BG_CHART where UNIT_ID = :unitId and CHART_TYPE = 0 )" +
                    " union  " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s" +
                    " where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and CHART_TYPE = 0)" +
                    ") where name like :name",
            nativeQuery = true
    )
    Page<Chart> getCurrentUserEditReportCharts(@Param("unitId") String unitId, @Param("name") String name,
                                               Pageable pageable);
    @Query(
            value = "select distinct * from (select ct.* from (" +
                    "(select * from BG_CHART where UNIT_ID = :unitId  and CHART_TYPE = 0)" +
                    " union  " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s " +
                    "where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and CHART_TYPE = 0)" +
                    ") ct inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID = ct.ID and tr.TAG_ID in (:tagIds) order by MODIFY_TIME desc ) where name like :name",
            countQuery = "select count(*) from (select distinct ct.* from (" +
                    "(select * from BG_CHART where UNIT_ID = :unitId and CHART_TYPE = 0)" +
                    " union  " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s " +
                    "where c.ID = s.BUSINESS_ID and c.ORDER_TYPE=5 and s.UNIT_ID = :unitId and CHART_TYPE = 0)" +
                    ") ct inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID = ct.ID and tr.TAG_ID in (:tagIds)) where name like :name",
            nativeQuery = true
    )
    Page<Chart> getCurrentUserEditReportCharts(@Param("unitId") String unitId,
                                               @Param("tagIds") String[] tagIds,
                                               @Param("name") String name,
                                               Pageable pageable);
    @Query(
            value = "select * from (select distinct ct.* from (" +
                    "(select * from BG_CHART where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and CHART_TYPE = 0) "+
                    "union all " +
                    "(select * from BG_CHART where ORDER_TYPE = 3 and UNIT_ID = :unitId and CHART_TYPE = 0 ) " +
                    "union all " +
                    "(select c.* from BG_CHART c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and CHART_TYPE = 0" +
                    ") " +
                    "union all " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE = 0" +
                    ") " +
                    "union all " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE = 0" +
                    ") " +
                    ") ct " +
                    "inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID=ct.ID and tr.TAG_ID in (:tagIds) order by MODIFY_TIME desc ) where name like :name",
            countQuery = "select count(*) from (select distinct ct.* from (" +
                    "(select * from BG_CHART where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and CHART_TYPE = 0) "+
                    "union all " +
                    "(select * from BG_CHART where ORDER_TYPE = 3 and UNIT_ID = :unitId and CHART_TYPE = 0) " +
                    "union all " +
                    "(select c.* from BG_CHART c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and CHART_TYPE = 0" +
                    ") " +
                    "union all " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE = 0" +
                    ") " +
                    "union all " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE = 0" +
                    ") " +
                    ") ct " +
                    " inner join BG_TAG_RELAITON tr on tr.BUSINESS_ID=ct.ID and tr.TAG_ID in (:tagIds)) where name like :name",
            nativeQuery = true
    )
    Page<Chart> getCurrentUserQueryReportChart(@Param("userId") String userId,
                                               @Param("unitId") String unitId,
                                               @Param("tagIds") String[] tagIds,
                                               @Param("name") String name,
                                               Pageable pageable
                                               );
    @Query(
            value = "select * from (" +
                    "(select * from BG_CHART where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and CHART_TYPE = 0) "+
                    "union " +
                    "(select * from BG_CHART where ORDER_TYPE = 3 and UNIT_ID = :unitId and CHART_TYPE = 0) " +
                    "union " +
                    "(select c.* from BG_CHART c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and CHART_TYPE = 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE = 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE = 0" +
                    ") " +
                    ") where name like :name order by MODIFY_TIME desc",
            countQuery = "select count(*) from (" +
                    "(select * from BG_CHART where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and CHART_TYPE = 0) "+
                    "union " +
                    "(select * from BG_CHART where ORDER_TYPE = 3 and UNIT_ID = :unitId and CHART_TYPE = 0) " +
                    "union " +
                    "(select c.* from BG_CHART c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and CHART_TYPE = 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE = 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE = 0" +
                    ") " +
                    ") where name like :name",
            nativeQuery = true
    )
    Page<Chart> getCurrentUserQueryReportChart(@Param("userId") String userId,
                                               @Param("unitId") String unitId,
                                               @Param("name") String name,
                                               Pageable pageable);

    @Modifying
    @Query(
            value = "delete from Chart where id in (?1)"
    )
    void batchDelete(String[] ids);

    @Query(
            value = "select * from (" +
                    "(select * from BG_CHART where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and CHART_TYPE <> 0) "+
                    "union " +
                    "(select * from BG_CHART where ORDER_TYPE = 3 and UNIT_ID = :unitId and CHART_TYPE <> 0) " +
                    "union " +
                    "(select c.* from BG_CHART c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    ") where name like :name and nvl(is_for_cockpit, 0) = 1 and business_type<>4 order by MODIFY_TIME desc ",
            countQuery = "select count(*) from (" +
                    "(select * from BG_CHART where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and CHART_TYPE <> 0) "+
                    "union " +
                    "(select * from BG_CHART where ORDER_TYPE = 3 and UNIT_ID = :unitId and CHART_TYPE <> 0) " +
                    "union " +
                    "(select c.* from BG_CHART c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    ") where name like :name and nvl(is_for_cockpit, 0) = 1 and business_type<>4",
            nativeQuery = true
    )
    Page<Chart> getCurrentUserQueryGraphChartsForCockpit(@Param("userId") String userId, @Param("unitId") String unitId, @Param("name") String name, Pageable pageable);

    @Query(
            value = "select * from (" +
                    "(select * from BG_CHART where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and CHART_TYPE <> 0) "+
                    "union " +
                    "(select * from BG_CHART where ORDER_TYPE = 3 and UNIT_ID = :unitId and CHART_TYPE <> 0) " +
                    "union " +
                    "(select c.* from BG_CHART c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    ") where name like :name and nvl(is_for_cockpit, 0) = 1 and CHART_TYPE in (:tagIds) and business_type<>4 order by MODIFY_TIME desc ",
            countQuery = "select count(*) from (" +
                    "(select * from BG_CHART where ORDER_TYPE = 2 and UNIT_ID in (select id from BASE_UNIT start with id = :unitId connect by id = PRIOR parent_id) and CHART_TYPE <> 0) "+
                    "union " +
                    "(select * from BG_CHART where ORDER_TYPE = 3 and UNIT_ID = :unitId and CHART_TYPE <> 0) " +
                    "union " +
                    "(select c.* from BG_CHART c , BG_SUBSCRIBE s where c.ORDER_TYPE = 4 " +
                    " and s.UNIT_ID = :unitId and s.USER_ID is null and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 5" +
                    " and s.UNIT_ID = :unitId and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    "union " +
                    "(select c.* from BG_CHART c, BG_SUBSCRIBE s where c.ORDER_TYPE = 6" +
                    " and s.USER_ID=:userId and s.BUSINESS_ID = c.ID and CHART_TYPE <> 0" +
                    ") " +
                    ") where name like :name and nvl(is_for_cockpit, 0) = 1 and CHART_TYPE in (:tagIds) and business_type<>4",
            nativeQuery = true
    )
    Page<Chart> getCurrentUserQueryGraphChartsForCockpit(@Param("userId") String userId,
                                               @Param("unitId") String unitId,
                                               @Param("tagIds") String[] tagIds,
                                               @Param("name") String name,
                                               Pageable pageable);

    @Query(
            value = "from Chart where id in (?1) and chartType= " + ChartType.TEXT_TITLE
    )
    List<Chart> getTextChartsByIds(String[] ids);

    @Query(
            value = "from Chart where id in (?1) and chartType in (?2)"
    )
    List<Chart> getChartsByIdsAndChartTypes(String[] ids, Integer[] chartTypes);
}
