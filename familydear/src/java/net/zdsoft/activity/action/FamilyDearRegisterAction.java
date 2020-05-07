package net.zdsoft.activity.action;

import net.zdsoft.activity.dto.FamilyDearAuditDto;
import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.activity.entity.FamilyDearRegister;
import net.zdsoft.activity.service.FamilyDearPermissionService;
import net.zdsoft.activity.service.FamilyDearRegisterService;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.familydear.entity.FamDearActivity;
import net.zdsoft.familydear.entity.FamDearArrange;
import net.zdsoft.familydear.entity.FamDearPlan;
import net.zdsoft.familydear.entity.FamdearActualReport;
import net.zdsoft.familydear.entity.FamdearMonth;
import net.zdsoft.familydear.entity.FamilyDearObject;
import net.zdsoft.familydear.service.FamDearActivityService;
import net.zdsoft.familydear.service.FamDearArrangeService;
import net.zdsoft.familydear.service.FamDearPlanService;
import net.zdsoft.familydear.service.FamdearActualReportService;
import net.zdsoft.familydear.service.FamdearMonthService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.zdsoft.framework.utils.ExportUtils.outputData;

@Controller
@RequestMapping("/familydear")
public class FamilyDearRegisterAction extends BaseAction{
	@Autowired
	private FamDearPlanService famDearPlanService;
	@Autowired
	private FamDearActivityService famDearActivityService;
	@Autowired
	private FamDearArrangeService famDearArrangeService;
	@Autowired
	private FamilyDearRegisterService familyDearRegisterService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private FamilyDearPermissionService familyDearPermissionService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private FamdearMonthService famdearMonthService;
	@Autowired
	private FamdearActualReportService famdearActualReportService;
	@Autowired
	McodeRemoteService mcodeRemoteService;

	public static final short DATE_FORMAT = HSSFDataFormat.getBuiltinFormat("m/d/yy");

	private short dateFormat = DATE_FORMAT; // 默认日期格式

	@RequestMapping("/abnormal/edu/index/page")
	public String pageIndex(ModelMap map){
		return "/familydear/registerInfo/registerInfoTab.ftl";
	}
	
