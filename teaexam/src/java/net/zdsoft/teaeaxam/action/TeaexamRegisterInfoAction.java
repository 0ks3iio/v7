package net.zdsoft.teaeaxam.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.remote.utils.PJHeadUrlUtils;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.teaeaxam.constant.TeaexamConstant;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;
import net.zdsoft.teaeaxam.entity.TeaexamRegisterInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.service.TeaexamInfoService;
import net.zdsoft.teaeaxam.service.TeaexamRegisterInfoService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectService;

@Controller
@RequestMapping("/teaexam")
public class TeaexamRegisterInfoAction extends TeaExamBaseAction{
	@Autowired
	private TeaexamInfoService teaexamInfoService;
	@Autowired
	private TeaexamSubjectService teaexamSubjectService;
	@Autowired
	private TeaexamRegisterInfoService teaexamRegisterInfoService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;

	@RequestMapping("/registerInfo/index/page")
	public String pageIndex(ModelMap map){
		return "/teaexam/registerInfo/registerInfoTab.ftl";
	}
	
	@RequestMapping("/registerInfo/beginRegister")
	public String beginRegister(HttpServletRequest req, String index, ModelMap map){
		int year = NumberUtils.toInt(req.getParameter("year")); 
		int type = NumberUtils.toInt(req.getParameter("type"));;
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		Date date = new Date();
		List<TeaexamInfo> teaexamInfoListTemp = new ArrayList<TeaexamInfo>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		if("1".equals(index)){
			teaexamInfoListTemp = teaexamInfoService.findByRegisterTime(dateString);
		}else{
			teaexamInfoListTemp = teaexamInfoService.findByRegisterEnd(year, type, dateString);
		}
	    String schoolId = getLoginInfo().getUnitId();
	    List<TeaexamInfo> teaexamInfoList = new ArrayList<TeaexamInfo>();
	    Set<String> examIdSet = new HashSet<String>();
	    for(TeaexamInfo exam : teaexamInfoListTemp){
	    	if(exam.getSchoolIds().contains(schoolId)){
	    		teaexamInfoList.add(exam);
	    		examIdSet.add(exam.getId());
	    	}
	    }
	    List<TeaexamRegisterInfo> registerInfoList = new ArrayList<TeaexamRegisterInfo>();
	    List<TeaexamSubject> subjectList = new ArrayList<TeaexamSubject>();
	    if(CollectionUtils.isNotEmpty(examIdSet)){
	    	subjectList = teaexamSubjectService.findByExamIds(examIdSet.toArray(new String[0]));
	    	registerInfoList = teaexamRegisterInfoService.findByTeacherIdAndExamIdIn(getLoginInfo().getOwnerId(), examIdSet.toArray(new String[0]));
	    }
		map.put("year", year);
		map.put("type", type);
	    map.put("subjectList", subjectList);
	    map.put("teaexamInfoList", teaexamInfoList);
	    map.put("registerInfoList", registerInfoList);
	    map.put("index", index);
		return "/teaexam/registerInfo/registerInfoBeginList.ftl";
	}
		
