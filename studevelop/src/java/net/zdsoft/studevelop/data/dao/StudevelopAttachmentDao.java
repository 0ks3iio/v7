package net.zdsoft.studevelop.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;

/**
 * 
 * @author weixh
 * @since 2017-7-20 上午11:34:42
 */
public interface StudevelopAttachmentDao extends BaseJpaRepositoryDao<StudevelopAttachment, String> {
	@Query("from StudevelopAttachment where objId=?1 and objecttype=?2 and isDeleted=0 order by creationTime")
	public List<StudevelopAttachment> getAttachmentByObjId(String objId, String objType);
	
	@Query("from StudevelopAttachment where isDeleted=0 and objId in (?1)")
	public List<StudevelopAttachment> findListByObjIds(String... objId);
	
	@Query("from StudevelopAttachment where isDeleted=0 and acadyear =?1 and semester =?2 and objId in (?3)")
	public 	List<StudevelopAttachment> findListByObjIds(String acadyear,String semester,String... objId);
	
	@Modifying
	@Query("update StudevelopAttachment set isDeleted=1 where isDeleted=0 and acadyear =?1 and semester =?2 and objId in (?3)")
	public 	Integer deleteByObjIds(String acadyear,String semester,String... objId);
	
	@Query("select count(*) from StudevelopAttachment where isDeleted=0 and objId=?1 and objecttype=?2")
	public Integer findAttachmentNumByObjId(String objId, String objType);
	
	@Query("from StudevelopAttachment where isDeleted=0 and objId in (?1)")
	public List<StudevelopAttachment> getAttachmentByObjIds(String[] objIds);
	
	@Modifying
	@Query("update StudevelopAttachment set isDeleted=1 where id in (?1)")
	public 	Integer deleteByIds(String... ids);
}
