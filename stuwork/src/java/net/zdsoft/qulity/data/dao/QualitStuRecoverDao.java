package net.zdsoft.qulity.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.qulity.data.entity.QualitStuRecover;

import java.util.List;

public interface QualitStuRecoverDao extends BaseJpaRepositoryDao<QualitStuRecover, String> {

    List<QualitStuRecover> findListByUnitIdAndAcadyear(String unitId, String acadyear);
}
