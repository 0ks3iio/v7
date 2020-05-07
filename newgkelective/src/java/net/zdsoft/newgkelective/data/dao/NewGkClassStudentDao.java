package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;

public interface NewGkClassStudentDao extends BaseJpaRepositoryDao<NewGkClassStudent, String>{

    @Query("From NewGkClassStudent where unitId=?1 and divideId=?2 and studentId = ?3")
	public List<NewGkClassStudent> findListByStudentId(String unitId,String divideId,String studentId);
    
    @Query("From NewGkClassStudent where unitId=?1 and divideId=?2 and studentId in (?3)")
    public List<NewGkClassStudent> findListByStudentIds(String unitId,String divideId,String[] studentIds);
    
    @Query("select s From NewGkClassStudent s,NewGkDivideClass c where c.divideId=?1 and c.classType in (?2) and c.divideId=s.divideId and c.id=s.classId  and s.studentId in (?3) and c.sourceType = ?4")
	List<NewGkClassStudent> findListByDivideStudentId(String divideId, String[] classType,String[] studentIds,String scourceType);
    
	List<NewGkClassStudent> findByStudentIdInAndClassIdIn(String[] studentIds, String[] classIds);
	
	List<NewGkClassStudent> findByUnitIdAndDivideIdAndClassIdIn(String unitId, String divideId, String[] classIds);

    // Basedata Sync Method
    void deleteByStudentIdIn(String... studentIds);

    // Basedata Sync Method
    void deleteByClassIdIn(String... classIds);

	public void deleteByIdIn(String[] ids);
}
