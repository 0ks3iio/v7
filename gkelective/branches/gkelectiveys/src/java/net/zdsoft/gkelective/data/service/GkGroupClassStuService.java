package net.zdsoft.gkelective.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;


public interface GkGroupClassStuService extends BaseService<GkGroupClassStu, String>{

	/**
	 * 查询预排组合班级下学生
	 * @param groupClassIds
	 * @return
	 */
	public Map<String,List<String>> findByGroupClassIdIn(String... groupClassIds);

	public void deleteByGroupClassIdIn(String... groupClassIds);

	/**
	 * 先删除 再新增
	 * @param stuIds
	 * @param groupClassId
	 */
	public void saveStuList(Set<String> stuIds, String groupClassId);

	public void deleteStu(String[] groupClassIds, String[] stuId);

	/**
	 * 找组合班学生
	 * @param roundId 必填
	 * @param stuId 可为空
	 * @return
	 */
	public List<GkGroupClassStu> findGkGroupClassStuList(String roundId, String[] stuId);

}
