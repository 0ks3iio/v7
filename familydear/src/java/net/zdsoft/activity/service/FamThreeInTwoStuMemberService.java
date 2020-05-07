package net.zdsoft.activity.service;

import net.zdsoft.activity.entity.FamDearThreeInTwoStuMember;
import net.zdsoft.basedata.service.BaseService;

public interface FamThreeInTwoStuMemberService extends BaseService<FamDearThreeInTwoStuMember,String>{
	
	public void deleteBystuId(String stuId);

}
