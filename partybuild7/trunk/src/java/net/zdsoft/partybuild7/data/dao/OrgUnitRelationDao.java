package net.zdsoft.partybuild7.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.entity.OrgUnitRelation;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrgUnitRelationDao extends BaseJpaRepositoryDao<OrgUnitRelation , String> {

    @Query(" From OrgUnitRelation where  unitId = ?1 ")
    public List<OrgUnitRelation> getAllByUnitId(String unitId);
}
