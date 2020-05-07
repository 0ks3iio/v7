package net.zdsoft.openapi.savedata.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.openapi.savedata.dao.RelationBaseDao;
import net.zdsoft.openapi.savedata.entity.RelationBase;
import net.zdsoft.openapi.savedata.service.RelationBaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangsj  2018年8月2日上午10:57:24
 */
@Service("relationBaseService")
public class RelationBaseServiceImpl extends BaseServiceImpl<RelationBase, String> implements RelationBaseService {

	@Autowired
	private RelationBaseDao relationBaseDao;
	
	@Override
	protected BaseJpaRepositoryDao<RelationBase, String> getJpaDao() {
		return relationBaseDao;
	}

	@Override
	protected Class<RelationBase> getEntityClass() {
		return RelationBase.class;
	}

	@Override
	public List<RelationBase> findByAreaAndTypeAndRelationIdIn(String area, String type,
			String... relationIds) {
		return relationBaseDao.findByAreaAndTypeAndRelationIdIn(area,type,relationIds);
	}


}
