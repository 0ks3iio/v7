package net.zdsoft.stuwork.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.dao.DyDormCheckRoleDao;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormCheckRole;
import net.zdsoft.stuwork.data.service.DyDormCheckRoleService;

@Service("dyDormCheckRoleService")
public class DyDormCheckRoleServiceImpl  extends BaseServiceImpl<DyDormCheckRole, String> implements DyDormCheckRoleService{
	@Autowired
	private DyDormCheckRoleDao dyDormCheckRoleDao; 
	
	@Override
	public List<DyDormCheckRole> getCheckRolesBy(String schoolId,String acadyear,String semester){
		return dyDormCheckRoleDao.getCheckRolesBy(schoolId, acadyear, semester);
	}
	@Override
	public void deleteBy(DormSearchDto dormDto){
		dyDormCheckRoleDao.deleteBy(dormDto.getUnitId(), dormDto.getSearchBuildId(), dormDto.getAcadyear(), dormDto.getSemesterStr());
	}
	@Override
	protected BaseJpaRepositoryDao<DyDormCheckRole, String> getJpaDao() {
		return dyDormCheckRoleDao;
	}

	@Override
	protected Class<DyDormCheckRole> getEntityClass() {
		return DyDormCheckRole.class;
	}

}
