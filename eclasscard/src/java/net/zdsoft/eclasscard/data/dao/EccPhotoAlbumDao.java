package net.zdsoft.eclasscard.data.dao;

import java.util.List;
import java.util.Set;

import net.zdsoft.eclasscard.data.entity.EccPhotoAlbum;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface EccPhotoAlbumDao extends BaseJpaRepositoryDao<EccPhotoAlbum, String>{

	@Query("From EccPhotoAlbum Where objectId = ?1 order by createTime desc")
	public List<EccPhotoAlbum> findByShowEccInfo(String objectId);
	
	@Query("From EccPhotoAlbum Where objectId in (?1) order by createTime")
	public List<EccPhotoAlbum> findByObjIds(String[] objectIds);
	
	@Query("Select pictrueDirpath From EccPhotoAlbum Where pictrueDirpath in (?1) Group By pictrueDirpath Having count(id) > 1")
	public Set<String> findPictrueDirpathMore(String[] pictrueDirpaths);
	
	@Query("From EccPhotoAlbum Where objectId in (?1) order by createTime desc")
	public List<EccPhotoAlbum> findByObjectIdIn(String[] array);

}
