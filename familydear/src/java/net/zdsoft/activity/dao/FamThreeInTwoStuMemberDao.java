package net.zdsoft.activity.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.activity.entity.FamDearThreeInTwoStuMember;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface FamThreeInTwoStuMemberDao extends BaseJpaRepositoryDao<FamDearThreeInTwoStuMember,String>{

	@Modifying
	@Query("delete From FamDearThreeInTwoStuMember where stuId=?1 ")
	public void deleteBystuId(String stuId);
}
