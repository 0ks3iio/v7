package net.zdsoft.newgkelective.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkDivideExDao;
import net.zdsoft.newgkelective.data.entity.NewGkDivideEx;
import net.zdsoft.newgkelective.data.service.NewGkDivideExService;

import org.apache.commons.lang3.ArrayUtils;
import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("newGkDivideExService")
public class NewGkDivideExServiceImpl extends BaseServiceImpl<NewGkDivideEx, String> 
	implements NewGkDivideExService{
	@Autowired
	private NewGkDivideExDao newGkDivideExDao;
	@Override
	protected BaseJpaRepositoryDao<NewGkDivideEx, String> getJpaDao() {
		return newGkDivideExDao;
	}

	@Override
	protected Class<NewGkDivideEx> getEntityClass() {
		return NewGkDivideEx.class;
	}

	@Override
	public List<NewGkDivideEx> findByDivideId(String divideId) {
		return newGkDivideExDao.findByDivideId(divideId);
	}
	
	public List<NewGkDivideEx> findByDivideIdAndGroupType(String divideId, String groupType){
		if(StringUtils.isEmpty(groupType)) {
			return findByDivideId(divideId);
		}
		return newGkDivideExDao.findByDivideIdAndGroupType(divideId, groupType);
	}

	@Override
	public void saveAndDel(String divideId, NewGkDivideEx[] newGkDivideExs) {
		deleteByDivideId(divideId);
		if(ArrayUtils.isNotEmpty(newGkDivideExs)){
			saveAll(newGkDivideExs);
		}
	}

	@Override
	public void deleteByDivideId(String divideId) {
		newGkDivideExDao.deleteByDivideId(divideId);
	}

}
