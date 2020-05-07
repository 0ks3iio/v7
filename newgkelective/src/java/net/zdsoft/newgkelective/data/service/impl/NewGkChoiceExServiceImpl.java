package net.zdsoft.newgkelective.data.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkChoiceExDao;
import net.zdsoft.newgkelective.data.entity.NewGkChoiceEx;
import net.zdsoft.newgkelective.data.service.NewGkChoiceExService;

@Service("newGkChoiceExService")
public class NewGkChoiceExServiceImpl extends BaseServiceImpl<NewGkChoiceEx, String> implements NewGkChoiceExService{
	@Autowired
	private NewGkChoiceExDao newGkChoiceExDao;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkChoiceEx, String> getJpaDao() {
		return newGkChoiceExDao;
	}

	@Override
	protected Class<NewGkChoiceEx> getEntityClass() {
		return NewGkChoiceEx.class;
	}

}
