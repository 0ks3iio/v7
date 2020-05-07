package net.zdsoft.stuwork.data.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dto.DyStudentRewardPointDto;
import net.zdsoft.stuwork.data.dto.DyStudentRewardSettingDto;
import net.zdsoft.stuwork.data.entity.DyStudentRewardPoint;
import net.zdsoft.stuwork.data.entity.DyStudentRewardProject;
import net.zdsoft.stuwork.data.entity.DyStudentRewardSetting;
import net.zdsoft.stuwork.data.service.DyPermissionService;
import net.zdsoft.stuwork.data.service.DyStudentRewardPointService;
import net.zdsoft.stuwork.data.service.DyStudentRewardProjectService;
import net.zdsoft.stuwork.data.service.DyStudentRewardSettingService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/stuwork") 
public class DyStudentRewardAction extends BaseAction{
	
	@Autowired
	private DyStudentRewardSettingService dyStudentRewardSettingService;
	
	@Autowired
	private DyStudentRewardPointService dyStudentRewardPointService;
	
	@Autowired
	private DyStudentRewardProjectService dyStudentRewardProjectService;
	
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	
	@Autowired
	private ClassRemoteService classRemoteService;
	
	@Autowired
	private DyPermissionService dyPermissionService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	
	@InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
	
	@RequestMapping("/studentReward/studentRewardSettingPage")
    @ControllerInfo(value = "学生奖励设置")
	public String studentRewardSettingPage(ModelMap map,String classesType){
		if(StringUtils.isBlank(classesType)){
			classesType="1";
		}
		
		List<DyStudentRewardSettingDto> studentRewardSettingDtoList = dyStudentRewardProjectService.findListByClassType(classesType,getLoginInfo().getUnitId(),true,StuworkConstants.STUDENT_REWARD_PERIOD_DEFAULT);
		//List<DyStudentRewardSetting> studentRewardSettingList = dyStudentRewardSettingService.findListByClassType(classedType,getLoginInfo().getUnitId());
		map.put("classesType", classesType);
		
		map.put("dyStudentRewardSettingDtoList", studentRewardSettingDtoList);
		return "/stuwork/studentReward/studentRewardSettingList.ftl";
	}
	@RequestMapping("/studentReward/studentRewardByOne")
	@ControllerInfo(value = "学生奖励设置one")
	public String studentRewardByOne(ModelMap map,String classesType){
		if(StringUtils.isBlank(classesType)){
			classesType="3";
		}
		List<DyStudentRewardSettingDto> studentRewardSettingDtoList = dyStudentRewardProjectService.findListByClassType(classesType,getLoginInfo().getUnitId(),true,StuworkConstants.STUDENT_REWARD_PERIOD_DEFAULT);
		map.put("classesType", classesType);
		map.put("dyStudentRewardSettingDtoList", studentRewardSettingDtoList);
		return "/stuwork/studentReward/studentRewardSettingListOne.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/studentReward/settingSave")
    @ControllerInfo(value = "学生奖励设置保存")
	public String studentRewardSettingSave(DyStudentRewardSettingDto studentRewardSettingDto,String classesType,ModelMap map){
		try{
			if(StringUtils.isBlank(classesType)){
				classesType="1";
			}
			List<DyStudentRewardSetting> dyStudentRewardSettings = studentRewardSettingDto.getDyStudentRewardSettings();
			if(CollectionUtils.isNotEmpty(dyStudentRewardSettings)){
				if("3".equals(classesType)){
					dyStudentRewardSettingService.saveSettingsOne(dyStudentRewardSettings,getLoginInfo().getUnitId(),classesType);
				}else
					dyStudentRewardSettingService.saveSettings(dyStudentRewardSettings,getLoginInfo().getUnitId(),classesType);
			}
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@RequestMapping("/studentReward/studentRewardProjectPage")
    @ControllerInfo(value = "学科竞赛设置")
	public String studentRewardProjectPage(String projectId,String settingId,ModelMap map){
		
		List<DyStudentRewardSettingDto> studentRewardSettingDtoList = dyStudentRewardProjectService.findListByClassType(StuworkConstants.STUDENT_REWARD_GAME,getLoginInfo().getUnitId(),true,StuworkConstants.STUDENT_REWARD_PERIOD_DEFAULT);
//		Map<String,List<>>
		JSONObject obj  = new JSONObject();
		for (DyStudentRewardSettingDto dto : studentRewardSettingDtoList) {
			DyStudentRewardProject project = dto.getDyStudentRewardProject();
			List<DyStudentRewardSetting> settings = dto.getDyStudentRewardSettings();
			JSONObject rewardClasses = obj.getJSONObject(project.getRewardClasses());
			if(rewardClasses==null){
				rewardClasses  = new JSONObject();
				JSONObject projectName  = new JSONObject();
				for (DyStudentRewardSetting setting : settings) {
					JSONObject  rewardGrade = projectName.getJSONObject(setting.getRewardGrade());
					if(rewardGrade==null){
						rewardGrade =  new JSONObject();
						//JSONObject rewardLevel = new JSONObject();
						rewardGrade.put(setting.getRewardLevel(), setting.getRewardLevel());
					}else{
//						JSONObject rewardLevel = rewardGrade.getJSONObject(setting.getRewardLevel());
//						if(rewardLevel==null){
//							rewardLevel = new JSONObject();
//						}
//						rewardLevel.put(setting.getRewardLevel(), setting.getRewardLevel());
						rewardGrade.put(setting.getRewardLevel(), setting.getRewardLevel());
					}
							
					projectName.put(setting.getRewardGrade(), rewardGrade);
				
				}
				rewardClasses.put(project.getProjectName(), projectName);
			}else{
				JSONObject projectName = rewardClasses.getJSONObject(project.getProjectName());
				if(projectName==null){
					projectName  = new JSONObject();
					for (DyStudentRewardSetting setting : settings) {
						JSONObject  rewardGrade = projectName.getJSONObject(setting.getRewardGrade());
						if(rewardGrade==null){
							rewardGrade =  new JSONObject();
//							JSONObject rewardLevel = new JSONObject();
//							rewardGrade.put(setting.getRewardLevel(), rewardLevel);
							rewardGrade.put(setting.getRewardLevel(), setting.getRewardLevel());
						}else{
//							JSONObject rewardLevel = rewardGrade.getJSONObject(setting.getRewardLevel());
//							if(rewardLevel==null){
//								rewardLevel = new JSONObject();
//							}
//							rewardLevel.put(setting.getRewardLevel(), setting.getRewardLevel());
//							rewardGrade.put(setting.getRewardLevel(), rewardLevel);
							
							rewardGrade.put(setting.getRewardLevel(), setting.getRewardLevel());
						}
								
						projectName.put(setting.getRewardGrade(), rewardGrade);
					
					}
				}else{
					for (DyStudentRewardSetting setting : settings) {
						JSONObject  rewardGrade = projectName.getJSONObject(setting.getRewardGrade());
						if(rewardGrade==null){
							rewardGrade =  new JSONObject();
//							JSONObject rewardLevel = new JSONObject();
//							rewardGrade.put(setting.getRewardLevel(), rewardLevel);
						}else{
//							JSONObject rewardLevel = rewardGrade.getJSONObject(setting.getRewardLevel());
//							if(rewardLevel==null){
//								rewardLevel = new JSONObject();
//							}
//							rewardLevel.put(setting.getRewardLevel(), setting.getRewardLevel());
//							rewardGrade.put(setting.getRewardLevel(), rewardLevel);
						}
						rewardGrade.put(setting.getRewardLevel(), setting.getRewardLevel());		
						projectName.put(setting.getRewardGrade(), rewardGrade);
					
					}
				}
			
				rewardClasses.put(project.getProjectName(), projectName);
			}
			
			
			
			obj.put(project.getRewardClasses(), rewardClasses);
			
		}
		map.put("hisObj", obj.toJSONString());
		
		
		if(StringUtils.isNotBlank(settingId)&&StringUtils.isNotBlank(projectId)){
			map.put("settingId",settingId);
			map.put("projectId", projectId);
			DyStudentRewardProject project= dyStudentRewardProjectService.findOne(projectId);
			DyStudentRewardSetting setting = dyStudentRewardSettingService.findOne(settingId);
			if(project!=null){
				map.put("rewardClasses",project.getRewardClasses());
				map.put("projectName",project.getProjectName());
			}
			if(setting!=null){
				map.put("rewardGrade",setting.getRewardGrade());
				map.put("rewardLevel",setting.getRewardLevel());
			}
		}
		return "/stuwork/studentReward/studentRewardProjectList.ftl";
	}
	
	
	@ResponseBody
	@RequestMapping("/studentReward/studentRewardSettingDelete")
    @ControllerInfo(value = "学科竞赛删除")
	public String studentRewardSettingDelete(String settingId,String projectId,ModelMap map){
		try{
			dyStudentRewardSettingService.deleteSetting( settingId, projectId);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	
	@ResponseBody
	@RequestMapping("/studentReward/studentRewardGameSave")
    @ControllerInfo(value = "学科竞赛设置保存")
	public String studentRewardGameSave(String rewardClasses,String projectName,String rewardGrade,String rewardLevel,String projectId,String settingId,ModelMap map){
		try{
			dyStudentRewardSettingService.rewardGameSave( rewardClasses, projectName, rewardGrade, rewardLevel,getLoginInfo().getUnitId(),projectId,settingId);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	

	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JSONObject obj = new JSONObject();
		obj.put("1", "2");
		obj.put("1", "3");
		obj.put("2", "3");
		System.out.println(obj.toJSONString());
		String acadyear = "2015-2016";
		int openAcadyearBefore = Integer.parseInt(acadyear.substring(0,4));
		int openAcadyearAfter = Integer.parseInt(acadyear.substring(5,9));
		
		String acadyear1 = (openAcadyearBefore-1)+"-"+(openAcadyearAfter-1);
		System.out.println(acadyear1);
		
		String[] xx =new String[8];
		xx[1]="1";
		xx[3] ="2";
		System.out.println(xx.toString());
		String xxX="1111111111";
		String yyX="1111111111";
		Set<String> x1= new HashSet<String>();
		x1.add(xxX);
		x1.add(yyX);
		System.out.println(x1.size());
		
		float allcount =(float) 1.00999999;
		DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		String p=decimalFormat.format(allcount);//format 返回的是字符串
		System.out.println(p);
//		retunMap.put(student.getId(), Float.valueOf(p));
		
		
		BigDecimal  a = new BigDecimal(100.21);
		BigDecimal  b = new BigDecimal(100);
		Float c = 100.21f;
		Float d = 100.01f;
		System.out.println((c+d));

	}
	
	
	@RequestMapping("/studentReward/studentRewardInputPage")
    @ControllerInfo(value = "奖励录入")
	public String studentInputHead(ModelMap map,String classesType,String acadyear,String semester,HttpServletRequest request){
		
		if(StringUtils.isBlank(classesType)){
			classesType="1";
		}
		
		if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)||classesType.equals(StuworkConstants.STUDENT_REWARD_OTHER)){
			LoginInfo info = getLoginInfo();
			String unitId = info.getUnitId();
//			String acadyear="";
			int semesterInt=0;
			List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
	        if(CollectionUtils.isEmpty(acadyearList)){
				return errorFtl(map,"学年学期不存在");
			}
	        if(StringUtils.isBlank(acadyear)){
	        	Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(0,unitId), Semester.class);
	        	if(se == null){
	            	se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
	            	semesterInt = se.getSemester();
	            	acadyear = se.getAcadyear();
	            }else{
	            	semesterInt = se.getSemester();
	            	acadyear = se.getAcadyear();
	            }
	        }else {
	        	semesterInt = NumberUtils.toInt(semester);
	        }
	        map.put("acadyearList", acadyearList);
	        map.put("semester", semesterInt);
	        map.put("acadyear", acadyear);
		}
		
		map.put("classesType", classesType);
		if(classesType.equals(StuworkConstants.STUDENT_REWARD_FESTIVAL)){
			Pagination page = createPagination();
//			page.setPageSize(10);
			List<DyStudentRewardSettingDto> studentRewardSettingDtoList = dyStudentRewardProjectService.findListByFestival(classesType,getLoginInfo().getUnitId(),page);
			map.put("dyStudentRewardSettingDtoList", studentRewardSettingDtoList);
			List<DyStudentRewardSettingDto> dyStudentRewardSettingDefaultDtoList = dyStudentRewardProjectService.findListByClassType(classesType,getLoginInfo().getUnitId(),false,"0");
			map.put("dyStudentRewardSettingDefaultDtoList", dyStudentRewardSettingDefaultDtoList);
			sendPagination(request, map, page);
			map.put("_pageSize", page.getPageSize());
			map.put("_pageIndex", page.getPageIndex());
			//map.put("dyStudentRewardSettingDtoList", studentRewardSettingDtoList);
		}else if(classesType.equals(StuworkConstants.STUDENT_REWARD_OTHER)){
			List<DyStudentRewardPoint> pointList = new ArrayList<DyStudentRewardPoint>();
			pointList = dyStudentRewardPointService.findBySettingId(StuworkConstants.STUDENT_OTHER_ID,NumberUtils.toInt(map.get("semester").toString()),map.get("acadyear").toString());
			map.put("pointList", pointList);
			return "/stuwork/studentReward/studentRewardOtherInputPage.ftl";
		}else {
			
			List<DyStudentRewardSettingDto> studentRewardSettingDtoList = dyStudentRewardProjectService.findListByClassType(classesType,getLoginInfo().getUnitId(),false,null);
			map.put("dyStudentRewardSettingDtoList", studentRewardSettingDtoList);
		}
		return "/stuwork/studentReward/studentRewardInputPage.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/studentReward/studentRewardDel")
	@ControllerInfo(value = "节假日删除，届次")
	public String studentRewardDel(ModelMap map,String pIds){
		try{
			if(StringUtils.isNotBlank(pIds)){
				String[] projectIds=pIds.split(",");
				dyStudentRewardProjectService.deletByIds(projectIds);
				dyStudentRewardSettingService.deleteByProIds(getLoginInfo().getUnitId(), projectIds);
				dyStudentRewardPointService.updateIsDeteletd(projectIds);
			}else{
				return error("操作失败！删除类型不能为空！");
			}
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	@ResponseBody
	@RequestMapping("/studentReward/studentRewardFestivalAdd")
	@ControllerInfo(value = "节假日新增，年份")
	public String studentRewardFestivalAdd(ModelMap map,String pIds,String acadyear,String semester,String rewardPeriod,String rewardClasses){
		try{
			if(StringUtils.isBlank(rewardPeriod)||StringUtils.isBlank(rewardClasses)){
				return error("操作失败！年份或节假日类型不能为空！");
			}
			if(StringUtils.isNotBlank(pIds)){
				dyStudentRewardSettingService.updateFestival(pIds.split(","), rewardPeriod, rewardClasses, acadyear, semester, getLoginInfo().getUnitId());
			}else{
				dyStudentRewardSettingService.addFestival(rewardPeriod,rewardClasses,acadyear,semester,getLoginInfo().getUnitId());
			}
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@ResponseBody
	@RequestMapping("/studentReward/studentRewardOtherAdd")
	@ControllerInfo(value = "其他奖励新增")
	public String studentRewardOtherAdd(ModelMap map,String acadyear,String semester,String remark,String stuId){
		try{
			if(StringUtils.isNotBlank(remark)&&StringUtils.isNotBlank(stuId)){
				dyStudentRewardPointService.addOtherPoint(remark,stuId,acadyear,semester,getLoginInfo().getUnitId());
			}else{
				return error("操作失败！项目名称或学生不能为空！");
			}
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@RequestMapping("/studentReward/studentRewardInputList")
	@ControllerInfo(value = "奖励录入")
	public String studentRewardInputList(HttpServletRequest request,ModelMap map,String settingId,String _pageIndex,String _pageSize, String projectId){
		map.put("projectId", projectId);
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();

		String acadyear  =request.getParameter("acadyear");
		Integer semester  =NumberUtils.toInt(request.getParameter("semester"));
		String field = StringUtils.trimToEmpty(request.getParameter("field"));
		String searchWord = StringUtils.trimToEmpty(request.getParameter("stuSearch"));
		map.put("field", field);
		map.put("stuSearch", searchWord);
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        if(StringUtils.isBlank(acadyear)){
        	Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(0,unitId), Semester.class);
        	if(se == null){
            	se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
            	semester = se.getSemester();
            	acadyear = se.getAcadyear();
            }else{
            	semester = se.getSemester();
            	acadyear = se.getAcadyear();
//            	DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, semester, new Date()), DateInfo.class);
//            	if(dateInfo != null){
//            		week =dateInfo.getWeek();
//            		map.put("week", week);
//            	}
            }
        }

		DyStudentRewardSetting setting = dyStudentRewardSettingService.findOne(settingId);
		if(setting!=null){
			DyStudentRewardProject project = dyStudentRewardProjectService.findOne(setting.getProjectId());
			if(project!=null){
				DyStudentRewardPointDto dto = new DyStudentRewardPointDto();
				dto.setDyStudentRewardProject(project);
				dto.setDyStudentRewardSetting(setting);
//				map.put("setting", setting);
//				map.put("project", project);
				map.put("classesType", project.getClassesType());
				List<DyStudentRewardPoint> pointList = new ArrayList<DyStudentRewardPoint>();
				if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(project.getClassesType())){
					map.put("_pageIndex", _pageIndex);
					map.put("_pageSize", _pageSize);
					pointList = dyStudentRewardPointService.findBySettingIdStuSearch(settingId,Integer.parseInt(project.getSemester()),project.getAcadyear(), field, searchWord);
				}else{
					pointList = dyStudentRewardPointService.findBySettingIdStuSearch(settingId,semester,acadyear, field, searchWord);
				}
//				map.put("pointList", pointList);
				dto.setDyStudentRewardPoints(pointList);
				map.put("dyStudentRewardPointDto", dto);
				
				if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(project.getClassesType())){
					map.put("acadyear", project.getAcadyear());
			        map.put("semester", Integer.parseInt(project.getSemester()));
				}else{
					map.put("acadyearList", acadyearList);
			        map.put("semester", semester);
			        map.put("acadyear", acadyear);
				}

			}else{
				return errorFtl(map,"对应的录入项目不存在");
			}
		}else{
			return errorFtl(map,"对应的录入项目不存在");
		}
		
		return "/stuwork/studentReward/studentRewardInputList.ftl";
	}
	
	
	@ResponseBody
	@RequestMapping("/studentReward/pointSave")
    @ControllerInfo(value = "")
	public String pointSave(DyStudentRewardPointDto pointDto,String acadyear,String semester,ModelMap map){
		try{
			dyStudentRewardPointService.saveDto(pointDto,acadyear,semester);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@ResponseBody
	@RequestMapping("/studentReward/studentRewardPointDelete")
    @ControllerInfo(value = "")
	public String pointDelete(String pointId,ModelMap map){
		try{
			if(StringUtils.isNotBlank(pointId)){
				dyStudentRewardPointService.delete(pointId);
			}else{
				return error("操作失败！删除的对象不存在!");
			}
		}catch(Exception e){
			log.error(e.getMessage(), e);
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}

	@ResponseBody
	@RequestMapping("/studentReward/studentRewardPointDeleteBatch")
	@ControllerInfo(value = "批量删除学生奖励信息")
	public String pointDeleteBatch(@RequestParam(value = "ids[]") String[] ids, ModelMap map){
		try{
			if(ArrayUtils.isNotEmpty(ids)){
				List<DyStudentRewardPoint> pos = dyStudentRewardPointService.findListByIdIn(ids);
				if (CollectionUtils.isNotEmpty(pos)) {
					dyStudentRewardPointService.deleteAll(pos.toArray(new DyStudentRewardPoint[0]));
				} else {
					return error("操作失败！删除的对象不存在!");
				}
			}else{
				return error("操作失败！删除的对象不存在!");
			}
		}catch(Exception e){
			log.error(e.getMessage(), e);
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
	}
	
	@RequestMapping("/studentReward/studentRewardPointEdit")
    @ControllerInfo(value = "")
	public String pointEdit(String pointId, String classesType, String projectId, String settingId, ModelMap map){
		if(StringUtils.isBlank(classesType)){
			classesType="1";
		}
		map.put("pointId", pointId);
		map.put("settingId", settingId);
		map.put("projectId", projectId);
		map.put("classesType", classesType);
		if(classesType.equals(StuworkConstants.STUDENT_REWARD_FESTIVAL)){
			Pagination page = createPagination();
//			page.setPageSize(10);
			List<DyStudentRewardSettingDto> studentRewardSettingDtoList = dyStudentRewardProjectService.findListByFestival(classesType,getLoginInfo().getUnitId(),page);
			map.put("dyStudentRewardSettingDtoList", studentRewardSettingDtoList);
			List<DyStudentRewardSettingDto> dyStudentRewardSettingDefaultDtoList = dyStudentRewardProjectService.findListByClassType(classesType,getLoginInfo().getUnitId(),false,"0");
			map.put("dyStudentRewardSettingDefaultDtoList", dyStudentRewardSettingDefaultDtoList);
			//map.put("dyStudentRewardSettingDtoList", studentRewardSettingDtoList);
		}else if(classesType.equals(StuworkConstants.STUDENT_REWARD_OTHER)){
			List<DyStudentRewardPoint> pointList = new ArrayList<DyStudentRewardPoint>();
			pointList = dyStudentRewardPointService.findBySettingId(StuworkConstants.STUDENT_OTHER_ID,NumberUtils.toInt(map.get("semester").toString()),map.get("acadyear").toString());
			map.put("pointList", pointList);
			return "/stuwork/studentReward/studentRewardOtherInputPage.ftl";
		}else {
			
			List<DyStudentRewardSettingDto> studentRewardSettingDtoList = dyStudentRewardProjectService.findListByClassType(classesType,getLoginInfo().getUnitId(),false,null);
			map.put("dyStudentRewardSettingDtoList", studentRewardSettingDtoList);
		}
		return "/stuwork/studentReward/studentRewardPointEdit.ftl";	
	}
	
	@ResponseBody
	@RequestMapping("/studentReward/studentRewardPointUpdate")
    @ControllerInfo(value = "")
	public String pointUpdate(String pointId, String projectId, ModelMap map){
		try{
			if(StringUtils.isNotBlank(pointId)){
				DyStudentRewardPoint point = dyStudentRewardPointService.findOne(pointId);
				point.setProjectId(projectId.split("-")[0]);
				point.setSettingId(projectId.split("-")[1]);

				DyStudentRewardSetting setting = dyStudentRewardSettingService.findOne(projectId.split("-")[1]);
				if(StringUtils.isBlank(setting.getRewardPoint())){
					point.setRewardPoint(0);
				}else{
					point.setRewardPoint(Float.parseFloat(setting.getRewardPoint()));
				}
				dyStudentRewardPointService.save(point);
			}else{
				return error("操作失败！删除的对象不存在!");
			}
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@RequestMapping("/studentReward/studentRewardSearchPage")
    @ControllerInfo(value = "")
	public String studentSearchHead(ModelMap map){
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
		if(null!=sem){
			String acadyear = sem.getAcadyear();
			int semester = sem.getSemester();
			map.put("acadyear", acadyear);
			map.put("semester", semester);
			
		}else{
			return errorFtl(map,"当前时间不在学年学期内，无法维护！");
		}

		//行政班级
		List<Clazz> clazzs =  SUtils.dt(classRemoteService.findBySchoolId(getLoginInfo().getUnitId()),new TR<List<Clazz>>() {});
		//班级权限
		Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
		
		Iterator<Clazz> it = clazzs.iterator();
		List<String> gradeIds = new ArrayList<>();
		while(it.hasNext()) {
		  Clazz clazz = it.next();
		  if (clazz.getIsGraduate()==1) {
			  it.remove();
			  continue;
		  }
		  if (!classPermission.contains(clazz.getId())) {
			  it.remove();
			  continue;
		  }
		  if(!gradeIds.contains(clazz.getGradeId())){
		  	gradeIds.add(clazz.getGradeId());
		  }
		}
		map.put("clazzList", clazzs);
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])), Grade.class);
		gradeList = gradeList.stream().sorted((a,b)->{
			if(a.getSection() == b.getSection()){
				return b.getOpenAcadyear().compareTo(a.getOpenAcadyear());
			}
			return a.getSection() - b.getSection();
		}).collect(Collectors.toList());
		map.put("gradeList", gradeList);
		map.put("acadyearList", acadyearList);
		return "/stuwork/studentReward/studentRewardSearchHead.ftl";
	}

	@RequestMapping("/studentReward/studentRewardClass")
	@ResponseBody
	public List<Clazz> studentRewardClass(String gradeId, ModelMap map){
		String schId = getLoginInfo().getUnitId();
		List<Clazz> clsList = null;
		if (StringUtils.isNotEmpty(gradeId)) {
			clsList = Clazz.dt(classRemoteService.findBySchoolIdGradeId(schId, gradeId));
		} else {
			clsList = Clazz.dt(classRemoteService.findBySchoolId(schId));
		}
		Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
		if(CollectionUtils.isEmpty(classPermission)){
			return new ArrayList<>();
		}
		clsList = clsList.stream().filter(e->(e.getIsGraduate() == 0 && classPermission.contains(e.getId()))).collect(Collectors.toList());
		return clsList;
	}

	@RequestMapping("/studentReward/studentRewardProject")
	@ResponseBody
	public List<DyStudentRewardProject> studentRewardProject(String classType, ModelMap map){
		List<DyStudentRewardProject> projects;
		if (StringUtils.isNotEmpty(classType)) {
			projects = dyStudentRewardProjectService.findByClassesType(classType, getLoginInfo().getUnitId());
		} else {
			projects = dyStudentRewardProjectService.findByUnitId(getLoginInfo().getUnitId());
		}
		if(CollectionUtils.isNotEmpty(projects)){
			Set<String> rcs = new HashSet<>();
			Iterator<DyStudentRewardProject> pit =  projects.iterator();
			while (pit.hasNext()){
				DyStudentRewardProject pr = pit.next();
				if(!rcs.contains(pr.getRewardClasses())){
					rcs.add(pr.getRewardClasses());
					continue;
				}
				pit.remove();
			}
		}
		return projects;
	}
	
	@RequestMapping("/studentReward/studentRewardSearchList")
	public String studentRewardSearchList(String classId,String acadyear, String semester, String studentCode, String studentName, ModelMap map, HttpServletRequest request){
		//班级权限
		String schId = getLoginInfo().getUnitId();
		Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
		List<String> clsIds = new ArrayList<>();
		if (StringUtils.isNotBlank(classId)) {
			clsIds.add(classId);
		} else {
			String gradeId = request.getParameter("gradeId");
			List<Clazz> clsList = null;
			if (StringUtils.isNotEmpty(gradeId)) {
				clsList = Clazz.dt(classRemoteService.findBySchoolIdGradeId(schId, gradeId));
			} else {
				clsList = Clazz.dt(classRemoteService.findBySchoolId(schId));
			}
			clsIds = EntityUtils.getList(clsList, Clazz::getId);
			clsIds = (List<String>)CollectionUtils.retainAll(classPermission, clsIds);
		}

		Student stuSear = new Student();
		stuSear.setStudentCode(StringUtils.trimToEmpty(studentCode));
		stuSear.setStudentName(StringUtils.trimToEmpty(studentName));
		List<Student> students = SUtils.dt(studentRemoteService.findByIdsClaIdLikeStuCodeNames(schId, null, clsIds.toArray(new String[0]), SUtils.s(stuSear), null),new TR<List<Student>>() {});
//		List<String> studentIds = EntityUtils.getList(students, "id");
		if(CollectionUtils.isEmpty(students)){
			return "/stuwork/studentReward/studentRewardSearchList.ftl";
		}

		Set<String> stuIdSet = EntityUtils.getSet(students, Student::getId);
//		Pagination page = createPagination();
		if(CollectionUtils.isNotEmpty(stuIdSet)){//TODO
			List<DyStudentRewardPoint> dyStudentRewardPointList = getPoints(stuIdSet.toArray(new String[0]), schId, acadyear, semester);
			if(CollectionUtils.isEmpty(dyStudentRewardPointList)){
				return "/stuwork/studentReward/studentRewardSearchList.ftl";
			}
			// 奖励类别、类型过滤
			String projectId = request.getParameter("projectId");
			String clsType = request.getParameter("clsType");
			Set<String> proIds = new HashSet<>();
			boolean checkPro = StringUtils.isNotEmpty(projectId) || StringUtils.isNotEmpty(clsType);
			List<DyStudentRewardProject> dyStudentRewardProjectList = null;
			if (checkPro) {
				if (!StuworkConstants.STUDENT_REWARD_OTHER.equals(clsType)) {
					if(StringUtils.isNotEmpty(projectId)){
						dyStudentRewardProjectList = dyStudentRewardProjectService.findByClasses(projectId,clsType,schId);
					} else {
						dyStudentRewardProjectList = dyStudentRewardProjectService.findByClassesType(clsType, schId);
					}
					proIds.addAll(EntityUtils.getSet(dyStudentRewardProjectList, DyStudentRewardProject::getId));
				} else {
					proIds.add(StuworkConstants.STUDENT_OTHER_ID);
				}
			}
			Iterator<DyStudentRewardPoint> pit = dyStudentRewardPointList.iterator();
			Set<String> projectIdSet = new HashSet<String>();
			while(pit.hasNext()){
				DyStudentRewardPoint item = pit.next();
				if(checkPro && !proIds.contains(item.getProjectId())){
					pit.remove();
					continue;
				}
				projectIdSet.add(item.getProjectId());
			}
			if (!StuworkConstants.STUDENT_REWARD_OTHER.equals(clsType)
					&& dyStudentRewardProjectList == null && projectIdSet.size() > 0) {
				dyStudentRewardProjectList = dyStudentRewardProjectService.findListByIds(projectIdSet.toArray(new String[0]));
			}
			Map<String, DyStudentRewardProject> projectMap = new HashMap<String, DyStudentRewardProject>();
			if (CollectionUtils.isNotEmpty(dyStudentRewardProjectList)) {
				for(DyStudentRewardProject item : dyStudentRewardProjectList){
					projectMap.put(item.getId(), item);
				}
			}
			int i = 1;
			for(DyStudentRewardPoint item : dyStudentRewardPointList){
				item.setOrderNum(i);
				i++;
				DyStudentRewardProject project = projectMap.get(item.getProjectId());
				if(project!=null && StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(project.getClassesType())) {
					item.setProjectName(item.getProjectName()+"/第"+project.getRewardPeriod()+"年");
				}else {
					if(StuworkConstants.STUDENT_OTHER_ID.equals(item.getProjectId())) {
						item.setProjectName(item.getRemark());
					}
				}
			}
			map.put("dyStudentRewardPointList", dyStudentRewardPointList);
		}
//		sendPagination(request, map, page);
		return "/stuwork/studentReward/studentRewardSearchList.ftl";
	}

	/**
	 * 获取学生奖励记录
	 */
	private List<DyStudentRewardPoint> getPoints(String[] studentIds, String schId, String acadyear, String semester){
		List<DyStudentRewardPoint> dyStudentRewardPointList;
		if (studentIds.length <= 800) {
			dyStudentRewardPointList = dyStudentRewardPointService.getStudentRewardPointByStudentId(schId, studentIds, acadyear, semester, null,true);
		} else {
			dyStudentRewardPointList = new ArrayList<>();
			int per = 800;
			int rou = studentIds.length % per==0?(studentIds.length/per):(studentIds.length/per+1);
			for (int i = 0;i<rou;i++) {
				int st = i*per;
				int et = (i+1)*per;
				if(et > studentIds.length){
					et = studentIds.length;
				}
				dyStudentRewardPointList.addAll(dyStudentRewardPointService.getStudentRewardPointByStudentId(schId, (String[])ArrayUtils.subarray(studentIds, st, et), acadyear, semester, null,true));
			}
		}
		return dyStudentRewardPointList;
	}

	@RequestMapping("/studentReward/studentRewardSearchExport")
	public void punishScoreQueryExport(String classId,String acadyear, String semester, String studentCode, String studentName, ModelMap map, HttpServletRequest request,HttpServletResponse response){
		String schId = getLoginInfo().getUnitId();
		//班级权限
		Set<String> classPermission = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());

		List<String> clsIds = new ArrayList<>();
		if (StringUtils.isNotBlank(classId)) {
			clsIds.add(classId);
		} else {
			String gradeId = request.getParameter("gradeId");
			List<Clazz> clsList = null;
			if (StringUtils.isNotEmpty(gradeId)) {
				clsList = Clazz.dt(classRemoteService.findBySchoolIdGradeId(schId, gradeId));
			} else {
				clsList = Clazz.dt(classRemoteService.findBySchoolIdAcadyear(schId, acadyear));
			}
			clsIds = EntityUtils.getList(clsList, Clazz::getId);
			clsIds = (List<String>)CollectionUtils.retainAll(classPermission, clsIds);
		}

		Student stuSear = new Student();
		stuSear.setStudentCode(StringUtils.trimToEmpty(studentCode));
		stuSear.setStudentName(StringUtils.trimToEmpty(studentName));
		List<Student> students = SUtils.dt(studentRemoteService.findByIdsClaIdLikeStuCodeNames(schId, null, clsIds.toArray(new String[0]), SUtils.s(stuSear), null),new TR<List<Student>>() {});
		List<String> stuIdSet = EntityUtils.getList(students, Student::getId);
		if(CollectionUtils.isEmpty(stuIdSet)){
			doExport(new ArrayList<DyStudentRewardPoint>(), acadyear, semester, response);
			return;
		}

		List<DyStudentRewardPoint> dyStudentRewardPointList = getPoints(stuIdSet.toArray(new String[0]), schId, acadyear, semester);
		if(CollectionUtils.isEmpty(dyStudentRewardPointList)){
			doExport(new ArrayList<DyStudentRewardPoint>(), acadyear, semester, response);
			return;
		}
		String projectId = request.getParameter("projectId");
		String clsType = request.getParameter("clsType");
		Set<String> proIds = new HashSet<>();
		boolean checkPro = StringUtils.isNotEmpty(projectId) || StringUtils.isNotEmpty(clsType);
		List<DyStudentRewardProject> dyStudentRewardProjectList = null;
		if (checkPro) {
			if(StringUtils.isNotEmpty(projectId)){
				proIds.add(projectId);
			} else if(StringUtils.isNotEmpty(clsType)){
				dyStudentRewardProjectList = dyStudentRewardProjectService.findByClassesType(clsType, getLoginInfo().getUnitId());
				proIds.addAll(EntityUtils.getSet(dyStudentRewardProjectList, DyStudentRewardProject::getId));
			}
			dyStudentRewardPointList = dyStudentRewardPointList.stream().filter(e->proIds.contains(e.getProjectId())).collect(Collectors.toList());
		}

		if(CollectionUtils.isEmpty(dyStudentRewardPointList)){
			doExport(new ArrayList<DyStudentRewardPoint>(), acadyear, semester, response);
			return;
		}
		int i = 1;
		for(DyStudentRewardPoint item : dyStudentRewardPointList){
			item.setOrderNum(i);
			i++;
		}
		doExport(dyStudentRewardPointList, acadyear, semester, response);
	}
	
	public void doExport(List<DyStudentRewardPoint> dyStudentRewardPointList, String acadyear, String semester, HttpServletResponse response){
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		for(DyStudentRewardPoint item : dyStudentRewardPointList){
			Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("序号", String.valueOf(item.getOrderNum()));
			sMap.put("班级", item.getClassName());
			sMap.put("姓名", item.getStudentName());
			sMap.put("学号", item.getStucode());
			sMap.put("类型", item.getRewardClasses());
			sMap.put("项目", item.getProjectName());
			sMap.put("备注", item.getRemark());
			sMap.put("级别", item.getRewardGrade());		
			sMap.put("奖级", item.getRewardLevel());
			sMap.put("分值", String.valueOf(item.getRewardPoint()));
			sMap.put("奖励时间", String.valueOf(item.getCreationTime()).split(" ")[0]+" "+String.valueOf(item.getCreationTime()).split(" ")[1].split(":")[0]+":"+String.valueOf(item.getCreationTime()).split(" ")[1].split(":")[1]);
			recordList.add(sMap);
		}
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile(acadyear+"学年第"+semester+"学期学生奖励信息", titleMap, sheetName2RecordListMap, response);	
	}
	
	public String getObjectName() {
		return "学生奖励";
	}
	
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("序号");
		tis.add("班级");
		tis.add("姓名");
		tis.add("学号");
		tis.add("类型");
		tis.add("项目");
		tis.add("备注");
		tis.add("级别");
		tis.add("奖级");
		tis.add("分值");
		tis.add("奖励时间");
		return tis;
	}
		
}
