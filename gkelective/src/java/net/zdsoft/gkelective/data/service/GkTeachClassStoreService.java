package net.zdsoft.gkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.entity.GkTeachClassStore;
import net.zdsoft.gkelective.data.entity.GkTeachClassStuStore;

public interface GkTeachClassStoreService extends BaseService<GkTeachClassStore, String>{
	
	/**
	 * 删除临时教学班 包括教学班中学生
	 * @param ids
	 */
	public void deleteByIds(String[] ids);
	
	public List<GkTeachClassStore> findByRoundsId(String roundsId);

	/**
     * 学生id,教学班ids
     * 
     * @param classIds
     * @return Map&lt;studentId, List&lt;teachClassId&gt;&gt;
     */
    public Map<String, List<String>> findMapWithStuIdByClassIds(String[] classIds);

	public List<GkTeachClassStuStore> findByClassIds(String[] classIds);

	public List<GkTeachClassStuStore> findByStuIds(String roundId, String[] stuIds);

	public void saveAllStu(List<GkTeachClassStuStore> saveList);

	public void delete(String[] classIds, String[] stuIds);
	
}
