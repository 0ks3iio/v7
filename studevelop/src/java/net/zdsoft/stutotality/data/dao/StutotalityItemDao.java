package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * stutotality_item
 * @author
 *
 */
public interface StutotalityItemDao extends BaseJpaRepositoryDao<StutotalityItem, String>{
    @Query("from StutotalityItem  where typeId in (?1) order by orderNumber")
	List<StutotalityItem> findListByTypeIds(String[] typeIds);

    @Query("From StutotalityItem where typeId = ?1 ORDER BY orderNumber asc")
    public List<StutotalityItem> findListByTypeId(String typeId);

    List<StutotalityItem> findByUnitId(String unitId);

    @Modifying
    @Query("Delete From StutotalityItem where  id in (?1) ")
    public void deleteByIds(String[] ids);

    @Query("from StutotalityItem where unitId = ?1 and subjectType in (?2) order by orderNumber")
    public List<StutotalityItem> findByUnitIdAndSubjectType(String unitId, String[] subjectTypes);

    @Query("from StutotalityItem where subjectId in (?1)")
    public List<StutotalityItem> findBySubjectIds(String[] subjectIds);

    @Query("from StutotalityItem where unitId = ?1 and acadyear = ?2 and semester =?3 and gradeId=?4 and subjectId in (?5) order by orderNumber")
    public List<StutotalityItem> findByParams(String unitId,String acadyear,String semester,String gradeId,String[] subjectIds);

    @Query("from StutotalityItem where unitId = ?1 and acadyear = ?2 and semester =?3 and subjectType in (?4) order by orderNumber")
    public List<StutotalityItem> findByUnitIdAndAcadyearAndSemesterAndSubjectId(String unitId,String acadyear,String semester,String[] subjectTypes);

    public void deleteByUnitIdAndSubjectType(String unitId, String subjectType);

    @Query("from StutotalityItem  where id in (?1) ORDER BY orderNumber asc")
    List<StutotalityItem> findListByIds(String[] ids);
}