	@RequestMapping("/registerInfo/beginRegister")
	public String beginRegister(HttpServletRequest req, String index, ModelMap map){
		int year = NumberUtils.toInt(req.getParameter("year")); 
		String activityName = req.getParameter("activityName");
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		List<FamDearPlan> FamDearPlanListTemp = new ArrayList<FamDearPlan>();
		FamDearPlanListTemp=famDearPlanService.getFamilyDearPlanList(getLoginInfo().getUnitId(), year+"");
		Set<String> planIdSet = new HashSet<String>();
		for (FamDearPlan famDearPlan : FamDearPlanListTemp) {
			planIdSet.add(famDearPlan.getId());
		}
		List<FamDearActivity> famDearActivities=new ArrayList<FamDearActivity>();
		if("1".equals(index)){
			if(CollectionUtils.isNotEmpty(planIdSet)){
				famDearActivities = famDearActivityService.findByPlanIds(planIdSet.toArray(new String[0]));
			}
		}else{
			if(StringUtils.isNotBlank(activityName)&&CollectionUtils.isNotEmpty(planIdSet)){
				famDearActivities=famDearActivityService.findByNameAndPlanIds(activityName, planIdSet.toArray(new String[0]));
			}else{
				if(CollectionUtils.isNotEmpty(planIdSet)){
					famDearActivities=famDearActivityService.findByPlanIds(planIdSet.toArray(new String[0]));
				}
			}
			
		}
	    List<FamDearArrange> arrangeList = new ArrayList<FamDearArrange>();
	    Set<String> acIdSet = new HashSet<String>();
	    for(FamDearActivity famDearActivity : famDearActivities){
	    	acIdSet.add(famDearActivity.getId());
	    }
	    List<FamilyDearRegister> familyDearRegisters=new ArrayList<FamilyDearRegister>();
	    Set<String> acIdUse = new HashSet<String>();		
	    Set<String> acIdUseOther = new HashSet<String>();		
	    Set<String> acIdUseOther1 = new HashSet<String>();		
	    if(CollectionUtils.isNotEmpty(acIdSet)){
	    	arrangeList = famDearArrangeService.getFamilyDearArrangeList(acIdSet.toArray(new String[0]));
	    	Set<String> arrIdSet=new HashSet<String>();
	    	for (FamDearArrange famDearArrange : arrangeList) {
	    		List<FamilyDearRegister> familyDearRegTemps=familyDearRegisterService.getFamilyDearReByArrangeIdAndActivityIdAndUnitId(getLoginInfo().getUnitId(), famDearArrange.getActivityId(), famDearArrange.getId());
	    		if(CollectionUtils.isNotEmpty(familyDearRegTemps)&&familyDearRegTemps.size()>=famDearArrange.getPeopleNumber()){
	    			famDearArrange.setCanFull(true);
				}
	    		if(DateUtils.compareForDay(famDearArrange.getApplyTime(),new Date())<=0&&DateUtils.compareForDay(famDearArrange.getApplyEndTime(),new Date())>=0){
	    			famDearArrange.setCanApply(true);
	    			acIdUse.add(famDearArrange.getActivityId());
	    		}else{
	    			if(DateUtils.compareForDay(famDearArrange.getApplyEndTime(),new Date())<0){
	    				famDearArrange.setCanLowTime(true);
	    			}
	    		}
	    		arrIdSet.add(famDearArrange.getId());
	    		if(DateUtils.compareForDay(famDearArrange.getApplyEndTime(),new Date())<0){
	    			acIdUseOther.add(famDearArrange.getActivityId());
	    		}else{
	    			acIdUseOther1.add(famDearArrange.getActivityId());
	    		}
			}
	    	familyDearRegisters = familyDearRegisterService.getFamilyDearRegisterList(getLoginInfo().getUnitId(),getLoginInfo().getOwnerId(), arrIdSet.toArray(new String[0]));
	    }
	    List<FamDearActivity> famDearActivitemp=new ArrayList<FamDearActivity>();
	    for (FamDearActivity famDearActivity : famDearActivities) {
	    	famDearActivity.setFileContent(StringEscapeUtils.unescapeXml(famDearActivity.getFileContent()));
	    	if("1".equals(index)){
	    		if(CollectionUtils.isNotEmpty(acIdUse)&&acIdUse.contains(famDearActivity.getId())){
	    			famDearActivitemp.add(famDearActivity);
	    		}
	    	}else{
	    		if(CollectionUtils.isNotEmpty(acIdUseOther1)){
	    			if(CollectionUtils.isNotEmpty(acIdUseOther)&&acIdUseOther.contains(famDearActivity.getId())&&!acIdUseOther1.contains(famDearActivity.getId())){
	    				famDearActivitemp.add(famDearActivity);
	    			}
	    		}else{
	    			if(CollectionUtils.isNotEmpty(acIdUseOther)&&acIdUseOther.contains(famDearActivity.getId())){
	    				famDearActivitemp.add(famDearActivity);
	    			}
	    		}
	    	}	
		}
		map.put("year", year);
		map.put("activityName", activityName);
	    map.put("famDearActivities", famDearActivitemp);
	    map.put("arrangeList", arrangeList);
	    map.put("familyDearRegisters", familyDearRegisters);
	    map.put("index", index);
		return "/familydear/registerInfo/registerInfoBeginList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/registerInfo/doRegister")
    @ControllerInfo(value = "")
	public String doRegister(String activityId, String arrangeId,String id, int status){
		try {
			if(status==0){
				familyDearRegisterService.delete(id);
			}else{
				FamilyDearRegister familyDearRegister=new FamilyDearRegister();
				if(StringUtils.isNotBlank(id)){
					familyDearRegister.setId(id);
				} else {
					familyDearRegister.setId(UuidUtils.generateUuid());
				}
				
				List<FamilyDearRegister> familyDearRegisters=familyDearRegisterService.getFamilyDearReByArrangeIdAndActivityIdAndUnitId(getLoginInfo().getUnitId(), activityId, arrangeId);
				FamDearArrange famDearArrange=famDearArrangeService.findOne(arrangeId);
				if(CollectionUtils.isNotEmpty(familyDearRegisters)&&familyDearRegisters.size()>=famDearArrange.getPeopleNumber()){
					return error("该批次安排已报满！");
				}
				List<FamilyDearRegister> faDearRegisters=familyDearRegisterService.getFamilyDearRegisterList(getLoginInfo().getUnitId(), getLoginInfo().getOwnerId());
				Set<String> faSet = EntityUtils.getSet(faDearRegisters, e->e.getArrangeId());
				if(CollectionUtils.isNotEmpty(faSet)){
					List<FamDearArrange> famDearArranges=famDearArrangeService.findListByIdsWithMaster(faSet.toArray(new String[0]));
					for (FamDearArrange famDearArrange2 : famDearArranges) {
						if(!(famDearArrange.getEndTime().compareTo(famDearArrange2.getStartTime())<0||famDearArrange.getStartTime().compareTo(famDearArrange2.getEndTime())>0)){
							return error("该批次和已报名批次时间有冲突！");
						}
					}
				}
				familyDearRegister.setActivityId(activityId);
				familyDearRegister.setApplyTime(new Date());
				familyDearRegister.setArrangeId(arrangeId);
				familyDearRegister.setStatus(status);
				familyDearRegister.setTeacherId(getLoginInfo().getOwnerId());
				familyDearRegister.setTeaUserId(getLoginInfo().getUserId());
				familyDearRegister.setUnitId(getLoginInfo().getUnitId());
				familyDearRegisterService.save(familyDearRegister);
				FamDearActivity famDearActivity = famDearActivityService.findOne(activityId);
				famDearActivity.setCreateTime(new Date());
				famDearActivityService.save(famDearActivity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@RequestMapping("/abnormal/audit/index/page")
	public String registerAuditTab(ModelMap map){
		List<FamilyDearPermission> familyDearPermissions=familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(), getLoginInfo().getUserId(), "1");
		boolean show=false;
		if(CollectionUtils.isNotEmpty(familyDearPermissions)){
			show=true;
		}
		map.put("show", show);
		return "/familydear/registerInfo/registerAuditTab.ftl";
	}

	@RequestMapping("/registerAudit/auditingHead")
	public String auditingHead(ModelMap map,HttpServletRequest request){
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR);

		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		List<FamDearPlan> FamDearPlanListTemp = new ArrayList<FamDearPlan>();
		FamDearPlanListTemp=famDearPlanService.getFamilyDearPlanList(getLoginInfo().getUnitId(), nowy+"");
		Set<String> planIdSet = new HashSet<String>();
		for (FamDearPlan famDearPlan : FamDearPlanListTemp) {
			planIdSet.add(famDearPlan.getId());
		}
		List<FamDearActivity> famDearActivities=new ArrayList<FamDearActivity>();
		if(CollectionUtils.isNotEmpty(planIdSet)){
			famDearActivities = famDearActivityService.findByPlanIds(planIdSet.toArray(new String[0]));
		}
		map.put("famDearActivities", famDearActivities);
//		map.put("activityId", activityId);
//		map.put("contryName", contryName);
//		if(CollectionUtils.isNotEmpty(list)) {
//			map.put("contryNameValue", list.get(0).getThisId());
//		}
		return "/familydear/registerInfo/auditingHead.ftl";
	}
	
	@RequestMapping("/registerAudit/auditingList")
	public String auditingList(String activityId, ModelMap map,HttpServletRequest request){
		String contryName=request.getParameter("contryName");
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		List<FamDearPlan> FamDearPlanListTemp = new ArrayList<FamDearPlan>();
		FamDearPlanListTemp=famDearPlanService.getFamilyDearPlanList(getLoginInfo().getUnitId(), nowy+"");
		Set<String> planIdSet = new HashSet<String>();
		for (FamDearPlan famDearPlan : FamDearPlanListTemp) {
			planIdSet.add(famDearPlan.getId());
		}
		List<FamDearActivity> famDearActivities=new ArrayList<FamDearActivity>();
		if(CollectionUtils.isNotEmpty(planIdSet)){
			famDearActivities = famDearActivityService.findByPlanIds(planIdSet.toArray(new String[0]));
		}
		Set<String> acIdSet = new HashSet<String>();
		if(StringUtils.isNotBlank(activityId)){
			acIdSet.add(activityId);
		}else{
			if(CollectionUtils.isNotEmpty(famDearActivities)){
				acIdSet.add(famDearActivities.get(0).getId());
				activityId=famDearActivities.get(0).getId();
			}
		}
		List<FamDearArrange> famDearArranges=new ArrayList<FamDearArrange>();
		Set<String> arranIdSet = new HashSet<String>();
//		if(StringUtils.isNotBlank(contryName)){
//			famDearArranges=famDearArrangeService.getFamilyDearArrangeListByContryName(contryName);
//			for (FamDearArrange famDearArrange : famDearArranges) {
//				arranIdSet.add(famDearArrange.getId());
//			}
//		}
		List<FamilyDearRegister> familyDearRegisters=new ArrayList<FamilyDearRegister>();
		if(CollectionUtils.isNotEmpty(acIdSet)){
			familyDearRegisters=familyDearRegisterService.getFamilyDearReByActivityIdsAndStatusAndUnitId(getLoginInfo().getUnitId(), 1, activityId,arranIdSet.toArray(new String[0]),contryName);
		}
		List<McodeDetail> list = new ArrayList<>();
		if(StringUtils.isNotBlank(contryName)) {
			list = SUtils.dt(mcodeRemoteService.findByMcodeContentLike("DM-XJJQC", contryName), new TR<List<McodeDetail>>() {
			});
		}
		map.put("registerInfoList", familyDearRegisters);
		map.put("activityId", activityId);
		map.put("contryName", contryName);
		if(CollectionUtils.isNotEmpty(list)) {
			map.put("contryNameValue", list.get(0).getThisId());
		}
		return "/familydear/registerInfo/auditingList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/registerAudit/doAuditing")
    @ControllerInfo(value = "")
	public String doAuditing(String id, int status, String remark){		
		try{
			FamilyDearRegister reg = familyDearRegisterService.findOne(id);
			reg.setStatus(status);
			if(status==-1 && StringUtils.isNotBlank(remark)){
				reg.setRemark(remark);
			}		
			reg.setAuditUserId(getLoginInfo().getUserId());
			reg.setAuditTime(new Date());
			familyDearRegisterService.save(reg);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}

	@RequestMapping("/registerAudit/auditingDel")
	@ControllerInfo(value = "删除")
	@ResponseBody
	public String auditingDel(String id,ModelMap map){
		try {
			familyDearRegisterService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}

	@RequestMapping("/registerAudit/auditingCancel")
	@ControllerInfo(value = "删除")
	@ResponseBody
	public String auditingCancel(String id,ModelMap map){
		try {
			FamilyDearRegister familyDearRegister = familyDearRegisterService.findOne(id);
			familyDearRegister.setStatus(1);
			FamDearArrange famDearArrange = famDearArrangeService.findOne(familyDearRegister.getArrangeId());
			if(famDearArrange!=null) {
				List<FamdearMonth> famdearMonths = famdearMonthService.findListBy(new String[]{"arrangeId"}, new String []{famDearArrange.getId()});
				List<FamdearActualReport> famdearActualReportList = famdearActualReportService.findListBy(new String[]{"arrangeId"}, new String []{famDearArrange.getId()});
				if(CollectionUtils.isNotEmpty(famdearMonths)||CollectionUtils.isNotEmpty(famdearActualReportList)){
					return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("该报名已维护了信息填报或每月活动填报，不能取消审核！"));
				}else {
					famDearArrange.setLeaderUserId("");
				}
			}
			famDearArrangeService.save(famDearArrange);
			familyDearRegisterService.save(familyDearRegister);
//			familyDearRegisterService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/registerAudit/bacthAudting")
    @ControllerInfo(value = "")
	public String bacthAudting(String[] ids, int status, String remark){		
		try{
			List<FamilyDearRegister> regList = familyDearRegisterService.findListByIds(ids);
            for(FamilyDearRegister reg : regList){
            	reg.setStatus(status);
    			if(status==-1 && StringUtils.isNotBlank(remark)){
    				reg.setRemark(remark);
    			}	
    			reg.setAuditUserId(getLoginInfo().getUserId());
    			reg.setAuditTime(new Date());
            }
            familyDearRegisterService.saveAll(regList.toArray(new FamilyDearRegister[0]));
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@RequestMapping("/registerAudit/haveAudtingHead")
	public String haveRegister(String activityId, HttpServletRequest req, ModelMap map){
		String contryName=req.getParameter("contryName");
		String teacherName=req.getParameter("teacherName");
		map.put("contryName", contryName);
		map.put("teacherName", teacherName);
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		List<FamDearPlan> FamDearPlanListTemp = new ArrayList<FamDearPlan>();
		FamDearPlanListTemp=famDearPlanService.getFamilyDearPlanList(getLoginInfo().getUnitId(), nowy+"");
		Set<String> planIdSet = new HashSet<String>();
		for (FamDearPlan famDearPlan : FamDearPlanListTemp) {
			planIdSet.add(famDearPlan.getId());
		}
		List<FamDearActivity> famDearActivities=new ArrayList<FamDearActivity>();
		if(CollectionUtils.isNotEmpty(planIdSet)){
			famDearActivities = famDearActivityService.findByPlanIds(planIdSet.toArray(new String[0]));
		}
		Set<String> acIdSet = new HashSet<String>();
		if(StringUtils.isNotBlank(activityId)){
			acIdSet.add(activityId);
		}else{
			if(CollectionUtils.isNotEmpty(famDearActivities)){
				acIdSet.add(famDearActivities.get(0).getId());
			}
		}
		map.put("famDearActivities", famDearActivities);
		return "/familydear/registerInfo/haveAudtingHead.ftl";
	}
	
	@RequestMapping("/registerAudit/haveAudtingList")
	public String haveAudtingList(String activityId, String contryName, String teacherName,ModelMap map,HttpServletRequest request){
		List<Teacher> teacherList = new ArrayList<Teacher>();
		List<FamDearArrange> famDearArranges=new ArrayList<FamDearArrange>();
		Set<String> teaIdSet = new HashSet<String>();
		Set<String> arranIdSet = new HashSet<String>();
		Pagination page = createPagination();
		if(StringUtils.isNotBlank(teacherName)){
			teacherList = SUtils.dt(teacherRemoteService.findByTeacherNameLike(teacherName), new TR<List<Teacher>>(){});
			for(Teacher t : teacherList){
				teaIdSet.add(t.getId());
			}
			map.put("teacherName", teacherName);
		}
		if(StringUtils.isNotBlank(contryName)){
			famDearArranges=famDearArrangeService.getFamilyDearArrangeListByContryName(contryName);
			for (FamDearArrange famDearArrange : famDearArranges) {
				arranIdSet.add(famDearArrange.getId());
			}
		}
		List<FamilyDearAuditDto> familyDearAuditDtos=new ArrayList<FamilyDearAuditDto>();
		if(StringUtils.isNotBlank(activityId)){
			familyDearAuditDtos=familyDearRegisterService.findByParams(getLoginInfo().getUnitId(), 1, activityId, arranIdSet.toArray(new String[0]),teaIdSet.toArray(new String[0]),teacherName,contryName, page);
		}
		map.put("hasPermission",false);
		List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"1");
		if(CollectionUtils.isNotEmpty(familyDearPermissions)) {
			map.put("hasPermission",true);
		}
		map.put("activityId", activityId);
		map.put("contryName", contryName);
		map.put("teacherName", teacherName);
		map.put("familyDearAuditDtos", familyDearAuditDtos);
		sendPagination(request, map, page);
		return "/familydear/registerInfo/haveAudtingList.ftl";
	}
	
	@RequestMapping("/nameList/index/page")
	public String name(HttpServletRequest req, ModelMap map){
		return "/familydear/registerInfo/nameTab.ftl";
	}
	
	@RequestMapping("/nameList/index/list")
	public String nameList(HttpServletRequest req, ModelMap map){
		int year = NumberUtils.toInt(req.getParameter("year")); 
		String deptId = req.getParameter("deptId");
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String activityTitle = req.getParameter("activityTitle");
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("hasPermission",false);
		List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"8");
		if(CollectionUtils.isNotEmpty(familyDearPermissions)) {
			map.put("hasPermission",true);
		}
		List<Dept> depts = SUtils.dt(deptRemoteService.findByUnitId(getLoginInfo().getUnitId()),new TR<List<Dept>>() {});
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		map.put("year", year);
		map.put("deptId", deptId);
		map.put("depts", depts);
		map.put("startTime",startTime);
		map.put("endTime",endTime);
		map.put("activityTitle",activityTitle);
		Date startDate = DateUtils.string2Date(startTime,"yyyy-MM-dd");
		Date endDate = DateUtils.string2Date(endTime,"yyyy-MM-dd");
		Pagination page = createPagination();
		List<FamDearPlan> FamDearPlanListTemp = new ArrayList<FamDearPlan>();
		FamDearPlanListTemp=famDearPlanService.getFamilyDearPlanList(getLoginInfo().getUnitId(), nowy+"");
		Set<String> planIdSet = new HashSet<String>();
		for (FamDearPlan famDearPlan : FamDearPlanListTemp) {
			planIdSet.add(famDearPlan.getId());
		}
		List<FamDearActivity> famDearActivities=new ArrayList<FamDearActivity>();
		if(CollectionUtils.isNotEmpty(planIdSet)){
			famDearActivities = famDearActivityService.findByNameAndPlanIds(activityTitle,planIdSet.toArray(new String[0]));
		}
		Set<String> activitIds=new HashSet<String>();
		for (FamDearActivity famDearActivity : famDearActivities) {
			activitIds.add(famDearActivity.getId());
		}
		List<FamilyDearRegister> familyDearRegisters=new ArrayList<FamilyDearRegister>();
		if(CollectionUtils.isNotEmpty(activitIds)){
			familyDearRegisters=familyDearRegisterService.getListByUnitIdAndTeacherIds(getLoginInfo().getUnitId(), deptId, year+"",startDate,endDate, activitIds.toArray(new String[0]), null);
		}
		
		Integer maxRow = familyDearRegisters.size();
		page.setMaxRowCount(maxRow);
		page.initialize();
		if(CollectionUtils.isNotEmpty(familyDearRegisters)){
			Integer oldCur = page.getCurRowNum();
			Integer newCur = page.getPageIndex()*page.getPageSize();
			newCur = newCur>maxRow?maxRow:newCur;
			List<FamilyDearRegister> newList = new ArrayList<FamilyDearRegister>();
			
			newList.addAll(familyDearRegisters.subList(oldCur-1, newCur));
			familyDearRegisters = newList;
			for(FamilyDearRegister familyDearRegister:familyDearRegisters){
				if(StringUtils.isNotBlank(familyDearRegister.getContrys())) {
					if (familyDearRegister.getContrys().length() > 5) {
						familyDearRegister.setContrysSub(familyDearRegister.getContrys().substring(0, 5) + "...");
					}else {
						familyDearRegister.setContrysSub(familyDearRegister.getContrys());
					}
				}
				if(StringUtils.isNotBlank(familyDearRegister.getBatchType())) {
					if (familyDearRegister.getBatchType().length() > 5) {
						familyDearRegister.setBatchTypeSub(familyDearRegister.getBatchType().substring(0, 5) + "...");
					}else {
						familyDearRegister.setBatchTypeSub(familyDearRegister.getBatchType());
					}
				}
			}
		}
		map.put("familyDearRegisters", familyDearRegisters);
		sendPagination(req, map, page);
		return "/familydear/registerInfo/nameList.ftl";
	}

	@RequestMapping("/nameList/export")
	public void doExport(HttpServletRequest req, ModelMap map){
		int year = NumberUtils.toInt(req.getParameter("year"));
		String deptId = req.getParameter("deptId");
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String activityTitle = req.getParameter("activityTitle");
		Date startDate = DateUtils.string2Date(startTime,"yyyy-MM-dd");
		Date endDate = DateUtils.string2Date(endTime,"yyyy-MM-dd");
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR);
		if(year == 0) {
			year = nowy;
		}
		List<Dept> depts = SUtils.dt(deptRemoteService.findByUnitId(getLoginInfo().getUnitId()),new TR<List<Dept>>() {});
		Pagination page = createPagination();
		List<FamDearPlan> FamDearPlanListTemp = new ArrayList<FamDearPlan>();
		FamDearPlanListTemp=famDearPlanService.getFamilyDearPlanList(getLoginInfo().getUnitId(), nowy+"");
		Set<String> planIdSet = new HashSet<String>();
		for (FamDearPlan famDearPlan : FamDearPlanListTemp) {
			planIdSet.add(famDearPlan.getId());
		}
		List<FamDearActivity> famDearActivities=new ArrayList<FamDearActivity>();
		if(CollectionUtils.isNotEmpty(planIdSet)){
			famDearActivities = famDearActivityService.findByNameAndPlanIds(activityTitle,planIdSet.toArray(new String[0]));
		}
		Set<String> activitIds=new HashSet<String>();
		for (FamDearActivity famDearActivity : famDearActivities) {
			activitIds.add(famDearActivity.getId());
		}
		List<FamilyDearRegister> familyDearRegisters=new ArrayList<FamilyDearRegister>();
		if(CollectionUtils.isNotEmpty(activitIds)){
			familyDearRegisters=familyDearRegisterService.getListByUnitIdAndTeacherIds(getLoginInfo().getUnitId(), deptId, year+"",startDate,endDate, activitIds.toArray(new String[0]), null);
		}

		Integer maxRow = familyDearRegisters.size();
		page.setMaxRowCount(maxRow);
		page.initialize();
		if(CollectionUtils.isNotEmpty(familyDearRegisters)){
			Integer oldCur = page.getCurRowNum();
			Integer newCur = page.getPageIndex()*page.getPageSize();
			newCur = newCur>maxRow?maxRow:newCur;
			List<FamilyDearRegister> newList = new ArrayList<FamilyDearRegister>();

			newList.addAll(familyDearRegisters.subList(oldCur-1, newCur));
			familyDearRegisters = newList;
		}
		String titleName = "";
		if(year!=0){
			titleName = titleName+year;
		}
		if(StringUtils.isNotBlank(deptId)){
			Dept dept = SUtils.dt(deptRemoteService.findOneById(deptId),new TR<Dept>(){});
			if(dept!=null){
				titleName = titleName+dept.getDeptName();
			}
		}
		doExportList1(familyDearRegisters,getResponse(),titleName);
	}

	private void doExportList1(List<FamilyDearRegister> familyDearRegisters, HttpServletResponse response, String titleName){
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		if(CollectionUtils.isNotEmpty(familyDearRegisters)){
			int index = 1;
			for(FamilyDearRegister familyDearRegister:familyDearRegisters) {
				Map<String, String> sMap = new HashMap<String, String>();
				sMap.put("序号", index+"");
				sMap.put("部门", familyDearRegister.getDeptName());
				String teacherName=familyDearRegister.getTeacherName();
				teacherName = StringUtils.replace(teacherName, "&quot;", "\"");
				teacherName = StringUtils.replace(teacherName, "&lt;", "<");
				teacherName = StringUtils.replace(teacherName, "&gt;", ">");
				teacherName = StringUtils.replace(teacherName, "&gt;", ">");
				teacherName = StringUtils.replace(teacherName, "&sim;", "~");
				teacherName = StringUtils.replace(teacherName, "&and;", "^");
				teacherName = StringUtils.replace(teacherName, "&hellip;", "...");
				teacherName = StringUtils.replace(teacherName, "&middot;", "·");
				sMap.put("姓名", teacherName);
				sMap.put("职务", familyDearRegister.getCadreType());
//				sMap.put("性别", familyDearRegister.getSex());
//				sMap.put("民族", familyDearRegister.getNationName());
//				sMap.put("手机号码", familyDearRegister.getPhone());
//				String name=familyDearRegister.getObjectName();
//				name = StringUtils.replace(name, "&quot;", "\"");
//				name = StringUtils.replace(name, "&lt;", "<");
//				name = StringUtils.replace(name, "&gt;", ">");
//				name = StringUtils.replace(name, "&gt;", ">");
//				name = StringUtils.replace(name, "&sim;", "~");
//				name = StringUtils.replace(name, "&and;", "^");
//				name = StringUtils.replace(name, "&hellip;", "...");
//				name = StringUtils.replace(name, "&middot;", "·");
//				sMap.put("亲戚姓名", name);
//				sMap.put("亲戚民族", familyDearRegister.getObjectNation());
//				sMap.put("身份证", familyDearRegister.getObjectCard());
				sMap.put("亲戚身份", familyDearRegister.getObjectType());
				sMap.put("结亲对象村", familyDearRegister.getContry());
//				sMap.put("家庭住址", familyDearRegister.getObjectDizhi());
//				sMap.put("联系电话", familyDearRegister.getObjectPhone());
				sMap.put("报名批次", familyDearRegister.getBatchType());
				sMap.put("访亲到达时间", familyDearRegister.getArriveTimeStr());
				sMap.put("返回时间", familyDearRegister.getReturnTimeStr());
//				sMap.put("是否上传火车票", familyDearRegister.getUpload());
//				sMap.put("备注", familyDearRegister.getRemarkNew());
				index = index+1;
				recordList.add(sMap);
			}
		}
		sheetName2RecordListMap.put("报名审核",recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList1();
		titleMap.put("报名审核", tis);
		ExportUtils exportUtils = ExportUtils.newInstance();
		exportUtils.exportXLSFile( titleName+"访亲人员名单信息导出", titleMap, sheetName2RecordListMap, response);
	}

	private List<String> getRowTitleList1() {
		List<String> tis = new ArrayList<String>();
		tis.add("序号");
		tis.add("部门");
		tis.add("姓名");
		tis.add("职务");
//		tis.add("性别");
//		tis.add("民族");
//		tis.add("手机号码");
//		tis.add("亲戚姓名");
//		tis.add("亲戚民族");
//		tis.add("身份证");
		tis.add("亲戚身份");
		tis.add("结亲对象村");
//		tis.add("家庭住址");
//		tis.add("联系电话");
		tis.add("报名批次");
		tis.add("访亲到达时间");
		tis.add("返回时间");
//		tis.add("是否上传火车票");
//		tis.add("备注");
		return tis;
	}

	@RequestMapping("/registerAudit/export")
	public void doExport(String activityId, String contryName, String teacherName,ModelMap map,HttpServletRequest request){
		String titleName = "";
		if(StringUtils.isNotBlank(activityId)) {
			FamDearActivity famDearActivity = famDearActivityService.findOne(activityId);
			titleName= famDearActivity.getTitle();
		}
		List<Teacher> teacherList = new ArrayList<Teacher>();
		List<FamDearArrange> famDearArranges=new ArrayList<FamDearArrange>();
		Set<String> teaIdSet = new HashSet<String>();
		Set<String> arranIdSet = new HashSet<String>();
		Pagination page = createPagination();
		if(StringUtils.isNotBlank(teacherName)){
			teacherList = SUtils.dt(teacherRemoteService.findByTeacherNameLike(teacherName), new TR<List<Teacher>>(){});
			for(Teacher t : teacherList){
				teaIdSet.add(t.getId());
			}
			map.put("teacherName", teacherName);
		}
		if(StringUtils.isNotBlank(contryName)){
			famDearArranges=famDearArrangeService.getFamilyDearArrangeListByContryName(contryName);
			for (FamDearArrange famDearArrange : famDearArranges) {
				arranIdSet.add(famDearArrange.getId());
			}
		}
		List<FamilyDearAuditDto> familyDearAuditDtos=new ArrayList<FamilyDearAuditDto>();
		if(StringUtils.isNotBlank(activityId)){
			familyDearAuditDtos=familyDearRegisterService.findByParams(getLoginInfo().getUnitId(), 1, activityId, arranIdSet.toArray(new String[0]),teaIdSet.toArray(new String[0]),teacherName,contryName, null);
		}
		for(FamilyDearAuditDto familyDearAuditDto:familyDearAuditDtos){
			String teachName=familyDearAuditDto.getTeacherName();
			teachName = StringUtils.replace(teachName, "&quot;", "\"");
			teachName = StringUtils.replace(teachName, "&lt;", "<");
			teachName = StringUtils.replace(teachName, "&gt;", ">");
			teachName = StringUtils.replace(teachName, "&gt;", ">");
			teachName = StringUtils.replace(teachName, "&sim;", "~");
			teachName = StringUtils.replace(teachName, "&and;", "^");
			teachName = StringUtils.replace(teachName, "&hellip;", "...");
			teachName = StringUtils.replace(teachName, "&middot;", "·");
			familyDearAuditDto.setTeacherName(teachName);
			List<FamilyDearObject> list = familyDearAuditDto.getFamilyDearObjectList();
			for(FamilyDearObject familyDearObject:list){
				String name=familyDearObject.getName();
				name = StringUtils.replace(name, "&quot;", "\"");
				name = StringUtils.replace(name, "&lt;", "<");
				name = StringUtils.replace(name, "&gt;", ">");
				name = StringUtils.replace(name, "&gt;", ">");
				name = StringUtils.replace(name, "&sim;", "~");
				name = StringUtils.replace(name, "&and;", "^");
				name = StringUtils.replace(name, "&hellip;", "...");
				name = StringUtils.replace(name, "&middot;", "·");
				familyDearObject.setName(name);
			}
		}
		doExportList(familyDearAuditDtos,getResponse(),titleName);
	}
	private void doExportList(List<FamilyDearAuditDto> familyDearAuditDtos, HttpServletResponse response, String titleName){
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		if(CollectionUtils.isNotEmpty(familyDearAuditDtos)){
			for(FamilyDearAuditDto familyDearAuditDto : familyDearAuditDtos){
				List<FamilyDearObject> list = familyDearAuditDto.getFamilyDearObjectList();
				if(CollectionUtils.isNotEmpty(list)) {

					for (FamilyDearObject familyDearObject : list) {
						Map<String,String> sMap = new HashMap<String,String>();
						sMap.put("干部部门", familyDearAuditDto.getDeptName());
						sMap.put("结亲村", familyDearAuditDto.getContrys());
						sMap.put("干部姓名", familyDearAuditDto.getTeacherName());
						sMap.put("干部联系电话", familyDearAuditDto.getTeacherPhone());
						sMap.put("结亲对象", familyDearObject.getName());
						sMap.put("结亲对象联系电话", familyDearObject.getMobilePhone());
						sMap.put("结亲对象村", familyDearObject.getVillage());
						sMap.put("报名批次", familyDearAuditDto.getBatchType());
						sMap.put("报名时间", DateUtils.date2String(familyDearAuditDto.getApplyTime(), "yyyy-MM-dd"));
						sMap.put("审核状态",familyDearAuditDto.getState()==2?"通过":"不通过");
						sMap.put("原因",familyDearAuditDto.getRemark());
						recordList.add(sMap);
					}
				}else {
					Map<String,String> sMap = new HashMap<String,String>();
					sMap.put("干部部门", familyDearAuditDto.getDeptName());
					sMap.put("结亲村", familyDearAuditDto.getContrys());
					sMap.put("干部姓名", familyDearAuditDto.getTeacherName());
					sMap.put("干部联系电话", familyDearAuditDto.getTeacherPhone());
					sMap.put("结亲对象", "");
					sMap.put("结亲对象联系电话", "");
					sMap.put("结亲对象村", "");
					sMap.put("报名批次", familyDearAuditDto.getBatchType());
					sMap.put("报名时间", DateUtils.date2String(familyDearAuditDto.getApplyTime(), "yyyy-MM-dd"));
					sMap.put("审核状态",familyDearAuditDto.getState()==2?"通过":"不通过");
					sMap.put("原因",familyDearAuditDto.getRemark());
					recordList.add(sMap);
				}
			}
		}
		sheetName2RecordListMap.put("报名审核",recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put("报名审核", tis);
		ExportUtils ex = ExportUtils.newInstance();
		exportXLSFile( titleName+"报名审核通过信息导出", titleMap, sheetName2RecordListMap,null, response);
	}

	private List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("干部部门");
		tis.add("结亲村");
		tis.add("干部姓名");
		tis.add("干部联系电话");
		tis.add("结亲对象");
		tis.add("结亲对象联系电话");
		tis.add("结亲对象村");
		tis.add("报名批次");
		tis.add("报名时间");
		tis.add("审核状态");
		tis.add("原因");
		return tis;
	}

	public void exportXLSFile(String fileName, Map<String, List<String>> fieldTitleMap,
							  Map<String, List<Map<String, String>>> sheetName2RecordListMap,List<CellRangeAddress> cellRangeAddresses, HttpServletResponse resp) {

		HSSFWorkbook workbook = new HSSFWorkbook();
		/**
		 * 由于style创建不能超过4000个所以把这个style放到循环外头 modify by zhuw 2013-3-29
		 * **/
		HSSFCellStyle cellDateStyle = workbook.createCellStyle();
		cellDateStyle.setDataFormat(dateFormat);
		HSSFSheet sheet = workbook.createSheet();
		int i = 0;
		for (Iterator<Map.Entry<String, List<Map<String, String>>>> iter = sheetName2RecordListMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<String, List<Map<String, String>>> entry = (Map.Entry<String, List<Map<String, String>>>) iter.next();
			String sheetName = entry.getKey(); // key 工作表名称
			List<Map<String, String>> recordList = entry.getValue(); // value
			// 工作表上的记录

			sheetName = StringUtils.replaceEach(sheetName, new String[] { "\\", "/", "?", "*", "[",
					"]","·"}, new String[] { "", "", "", "", "", "","" });
			if (StringUtils.EMPTY.equals(sheetName)) {
				sheetName = " ";
			}
			workbook.setSheetName(i++, sheetName);

			// 创建首行信息栏
			HSSFRow row = workbook.getSheetAt(i - 1).createRow(0);
			List<String> fieldTitleList = fieldTitleMap.get(entry.getKey());
			String[] fieldTitles = fieldTitleList.toArray(new String[0]);
			for (int j = 0; j < fieldTitles.length; j++) {
				HSSFCell cell = row.createCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString(fieldTitles[j]));
			}

			// 写入每条记录
			int rowNum = 1; // 行号
			for (int j = 0, m = recordList.size(); j < m; j++) {
				HSSFRow _row = workbook.getSheetAt(i - 1).createRow(rowNum++);
				Map<String, String> field2ValueMap = recordList.get(j);

				for (int k = 0; k < fieldTitles.length; k++) {
					HSSFCell cell = _row.createCell(k);
					Object value = field2ValueMap.get(fieldTitles[k]);

					if (value == null) {
						cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
					}
					else if (value instanceof String) {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue((String) value);
					}
					else if (value instanceof Boolean) {
						cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
						cell.setCellValue(((Boolean) value).booleanValue());
					}
					else if (value instanceof Integer) {
						cell.setCellValue(((Integer) value).intValue());
					}
					else if (value instanceof Double) {
						cell.setCellValue(((Double) value).doubleValue());
					}
					else if (value instanceof Date) {
						// HSSFCellStyle cellDateStyle = workbook
						// .createCellStyle();
						// cellDateStyle.setDataFormat(dateFormat);
						cell.setCellStyle(cellDateStyle);
						cell.setCellValue((Date) value);
					}
					else {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
		// 合并日期占两行(4个参数，分别为起始行，结束行，起始列，结束列)
		// 行和列都是从0开始计数，且起始结束都会合并
		// 这里是合并excel中日期的两行为一行
//		if(CollectionUtils.isNotEmpty(cellRangeAddresses)) {
//			for (CellRangeAddress cellRangeAddress : cellRangeAddresses) {
//				sheet.addMergedRegion(cellRangeAddress);
//			}
//		}

//		sheetName2RecordListMap.put("报名审核",recordList);
//		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
//		List<String> tis = getRowTitleList();
//		titleMap.put("报名审核", tis);
//		ExportUtils ex = ExportUtils.newInstance();
//		List<CellRangeAddress> cellRangeAddresses = new ArrayList<>();
//		CellRangeAddress cellRangeAddress1 = new CellRangeAddress(1, list.size(), 0, 0);
//		CellRangeAddress cellRangeAddress2 =new CellRangeAddress(1, list.size(), 1, 1);
//		CellRangeAddress cellRangeAddress3 =new CellRangeAddress(1, list.size(), 2, 2);
//		CellRangeAddress cellRangeAddress4 = new CellRangeAddress(1, list.size(), 3, 3);
//		CellRangeAddress cellRangeAddress5 = new CellRangeAddress(1, list.size(), 7, 7);
//		CellRangeAddress cellRangeAddress6 = new CellRangeAddress(1, list.size(), 8, 8);
//		cellRangeAddresses.add(cellRangeAddress1);
//		cellRangeAddresses.add(cellRangeAddress2);
//		cellRangeAddresses.add(cellRangeAddress3);
//		cellRangeAddresses.add(cellRangeAddress4);
//		cellRangeAddresses.add(cellRangeAddress5);
//		cellRangeAddresses.add(cellRangeAddress6);
//		exportXLSFile( titleName+"报名审核通过信息导出", titleMap, sheetName2RecordListMap,cellRangeAddresses, response);
		outputData(workbook, fileName, resp); // 导出文件
	}
}
