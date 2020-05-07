package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccAttachFolder;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccAttachFolderDao extends BaseJpaRepositoryDao<EccAttachFolder, String>{

	@Query("From EccAttachFolder Where id in (?1) order by createTime desc")
	public List<EccAttachFolder> findByIdsOrder(String[] ids);

	@Modifying
    @Query("update from EccAttachFolder set isShow = 1 Where id = ?1")
	public void updateIsShow(String id);
	
	@Modifying
	@Query("update from EccAttachFolder set isShow = 0 Where id in (?1)")
	public void updateIsNotShow(String[] ids);

	@Query("From EccAttachFolder Where unitId = ?1 and range = ?2 order by createTime desc")
	public List<EccAttachFolder> findListBySchIdRange2(String unitId, int ranges);

	@Modifying
    @Query("update from EccAttachFolder set sendType = ?1 Where id = ?2")
	public void updateSendType(Integer sendType,String id);

}
