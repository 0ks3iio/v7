package net.zdsoft.basedata.dingding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.dingding.entity.DdSyncdataLog;
import net.zdsoft.basedata.dingding.entity.DdUnitOpen;
import net.zdsoft.basedata.dingding.entity.DingDingDept;
import net.zdsoft.basedata.dingding.entity.DingDingUser;
import net.zdsoft.basedata.dingding.service.DingDingDeptService;
import net.zdsoft.basedata.dingding.service.DingDingSyncDataService;
import net.zdsoft.basedata.dingding.service.DingDingSyncdataLogService;
import net.zdsoft.basedata.dingding.service.DingDingUnitOpenService;
import net.zdsoft.basedata.dingding.service.DingDingUserService;
import net.zdsoft.basedata.dingding.utils.DingDingUtils;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.entity.UserDept;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dingDingSyncDataService")
public class DingDingSyncDataServiceImpl implements DingDingSyncDataService {
	private static Logger log = Logger
			.getLogger(DingDingSyncDataServiceImpl.class);
	@Autowired
	DeptService deptService;

	@Autowired
	UserService userService;

	@Autowired
	DingDingDeptService dingDingDeptService;

	@Autowired
	DingDingUserService dingDingUserService;

	@Autowired
	DingDingSyncdataLogService dingDingSyncdataLogService;

	@Autowired
	DingDingUnitOpenService dingDingUnitOpenService;

	// 同步数据到钉钉 根据更新时间
	public void dealData2DingDingByUpdateTime() {
		List<DdUnitOpen> unitList = dingDingUnitOpenService.findByState(1);

		for (DdUnitOpen unit : unitList) {

			String accessToken = DingDingUtils.getAccessToken(unit.getCorpId(),
					unit.getCorpSecret());

			List<Dept> deptList = deptService.findByUnitIdAndModifyTime(
					unit.getUnitId(), DateUtils.addMinute(new Date(), -30));
			List<User> userList = userService.findByUnitIdAndModifyTime(
					unit.getUnitId(), DateUtils.addMinute(new Date(), -30));
			List<Dept> deptDeleteList = new ArrayList<Dept>();
			List<Dept> deptOtherList = new ArrayList<Dept>();
			List<User> userDeleteList = new ArrayList<User>();
			List<User> userOtherList = new ArrayList<User>();
			for (Dept dept : deptList) {
				if (dept.getIsDeleted() == 1) {
					deptDeleteList.add(dept);
				} else {
					deptOtherList.add(dept);
				}
			}
			for (User user : userList) {
				if (user.getIsDeleted() == 1) {
					userDeleteList.add(user);
				} else {
					userOtherList.add(user);
				}
			}
			// 删除数据的要先删除用户再删除部门
			dealUsers(unit.getUnitId(), accessToken, userDeleteList,
					new HashMap<String, String>());
			dealDepts(unit.getUnitId(), accessToken, deptDeleteList);

			Map<String, String> dingdingIdMap = dealDepts(unit.getUnitId(),
					accessToken, deptOtherList);
			dealUsers(unit.getUnitId(), accessToken, userOtherList,
					dingdingIdMap);
		}
	}

