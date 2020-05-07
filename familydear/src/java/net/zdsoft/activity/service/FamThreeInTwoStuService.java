package net.zdsoft.activity.service;

import java.util.List;

import net.zdsoft.activity.entity.FamDearThreeInTwoStu;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;

public interface FamThreeInTwoStuService extends BaseService<FamDearThreeInTwoStu,String>{
	
	public List<FamDearThreeInTwoStu> getDearStuByIdentityCard(String identitycard, String id);
	
	public void saveFamilyMember(FamDearThreeInTwoStu famDearThreeInTwoStu);
	
	public void saveCadre(String objId, String teacherId);
	
	public List<FamDearThreeInTwoStu> getDearStuByUnitIdAndOthers(String unitId,String userId,String stuName,String ganbName,String stuPhone,boolean isAdmin, Pagination page);

	public String saveImportDate(List<String[]> arrDatas, LoginInfo loginInfo);
	
	public List<FamDearThreeInTwoStu> getDearStuByUnitIdAndTeacherId(String unitId,String teacherId);
	
	public List<FamDearThreeInTwoStu> getFamDearThreeInTwoStuByStuName(String unitId,String stuName);

}
