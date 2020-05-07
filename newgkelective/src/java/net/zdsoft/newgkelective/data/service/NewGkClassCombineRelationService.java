package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkClassCombineRelation;

public interface NewGkClassCombineRelationService extends BaseService<NewGkClassCombineRelation, String> {
	
	public List<NewGkClassCombineRelation> findByArrayItemId(String unitId, String arrayItemId);
	
	/**
	 * 获取合班 班级组合
	 * @param unitId
	 * @param arrayItemId
	 * @return
	 */
	List<Set<String>> getCombineRelation(String unitId, String arrayItemId);

	/**
	 * 获取 合班 或者 同时排课 关系
	 * @param relaList
	 * @return 所有的 合班/同排 组合，一个元素 表示一个 合班/同排的 组合，比如1班 2班数学合班 ，Set 包含clsId-sub-subType,clsId2-sub-subType
	 */
    List<Set<String>> getGroupRelations(List<NewGkClassCombineRelation> relaList);

    public void deleteByArrayItemId(String arrayItemId);

    // Basedata Sync Method
    void deleteBySubjectIdOrClassIds(String... subjectIds);
}
