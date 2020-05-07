package net.zdsoft.framework.pagedata.dao;

import java.util.List;

import net.zdsoft.framework.pagedata.entity.FwDataDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FwDataDetailJpaDao extends JpaRepository<FwDataDetail, String> {

    @Query("from FwDataDetail where groupCode = ?1 order by isHiden, displayOrder")
    List<FwDataDetail> findByGroupCodeOrderBy(String groupCode);
    
    @Query("from FwDataDetail where groupCode = ?1 and isHiden = 0 order by displayOrder")
    List<FwDataDetail> findByGroupCodeUnHidenOrderBy(String groupCode);
}
