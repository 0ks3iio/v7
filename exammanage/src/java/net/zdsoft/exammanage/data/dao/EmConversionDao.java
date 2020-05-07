package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmConversion;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmConversionDao extends BaseJpaRepositoryDao<EmConversion, String> {


    @Query("From EmConversion where unitId = ?1 order by scoreRank")
    List<EmConversion> findByUnitId(String unitId);


}
