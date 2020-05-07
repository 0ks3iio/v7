package net.zdsoft.evaluation.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.evaluation.data.dto.OptionDto;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;


public interface TeachEvaluateItemService extends BaseService<TeachEvaluateItem, String>{

	/**
	 * 查询list 包涵选项
	 * @param unitId
	 * @param dto
	 * @return
	 */
	public List<TeachEvaluateItem> findByDto(String unitId,OptionDto dto);
	
	/**
	 * 查询list 不包涵选项
	 * @param unitId
	 * @param dto
	 * @return
	 */
	public List<TeachEvaluateItem> findByCon(String unitId,String evaluateType,String itemType);
	/**
	 * 查询list 不包涵选项
	 * @param unitId
	 * @param dto
	 * @return
	 */
	public List<TeachEvaluateItem> findForCheck(String unitId,String itemType,String itemName);
	/**
	 * 查询list 包涵选项
	 * @param unitId
	 * @param evaluateType
	 * @return
	 */
	public List<TeachEvaluateItem> findByUidAndEvaType(String unitId,String evaluateType,String projectId);
	/**
	 * 保存两张表内容
	 * @param unitId
	 * @param TeachEvaluateItem
	 * @return
	 */
	public void saveItem(String unitId,TeachEvaluateItem evaItem);
	
	public List<TeachEvaluateItem> findByEvaluateType(String unitId,
			String evaluateType,String projectId);

	public void deleteByProjectId(String projectId);
}
