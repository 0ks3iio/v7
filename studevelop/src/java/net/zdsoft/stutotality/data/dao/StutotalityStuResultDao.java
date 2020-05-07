package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityStuResult;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * stutotality_stu_result 
 * @author 
 * 
 */
public interface StutotalityStuResultDao extends BaseJpaRepositoryDao<StutotalityStuResult, String>{
    @Query("from StutotalityStuResult  where acadyear=?1 and semester=?2 and type =?5 and optionId in (?4) and studentId in (?3) ")
    public List<StutotalityStuResult> findListByStudentIdsAndOptionIdsAndType (String year,String semester,String[] studentIds,String[] optionIds,String resultType);
    @Query("from StutotalityStuResult  where acadyear=?1 and semester=?2 and studentId=?3 and type =?5 and itemHealthId in (?4)")
    public List<StutotalityStuResult> findListByItemIdsAndStudentIdAndType(String year,String semester,String studentId, String[] itemIds,String resultType);

    @Query("from StutotalityStuResult  where acadyear=?1 and semester=?2 and type =?4 and itemHealthId in (?3)")
    public List<StutotalityStuResult> findListByItemIdsAndType(String year,String semester,String[] itemIds,String resultType);

    List<StutotalityStuResult> findByAcadyearAndSemesterAndUnitIdAndType(String year,String semester,String unitId,String type);

    List<StutotalityStuResult> findByAcadyearAndSemesterAndUnitIdAndTypeAndStudentId(String year,String semester,String unitId,String type,String studentId);

    List<StutotalityStuResult> findByAcadyearAndSemesterAndUnitIdAndTypeAndItemHealthIdAndOptionId(String year,String semester,String unitId,String type,String itemHealthId,String optionId);

    List<StutotalityStuResult> findByAcadyearAndSemesterAndUnitIdAndTypeAndItemHealthId(String year,String semester,String unitId,String type,String itemHealthId);

    @Query("from StutotalityStuResult where unitId = ?1 and acadyear = ?2 and semester=?3 and studentId =?4 ")
    List<StutotalityStuResult> findListByParms(String unitId,String acadyear,String semester,String studentId);
    @Query("from StutotalityStuResult where unitId = ?1 and acadyear = ?2 and semester=?3 and studentId =?4 and itemHealthId in (?5)")
    List<StutotalityStuResult> findListByParms(String unitId,String acadyear,String semester,String studentId,String[] itemIds);
    @Query("from StutotalityStuResult where  acadyear = ?1 and semester=?2 and type <> ?3 and studentId in (?4)   ")
    List<StutotalityStuResult> findListByStudentIds(String acadyear,String semester,String type,String[] studentIds);
    @Query("from StutotalityStuResult where  acadyear = ?1 and semester=?2  and itemHealthId in (?3) ")
    List<StutotalityStuResult> findListByItemHealthIds(String acadyear,String semester,String[] itemHealthIds);
    @Query("from StutotalityStuResult where  acadyear = ?1 and semester=?2 and studentId in (?3) and itemHealthId in (?4) ")
    List<StutotalityStuResult> findListByStudentIds(String acadyear,String semester,String[] studentIds,String[] itemHealthIds);

    @Modifying
    @Query("delete from StutotalityStuResult where unitId = ?1 and acadyear = ?2 and semester=?3 and type =?5 and studentId in (?4)")
    void deleteByStudentIds(String unitId, String acadyear, String semester, String[] studentId, String Type);

}
