package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.entity.CustomRole;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.dto.DiathesisChildProjectDto;
import net.zdsoft.diathesis.data.dto.DiathesisIdAndNameDto;
import net.zdsoft.diathesis.data.entity.DiathesisProject;
import net.zdsoft.diathesis.data.vo.DiathesisChildProjectVo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: panlf
 * @Date: 2019/3/29 9:41
 */
public interface DiathesisProjectService extends BaseService<DiathesisProject, String> {
    /**
     * 查找一级类目
     * @param unitId
     * @return
     */
    List<DiathesisIdAndNameDto> findTopProjectByUnitId(String unitId);

	/**
	 * 返回正在使用的项目的集合 ,不一定是本单位的,如果没有项目权限,那么就是返回上级单位的
	 * @param unitId
	 * @param projectTypes    DiathesisConstant.PROJECT_TOP
	 * @return
	 */
	List<DiathesisProject> findListByUnitIdAndProjectTypeIn(String unitId, String[] projectTypes);

	void deleteByProjectId(String projectId);

	/**
	 * 顶级类目修改 (替换单位下所有的 顶级类目)
	 * @param list
	 */
	void updateTopProjects(List<DiathesisIdAndNameDto> list, String unitId,String realName);

	/**
	 * 返回子级项目(不包括写实记录)
	 * @param parentId
	 * @param usingUnitId
	 * @param realName
	 * @return
	 */
	List<DiathesisChildProjectDto> findChildProjectByParentIdAndUnitId(String parentId, String unitId, String usingUnitId, String realName);

	Optional<DiathesisProject> findProjectById(String projectId);

	/**
	 * 返回写实记录的 id和name集合
	 * @param parentId
	 * @return
	 */
	List<DiathesisIdAndNameDto> findRecordsByParentIdAndUnitId(String parentId,String unitId);

	/**
	 * 初始化单位
	 * @param unitId
	 */
	void addAutoSetting(String unitId, String realName);
	/*void addSetting(String unitId, String realName);
*/
	/**
	 * 保存二级,三级项目设置信息
	 *
	 */
	void saveChildProjects(DiathesisChildProjectVo diathesisChildProjectVo, String realName);

	void updateRecords(List<DiathesisIdAndNameDto> diathesisIdAndNameDtoList, String unitId, String realName,String parentId);


//	/**
//	 * 初始化写实记录
//	 * @param unitId
//	 */
//	void autoRecordProject(String unitId, String ownerId, String realName);


	void autoCustonRole(String unitId);

	CustomRole createNewCustomRole(String unitId, String roleName, String roleCode, String orderId);

//	/**
//	 * 删除单位下 auditor_type 和input_types字段中的某一个角色
//	 * @param unitId
//	 * @param roleCode
//	 */
//	void deleteRoleByUnitIdAndRoleCode(String unitId, String roleCode);

	/**
	 * 没有权限的单位只能修改 2级类目的评价人  和一级项目的 评价占比
	 * @param diathesisChildProjectVo
	 * @param realName
	 */
//	void saveUnAuthorChildProjects(DiathesisChildProjectVo diathesisChildProjectVo, String realName);

	/**
	 * 初始化 二级，三级 项目
	 * @param unitId
	 * @param parentUnitId
	 * @param realName
	 */
//    void autoChildProject(String unitId, String parentUnitId, String realName);

	/**
	 * 查看角色是否在使用中
	 * @param unitId
	 * @param roleCode
	 * @return
	 */
	//Integer countByUnitIdAndLikeRoleCode(String unitId, String roleCode);

	/**
	 * 返回本单位的项目信息 (不管有没有在使用)
	 * @return
	 */
//	List<DiathesisProject> findMyProjectByUnitAndTypeIn(String unitId,String[] types);

    void updateRoleUser(String roleId, List<String> userIds);

	Integer countTopProjectByUnitId(String unitId);

//	void deleteByUnitIdAndParentIdIn(String unitId, List<String> topIds);

	/**
	 *
	 */
	List<DiathesisProject> findByUnitIdAndParentIdIn(String unitId, List<String> topIds);

	void deleteByIdIn(List<String> ids);

    Map<String, Integer> countTopProjectMap( List<String> topProjectIds, String unitId,String projectType);
    
    List<DiathesisProject> findByUnitIdAndProjectTypeIn(String unitId,String[] projectTypes);

//    Integer[] findProjectEvaluationNumByUnitId(String unitId);
}
