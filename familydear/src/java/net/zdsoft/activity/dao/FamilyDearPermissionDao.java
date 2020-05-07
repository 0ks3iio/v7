package net.zdsoft.activity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface FamilyDearPermissionDao extends BaseJpaRepositoryDao<FamilyDearPermission,String>{

	@Query("From FamilyDearPermission where unitId=?1 and permissionType=?2 ")
	public List<FamilyDearPermission> getFamilyDearPermissionList(String unitId, String permissionType);
	@Query("From FamilyDearPermission where unitId=?1 and userIds like concat('%',?2,'%') and permissionType like concat('%',?3,'%') ")
	public List<FamilyDearPermission> getFamilyDearPermissionListByPermissionType(String unitId,String userId,String permissionType);
}
