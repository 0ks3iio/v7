package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccAttachFolder;

public interface EccAttachFolderService extends BaseService<EccAttachFolder, String> {

	public List<EccAttachFolder> findListByObjId(String objectId,int range);

	public void saveNewFolder(EccAttachFolder attachFolder,String objectId);

	public void deleteById(String id,int range);

	public void updateIsShow(String id);
	
	public void updateIsShow(String id, String eccInfoIds);
	
	public void updateIsNotShow(String[] id);

	public void saveVideo(List<AttFileDto> fileDtos, String folderId,String unitId);

	public List<EccAttachFolder> findListBySchIdRange2(String unitId,
			int ranges);

	public void updateNotShow(String id);

	public List<EccAttachFolder> findByObjIdAndRangeAndType(String objectId,Integer range, Integer type);


}
