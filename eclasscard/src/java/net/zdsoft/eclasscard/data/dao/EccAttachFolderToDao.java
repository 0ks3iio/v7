package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccAttachFolderTo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccAttachFolderToDao extends BaseJpaRepositoryDao<EccAttachFolderTo, String>{

	@Query("From EccAttachFolderTo Where sendObjectId = ?1 and range = ?2")
	public List<EccAttachFolderTo> findByObjIdAndRange(String objectId, int range);

	@Modifying
    @Query("delete from EccAttachFolderTo Where sendObjectId = ?1 and range = ?2")
	public void deleteByObjAndRange(String objectId, int range);

	@Query("From EccAttachFolderTo Where folderId = ?1")
	public List<EccAttachFolderTo> findByFolderId(String folderId);

	@Modifying
	@Query("delete from EccAttachFolderTo Where folderId = ?1 and range = ?2")
	public void deleteByFolderAndRange(String folderId, int range);

	@Query("From EccAttachFolderTo Where sendObjectId = ?1 and range = ?2 and type = ?3")
	public List<EccAttachFolderTo> findByObjIdAndRangeAndType(String objectId,
			Integer range, Integer type);

}
