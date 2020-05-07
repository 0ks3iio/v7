package net.zdsoft.basedata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.TeachBuildingDao;
import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.service.TeachBuildingService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("teachBuildingService")
public class TeachBuildingServiceImpl extends BaseServiceImpl<TeachBuilding, String> implements TeachBuildingService{

	@Autowired
	private TeachBuildingDao teachBuildingDao;
	@Override
	public List<TeachBuilding> findByUnitId(String unitId) {
		return teachBuildingDao.findByUnitIdAndIsDeleted(unitId, 0);
	}

	@Override
	protected BaseJpaRepositoryDao<TeachBuilding, String> getJpaDao() {
		return teachBuildingDao;
	}

	@Override
	protected Class<TeachBuilding> getEntityClass() {
		return TeachBuilding.class;
	}

	@Override
	public Map<String, String> findTeachBuildMap(String[] ids) {
		List<Object[]> list = null;
		if(ids != null && ids.length>0) {
			list = teachBuildingDao.findPartTeaBuiByIds(ids);
		}
		Map<String, String> map =new HashMap<>();
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] strs :list){
				map.put((String)strs[0], (String)strs[1]);
			}
		}
		return map;
	}

	@Override
	public List<TeachBuilding> findByUnitIdIn(String[] uidList) {
		return teachBuildingDao.findByUnitIdIn(uidList);
	}

}
