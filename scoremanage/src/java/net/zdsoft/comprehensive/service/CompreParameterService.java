package net.zdsoft.comprehensive.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.comprehensive.entity.CompreParameter;

public interface CompreParameterService extends BaseService<CompreParameter, String>{
	
//分割线，以上为原有方法，方便后续删除
	
	List<CompreParameter> findCompreParameterByTypeAndUnitId(String type,String unitId);

	void saveAllCompreParameter(List<CompreParameter> compreParameterList);

	List<CompreParameter> findCompreParameterByUnitId(String unitId);
}