	// 同步数据到钉钉 所有数据
	public void dealData2DingDing() {
		List<DdUnitOpen> unitList = dingDingUnitOpenService.findByState(1);

		for (DdUnitOpen unit : unitList) {
			String accessToken = DingDingUtils.getAccessToken(unit.getCorpId(),
					unit.getCorpSecret());
			List<Dept> deptList = deptService.findAllByUnitId(unit.getUnitId());
			List<User> userList = userService.findAllByUnitId(unit.getUnitId());
			// 根据更新时间获取最新的数据
			// List<Dept> deptList = deptService
			// .findByIdIn(new String[] { "402880675122DD4F0151235D8A7900A0" });
			//
			// deptList=new ArrayList<Dept>();
			// userList = userService
			// .findByIds(new String[] { "40288092531102A70153111AD8B0000F" });
			// 组织数据
			List<Dept> deptDeleteList = new ArrayList<Dept>();
			List<Dept> deptOtherList = new ArrayList<Dept>();
			List<User> userDeleteList = new ArrayList<User>();
			List<User> userOtherList = new ArrayList<User>();
			for (Dept dept : deptList) {
				if (dept.getIsDeleted() == 1) {
					deptDeleteList.add(dept);
				} else {
					deptOtherList.add(dept);
				}
			}
			for (User user : userList) {
				if (user.getIsDeleted() == 1) {
					userDeleteList.add(user);
				} else {
					userOtherList.add(user);
				}
			}
			// 删除数据的要先删除用户再删除部门
			dealUsers(unit.getUnitId(), accessToken, userDeleteList,
					new HashMap<String, String>());
			dealDepts(unit.getUnitId(), accessToken, deptDeleteList);

			Map<String, String> dingdingIdMap = dealDepts(unit.getUnitId(),
					accessToken, deptOtherList);
			dealUsers(unit.getUnitId(), accessToken, userOtherList,
					dingdingIdMap);
		}

	}

	private Map<String, String> dealDepts(String unitId, String accessToken,
			List<Dept> deptList) {
		List<Dept> deptAllList = deptService.findAllByUnitId(unitId);
		Map<String, Dept> deptMap = new HashMap<String, Dept>();
		for (Dept dept : deptAllList) {
			deptMap.put(dept.getId(), dept);
		}
		Map<String, String> dingdingIdMap = new HashMap<String, String>();
		for (Dept dept : deptList) {
			if (dept.getIsDeleted() == 1) {
				if (StringUtils.isNotBlank(dept.getDingdingId())) {
					dingDingDeptService.deleteDept(unitId, accessToken,
							dept.getDeptName(), dept.getDingdingId());
				}
			} else {
				if (StringUtils.isNotBlank(dept.getDingdingId())) {
					DingDingDept ddDept = new DingDingDept();
					ddDept.setId(dept.getDingdingId());
					ddDept.setName(dept.getDeptName());
					if (Constant.GUID_ZERO.equals(dept.getParentId())) {
						ddDept.setParentid("1");
					} else {
						Dept parentDept = deptMap.get(dept.getParentId());
						if (parentDept != null
								&& StringUtils.isNotBlank(parentDept
										.getDingdingId())) {
							ddDept.setParentid(parentDept.getDingdingId());
						} else {
							String ddId = dingdingIdMap.get(dept.getParentId());
							if (StringUtils.isNotBlank(ddId)) {
								ddDept.setParentid(ddId);
							}
						}
					}
					dingDingDeptService.updateDept(unitId, accessToken,
							dept.getDeptName(), SUtils.s(ddDept));
				} else {
					DingDingDept ddDept = new DingDingDept();
					ddDept.setName(dept.getDeptName());
					if (Constant.GUID_ZERO.equals(dept.getParentId())) {
						ddDept.setParentid("1");
					} else {
						Dept parentDept = deptMap.get(dept.getParentId());
						if (parentDept != null
								&& StringUtils.isNotBlank(parentDept
										.getDingdingId())) {
							ddDept.setParentid(parentDept.getDingdingId());
						} else {
							String ddId = dingdingIdMap.get(dept.getParentId());
							if (StringUtils.isNotBlank(ddId)) {
								ddDept.setParentid(ddId);
							}
						}
					}
					String dingdingId = dingDingDeptService.addDept(unitId,
							accessToken, dept.getId(), dept.getDeptName(),
							SUtils.s(ddDept));
					if (StringUtils.isNotBlank(dingdingId)) {
						dingdingIdMap.put(dept.getId(), dingdingId);
					}
				}
			}

		}
		return dingdingIdMap;
	}

