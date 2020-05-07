package net.zdsoft.basedata.dingding.dao;

import java.util.List;

import net.zdsoft.basedata.dingding.entity.DdUnitOpen;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface DingDingUnitOpenDao extends BaseJpaRepositoryDao<DdUnitOpen, String>{

    @Query("from DdUnitOpen where state = ?1")
    List<DdUnitOpen> findByState(Integer state);
    
    @Query("from DdUnitOpen where unit_id = ?1 AND state = ?2")
    List<DdUnitOpen> findByUnitIdAndState(String unitId,Integer state);

}
