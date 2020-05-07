package net.zdsoft.studevelop.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopTemplate;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by luf on 2018/12/19.
 */
public interface StudevelopTemplateDao extends BaseJpaRepositoryDao<StudevelopTemplate,String> {
    @Query("From StudevelopTemplate where acadyear=?1 and semester=?2  and  gradeId=?3  and code = ?4 and unitId = ?5")
    public List<StudevelopTemplate> getTemplateByCode(String acadyear , String semester , String gradeId  , String code , String unitId);


    @Query(nativeQuery = true ,value = "select * from  studevelop_template where acadyear=?1 and semester=?2 and section=?3 and code=?4 and unit_id=?5 " )
    public List<StudevelopTemplate> getTemplateBySection(String acadyear ,String semester ,String section  ,String code ,String unitId);

    @Query(nativeQuery = true ,value = "select * from  studevelop_template where   section=?1 and code=?2 and unit_id=?3 " )
    public List<StudevelopTemplate> getTemplateBySectionCode(String section  ,String code ,String unitId);
    
    @Modifying
	@Query("delete from StudevelopTemplate where unitId = ?1 and acadyear = ?2 and semester = ?3")
	public void deleteBySemester(String unitId, String acadyear, String semester);
}
