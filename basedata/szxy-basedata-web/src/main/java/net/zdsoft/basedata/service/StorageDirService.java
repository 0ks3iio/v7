package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.StorageDir;

public interface StorageDirService extends BaseService<StorageDir, String> {

    List<StorageDir> findByTypeAndActiove(Integer type, String actiove);

    List<StorageDir> findByTypeAndPreset(Integer type, String preset);
    
    public StorageDir getStorageDir(String storageDirId);

}
