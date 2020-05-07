package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityScale;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * stutotality_scale 
 * @author 
 * 
 */
public interface StutotalityScaleDao extends BaseJpaRepositoryDao<StutotalityScale, String>{
       //根据 学校 学年 学期 年级 规则
        @Query("From StutotalityScale where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeId = ?4 and type = ?5")
        List<StutotalityScale> findAllByUnitIdAndAcadyearAndSemesterAndGradeId(String unitId, String acadyear, String semester, String gradeId, String type);
       //根据 学校 年级Id  规则
      @Query("From StutotalityScale where unitId = ?1 and gradeId = ?2 and type = ?3")
      List<StutotalityScale> findAllByUnitIdAndGradeIdAndType(String unitId, String gradeId ,String type);
        //根据 学校 学年 学期 年级
        @Query("From StutotalityScale where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeId = ?4 ")
        List<StutotalityScale> findAllByUnitIdAndGradeId(String unitId, String acadyear, String semester, String gradeId);

       @Modifying
       @Query("delete from StutotalityScale where id in (?1)")
        void deleteByIds(String[] ids);

        @Modifying
        @Query("delete from StutotalityScale where unitId =?1 and acadyear = ?2 and semester = ?3 and gradeId = ?4")
        void deleteByUnitIdAndAcadyearAAndSemesterAndGradeId(String unitId, String acadyear, String semester, String gradeId);
    @Modifying
    @Query("delete from StutotalityScale where unitId =?1 and acadyear = ?2 and semester = ?3 and classId = ?4")
    void deleteByUnitIdAndAcadyearAndSemesterAndClassId(String unitId, String acadyear, String semester, String classId );

        @Modifying
        @Query("delete from StutotalityScale where unitId =?1 and acadyear = ?2 and semester = ?3 and type = ?4")
        void deleteByUnitIdAndAcadyearAAndSemesterAndType(String unitId, String acadyear, String semester, String type);

        @Query(" from StutotalityScale where id in (?1)")
      List<StutotalityScale> findByIds(String[] ids);

        @Query(" from StutotalityScale where itemId in (?1)")
        List<StutotalityScale> findByItemIds(String[] itemIds);
    @Query("From StutotalityScale where unitId = ?1 and acadyear = ?2 and semester = ?3 and type = ?5 and classId in (?4) ")
    List<StutotalityScale> findByUnitIdAndClassIdsAndtype(String unitId, String acadyear, String semester, String[] classIds, String type);

}
