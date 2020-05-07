package net.zdsoft.activity.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.activity.dao.FamThreeInTwoStuMemberDao;
import net.zdsoft.activity.entity.FamDearThreeInTwoStuMember;
import net.zdsoft.activity.service.FamThreeInTwoStuMemberService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("FamThreeInTwoStuMemberService")
public class FamThreeInTwoStuMemberServiceImpl extends BaseServiceImpl<FamDearThreeInTwoStuMember,String> implements FamThreeInTwoStuMemberService{

	@Autowired
	private FamThreeInTwoStuMemberDao famThreeInTwoStuMemberDao;
	
	@Override
	public void deleteBystuId(String stuId) {
		famThreeInTwoStuMemberDao.deleteBystuId(stuId);
	}

	@Override
	protected BaseJpaRepositoryDao<FamDearThreeInTwoStuMember, String> getJpaDao() {
		return famThreeInTwoStuMemberDao;
	}

	@Override
	protected Class<FamDearThreeInTwoStuMember> getEntityClass() {
		return FamDearThreeInTwoStuMember.class;
	}

}
