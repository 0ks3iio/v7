package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface StorageDirDao extends BaseJpaRepositoryDao<StorageDir, String> {
	
	@Query("From StorageDir where type=?1 and active=?2")
	List<StorageDir> findByTypeAndActiove(Integer type, String actiove);
	@Query("From StorageDir where type=?1 and preset=?2")
	List<StorageDir> findByTypeAndPreset(Integer type, String preset);
	@Query("From StorageDir where id=?1")
	StorageDir getStorageDir(String storageDirId);

}
