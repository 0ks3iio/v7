package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.dto.DyWeekCheckTable;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleUser;

/**
 * dy_week_check_role_user
 * @author 
 * 
 */
public interface DyWeekCheckRoleUserService extends BaseService<DyWeekCheckRoleUser, String>{
	/**
	 * 
	 * @param roleType
	 * @return
	 */
	public List<DyWeekCheckRoleUser> findByRoleType(String roleType);
	/**
	 * 判断用户在当前时间下所拥有维护项目的权限
	 * @param unitId
	 * @param userId
	 * @param acadyear TODO
	 * @param semester TODO
	 * @return
	 */
	public List<DyWeekCheckRoleUser> findByUserId(String unitId, String userId, String acadyear, String semester);
	/**
	 * 取总管理员
	 * @param unitId TODO
	 * @param roleType 01
	 * @param userId
	 * @return
	 */
	public DyWeekCheckRoleUser findByRoleTypeAndUser(String unitId,
			String roleType, String userId);
	/**
	 * 取某个单位所有总管理员
	 * @param unitId
	 * @return
	 */
	public List<DyWeekCheckRoleUser> findAdminByUnitId(String unitId);
	
	public List<DyWeekCheckRoleUser> findByRoleType(String unitId,
			String roleType, String acadyear, String semester);
	
	public void saveList(List<DyWeekCheckRoleUser> roleUserlist, String schoolId, String roleType, String acadyear, String semester, String gradeId, String section);
	public void saveClass(DyWeekCheckRoleUser roleUser);
	/**
	 * 查找某一天所有的值周干部
	 * @param unitId
	 * @param dutyDate
	 * @return
	 */
	public List<DyWeekCheckRoleUser> findCheckTeacher(String unitId,
			String dutyDate);

	/**
	 * 按照userid跟schoolid查找某个值周干部
	 * @param unitId
	 * @param dutyDate
	 * @return
	 */
	public List<DyWeekCheckRoleUser> findCheckTeacherByUserId(String unitId,String userId,
													  String dutyDate);
	/**
	 *保存值周干部
	 * @param unitId
	 * @param dutyDate
	 * @param roles
	 */
	public void saveCheckTeacher(String unitId, String dutyDate,
			List<DyWeekCheckRoleUser> roles);
	/**
	 * 获得值周表（值周干部/值周班）
	 * @param unitId
	 * @param acadyear
	 * @param se
	 * @param roleType
	 * @param roleId 角色id（userId 或classId）
	 * @return
	 */
	public List<DyWeekCheckTable> findByCheckTable(String unitId,
			String acadyear, String se, String roleType, String roleId);
	/**
	 * 导入值周班信息
	 * @param schoolId TODO
	 * @param acadyear TODO
	 * @param semester TODO
	 * @param sections
	 * @param dateInfos TODO
	 * @param datas [week,时间范围,section[0],section[1]...]
	 * @return
	 */
	public String roleClassImport(String schoolId, String acadyear, String semester, String sections, List<String[]> datas);
	/**
	 * 导入值班干部信息
	 * @param schoolId TODO
	 * @param acadyear TODO
	 * @param semester TODO
	 * @param sections
	 * @param datas [date,week,day,section[0],section[1]...]
	 * @return
	 */
	public String roleTeacherImport(String unitId, String acadyear,
			String semester, String sections, List<String[]> datas);
}