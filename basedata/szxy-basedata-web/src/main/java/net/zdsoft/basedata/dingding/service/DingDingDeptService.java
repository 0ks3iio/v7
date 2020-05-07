package net.zdsoft.basedata.dingding.service;

public interface DingDingDeptService {
	
	/**
	 * 从钉钉获取所有部门信息
	 */
	public void getDepts();
	
	/**
	 * 添加钉钉部门
	 * 
	 * @param content
	 */
	public String addDept(String unitId,String accessToken,String deptId,String deptName,String content);

	/**
	 * 修改钉钉部门
	 * 
	 * @param content
	 */
	public void updateDept(String unitId,String accessToken,String deptName,String content);

	/**
	 * 删除钉钉部门
	 * 
	 * @param deptId
	 */
	public void deleteDept(String unitId,String accessToken,String deptName,String deptId);
}
