package net.zdsoft.gkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.entity.GkAllocation;

public interface GkAllocationService extends BaseService<GkAllocation, String>{
	
	/**
	 * 查询某一次排班的优先顺序
	 * @param arrangeSubjectId
	 * @return
	 */
	public List<GkAllocation> findByArrangeIdIsUsing(String arrangeSubjectId);

	/**
	 * 获取规则
	 * @param arrangeId
	 * @param isUsing 是否启用
	 * @param isZero 未找到时是否取32个0的初始数据
	 * @return
	 */
	public List<GkAllocation> findAllocationList(String arrangeId,boolean isUsing,boolean isZero);

	public void saveBatch(List<GkAllocation> allocationList);
}
