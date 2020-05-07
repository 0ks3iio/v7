package net.zdsoft.eclasscard.data.dao;

import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccAttenceNoticeSetDao extends BaseJpaRepositoryDao<EccAttenceNoticeSet, String> {

	
	EccAttenceNoticeSet findByUnitIdAndType(String unitId, String type);

}
