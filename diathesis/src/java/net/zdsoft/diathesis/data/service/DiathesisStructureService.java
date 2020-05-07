package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.entity.DiathesisStructure;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/3/29 9:41
 */
public interface DiathesisStructureService extends BaseService<DiathesisStructure, String> {

	/**
	 * 查询项目下自定义字段集合
	 * @param projectId
	 * @return
	 */
	List<DiathesisStructure> findListByProjectId(String projectId);
	
	List<DiathesisStructure> findListByProjectIdIn(String[] projectIds);

	void deleteByProjectIdIn(List<String> projectIds);

    void deleteByIdIn(List<String> ids);

	List<DiathesisStructure> findListBySingleTypeAndProjectTypeIn(List<String> projectIds);
}
