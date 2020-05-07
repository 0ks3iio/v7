package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmNotLimit;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmNotLimitDao extends BaseJpaRepositoryDao<EmNotLimit, String> {
    @Query("select teacherId from EmNotLimit where unitId = ?1")
    List<String> findByUnitId(String unitId);

    void deleteByUnitId(String unitId);
}
