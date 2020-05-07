package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.dao.DiathesisStructureDao;
import net.zdsoft.diathesis.data.entity.DiathesisStructure;
import net.zdsoft.diathesis.data.service.DiathesisStructureService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/3/29 9:44
 */
@Service("diathesisStructureService")
public class DiathesisStructureServiceImpl extends BaseServiceImpl<DiathesisStructure, String>  implements DiathesisStructureService {
	
    @Autowired
    private DiathesisStructureDao diathesisStructureDao;

	@Override
	protected BaseJpaRepositoryDao<DiathesisStructure, String> getJpaDao() {
		return diathesisStructureDao;
	}

	@Override
	protected Class<DiathesisStructure> getEntityClass() {
		return DiathesisStructure.class;
	}

	@Override
	public List<DiathesisStructure> findListByProjectId( String projectId) {
		return diathesisStructureDao.findByProjectId(projectId);
	}

	@Override
	public List<DiathesisStructure> findListByProjectIdIn(String[] projectIds) {
		if(projectIds==null || projectIds.length==0)return new ArrayList<>();
		return diathesisStructureDao.findByProjectIdIn(projectIds);
	}

	@Override
	public void deleteByProjectIdIn(List<String> projectIds) {
		if(CollectionUtils.isEmpty(projectIds))return;
		diathesisStructureDao.deleteByProjectIdIn(projectIds);
	}

	@Override
	public void deleteByIdIn(List<String> ids) {
		if(CollectionUtils.isEmpty(ids))return;
		diathesisStructureDao.deleteByIdIn(ids);
	}

	@Override
	public List<DiathesisStructure> findListBySingleTypeAndProjectTypeIn(List<String> projectIds) {
		if(CollectionUtils.isEmpty(projectIds))return new ArrayList<>();
		return diathesisStructureDao.findListBySingleTypeAndProjectTypeIn(projectIds);
	}


}
