package net.zdsoft.studevelop.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItem;

import org.springframework.data.jpa.repository.Query;

public interface StuDevelopPerformItemDao extends BaseJpaRepositoryDao<StuDevelopPerformItem ,String>   {

    //
    @Query(nativeQuery = true ,value="select max(display_order) from studevelop_perform_item where unit_id = ?1 ")
    public Integer getMaxDisplayOrderByUnitId(String unitId);

    @Query(nativeQuery = true ,value="select * from studevelop_perform_item  where unit_id = ?1 and item_grade = ?2 order by display_order desc ")
    public List<StuDevelopPerformItem> getStuDevelopPerformItemsByUnitIdAndGrade(String unitId, String itemGrade );

    @Query(nativeQuery = true ,value="select * from studevelop_perform_item  where unit_id = ?1 and item_grade  in ?2")
    public List<StuDevelopPerformItem> getStuDevelopPerformItemsByUnitIdAndGrades(String unitId, String[] itemGrades );

    @Query(nativeQuery = true , value=("delete from  studevelop_perform_item where id = ?1 "))
    public void deleteByItemId(String itemId);

    @Query(nativeQuery = true ,value = " select * from studevelop_perform_item where unit_id =?1 and item_grade = ?2 and and item_name = ?3 and  id != ?4 ")
    public StuDevelopPerformItem getStuDevelopPerformItemsByIdAndGrade(String unitId , String gradeCode ,String itemName ,String itemId);

}
