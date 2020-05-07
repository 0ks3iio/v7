package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.Dept;

public interface DeptRemoteService extends BaseRemoteService<Dept, String> {
	
	/**
	 * 得到一个单位的所有部门
	 * @param unitId
	 * @return
	 */
	public String findByUnitId(String unitId);
	
	/**
	 * 根据父部门ID得到下属部门列表，根据排序号排序
	 * @param parentId
	 * @return
	 */
	public String findByParentId(String parentId);
	
	/**
	 * 取院系的直属部门
	 * @param instituteId
	 * @return
	 */
	public String findByInstituteId(String instituteId);
	/**
	 * 得到该单位该父部门的下级部门列表，根据排序号排列。
	 * @param unitId
	 * @param parentId
	 * @return
	 */
	public String findByUnitIdAndParentId(String unitId, String parentId);
	
	/**
	 * 
	 * @param parentId
	 * @param deptName
	 * @return
	 */
	public String findByParentIdAndDeptName(String parentId, String deptName);
	
	/**
	 * 分管校长
	 * @param unitId
	 * @param deputyHeadId
	 * @return
	 */
	public String findByUnitIdAndDeputyHeadId(String unitId, String deputyHeadId);
	
	/**
	 * 取校区下面的所有部门
	 * @param areaId
	 * @return
	 */
	public String findByAreaId(String areaId);
	
	/**
	 * 根据教师id判读是否这个部门下成员，如果是返回教研组id
	 * @param userId
	 * @return
	 */
	public String findByTeacherId(String userId);
	
	/**
	 * 根据分管领导id获取部门负责人信息
	 * @param unitId
	 * @param leaderId
	 * @return
	 */
	public String findByUnitIdAndLeaderId(String unitId, String leaderId);
	
	/**
	 * 根据部门编号获取单位信息
	 * @param unitId
	 * @param code
	 * @return
	 */
	public String findByUnitAndCode(String unitId, String code);
	
	/**
	 * 
	 * @param unitId
	 * @param deptName
	 * @return
	 */
	public String findByUnitIdAndDeptNameLike(String unitId, String deptName);

	/**
	 * 数组形式entitys参数，返回list的json数据
	 * @param entitys
	 * @return
	 */
	public String saveAllEntitys(String entitys);

	/**
	 * 根据单位ids取正常部门
	 * @param unitIds
	 * @return Map<String, List<Dept>>
	 */
	public String findByUnitIdMap(String[] unitIds);


}
