package net.zdsoft.gkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.dto.GkConditionDto;
import net.zdsoft.gkelective.data.entity.GkCondition;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;

public interface GkConditionService extends BaseService<GkCondition, String>{
	/**
	 * 保存开班设置
	 * @param dtos
	 * @param type TODO
	 */
	public void saveConditions(List<GkConditionDto> dtos, String type);
	
	/**
	 * 删除安排下的所有开班数据
	 * @param roundsId
	 * @param type
	 */
	public void deleteAllConditions(String roundsId, String type);
	
	/**
	 * 删除
	 * @param ids
	 */
	public void deleteByIds(String... ids);
	
	/**
	 * 根据选课轮次id和开班类型查找开班条件
	 * @param roundsId
	 * @param type
	 * @param openClassType TODO
	 * @return db不存在数据时，根据学生选课结果封装默认数据，单科数据中选考、学考放在一个list中
	 */
	public List<GkConditionDto> findByRoundsIdAndTypeWithDefault(String roundsId, String type, String openClassType);
	
	/**
	 * 根据学生选课数据获得学生选课组合（包含不走班课程）
	 * @param gkId
	 * @return
	 */
	public List<GkConditionDto> findGroupByGkResult(String gkId,String[] stuids);
	
	/**
	 * 根据条件查询结果
	 * @param roundsId(不能为空)
	 * @param type
	 * @return
	 */
	public List<GkCondition> findByGkConditions(String roundsId,
			String type);
	/**
	 * 组装组合
	 * @param roundsId
	 * @param type
	 * @return
	 */
	public List<GkConditionDto> findByGkConditionDtos(String roundsId,
			String type);
	/**
	 * 同时删除开班信息和组合信息
	 * @param roundsId
	 */
	public void deleteByRoundsId(String roundsId);

	/**
	 * 删除安排下的所有开班数据  系统锁定功能
	 * @param roundsId
	 */
	public void deleteAllArrange(String roundsId,GkSubjectArrange gkArrange);
}
