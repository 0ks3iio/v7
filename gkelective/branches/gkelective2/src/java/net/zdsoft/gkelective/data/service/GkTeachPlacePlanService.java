package net.zdsoft.gkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.dto.GkTeachPlacePlanDto;
import net.zdsoft.gkelective.data.entity.GkBatch;
import net.zdsoft.gkelective.data.entity.GkTeachPlacePlan;

public interface GkTeachPlacePlanService extends BaseService<GkTeachPlacePlan, String>{

	/**
	 * 
	 * @param arrangeId
	 * @param isMakeRounds 是否需要组装轮次内容
	 * @return
	 */
	public List<GkTeachPlacePlan> findBySubjectArrangeId(String arrangeId,boolean isMakeRounds);

	/**
	 * 新增方案
	 * @param plan
	 */
	public void savePlan(GkTeachPlacePlan plan);

	/**
	 * 查询没有删除
	 * @param planId
	 * @param isMakeRounds
	 * @return
	 */
	public GkTeachPlacePlan findPlanById(String planId,boolean isMakeRounds);

	/**
	 * 保存教师 场地基础选择信息
	 * @param dto
	 * @param type 1:只保存 2:保存 并自动分配教师场地
	 */
	public void saveItem(GkTeachPlacePlanDto dto,String type);

	public void saveTeaAndCourseSch(List<TeachClass> clsList, String planId,
			String unitId);

	public void saveAllAndCourseSchedule(List<GkBatch> gkBatchList, String planId,
			String unitId);

	/**
	 * 查询没有删除的
	 * @param roundsId
	 * @return
	 */
	public List<GkTeachPlacePlan> findByRoundId(String roundsId);

	public void deleteByPlanId(String planId);

}
