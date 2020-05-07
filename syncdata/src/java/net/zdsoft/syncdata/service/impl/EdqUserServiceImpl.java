package net.zdsoft.syncdata.service.impl;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.syncdata.constant.JledqConstant;
import net.zdsoft.syncdata.dto.ResultDto;
import net.zdsoft.syncdata.entity.EdqUser;
import net.zdsoft.syncdata.service.EdqUserService;
import net.zdsoft.syncdata.util.JledqSyncDataUtil;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * 
 * @author weixh
 * @since 2017年11月30日 上午11:11:03
 */
@Service("edqUserService")
@Lazy(true)
public class EdqUserServiceImpl implements EdqUserService {
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
//	@Autowired
//	private FamilyRemoteService familyRemoteService;
	
	public ResultDto saveEdqUser(EdqUser eu) {
		ResultDto dto = new ResultDto(JledqConstant.STATUS_SUC, "非教师家长学生账号暂不做处理！");
		try {
			if(eu.getOperationType() != JledqConstant.OPERATION_ADD){
				return new ResultDto(JledqConstant.STATUS_SUC, "非新增操作的数据暂不做处理！");
			}
			User us = User.dc(userRemoteService.findByUsername(eu.getYhzh()));
			if(us != null) {
				return new ResultDto(JledqConstant.STATUS_FAIL, "用户名["+eu.getYhzh()+"]对应的用户已存在，无法新增！");
			}
			if(StringUtils.isEmpty(eu.getZzdm()) || eu.getYhlx() < 1) {
				dto.setMsg("没有用户所属组织代码或无效的用户类型！");
				return dto;
			}
			switch(eu.getYhlx()) {
			case JledqConstant.USER_YHLX_ADMIN : {
				return dto;
			}
			case JledqConstant.USER_YHLX_DISTRICT : {
				return dto;
			}
			case JledqConstant.USER_YHLX_SCH : {
				return dealTea(eu);
			}
			case JledqConstant.USER_YHLX_EDU : {
				return dealTea(eu);
			}
			case JledqConstant.USER_YHLX_TEA : {
				return dealTea(eu);
			}
			case JledqConstant.USER_YHLX_STU : {
				return dealStudent(eu);
			}
			case JledqConstant.USER_YHLX_FAM : {
				return dealFam(eu);
			}
			default : {
				dto.setMsg("");
				return dto;
			}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			dto.setMsg("数据同步失败："+e.getMessage());
			dto.setStatus(JledqConstant.STATUS_FAIL);
		}
		
		return dto;
//		if(us == null || us.getIsDeleted() == 1){
//			return new ResultDto(JledqSyncDataUtil.STATUS_FAIL, "该用户不存在或已被软删！");
//		}
//		if(us.getUserState() == null){
//			us.setUserState(User.USER_MARK_NORMAL);
//		}
//		if(us.getUserState() != User.USER_MARK_NORMAL){
//			us.setUserState(User.USER_MARK_LOCK);
//		}
//		int[] zts = {JledqSyncDataUtil.USER_ZT_NORMAL, JledqSyncDataUtil.USER_ZT_UNNORMAL};
//		if(ArrayUtils.contains(zts, eu.getZt()) && us.getUserState() != eu.getZt()){
//			userRemoteService.updateStateByUsername(eu.getZt(), us.getUsername());
//		}
//		userRemoteService.updateUser(us.getId(), eu.getYhm(), 
//					us.getSex(), eu.getSjhm(), us.getBirthday());
	}
	
	/**
	 * 处理单位、部门数据
	 * @param eu
	 */
	private void dealOrg(EdqUser eu) {
		School sch = SUtils.dc(schoolRemoteService.findByCode(eu.getZzdm()), School.class);
		if(sch != null) {
			eu.setUnitId(sch.getId());
			if(eu.getYhlx() != JledqConstant.USER_YHLX_STU
					&& eu.getYhlx() != JledqConstant.USER_YHLX_FAM) {
				List<Dept> dps = SUtils.dt(deptRemoteService.findByUnitId(eu.getUnitId()),
						new TR<List<Dept>>() {});
				if(CollectionUtils.isNotEmpty(dps)) {
					eu.setDeptId(dps.get(0).getId());
				} else {
					eu.setDeptId(BaseConstants.ZERO_GUID);
				}
			}
		} else if(eu.getYhlx() == JledqConstant.USER_YHLX_EDU
				|| eu.getYhlx() == JledqConstant.USER_YHLX_TEA){
			eu.setUnitId(JledqSyncDataUtil.getEdqEduUnitId());
			Dept dp = SUtils.dc(deptRemoteService.findByUnitAndCode(JledqSyncDataUtil.getEdqEduUnitId(), 
							org.apache.commons.lang3.StringUtils.right(eu.getZzdm(), 6)), Dept.class);
			if(dp == null) {
				List<Dept> dps = SUtils.dt(deptRemoteService.findByUnitId(JledqSyncDataUtil.getEdqEduUnitId()),
						new TR<List<Dept>>() {});
				if(CollectionUtils.isNotEmpty(dps)) {
					dp = dps.get(0);
				}
			}
			if(dp != null) {
				eu.setDeptId(dp.getId());
			} else {
				eu.setDeptId(BaseConstants.ZERO_GUID);
			}
		} else {
			throw new RuntimeException("用户对应的所属组织机构不存在！");
		}
		
	}
	
	private ResultDto dealStudent(EdqUser eu) {
		ResultDto dto = new ResultDto(JledqConstant.STATUS_SUC, "");
		dealOrg(eu);
		Student ss = new Student();
		List<Clazz> clss = Clazz.dt(classRemoteService.findBySchoolId(eu.getUnitId()));
		if(CollectionUtils.isNotEmpty(clss)) {
			ss.setClassId(clss.get(0).getId());
			ss.setEnrollYear(clss.get(0).getAcadyear());
		} else {
			ss.setClassId(BaseConstants.ZERO_GUID);
		}
		ss.setSchoolId(eu.getUnitId());
		ss.setSex(0);
		ss.setStudentName(eu.getYhm());
		ss.setId(UuidUtils.generateUuid());
		ss.setMobilePhone(eu.getSjhm());
		ss.setIsLeaveSchool(0);
		ss.setEventSource(0);
		ss.setIsDeleted(0);
		ss.setCreationTime(new Date());
		ss.setModifyTime(ss.getCreationTime());
		studentRemoteService.saveAllEntitys(SUtils.s(new Student[] {ss}));
		
		User user = new User();
		getUser(user, eu);
		user.setOwnerId(ss.getId());
		userRemoteService.saveUser(user);
		return dto;
	}
	
	private ResultDto dealFam(EdqUser eu) {
		ResultDto dto = new ResultDto(JledqConstant.STATUS_SUC, "家长账号暂不处理！");
//		dealOrg(eu);
		//TODO
		return dto;
	}
	
	private ResultDto dealTea(EdqUser eu) {
		dealOrg(eu);
		ResultDto dto = new ResultDto(JledqConstant.STATUS_SUC, "");
		Teacher teacher = new Teacher();
		teacher.setTeacherName(eu.getYhm());
		teacher.setTeacherCode("1");
		teacher.setDisplayOrder(eu.getPx());
		teacher.setMobilePhone(eu.getSjhm());
		teacher.setId(UuidUtils.generateUuid());
		teacher.setUnitId(eu.getUnitId());
		teacher.setSex(0);
		teacher.setDeptId(StringUtils.isEmpty(eu.getDeptId())?BaseConstants.ZERO_GUID:eu.getDeptId());
		teacher.setCreationTime(new Date());
		teacher.setModifyTime(teacher.getCreationTime());
		teacher.setIsDeleted(0);
		teacher.setEventSource(0);
		teacher.setIncumbencySign("11");
		teacherRemoteService.saveAllEntitys(SUtils.s(new Teacher[] {teacher}));
		User user = new User();
		getUser(user, eu);
		user.setOwnerId(teacher.getId());
		userRemoteService.saveUser(user);
		return dto;
	}
	
	private void getUser(User user, EdqUser eu) {
		eu.toUser(user);
		if(StringUtils.isBlank(user.getId())) {
			user.setId(UuidUtils.generateUuid());
			user.setAccountId(UuidUtils.generateUuid());
			user.setCreationTime(new Date());
			PWD pwd = new PWD(JledqSyncDataUtil.getDefaultPwd());
			String passwordInit = pwd.encode();
			user.setPassword(passwordInit);
		}
		user.setModifyTime(new Date());
		user.setEventSource(0);
		user.setIsDeleted(0);
	}
}
