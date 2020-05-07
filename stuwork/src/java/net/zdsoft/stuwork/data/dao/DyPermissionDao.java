package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyPermission;

public interface DyPermissionDao extends BaseJpaRepositoryDao<DyPermission, String>{

	@Query("From DyPermission where unitId = ?1 and classType=?2 and  permissionType=?3 ")
	public List<DyPermission> findListByUnitId(String unitId,String classsType,String permissionType);

	@Modifying
    @Query("delete from DyPermission Where classId = ?1 and classType=?2 and  permissionType=?3 ")
	public void deleteByClassId(String classId ,String classsType,String permissionType);

	@Query("From DyPermission where userId = ?1 and classType=?2 and  permissionType=?3 ")
	public List<DyPermission> findListByUserId(String userId ,String classsType,String permissionType);
	
	@Query("From DyPermission where  classType=?1 and  permissionType=?2 and userId in (?3)")
	public List<DyPermission> findListByUserIds(String classsType,String permissionType ,String[] userIds );

}
