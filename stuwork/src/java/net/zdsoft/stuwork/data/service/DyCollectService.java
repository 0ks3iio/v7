package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.stuwork.data.dto.DyCollectDto;

public interface DyCollectService {
	/**
	 * 获取汇总查询24的结果  
	 * @param unitId
	 * @param type
	 * @param acadyear
	 * @param semester
	 * @param studentIds
	 * @return
	 */
	List<DyCollectDto> findCollectList2(String unitId,String acadyear,String semester,String[] studentIds);
	/**
	 * 获取汇总查询24的结果  
	 * @param unitId
	 * @param type
	 * @param acadyear
	 * @param semester
	 * @param studentIds
	 * @return
	 */
	List<DyCollectDto> findCollectList4(String unitId,String acadyear,String semester,String[] studentIds);
}
