package net.zdsoft.gkelective.data.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.dto.GkArrangeGroupResultDto;
import net.zdsoft.gkelective.data.dto.GkGroupDto;
import net.zdsoft.gkelective.data.entity.GkGroupClass;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;

public interface GkGroupClassService extends BaseService<GkGroupClass, String>{
	
	/**
	 * 组合开班结果，展示用，含班级人数和科目、教师等名称显示
	 * @param arrangeId
	 * @return
	 */
	List<GkArrangeGroupResultDto> findGroupResultsByRoundsId(String roundsId);

	public List<GkGroupClass> saveAllEntitys(GkGroupClass... gkGroupClass);

	/**
	 * 查询该轮次下预排班级
	 * @param roundsId
	 * @param groupType(可为null)
	 * @return
	 */
	List<GkGroupDto> findGkGroupDtoByRoundsId(String roundsId,String groupType);
	
	boolean hasGkGroupByRoundsId(String roundsId,String groupType);
	
	/**
	 * 删除某轮次下预排信息
	 * @param roundsId
	 * @param groupType(可为null)
	 */
	public void deleteByRoundsId(String roundsId,String groupType);


	public List<GkGroupClass> findByRoundsId(String roundId);

	/**
	 * 
	 * @param subjectIds可为null
	 * @param roundsId
	 * @return
	 */
	public List<GkGroupClass> findGkGroupClassBySubjectIds(String subjectIds,String roundsId);

	/**
	 * 
	 * @param roundId
	 * @param subjectIds
	 * @param id 不等于的id   可为null
	 * @return
	 */
	public List<GkGroupClass> findGkGroupClssList(String roundsId, String subjectIds, String id);

	/**
	 * 删除组合班 同时删除组合下学生
	 * @param groupId
	 */
	public void deleteById(String... groupId);

	public void deleteBySubjectIds(String subjectIds,String roundsId);
	
	public GkGroupClass findById(String groupId);

	/**
	 * groupId 删除组合班 同时删除组合下学生  updateGroup 修改组合下学生
	 * @param groupId
	 * @param updateGroup
	 */
	public void update(Set<String> groupId, List<GkGroupClass> updateGroup);

	public List<GkGroupClass> findGkGroupClassList(String roundsId, String[] subjectIds);

	public void saveGroup(List<GkGroupClass> groupClass,
			List<GkGroupClassStu> groupClassStu);

	public List<GkGroupClass> findByRoundsIdType(String roundsId, String groupType);


}
