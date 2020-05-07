package net.zdsoft.activity.dao;

import java.util.List;

import net.zdsoft.activity.entity.FamilyDearRegister;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FamilyDearRegisterDao extends BaseJpaRepositoryDao<FamilyDearRegister,String>,FamilyDearRegisterJdbcDao{

	@Query("From FamilyDearRegister where unitId=?1 and teacherId=?2 and arrangeId in (?3)")
	public List<FamilyDearRegister> getFamilyDearRegisterList(String unitId,String teacherId, String[] arrangeIds); 
	
	@Query("From FamilyDearRegister where status!=-1 and unitId=?1 and activityId=?2 and arrangeId=?3")
	public List<FamilyDearRegister> getFamilyDearReByArrangeIdAndActivityIdAndUnitId(String unitId,String activityId,String arrangeId);
	@Query("From FamilyDearRegister where arrangeId in (?1)")
	public List<FamilyDearRegister> getFamilyDearRegisterListByArrangeId(String[] arrangeIds);

	@Query("From FamilyDearRegister where status!=-1 and unitId=?1 and teacherId=?2")
	public List<FamilyDearRegister> getFamilyDearRegisterList(String unitId ,String teacherId);

	@Modifying
	@Query("delete from FamilyDearRegister where arrangeId in (?1)")
	public void deleteByIds(String[] arrangeIds);
	
}
