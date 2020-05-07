package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopMasterWords;

import java.util.List;

/**
 * studevelop_master_words
 * @author 
 * 
 */
public interface StudevelopMasterWordsService extends BaseService<StudevelopMasterWords, String>{

	/**
	 * 新增studevelop_master_words
	 * @param studevelopMasterWords
	 * @return
	 */
	public void save(StudevelopMasterWords studevelopMasterWords);

	/**
	 * 根据ids数组删除studevelop_master_words数据
	 * @param ids
	 * @return
	 */
	public Integer delete(String[] ids);

	/**
	 * 更新studevelop_master_words
	 * @param studevelopMasterWords
	 * @return
	 */
	public Integer update(StudevelopMasterWords studevelopMasterWords);

	/**
	 * 根据UnitId获取studevelop_master_words
	 * @param unitId
	 * @return
	 */
	public List<StudevelopMasterWords> getStudevelopMasterWordsByUnitId(String unitId);

}