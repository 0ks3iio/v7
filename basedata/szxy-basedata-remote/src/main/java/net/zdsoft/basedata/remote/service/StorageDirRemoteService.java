package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.StorageDir;

public interface StorageDirRemoteService extends BaseRemoteService<StorageDir,String> {

	public String findByTypeAndActiove(Integer type,String actiove);
	
	public String findByTypeAndPreset(Integer type,String preset);

}
