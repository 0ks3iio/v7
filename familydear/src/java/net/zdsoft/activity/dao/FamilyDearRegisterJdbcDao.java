package net.zdsoft.activity.dao;

import java.util.List;

import net.zdsoft.activity.entity.FamilyDearRegister;
import net.zdsoft.framework.entity.Pagination;

public interface FamilyDearRegisterJdbcDao {

	public List<FamilyDearRegister> getListByUnitIdAndTeacherIds(String unitId,String year, String[] activityIds,Pagination page);
}
