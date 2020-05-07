package net.zdsoft.bigdata.dataAnalysis.dao;

import net.zdsoft.bigdata.data.entity.MultiReport;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceptionReportDao extends BaseJpaRepositoryDao<MultiReport, String> {

    @Query("From MultiReport where creatorUserId = ?1 and type = ?2 order by modifyTime desc ")
    public List<MultiReport> findMultiReportsByUserIdAndType(String creatorUserId, Integer type);

    @Query("select max(orderId) from MultiReport where unitId = ?1 and type = ?2")
    public Integer getMaxOrderIdByUnitIdAndType(String unitId, Integer type);

    @Query("select max(orderId) from MultiReport where creatorUserId = ?1 and type = ?2")
    public Integer getMaxOrderIdByUserIdAndType(String creatorUserId, Integer type);

    @Query(
            value = "select * from BG_MULTI_REPORT where CREATOR_USER_ID = :userId and TYPE = :type " +
                    "and NAME like :name order by ORDER_ID",
            nativeQuery = true
    )
    List<MultiReport> getCurrentUserEditMultiReport(@Param("userId") String userId, @Param("type") Integer type, @Param("name") String name);
}
