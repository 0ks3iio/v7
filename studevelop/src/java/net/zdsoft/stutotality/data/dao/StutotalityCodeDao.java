package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityCode;

import java.util.List;

public interface StutotalityCodeDao extends BaseJpaRepositoryDao<StutotalityCode, String> {

    List<StutotalityCode> findListByUnitIdAndTypeAndNameIn(String unitId, Integer type, String[] names);
}
