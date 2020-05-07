package net.zdsoft.eclasscard.data.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.dto.FileDto;
import net.zdsoft.eclasscard.data.entity.EccPhotoAlbum;
import net.zdsoft.framework.entity.Pagination;


public interface EccPhotoAlbumService extends BaseService<EccPhotoAlbum, String>{

	public List<EccPhotoAlbum> findByShowEccInfo(String eccInfoId);

	public List<EccPhotoAlbum> findListByObjectIdPage(String objectId,Pagination page);

	public void deleteByObjId(String id);
	
	public List<EccPhotoAlbum> findByObjIdsTopOne(String[] objectIds);
	public List<EccPhotoAlbum> findByObjectIdIn(String[] array);
	public Set<String> findPictrueDirpathMore(String[] pictrueDirpaths);

	public void saveToMore(String unitId,String sendType, String[] eccInfoIds,List<FileDto> fileDtos);


}
