package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccAttachFolderTo;

public interface EccAttachFolderToService extends BaseService<EccAttachFolderTo, String> {

	public List<EccAttachFolderTo> findByObjIdAndRange(String objectId, int range);

	public void deleteByObjAndRange(String objectId, int range);

	public  List<EccAttachFolderTo> findByFolderId(String folderId);

	public void deleteByFolderAndRange(String folderId,int range);
	
	public List<EccAttachFolderTo> findByObjIdAndRangeAndType(String objectId,Integer range, Integer type);

}
