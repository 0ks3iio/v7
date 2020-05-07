package net.zdsoft.bigdata.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.system.dao.BgRolePermDao;
import net.zdsoft.bigdata.system.entity.BgRolePerm;
import net.zdsoft.bigdata.system.service.BgRolePermService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bgRolePermService")
public class BgRolePermServiceImpl  extends BaseServiceImpl<BgRolePerm, String> implements BgRolePermService{

	@Autowired
	private BgRolePermDao bgRolePermDao;
	
	@Override
	public List<BgRolePerm> findRolePermListByRoleId(String roleId) {
		return bgRolePermDao.findRolePermListByRoleId(roleId);
	}
	
	@Override
	public List<BgRolePerm> findRolePermListByRoleIds(String[] roleIds) {
		return bgRolePermDao.findRolePermListByRoleIds(roleIds);
	}

	@Override
	public void deleteByRoleId(String roleId) {
		bgRolePermDao.deleteByRoleId(roleId);
	}
	
	@Override
	public void saveRolePerm(String roleId, String moduleIds) {
		deleteByRoleId(roleId);
		List<BgRolePerm> resultList=new ArrayList<BgRolePerm>();
		if(StringUtils.isNotBlank(moduleIds)){
			String[] moduleIdArray=moduleIds.split(",");
			for(String moduleId:moduleIdArray){
				BgRolePerm rolePerm=new BgRolePerm();
				rolePerm.setId(UuidUtils.generateUuid());
				rolePerm.setRoleId(roleId);
				rolePerm.setModuleId(moduleId);
				rolePerm.setCreationTime(new Date());
				resultList.add(rolePerm);
			}
			saveAll(resultList.toArray(new BgRolePerm[0]));
		}
	}

	@Override
	protected BaseJpaRepositoryDao<BgRolePerm, String> getJpaDao() {
		return bgRolePermDao;
	}

	@Override
	protected Class<BgRolePerm> getEntityClass() {
		return BgRolePerm.class;
	}

}
