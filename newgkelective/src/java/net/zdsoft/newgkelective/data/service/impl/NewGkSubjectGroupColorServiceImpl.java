package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkSubjectGroupColorDao;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectGroupColor;
import net.zdsoft.newgkelective.data.service.NewGkSubjectGroupColorService;


@Service("newGkSubjectGroupColorService")
public class NewGkSubjectGroupColorServiceImpl extends BaseServiceImpl<NewGkSubjectGroupColor, String> 
	implements NewGkSubjectGroupColorService {

	@Autowired
	private NewGkSubjectGroupColorDao newGkSubjectGroupColorDao;
	
	@Override
	public List<NewGkSubjectGroupColor> findByUnitIdGroupType(String unitId, String[] groupTypes) {
		if(StringUtils.isBlank(unitId)) {
			return new ArrayList<>();
		}
		List<NewGkSubjectGroupColor> sgcList = null;
		if(groupTypes != null && groupTypes.length>0) {
			sgcList = newGkSubjectGroupColorDao.findByUnitIdAndGroupTypeIn(unitId, groupTypes);
		}else {
			sgcList = newGkSubjectGroupColorDao.findByUnitId(unitId);
		}
		return sgcList;
	}

	@Override
	protected BaseJpaRepositoryDao<NewGkSubjectGroupColor, String> getJpaDao() {
		return newGkSubjectGroupColorDao;
	}

	@Override
	protected Class<NewGkSubjectGroupColor> getEntityClass() {
		return NewGkSubjectGroupColor.class;
	}
	
}
