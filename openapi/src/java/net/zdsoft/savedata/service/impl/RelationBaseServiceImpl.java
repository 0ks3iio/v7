package net.zdsoft.savedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.savedata.dao.RelationBaseDao;
import net.zdsoft.savedata.entity.RelationBase;
import net.zdsoft.savedata.service.RelationBaseService;

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
