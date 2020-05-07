package net.zdsoft.basedata.remote.service;

import java.util.List;

import net.zdsoft.basedata.entity.Teacher;

public interface TeacherRemoteService extends
		BaseRemoteService<Teacher, String> {
	public String findByUnitId(String unitId);

	public String findByDeptId(String deptId);

	public String findByNameCodeLike(String name, String code,
			String... unitIds);

	public String findByIdentityCardNo(String... cardNos);

	public String findByDeptIds(String... deptIds);

	public String findByInstituteId(String instituteId);

	public String findByUnitId(String unitId, String pagination);

	public String findByDeptId(String deptId, String pagination);

	public String findByUnitIdUserState(String unitId, Integer userState);

	public String findByDeptIdUserState(String deptId, Integer userState);

	public String findByWeaveUnitId(String weaveUnitId);

	/**
	 * 数组形式entitys参数，返回list的json数据
	 * 
	 * @param entitys
	 * @return
	 */
	public String saveAllEntitys(String entitys);

	/**
	 * 根据部门ids获取正常的教师
	 * 
	 * @param deptIds
	 * @return Map<String, List<Teacher>>
	 */
	public String findMapByDeptIdIn(String[] deptIds);

	String findByTeacherNameAndIdentityCard(String realName, String identityCard);

	public String findByTeacherNameAndIdentityCardWithNoUser(String realName,
			String identityCard);

	public String findByTeacherNameAndMobilePhone(String realName,
			String mobilePhone);

	public String findByNameLikeIdNotIn(String name, String unitId,
			String pagination, String... ids);

	/**
	 * 根据一卡通卡号获取教师信息
	 * 
	 * @param unitId
	 * @param cardNumber
	 * @return
	 */
	public String findByCardNumber(String unitId, String cardNumber);

	/**
	 * 更新教师一卡通号
	 * 
	 * @return
	 */
	public int[] updateCardNumber(List<String[]> techerCardList);

	/**
	 * @param array
	 * @return
	 */
	public String findByUnitIdIn(String[] unitIds);

	/**
	 * @param teacherName
	 * @return
	 */
	public String findByTeacherNameLike(String teacherName);
	
	//map<id,name>
	public String findPartByTeacher(String[] ids);
	
	
	/**
	 * @param
	 * @return
	 */
	public String findByUnitIdIn(String[] unitIds,String pagination);

	long countByUnitIds(String[] unitIds);
}
