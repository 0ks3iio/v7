package net.zdsoft.activity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.activity.entity.FamDearThreeInTwoStu;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface FamThreeInTwoStuDao extends BaseJpaRepositoryDao<FamDearThreeInTwoStu,String>{
	
	@Query("From FamDearThreeInTwoStu where identityCard=?1 and id !=?2 and isDelete=0")
	public List<FamDearThreeInTwoStu> getDearStuByIdentityCard(String identitycard, String id);
	
	@Query("From FamDearThreeInTwoStu where unitId=?1 and teacherId=?2 and isDelete=0")
	public List<FamDearThreeInTwoStu> getDearStuByUnitIdAndTeacherId(String unitId,String teacherId);
	
	@Query("From FamDearThreeInTwoStu where unitId=?1 and stuname like concat('%',?2,'%') and isDelete=0")
	public List<FamDearThreeInTwoStu> getFamDearThreeInTwoStuByStuName(String unitId,String stuName);

}
