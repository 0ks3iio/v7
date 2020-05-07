package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;

public interface NewGkTeacherPlanService extends BaseService<NewGkTeacherPlan, String>{

	List<NewGkTeacherPlan> findByArrayItemIds(String[] teacherArrangeIds,boolean isMakeEx);
	List<NewGkTeacherPlan> findByArrayItemIdsWithMaster(String[] teacherArrangeIds, boolean isMakeEx);
	/**
	 * 
	 * @param arrayItemId
	 * @param ents
	 * @param divideId
	 * @param arrayId 修改排课特征时传入
	 * @return
	 */
	public String saveAddUpList(String arrayItemId,List<NewGkTeacherPlan> ents ,String divideId, String arrayId ,String unitId);

	void deleteByIdIn(String[] ids);

	List<NewGkTeacherPlan> findByArrayItemIdAndSubjectIdIn(String arrayItemId, String[] subjectIds, boolean isMakeEx);
	List<NewGkTeacherPlan> findByArrayItemIdAndSubjectIdInWithMaster(String arrayItemId, String[] subjectIds, boolean isMakeEx);
	/**
	 * 删除相应扩展信息
	 * @param arrayItemId
	 */
	void deleteByArrayItemId(String arrayItemId);

	void saveList(List<NewGkTeacherPlan> teacherPlanList, List<NewGkTeacherPlanEx> planExList);

	void saveImportPlan(String arrayItemId,String[] subjectIds, Map<String, Map<String, String>> subjectTeacherClazz);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subIds);
}