	private void dealUsers(String unitId, String accessToken,
			List<User> userList, Map<String, String> dingdingIdMap) {
		List<Dept> deptList = deptService.findByUnitId(unitId);
		Map<String, Dept> deptMap = new HashMap<String, Dept>();
		for (Dept dept : deptList) {
			deptMap.put(dept.getId(), dept);
		}

		for (User user : userList) {
			DdSyncdataLog dataLog = new DdSyncdataLog();
			dataLog.setUnitId(unitId);
			dataLog.setDataType("user");
			dataLog.setObjectName(user.getRealName());
			dataLog.setHandlerType("关键字段缺失");
			if (StringUtils.isBlank(user.getMobilePhone())) {
				log.error("－－－－－－用户[" + user.getRealName() + "]不存在手机号，跳过－－－－－－");
				dataLog.setResult(0);
				dataLog.setRemark("手机号不存在");
				dingDingSyncdataLogService.insertLogs(dataLog);
				continue;
			}
			Dept dept = deptMap.get(user.getDeptId());
			if (dept == null && user.getIsDeleted() != 1) {
				log.error("－－－－－－用户[" + user.getRealName() + "]部门不存在，跳过－－－－－－");
				dataLog.setResult(0);
				dataLog.setRemark("部门不存在");
				dingDingSyncdataLogService.insertLogs(dataLog);
				continue;
			}
			if (user.getIsDeleted() == 1) {
				if (StringUtils.isNotBlank(user.getDingdingId())) {
					dingDingUserService.deleteUser(unitId, accessToken,
							user.getRealName(), user.getDingdingId());
				}
			} else {
				if (StringUtils.isNotBlank(user.getDingdingId())) {
					DingDingUser ddUser = new DingDingUser();
					ddUser.setUserid(user.getDingdingId());
					ddUser.setName(user.getRealName());
					ddUser.setMobile(user.getMobilePhone());
					List<String> departments = new ArrayList<String>();
					String dingdingId = dept.getDingdingId();
					if (StringUtils.isBlank(dingdingId)) {
						dingdingId = dingdingIdMap.get(dept.getId());
					}
					departments.add(dingdingId);
					// 相关部门
					List<UserDept> userDeptList = user.getDeptList();
					if (CollectionUtils.isNotEmpty(userDeptList)) {
						for (UserDept userDept : userDeptList) {
							Dept tempDept = deptMap.get(userDept.getDeptId());
							if (tempDept != null) {
								String tempDingdingId = tempDept
										.getDingdingId();
								if (StringUtils.isBlank(tempDingdingId)) {
									tempDingdingId = dingdingIdMap.get(tempDept
											.getId());
								}
								departments.add(tempDingdingId);
							}
						}
					}

					ddUser.setDepartment(departments);
					dingDingUserService.updateUser(unitId, accessToken,
							user.getRealName(), SUtils.s(ddUser));
				} else {
					DingDingUser ddUser = new DingDingUser();
					ddUser.setUserid(UuidUtils.generateUuid());
					ddUser.setName(user.getRealName());
					ddUser.setMobile(user.getMobilePhone());
					List<String> departments = new ArrayList<String>();
					String dingdingId = dept.getDingdingId();
					if (StringUtils.isBlank(dingdingId)) {
						dingdingId = dingdingIdMap.get(dept.getId());
					}
					departments.add(dingdingId);
					// 相关部门
					List<UserDept> userDeptList = user.getDeptList();
					if (CollectionUtils.isNotEmpty(userDeptList)) {
						for (UserDept userDept : userDeptList) {
							Dept tempDept = deptMap.get(userDept.getDeptId());
							if (tempDept != null) {
								String tempDingdingId = tempDept
										.getDingdingId();
								if (StringUtils.isBlank(tempDingdingId)) {
									tempDingdingId = dingdingIdMap.get(tempDept
											.getId());
								}
								departments.add(tempDingdingId);
							}
						}
					}
					ddUser.setDepartment(departments);
					dingDingUserService.addUser(unitId, accessToken,
							user.getId(), user.getRealName(), SUtils.s(ddUser));
				}
			}
		}
	}

	@Override
	public void getDepts() {
		dingDingDeptService.getDepts();
	}

	@Override
	public void getUsers() {
		dingDingUserService.getUsers();
	}
}
