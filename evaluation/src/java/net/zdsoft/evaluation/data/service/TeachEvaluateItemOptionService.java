package net.zdsoft.evaluation.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItemOption;

public interface TeachEvaluateItemOptionService extends BaseService<TeachEvaluateItemOption, String>{

	/**
	 * 通过unitId获取 选项list
	 * @param unitId
	 * @param itemIds
	 * @return
	 */
	public List<TeachEvaluateItemOption> findByUnitId(String unitId);
	/**
	 * 通过itemids获取 选项list
	 * @param unitId
	 * @param itemIds
	 * @return
	 */
	public List<TeachEvaluateItemOption> findByItemIds(String unitId,String[] itemIds);
	/**
	 * 更新选项(删除后添加)  
	 * @param unitId
	 * @param itemId
	 * @return optionList
	 */
	public String  updateOption(String unitId,String itemId,List<TeachEvaluateItemOption> optionList);
	/**
	 * 删除选项  
	 * @param unitId
	 * @param itemId
	 * @return 
	 */
	public void  deleteByItemId(String unitId,String itemId);
}
