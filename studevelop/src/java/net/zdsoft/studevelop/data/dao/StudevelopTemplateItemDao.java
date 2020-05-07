package net.zdsoft.studevelop.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateItem;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by luf on 2018/12/14.
 */
public interface StudevelopTemplateItemDao extends BaseJpaRepositoryDao<StudevelopTemplateItem , String> {

    @Query("From StudevelopTemplateItem where templateId=?1 and objectType=?2 and isClosed='0' order by creationTime desc")
    public List<StudevelopTemplateItem> getTemplateItemListByObjectType(String templateId , String objectType);
    @Query("From StudevelopTemplateItem where itemName=?1 and templateId=?2 and objectType=?3 and id != ?4  ")
    public List<StudevelopTemplateItem> getTemplateItemListByItemName(String itemName ,String templateId ,String objectType,String id);
    @Query("From StudevelopTemplateItem where templateId=?1 and isClosed='0'   order by creationTime desc ")
    public List<StudevelopTemplateItem> getTemplateItemListByTemplateId(String templateId );
    
    @Modifying
	@Query("delete from StudevelopTemplateItem where templateId in (?1)")
	public void deleteByTemplateIds(String[] templateIds);

}
