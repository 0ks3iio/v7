package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.entity.DiathesisOption;

import java.util.List;
import java.util.Map;

/**
 * @Author: panlf
 * @Date: 2019/3/29 9:41
 */
public interface DiathesisOptionService extends BaseService<DiathesisOption, String> {

	/**
	 * 查询选项集合
	 * @param unitId
	 * @param projectId
	 * @return
	 */
	List<DiathesisOption> findListByUnitIdAndProjectId(String unitId, String projectId);

	/**
	 * 查询选项键值对
	 * @param unitId
	 * @param projectId
	 * @return
	 */
	Map<String, String> findMapByUnitIdAndProjectId(String unitId, String projectId);

	Map<String, String> findMapByUnitIdAndStructureIdIn(String unitId, String[] structureIds);


	List<DiathesisOption> findByUnitId(String usingUnitId);

    List<DiathesisOption> findListByStructureIdIn(String[] structureIds);

    void deleteByProjectIdIn(List<String> projectIds);


    void deleteByIn(List<String> oldIds);

	List<DiathesisOption> findListByProjectIdIn(List<String> projectIds);
}
