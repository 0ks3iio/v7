package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;

public interface NewGkTeacherPlanExService extends BaseService<NewGkTeacherPlanEx, String>{
	void deleteByTeacherPlanIdIn(String[] teacherPlanIds);
	
	void deleteByIdIn(String[]id);
	
	List<String> findByTeacherPlanId(String teacherPlanId);
	
	List<NewGkTeacherPlanEx> findByTeacherId(String arrayItemId, String[] teacherId);
	/**
	 * 
	 * @param unitId
	 * @param arrayId 仅在修改排课特征时传入，用于更新预排课表
	 * @param subjectId
	 * @param itemId
	 * @param planExList
	 */
	void saveExs(String unitId, String arrayId, String subjectId, String itemId, List<NewGkTeacherPlanEx> planExList);

    // Basedata Sync Method
    void deleteByTeacherIds(String... teacherIds);

    // Basedata Sync Method
    void deleteByClassIds(String... classIds);
}
