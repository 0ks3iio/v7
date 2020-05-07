package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface AttachmentDao extends BaseJpaRepositoryDao<Attachment, String>{

	@Query("From Attachment Where objId in (?1) order by creationTime desc,id desc")
	public List<Attachment> findByObjectIdOrder(String... objectId);

	@Query("From Attachment Where objecttype = ?1 and objId in (?2) order by creationTime desc,id desc")
	public List<Attachment> findByObjectTypeAndIdOrder(String objectType,String... objectId);
	
	@Modifying
	@Query("delete From Attachment Where id in (?1)")
	public void deleteByIds(String[] attachmentIds);
	
	@Modifying
	@Query("update Attachment set filename = ?2 Where id = ?1")
	public void updateFileNameById(String attachmentId,String filename);

}
