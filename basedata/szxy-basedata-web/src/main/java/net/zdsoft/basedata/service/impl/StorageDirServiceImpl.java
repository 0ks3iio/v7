package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.StorageDirDao;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.service.StorageDirService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("storageDirService")
public class StorageDirServiceImpl extends BaseServiceImpl<StorageDir, String> implements StorageDirService {

    @Autowired
    private StorageDirDao storageDirDao;

    @Override
    protected BaseJpaRepositoryDao<StorageDir, String> getJpaDao() {
        return storageDirDao;
    }

    @Override
    protected Class<StorageDir> getEntityClass() {
        return StorageDir.class;
    }

    @Override
    public List<StorageDir> findByTypeAndActiove(Integer type, String actiove) {
        return storageDirDao.findByTypeAndActiove(type, actiove);
    }

    @Override
    public List<StorageDir> findByTypeAndPreset(Integer type, String preset) {
        return storageDirDao.findByTypeAndPreset(type, preset);
    }

	@Override
	public StorageDir getStorageDir(String storageDirId) {
		return storageDirDao.getStorageDir(storageDirId);
	}

}
