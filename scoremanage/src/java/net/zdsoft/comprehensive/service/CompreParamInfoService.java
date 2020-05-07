package net.zdsoft.comprehensive.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.comprehensive.entity.CompreParameterInfo;

public interface CompreParamInfoService extends
		BaseService<CompreParameterInfo, String> {

	
	//合并到findByInfoKey
//	List<CompreParameterInfo> getCompreParamInfoListByUnitIdAndInfoKey(
//			String unitId, String infoKey);

	
	void saveAll(CompreParameterInfo[] array);
	

	void delete(Integer mcPrefix, Integer mcSuffix, String unidId,
			String... infoKeys);

	
//分割线，以上为原有方法，方便后续删除
	List<CompreParameterInfo> findByInfoKey(String unitId, String... infoKey);
	
	void deleteAllByInfoKeys(String unitId, String... infoKey);
	
	void deleteAndSaveOther(String unitId, String[] parkeys,
			List<CompreParameterInfo> compreParamInfoList);

	void deleteAndSaveEnglish(String unitId, String[] parkeys,
			List<CompreParameterInfo> compreParamInfoListSave);

	void deleteAndSaveXk(String unitId, String[] parkeys, List<CompreParameterInfo> compreParamInfoListSave);

	
}
