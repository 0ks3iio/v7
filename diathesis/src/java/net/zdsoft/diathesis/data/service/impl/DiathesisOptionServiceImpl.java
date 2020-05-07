package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.dao.DiathesisOptionDao;
import net.zdsoft.diathesis.data.entity.DiathesisOption;
import net.zdsoft.diathesis.data.service.DiathesisOptionService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: panlf
 * @Date: 2019/3/29 9:44
 */
@Service("diathesisOptionService")
public class DiathesisOptionServiceImpl extends BaseServiceImpl<DiathesisOption, String>  implements DiathesisOptionService {
	
    @Autowired
    private DiathesisOptionDao diathesisOptionDao;

	@Override
	protected BaseJpaRepositoryDao<DiathesisOption, String> getJpaDao() {
		return diathesisOptionDao;
	}

	@Override
	protected Class<DiathesisOption> getEntityClass() {
		return DiathesisOption.class;
	}

	@Override
	public List<DiathesisOption> findListByUnitIdAndProjectId(String unitId, String projectId) {
		return diathesisOptionDao.findByUnitIdAndProjectId(unitId, projectId);
	}

	@Override
	public Map<String, String> findMapByUnitIdAndProjectId(String unitId, String projectId) {
		Map<String, String> map = new HashMap<String, String>();
		List<Object[]> list = diathesisOptionDao.findMapByUnitIdAndProjectId(unitId, projectId);
		if(CollectionUtils.isNotEmpty(list)){
			for (Object[] objects : list) {
				map.put((String)objects[0], (String)objects[1]);
			}
		}
		return map;
	}

	@Override
	public Map<String, String> findMapByUnitIdAndStructureIdIn(String unitId, String[] structureIds) {
		Map<String, String> map = new HashMap<String, String>();
		List<Object[]> list = diathesisOptionDao.findMapByUnitIdAndStructureIdIn(unitId, structureIds);
		if(CollectionUtils.isNotEmpty(list)){
			for (Object[] objects : list) {
				map.put((String)objects[0], (String)objects[1]);
			}
		}
		return map;
	}

	@Override
	public List<DiathesisOption> findByUnitId(String unitId) {
		return diathesisOptionDao.findByUnitId(unitId);
	}

	@Override
	public List<DiathesisOption> findListByStructureIdIn(String[] structureIds) {
		if (structureIds==null || structureIds.length==0)return new ArrayList<>();
		return diathesisOptionDao.findListByStructureIdIn(structureIds);
	}

	@Override
	public void deleteByProjectIdIn(List<String> projectIds) {
		if(CollectionUtils.isEmpty(projectIds))return;
		diathesisOptionDao.deleteByProjectIdIn(projectIds);
	}

	@Override
	public void deleteByIn(List<String> ids) {
		if(CollectionUtils.isEmpty(ids))return;
		diathesisOptionDao.deleteByIdIn(ids);
	}

	@Override
	public List<DiathesisOption> findListByProjectIdIn(List<String> projectIds) {
		if(CollectionUtils.isEmpty(projectIds))return new ArrayList<>();
		return diathesisOptionDao.findListByProjectIdIn( projectIds);
	}

}
