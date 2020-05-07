package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityStuFinal;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * stutotality_stu_final 
 * @author 
 * 
 */
public interface StutotalityStuFinalDao extends BaseJpaRepositoryDao<StutotalityStuFinal, String>{
    @Query("from StutotalityStuFinal where studentId in(?1)")
    public List<StutotalityStuFinal> findListByStuIds(String [] studentIds);

    public List<StutotalityStuFinal> findByAcadyearAndSemesterAndUnitIdAndStudentIdIn(String year, String semester, String unitId,String[] studentIds);

    public List<StutotalityStuFinal> findByAcadyearAndSemesterAndUnitId(String year, String semester, String unitId);

    @Modifying

    @Query("delete from StutotalityStuFinal where id in (?1)")
    void deleteByIds(String[] ids);

}
