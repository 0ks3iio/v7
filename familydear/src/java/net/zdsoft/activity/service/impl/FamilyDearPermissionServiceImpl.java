package net.zdsoft.activity.service.impl;

import java.util.List;

import net.zdsoft.activity.dao.FamilyDearPermissionDao;
import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.activity.service.FamilyDearPermissionService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("FamilyDearPermissionService")
public class FamilyDearPermissionServiceImpl extends BaseServiceImpl<FamilyDearPermission,String> implements FamilyDearPermissionService {
	
	@Autowired
	private FamilyDearPermissionDao familyDearPermissionDao;
	
	@Override
	public List<FamilyDearPermission> getFamilyDearPermissionList(
			String unitId, String permissionType) {
		return familyDearPermissionDao.getFamilyDearPermissionList(unitId, permissionType);
	}

	@Override
	public List<FamilyDearPermission> getFamilyDearPermissionListByPermissionType(String unitId, String userId, String permissionType) {

		return familyDearPermissionDao.getFamilyDearPermissionListByPermissionType(unitId, userId, permissionType);
	}

	@Override
	protected BaseJpaRepositoryDao<FamilyDearPermission, String> getJpaDao() {
		return familyDearPermissionDao;
	}

	@Override
	protected Class<FamilyDearPermission> getEntityClass() {
		return FamilyDearPermission.class;
	}

}