	@ResponseBody
	@RequestMapping("/registerInfo/doRegister")
    @ControllerInfo(value = "")
	public String doRegister(String examId, String subId, String id, int status){		
		try{
			if(status == 0){				
				teaexamRegisterInfoService.delete(id);
			}else{
				TeaexamRegisterInfo registerInfo = new TeaexamRegisterInfo();
				if(StringUtils.isNotBlank(id)){
					registerInfo.setId(id);
				} else {
					registerInfo.setId(UuidUtils.generateUuid());
				}
				
				List<TeaexamSubject> subList = teaexamSubjectService.findByExamIds(new String[]{examId});
				Map<String, TeaexamSubject> subMap = new HashMap<String, TeaexamSubject>();
				for(TeaexamSubject sub : subList){
					subMap.put(sub.getId(), sub);
				}
				if(status == 1){
					List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findByTeacherIdAndExamIdIn(getLoginInfo().getOwnerId(), new String[]{examId});
				    for(TeaexamRegisterInfo reg : regList){
				    	TeaexamSubject item1 = subMap.get(reg.getSubjectInfoId());
				    	TeaexamSubject item2 = subMap.get(subId);
				    	if((reg.getStatus()==1 || reg.getStatus()==2) 
				    			&& !(item1.getStartTime().getTime() >= item2.getEndTime().getTime()
				    			|| item1.getEndTime().getTime() <= item2.getStartTime().getTime())){
				    		return error("存在已报名的科目或报名成功的科目与该科目的考试时间相同！");
				    	}
				    }
				}
				
				registerInfo.setExamId(examId);
				registerInfo.setSubjectInfoId(subId);
				registerInfo.setSchoolId(getLoginInfo().getUnitId());
				registerInfo.setTeacherId(getLoginInfo().getOwnerId());
				registerInfo.setTeaUserId(getLoginInfo().getUserId());
				registerInfo.setStatus(status);
				registerInfo.setModifyTime(new Date());
				registerInfo.setCreationTime(new Date());
				teaexamRegisterInfoService.save(registerInfo);
			}
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@RequestMapping("/registerAudit/index/page")
	public String registerAuditTab(ModelMap map){
		return "/teaexam/registerInfo/registerAuditTab.ftl";
	}
	
	@RequestMapping("/registerAudit/auditingList")
	public String auditingList(String examId, ModelMap map,HttpServletRequest request){
		try {
			int infoType = NumberUtils.toInt(request.getParameter("infoType"));
			map.put("infoType", infoType);
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(date);
			List<TeaexamInfo> teaexamInfoListTemp = teaexamInfoService.findByRegisterTimeAndUnitId(getLoginInfo().getUnitId(),infoType, dateString);
			List<String> examIdSet = new ArrayList<>();
			if(StringUtils.isNotBlank(examId)){
				examIdSet.add(examId);
			}else{
				if(CollectionUtils.isNotEmpty(teaexamInfoListTemp)){
					examIdSet.add(teaexamInfoListTemp.get(0).getId());
				}
			}
			Pagination page = createPagination();
			List<TeaexamRegisterInfo> registerInfoList = new ArrayList<TeaexamRegisterInfo>();
			if(CollectionUtils.isNotEmpty(examIdSet)){
				registerInfoList = teaexamRegisterInfoService.findBy(examIdSet.get(0), null, null, TeaexamConstant.STATUS_AUDITTING + "", null, null, page);//findByStatusAndExamIdIn(1,examIdSet.toArray(new String[0]));
				Set<String> teaIdSet = new HashSet<String>();
				Set<String> unitIdSet = new HashSet<String>();
				Set<String> subIdSet = new HashSet<String>();
				Set<String> userIdSet = new HashSet<String>();
				for(TeaexamRegisterInfo reg : registerInfoList){
					teaIdSet.add(reg.getTeacherId());
					unitIdSet.add(reg.getSchoolId());
					subIdSet.add(reg.getSubjectInfoId());
					userIdSet.add(reg.getTeaUserId());
				}
				Map<String, String> userMap = new HashMap<String, String>();
				if(CollectionUtils.isNotEmpty(userIdSet)){
					List<User> userList = SUtils.dt(userRemoteService.findListByIds(userIdSet.toArray(new String[0])), new TR<List<User>>(){});
					for(User u : userList){
						userMap.put(u.getId(), u.getAvatarUrl());
					}
				}
				
				List<TeaexamSubject> subList = new ArrayList<TeaexamSubject>();
				Map<String, String> subNameMap = new HashMap<String, String>();
				if(CollectionUtils.isNotEmpty(subIdSet)){
					subList = teaexamSubjectService.findListByIds(subIdSet.toArray(new String[0]));
					for(TeaexamSubject sub : subList){
						String subctionName = "";
						if(sub.getSection()==1){
							subctionName = "小学";
						} else if (0 == sub.getSection()) {
							subctionName = "学前";
						}else if(sub.getSection()==2){
							subctionName = "初中";
						}else if(sub.getSection()==3){
							subctionName = "高中";
						}
						subNameMap.put(sub.getId(), sub.getSubjectName()+"("+subctionName+")");
					}
				}
				List<Unit> unitList = new ArrayList<Unit>();
				if(CollectionUtils.isNotEmpty(unitIdSet)){
					unitList = SUtils.dt(unitRemoteService.findListByIds(unitIdSet.toArray(new String[0])), new TR<List<Unit>>(){});
				}
				Map<String, String> teacherUnitNameMap = new HashMap<String, String>();
				for(Unit unit : unitList){
					teacherUnitNameMap.put(unit.getId(), unit.getUnitName());
				}
				Map<String, McodeDetail> sexMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XB"),new TypeReference<Map<String,McodeDetail>>(){});
				Map<String, McodeDetail> mzMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-MZ"),new TypeReference<Map<String,McodeDetail>>(){});
				if(CollectionUtils.isNotEmpty(teaIdSet)){				
					List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teaIdSet.toArray(new String[0])), new TR<List<Teacher>>(){}); 
				    Map<String, String> teacherNameMap = new HashMap<String, String>();
				    Map<String, Integer> teacherSexMap = new HashMap<String, Integer>();
				    Map<String, String> teacherNationMap = new HashMap<String, String>();
				    Map<String, String> teacherIdCardMap = new HashMap<String, String>();
					for(Teacher teacher : teacherList){
						teacherNameMap.put(teacher.getId(), teacher.getTeacherName());
						teacherSexMap.put(teacher.getId(), teacher.getSex());
						teacherNationMap.put(teacher.getId(), teacher.getNation());
						teacherIdCardMap.put(teacher.getId(), teacher.getIdentityCard());
				    }
					for(TeaexamRegisterInfo reg : registerInfoList){
						reg.setTeacherName(teacherNameMap.get(reg.getTeacherId()));
						Integer sex = teacherSexMap.get(reg.getTeacherId());
						if (sex != null && sexMap.containsKey(String.valueOf(sex))) {
							reg.setSex(sexMap.get(String.valueOf(sex))
									.getMcodeContent());
						}
						String nation = teacherNationMap.get(reg.getTeacherId());
						if(StringUtils.isNotEmpty(nation) && mzMap.containsKey(nation)){
							reg.setNation(mzMap.get(nation).getMcodeContent());
						}
						reg.setIdentityCard(teacherIdCardMap.get(reg.getTeacherId()));
						reg.setUnitName(teacherUnitNameMap.get(reg.getSchoolId()));
						reg.setSubName(subNameMap.get(reg.getSubjectInfoId()));
						String photo = userMap.get(reg.getTeaUserId());
						if (StringUtils.isNotEmpty(photo)) {
							reg.setAvatarUrl(PJHeadUrlUtils.getShowAvatarUrl(request.getContextPath(),
									photo, getFileURL()));
						}
					}
				}
				
			}
			map.put("registerInfoList", registerInfoList);
			map.put("teaexamInfoList", teaexamInfoListTemp);
			map.put("examId", examId);
			map.put("Pagination", page);
			sendPagination(request, map, page);
		} catch (Exception e) {
			e.printStackTrace();
			return errorFtl(map, "数据整理错误，请联系技术人员");
		}
		return "/teaexam/registerInfo/auditingList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/registerAudit/doAuditing")
    @ControllerInfo(value = "")
	public String doAuditing(String id, int status, String remark){		
		try{
			TeaexamRegisterInfo reg = teaexamRegisterInfoService.findOne(id);
			reg.setStatus(status);
			if(status==-1 && StringUtils.isNotBlank(remark)){
				reg.setRemark(remark);
			}		
			reg.setOperatorId(getLoginInfo().getUserId());
			reg.setModifyTime(new Date());
			teaexamRegisterInfoService.save(reg);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@ResponseBody
	@RequestMapping("/registerAudit/bacthAudting")
    @ControllerInfo(value = "")
	public String bacthAudting(String[] ids, int status, String remark){		
		try{
			List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findListByIds(ids);
            for(TeaexamRegisterInfo reg : regList){
            	reg.setStatus(status);
    			if(status==-1 && StringUtils.isNotBlank(remark)){
    				reg.setRemark(remark);
    			}	
    			reg.setOperatorId(getLoginInfo().getUserId());
    			reg.setModifyTime(new Date());
            }
            teaexamRegisterInfoService.saveAll(regList.toArray(new TeaexamRegisterInfo[0]));
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@RequestMapping("/registerAudit/haveAudtingHead")
	public String haveRegister(String examId, HttpServletRequest req, ModelMap map){
		int year = NumberUtils.toInt(req.getParameter("year")); 
		int type = NumberUtils.toInt(req.getParameter("type"));;
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		List<TeaexamInfo> examListTemp = teaexamInfoService.findByInfoYearType(unitId, year, type);
		List<TeaexamInfo> examList = new ArrayList<TeaexamInfo>();
		for(TeaexamInfo exam : examListTemp){
			if(2==exam.getState()){
				examList.add(exam);
			}
		}
		Date date = new Date();
		for(TeaexamInfo exam : examListTemp){
			if(date.getTime()>=exam.getRegisterBegin().getTime() && date.getTime()<=exam.getRegisterEnd().getTime()){
				examId = exam.getId();
			}
		}
		if(StringUtils.isBlank(examId) && CollectionUtils.isNotEmpty(examList)){
			examId = examList.get(0).getId();
		}
		map.put("year", year);
		map.put("type", type);
		map.put("examList", examList);
		map.put("examId", examId);
		return "/teaexam/registerInfo/haveAudtingHead.ftl";
	}
	
	@RequestMapping("/registerAudit/haveAudtingList")
	public String haveAudtingList(String type, String examId, String subId, String schId, String status, String teacherName, String identityCard, ModelMap map,HttpServletRequest request){
		TeaexamInfo exam = null;
		if(StringUtils.isNotBlank(examId)){		
			exam = teaexamInfoService.findOne(examId);
		}
		if(exam != null) {
			String schoolIds = exam.getSchoolIds();
			if(StringUtils.isNotBlank(schoolIds)){			
				String[] schoolIdArr = schoolIds.split(",");
				List<Unit> unitList2 = new ArrayList<Unit>();
				if(schoolIdArr.length>0){
					unitList2 = SUtils.dt(unitRemoteService.findListByIds(schoolIdArr), new TR<List<Unit>>(){});
				}
				List<TeaexamSubject> subList2 = teaexamSubjectService.findByExamIds(new String[]{examId});
				Pagination page = createPagination();
				List<Teacher> teacherList2 = new ArrayList<Teacher>();
				Set<String> teaIdSet2 = new HashSet<String>();
				String state = "";
				if(StringUtils.isNotBlank(identityCard)){
					teacherList2 = SUtils.dt(teacherRemoteService.findByIdentityCardNo(new String[]{identityCard}), new TR<List<Teacher>>(){});
					for(Teacher t : teacherList2){
						teaIdSet2.add(t.getId());
					}
					state = "1";
					map.put("searchCon", identityCard);
				}
				if(StringUtils.isNotBlank(teacherName)){
					teacherList2 = SUtils.dt(teacherRemoteService.findByTeacherNameLike(teacherName), new TR<List<Teacher>>(){});
					for(Teacher t : teacherList2){
						teaIdSet2.add(t.getId());
					}
					state = "2";
					map.put("searchCon", teacherName);
				}
				
				List<TeaexamRegisterInfo> registerInfoList = teaexamRegisterInfoService.findBy(examId, subId, schId, status, state, teaIdSet2.toArray(new String[0]), page);
				
				Set<String> teaIdSet = new HashSet<String>();
				Set<String> unitIdSet = new HashSet<String>();
				Set<String> subIdSet = new HashSet<String>();
				Set<String> opIdSet = new HashSet<String>();
				Set<String> userIdSet = new HashSet<String>();
				for(TeaexamRegisterInfo reg : registerInfoList){
					teaIdSet.add(reg.getTeacherId());
					unitIdSet.add(reg.getSchoolId());
					subIdSet.add(reg.getSubjectInfoId());
					opIdSet.add(reg.getOperatorId());
					userIdSet.add(reg.getTeaUserId());
				}
				Map<String, String> userMap = new HashMap<String, String>();
				if(CollectionUtils.isNotEmpty(userIdSet)){
					List<User> userList = SUtils.dt(userRemoteService.findListByIds(userIdSet.toArray(new String[0])), new TR<List<User>>(){});
					for(User u : userList){
						userMap.put(u.getId(), u.getAvatarUrl());
					}
				}
				Map<String, String> subNameMap = new HashMap<String, String>();
				for(TeaexamSubject sub : subList2){
					String subctionName = "";
					if(sub.getSection()==1){
						subctionName = "小学";
					} else if (0 == sub.getSection()) {
						subctionName = "学前";
					}else if(sub.getSection()==2){
						subctionName = "初中";
					}else if(sub.getSection()==3){
						subctionName = "高中";
					}
					subNameMap.put(sub.getId(), sub.getSubjectName()+"("+subctionName+")");
				}
				Map<String, String> userNameMap = new HashMap<String, String>();
				if(CollectionUtils.isNotEmpty(opIdSet)){
					List<User> userList = SUtils.dt(userRemoteService.findListByIds(opIdSet.toArray(new String[0])), new TR<List<User>>(){});
					for(User u : userList){
						userNameMap.put(u.getId(), u.getRealName());
					}
				}
				List<Unit> unitList = new ArrayList<Unit>();
				if(CollectionUtils.isNotEmpty(unitIdSet)){
					unitList = SUtils.dt(unitRemoteService.findListByIds(unitIdSet.toArray(new String[0])), new TR<List<Unit>>(){});
				}
				Map<String, String> teacherUnitNameMap = new HashMap<String, String>();
				for(Unit unit : unitList){
					teacherUnitNameMap.put(unit.getId(), unit.getUnitName());
				}
				Map<String, McodeDetail> sexMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XB"),new TypeReference<Map<String,McodeDetail>>(){});
				Map<String, McodeDetail> mzMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-MZ"),new TypeReference<Map<String,McodeDetail>>(){});
				if(CollectionUtils.isNotEmpty(teaIdSet)){				
					List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teaIdSet.toArray(new String[0])), new TR<List<Teacher>>(){}); 
					Map<String, String> teacherNameMap = new HashMap<String, String>();
					Map<String, Integer> teacherSexMap = new HashMap<String, Integer>();
					Map<String, String> teacherNationMap = new HashMap<String, String>();
					Map<String, String> teacherIdCardMap = new HashMap<String, String>();
					for(Teacher teacher : teacherList){
						teacherNameMap.put(teacher.getId(), teacher.getTeacherName());
						if (teacher.getSex() != null) {
							teacherSexMap.put(teacher.getId(), teacher.getSex());
						}
						teacherNationMap.put(teacher.getId(), teacher.getNation());
						teacherIdCardMap.put(teacher.getId(), teacher.getIdentityCard());
					}
					for(TeaexamRegisterInfo reg : registerInfoList){
						reg.setTeacherName(teacherNameMap.get(reg.getTeacherId()));
						Integer sex = teacherSexMap.get(reg.getTeacherId());
						if (sex != null && sexMap.containsKey(String.valueOf(sex))) {
							reg.setSex(sexMap.get(String.valueOf(sex))
									.getMcodeContent());
						}
						String nation = teacherNationMap.get(reg.getTeacherId());
						if(null!=nation && mzMap.containsKey(nation)){
							reg.setNation(mzMap.get(nation).getMcodeContent());
						}else{
							reg.setNation("");
						}
						reg.setIdentityCard(teacherIdCardMap.get(reg.getTeacherId()));
						reg.setUnitName(teacherUnitNameMap.get(reg.getSchoolId()));
						reg.setSubName(subNameMap.get(reg.getSubjectInfoId()));
						reg.setUserName(userNameMap.get(reg.getOperatorId()));
						reg.setAvatarUrl(PJHeadUrlUtils.getShowAvatarUrl(request.getContextPath(), userMap.get(reg.getTeaUserId()), getFileURL()));
					}
				}
				map.put("subList", subList2);
				map.put("subId", subId);
				map.put("unitList", unitList2);
				map.put("schId", schId);
				map.put("status", status);
				map.put("registerInfoList", registerInfoList);
				map.put("Pagination", page);
				map.put("examId", examId);
				sendPagination(request, map, page);
			}
		}
		if(StringUtils.isBlank(type)){
			type = "1";
		}
		map.put("type", type);
		int infoType = NumberUtils.toInt(request.getParameter("infoType"));
		map.put("infoType", infoType);
		return "/teaexam/registerInfo/haveAudtingList.ftl";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/registerAudit/export")
	public void exportRegs(HttpServletRequest req, HttpServletResponse resp, ModelMap map) {
		String examId = req.getParameter("examId");
		TeaexamInfo exam = null;
		if(StringUtils.isNotBlank(examId)){		
			exam = teaexamInfoService.findOne(examId);
		}
		if(exam == null) {
			return;
		}
		String subId=req.getParameter("subId"), schId=req.getParameter("schId");
		List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findBy(examId, subId, schId, TeaexamConstant.STATUS_PASS+"", null, null, null);
		if(CollectionUtils.isEmpty(regList)) {
			return;
		}
		List<String> unitIds = EntityUtils.getList(regList, TeaexamRegisterInfo::getSchoolId);
		List<String> tIds = EntityUtils.getList(regList, TeaexamRegisterInfo::getTeacherId);
		List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(tIds.toArray(new String[0])), new TR<List<Teacher>>(){});
		Map<String, Teacher> teaMap = EntityUtils.getMap(teacherList, Teacher::getId);
		Map<String, String> unitNameMap = EntityUtils.getMap(Unit.dt(unitRemoteService.findListByIds(unitIds.toArray(new String[0]))), Unit::getId, Unit::getUnitName);
		List<McodeDetail> dms = SUtils.dt(mcodeRemoteService.findByMcodeIds(new String[] {"DM-XB", "DM-MZ", "DM-XD"}), McodeDetail.class);
		Map<String, String> dmMap = new HashMap<>();
		for(McodeDetail md : dms) {
			dmMap.put(md.getMcodeId()+md.getThisId(), md.getMcodeContent());
		}
		Map<String, String> subNameMap = null;
		boolean examEdit = exam.getInfoType() == TeaexamConstant.EXAM_INFOTYPE_0;
		if(examEdit) {
			subNameMap = new HashMap<>();
			List<TeaexamSubject> subList2 = teaexamSubjectService.findByExamIds(new String[]{examId});
			for(TeaexamSubject sub : subList2){
				String subctionName = dmMap.get("DM-XD"+sub.getSection());
				subNameMap.put(sub.getId(), sub.getSubjectName()+"("+subctionName+")");
			}
		}
		boolean hasSub = MapUtils.isNotEmpty(subNameMap);
		for(Iterator<TeaexamRegisterInfo> it = regList.iterator();it.hasNext();){
			TeaexamRegisterInfo reg = it.next();
			Teacher tea = teaMap.get(reg.getTeacherId());
			if(tea == null) {
				it.remove();
			}
			reg.setTeacherName(tea.getTeacherName());
			reg.setIdentityCard(tea.getIdentityCard());
			reg.setSex(dmMap.get("DM-XB"+String.valueOf(tea.getSex())));
			reg.setNation(dmMap.get("DM-MZ"+tea.getNation()));
			reg.setUnitName(unitNameMap.get(reg.getSchoolId()));
			if(hasSub && subNameMap.containsKey(reg.getSubjectInfoId())) {
				reg.setSubName(subNameMap.get(reg.getSubjectInfoId()));
			}
		}
		StringBuffer fileName = new StringBuffer(exam.getExamName());
		if(StringUtils.isNotEmpty(subId) && hasSub && subNameMap.containsKey(subId)) {
			fileName.append("-"+subNameMap.get(subId));
		}
		if(StringUtils.isNotEmpty(schId) && unitNameMap.containsKey(schId)) {
			fileName.append("-"+unitNameMap.get(schId));
		}
		fileName.append("通过名单");
		String[] pros;
		String[] titles;
		if (examEdit) {
			pros = new String[] { "teacherName", "sex", "nation", "identityCard", "unitName", "subName" };
			titles = new String[] { "教师姓名", "性别", "民族", "身份证号", "单位", "报名科目" };
		} else {
			pros = new String[] { "teacherName", "sex", "nation", "identityCard", "unitName" };
			titles = new String[] { "教师姓名", "性别", "民族", "身份证号", "单位" };
		}
		Map records = new HashMap<>();
		records.put(fileName.toString(), regList);
		ExportUtils.newInstance().exportXLSFile(titles, pros, records, fileName.toString(), resp);
	}
	
	@ResponseBody
	@RequestMapping("/registerAudit/examList")
	public List<TeaexamInfo> examList(int year, int infoType){
		List<TeaexamInfo> examListTemp = teaexamInfoService.findByInfoYearType(getLoginInfo().getUnitId(), year, infoType);
		List<TeaexamInfo> examList = new ArrayList<TeaexamInfo>();
		for(TeaexamInfo exam : examListTemp){
			if(2==exam.getState()){
				examList.add(exam);
			}
		}
		return examList;
	}
	
	@RequestMapping("/query/registerInfo/page")
	@ControllerInfo("教职工报名情况-首页")
	public String queryIndex(HttpServletRequest req, ModelMap map) {
		int year = NumberUtils.toInt(req.getParameter("year")); 
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		map.put("year", year);
		List<TeaexamInfo> examList = teaexamInfoService.findByInfoYearSchoolId(year, getLoginInfo().getUnitId());
		map.put("examList", examList);
		return "/teaexam/registerInfo/registerQueryIndex.ftl";
	}
	
	@RequestMapping("/query/registerInfo/list")
	@ControllerInfo("教职工报名情况-报名老师列表")
	public String queryList(HttpServletRequest req, ModelMap map) {
		String examId = req.getParameter("examId");
		if(StringUtils.isEmpty(examId)) {
			return "/teaexam/registerInfo/registerQueryList.ftl";
		}
		TeaexamInfo exam = null;
		if(StringUtils.isNotBlank(examId)){		
			exam = teaexamInfoService.findOne(examId);
		}
		if(exam == null) {
			return "/teaexam/registerInfo/registerQueryList.ftl";
		}
		map.put("exam", exam);
		List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findBy(examId, null, getLoginInfo().getUnitId(), null, null, null, null);
		if(CollectionUtils.isEmpty(regList)) {
			return "/teaexam/registerInfo/registerQueryList.ftl";
		}
		List<String> tIds = EntityUtils.getList(regList, TeaexamRegisterInfo::getTeacherId);
		List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(tIds.toArray(new String[0])), new TR<List<Teacher>>(){});
		List<String> dpIds = EntityUtils.getList(teacherList, Teacher::getDeptId);
		Map<String, String> deptNameMap = EntityUtils.getMap(Dept.dt(deptRemoteService.findListByIds(dpIds.toArray(new String[0]))), 
				Dept::getId, Dept::getDeptName);
		Map<String, Teacher> teaMap = EntityUtils.getMap(teacherList, Teacher::getId);
		Map<String, String> subNameMap = new HashMap<>();
		if (exam.getInfoType() == 0) {
			List<TeaexamSubject> subList2 = teaexamSubjectService.findByExamIds(new String[] { examId });
			Map<String, McodeDetail> dmMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XD"),
					new TR<Map<String, McodeDetail>>() {
					});
			for (TeaexamSubject sub : subList2) {
				String subctionName = "";
				if (dmMap.containsKey(sub.getSection() + "")) {
					subctionName = "(" + dmMap.get(sub.getSection() + "").getMcodeContent() + ")";
				}
				subNameMap.put(sub.getId(), sub.getSubjectName() + subctionName);
			} 
		}
		boolean hasSub = MapUtils.isNotEmpty(subNameMap);
		for(Iterator<TeaexamRegisterInfo> it = regList.iterator();it.hasNext();){
			TeaexamRegisterInfo reg = it.next();
			Teacher tea = teaMap.get(reg.getTeacherId());
			if(tea == null) {
				it.remove();
			}
			reg.setTeacherName(tea.getTeacherName());
			reg.setUserName(deptNameMap.get(tea.getDeptId()));
			reg.setIdentityCard(tea.getIdentityCard());
			reg.setSex(String.valueOf(tea.getSex()));
			reg.setNation(tea.getNation());
			if (exam.getInfoType() == 0) {
				if (hasSub && subNameMap.containsKey(reg.getSubjectInfoId())) {
					reg.setSubName(subNameMap.get(reg.getSubjectInfoId()));
				} 
			} else {
				reg.setSubName(exam.getTrainItems());
			}
		}
		regList.stream().sorted((a,b)->{
			if(!StringUtils.equals(a.getSubName(), b.getSubName())) {
				return StringUtils.trimToEmpty(a.getSubName()).compareTo(b.getSubName());
			}
			if(a.getStatus() != b.getStatus()) {
				return a.getStatus() - b.getStatus();
			}
			return -1;
		});
		map.put("examId", examId);
		map.put("regList", regList);
		return "/teaexam/registerInfo/registerQueryList.ftl";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/query/registerInfo/export")
	@ControllerInfo("教职工报名情况-列表导出")
	public void queryExport(HttpServletRequest req, HttpServletResponse resp, ModelMap map) {
		queryList(req, map);
		if (!map.containsKey("regList")) {
			return;
		}
		TeaexamInfo exam = (TeaexamInfo) map.get("exam");
		List<TeaexamRegisterInfo> regList = (List<TeaexamRegisterInfo>) map.get("regList");
		for(TeaexamRegisterInfo info : regList) {
			if(info.getStatus() == 1) {
				info.setUnitName("审核中");
			} else if(info.getStatus() == 2) {
				info.setUnitName("报名成功");
			} else {
				info.setUnitName("审核不通过("+info.getRemark()+")");
			}
		}
		String fileName = exam.getExamName()+"报名名单";
		String[] pros = new String[] { "teacherName", "userName", "identityCard", "subName", "unitName" };
		String[] titles = new String[] { "教师姓名", "部门", "身份证号", "报名科目/培训项目", "报名状态" };
		Map records = new HashMap<>();
		records.put(fileName.toString(), regList);
		ExportUtils.newInstance().exportXLSFile(titles, pros, records, fileName, resp);
	}
}
