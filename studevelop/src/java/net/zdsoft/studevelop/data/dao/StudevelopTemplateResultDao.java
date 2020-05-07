package net.zdsoft.studevelop.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateResult;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by luf on 2018/12/14.
 */
public interface StudevelopTemplateResultDao extends BaseJpaRepositoryDao<StudevelopTemplateResult ,String> {

    @Query("From StudevelopTemplateResult where templateItemId in ?1 and studentId=?2 and acadyear=?3 and semester=?4")
    public List<StudevelopTemplateResult> getTemplateResultByStudentId(String[] templateItemIds ,String studentId ,String acadyear ,String semester);
    
    @Query("From StudevelopTemplateResult where templateItemId in ?1 and studentId  in ?2 and acadyear=?3 and semester=?4")
    List<StudevelopTemplateResult> getTemplateResultByStudentIds(String[] templateItemIds , String[] studentIds,String acadyear ,String semester);
    
    @Modifying
    @Query("delete From StudevelopTemplateResult where templateItemId in ?1 and studentId  in ?2 and acadyear=?3 and semester=?4")
    Integer deleteByItemIdsStuIds(String[] templateItemIds , String[] studentIds,String acadyear ,String semester);
    @Modifying
    @Query("delete From StudevelopTemplateResult where templateItemId in ?1 and studentId  in ?2 and acadyear=?3 and semester=?4 and subjectId =?5")
    Integer deleteByItemIdsStuIdsSubId(String[] templateItemIds , String[] studentIds,String acadyear ,String semester,String subjectId);
    
    @Modifying
	@Query("delete from StudevelopTemplateResult where templateId in (?1)")
	public void deleteByTemplateIds(String[] templateIds);
}
