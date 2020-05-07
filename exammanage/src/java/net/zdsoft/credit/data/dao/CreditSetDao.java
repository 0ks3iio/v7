package net.zdsoft.credit.data.dao;

import net.zdsoft.credit.data.entity.CreditSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface CreditSetDao extends BaseJpaRepositoryDao<CreditSet, String> {

    CreditSet findByUnitIdAndAcadyearAndSemester(String unitId, String acadyear, String semester);


}
