package net.zdsoft.eclasscard.data.action.show;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.AttanceGradeDto;
import net.zdsoft.eclasscard.data.dto.DormBuildingStuNumDto;
import net.zdsoft.eclasscard.data.dto.NoneMsgDto;
import net.zdsoft.eclasscard.data.dto.StuClockResultDto;
import net.zdsoft.eclasscard.data.entity.*;
import net.zdsoft.eclasscard.data.service.*;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import net.zdsoft.system.entity.config.UnitIni;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import net.zdsoft.system.remote.service.UnitIniRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
@Controller
@RequestMapping("/eccShow/eclasscard")
public class EccIndexAction extends BaseAction {
	
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private EccBulletinService eccBulletinService;
	@Autowired
	private EccPhotoAlbumService eccPhotoAlbumService;
	@Autowired
	private EccClassDescService eccClassDescService;
	@Autowired
	private EccClockInService eccClockInService;
	@Autowired
	private EccDormAttenceService eccDormAttenceService;
	@Autowired
	private EccStudormAttenceService eccStudormAttenceService;
	@Autowired
	private EccAttenceDormGradeService eccAttenceDormGradeService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	@Autowired
	private UnitIniRemoteService unitIniRemoteService;
	private static OpenApiOfficeService openApiOfficeService;

    public static OpenApiOfficeService getOpenApiOfficeService() {
        if (openApiOfficeService == null) {
            openApiOfficeService = Evn.getBean("openApiOfficeService");
            if(openApiOfficeService == null){
				System.out.println("openApiOfficeService为null，需开启dubbo服务");
			}
        }
        return openApiOfficeService;
    }
	
	@RequestMapping("/showIndex")
	@ControllerInfo("进入班牌首页")
    public String showIndex(String cardId,String deviceNumber,String view,HttpServletRequest request,ModelMap map){
		NoneMsgDto msgDto = new NoneMsgDto();
		if(StringUtils.isBlank(view)){
			view = EccConstants.ECC_VIEW_1;
		}
		//TODO 客户端没更新还是走deviceNumber
		if(StringUtils.isBlank(cardId)&& StringUtils.isNotBlank(deviceNumber)){
			List<EccInfo> eccInfos = eccInfoService.findListBy("name", deviceNumber);
			if(CollectionUtils.isNotEmpty(eccInfos)){
				if(eccInfos.get(0)!=null){//德育版，客户端暂时不更新
					if(UnitIni.ECC_USE_VERSION_HW.equals(EccNeedServiceUtils.getEClassCardVerison(eccInfos.get(0).getUnitId()))){
						cardId = eccInfos.get(0).getId();
					}
				}
			}
		}
		if(StringUtils.isBlank(cardId)){
			map.put("cardId", cardId);
			map.put("view", view);
			msgDto.setBtn(false);
			msgDto.setMsg("请登入设置界面，设置班牌号");
			map.put("msgDto", msgDto);
			return "/eclasscard/common/show/hintPage.ftl";
		}
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			map.put("cardId", cardId);
			map.put("view", view);
			msgDto.setBtn(false);
			msgDto.setMsg("班牌已删除，请重新设置");
			map.put("msgDto", msgDto);
			return "/eclasscard/common/show/hintPage.ftl";
		}
		if(UnitIni.ECC_USE_VERSION_STANDARD.equals(EccNeedServiceUtils.getEClassCardVerison(eccInfo.getUnitId()))){
			return "redirect:/eccShow/eclasscard/standard/showIndex?cardId="+cardId+"&view="+view;
//			return "redirect:/eccShow/eclasscard/standard/showindex/fullbulletin?deviceNumber="+deviceNumber+"&view="+view;
		}
		String basePath = request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath();
		if(StringUtils.isBlank(eccInfo.getType())){
			msgDto.setBtn(true);
			msgDto.setMsg("请到后台，配置班牌信息");
			map.put("msgDto", msgDto);
			return "/eclasscard/common/show/hintPage.ftl";
		}
		if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
			Clazz clazz = SUtils.dt(classRemoteService.findOneById(eccInfo.getClassId()),new TR<Clazz>() {});
			if(clazz == null){
				msgDto.setBtn(true);
				msgDto.setMsg("请到后台，配置班牌班级信息");
				map.put("msgDto", msgDto);
				return "/eclasscard/common/show/hintPage.ftl";
			}
		}
		map.put("cardId", cardId);
		map.put("view", view);
		map.put("eccInfo", eccInfo);
		if("https".equals(request.getScheme())){
			map.put("webSocketUrl", "wss://" + basePath + "/eClassCard/webSocketServer?sid="+cardId);
			map.put("eccIndexUrl", "https://" + basePath + "/eccShow/eclasscard/showIndex?cardId="+cardId+"&view="+view);
			map.put("sockJSUrl", "https://" + basePath + "/eClassCard/sockjs/webSocketServer?sid="+cardId);
		}else{
			map.put("webSocketUrl", "ws://" + basePath + "/eClassCard/webSocketServer?sid="+cardId);
			map.put("eccIndexUrl", "http://" + basePath + "/eccShow/eclasscard/showIndex?cardId="+cardId+"&view="+view);
			map.put("sockJSUrl", "http://" + basePath + "/eClassCard/sockjs/webSocketServer?sid="+cardId);
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/verticalshow/eccIndex.ftl";
		}else{
			return "/eclasscard/show/eccIndex.ftl";
		}
	}
	
	@RequestMapping("/showIndex/showHeader")
	@ControllerInfo("班牌头部")
    public String showHeader(String back,String view,String type,ModelMap map){
		if("1".equals(back)){
			map.put("back", false);
		}else{
			map.put("back", true);
		}
		map.put("type", type);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/verticalshow/eccHeader.ftl";
		}else{
			return "/eclasscard/show/eccHeader.ftl";
		}
	}
	@RequestMapping("/showIndex/showFooter")
	@ControllerInfo("班牌底部")
	public String showFooter(String view,String cardId,String type,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
		map.put("eccInfo", eccInfo);
		map.put("type", type);
		return "/eclasscard/verticalshow/eccFooter.ftl";
	}
	
	@RequestMapping("/showIndex/class/info")
	@ControllerInfo("班牌行政班信息")
	public String showIndexClassInfo(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
			if(StringUtils.isNotBlank(eccInfo.getClassId())){
				Clazz clazz = SUtils.dt(classRemoteService.findOneById(eccInfo.getClassId()),new TR<Clazz>() {});
				if(clazz!=null){
					List<Student> students = SUtils.dt(studentRemoteService.findByClassIds(new String[]{clazz.getId()}),new TR<List<Student>>() {});
					User user = SUtils.dt(userRemoteService.findByOwnerId(clazz.getTeacherId()),new TR<User>() {});
					Grade grade=SUtils.dt(gradeRemoteService.findOneById(clazz.getGradeId()),new TR<Grade>() {});
					if(CollectionUtils.isNotEmpty(students)){
						map.put("classStuNum", students.size());
					}
					if(grade!=null){
						map.put("className", grade.getGradeName()+clazz.getClassName());
					}
					if(user!=null){
						map.put("teacherName", user.getRealName());
						map.put("teacherUserName", user.getUsername());
					}
				}
				//查询当前时间，该班级下的请假学生ids
				Set<String> studentIds = Sets.newHashSet();
				if(getOpenApiOfficeService()!=null){
					String jsonStr1 = getOpenApiOfficeService().getHwStuLeavesByUnitId(eccInfo.getUnitId(), eccInfo.getClassId(), "1", null, new Date());
					String jsonStr2 = getOpenApiOfficeService().getHwStuLeavesByUnitId(eccInfo.getUnitId(), eccInfo.getClassId(), "2", null, new Date());
					JSONArray strings1 = EccUtils.getResultArray(jsonStr1, "studentIds");
					JSONArray strings2 = EccUtils.getResultArray(jsonStr2, "studentIds");
					for (int i = 0; i < strings1.size(); i++) {
						studentIds.add(strings1.get(i).toString());
					}
					for (int i = 0; i < strings2.size(); i++) {
						studentIds.add(strings2.get(i).toString());
					}
				}
				String leaveStusName = "";
				if(studentIds.size()>0){
					List<User> users = SUtils.dt(userRemoteService.findByOwnerIds(studentIds.toArray(new String[studentIds.size()])),new TR<List<User>>() {});
					int index = 0;
					for(User user:users){
						if(StringUtils.isBlank(leaveStusName)){
							leaveStusName = user.getRealName();
						}else{
							leaveStusName += ","+user.getRealName();
							if(EccConstants.ECC_VIEW_2.equals(view)&&index>0){
								leaveStusName+="...";
								break;
							}
						}
						index++;
					}
				}
				if(StringUtils.isBlank(leaveStusName)){
					leaveStusName="无请假人员";
				}
				map.put("leaveStusName", leaveStusName);
				map.put("leaveStus", studentIds.size());
			}
			map.put("unitId", eccInfo.getUnitId());
			map.put("classId", eccInfo.getClassId());
			if(EccConstants.ECC_VIEW_2.equals(view)){
				return "/eclasscard/verticalshow/information/eccClassInfo.ftl";
			}else{
				return "/eclasscard/show/information/eccClassInfo.ftl";
			}
	}
	
	@RequestMapping("/showIndex/tclass/info")
	@ControllerInfo("班牌非行政班信息")
	public String showIndexTclassInfo(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
		if(StringUtils.isNotBlank(eccInfo.getPlaceId())){
			TeachPlace place =  SUtils.dt(teachPlaceRemoteService.findTeachPlaceById(eccInfo.getPlaceId()),new TR<TeachPlace>() {});
			map.put("placeName", place.getPlaceName());
			map.put("placeNum", place.getPlaceNum());
			map.put("placeType", place.getPlaceType());
			map.put("placeAddress", place.getPlaceAddress());
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/verticalshow/information/eccTclassInfo.ftl";
		}else{
			return "/eclasscard/show/information/eccTclassInfo.ftl";
		}
	}
	
	@RequestMapping("/showIndex/dorm/info")
	@ControllerInfo("班牌寝室楼信息")
	public String showIndexDormInfo(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, eccInfo.getUnitId()), Semester.class);
		if(StringUtils.isNotBlank(eccInfo.getPlaceId())){
			if(semester==null){
				map.put("placeName", "");
				map.put("roomNums", 0);
				map.put("studentNums", 0);
			}else{
				//德育寝室楼接口
				String jsonStr = stuworkRemoteService.findRoomNumAndStuNumByBuildingId(eccInfo.getUnitId(), semester.getAcadyear(), semester.getSemester()+"", eccInfo.getPlaceId());
				DormBuildingStuNumDto buildingStuNumDto  = SUtils.dc(jsonStr,DormBuildingStuNumDto.class);
				if(buildingStuNumDto!=null){
					map.put("placeName", buildingStuNumDto.getBuildingName());
					map.put("roomNums", buildingStuNumDto.getRoomNums());
					map.put("studentNums", buildingStuNumDto.getStudentNums());
				}
			}
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/verticalshow/information/eccDormInfo.ftl";
		}else{
			return "/eclasscard/show/information/eccDormInfo.ftl";
		}
	}
		
	@RequestMapping("/showIndex/description")
	@ControllerInfo("班牌班级简介")
	public String showClassDescription(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
		EccClassDesc classDesc = eccClassDescService.findOneBy("classId", eccInfo.getClassId());
		if(classDesc==null){
			classDesc = new EccClassDesc();
		}
		map.put("classDesc",classDesc);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			if(StringUtils.isNotBlank(classDesc.getContent())){
				String conStr = EccUtils.delHTMLTag(classDesc.getContent());
				if(StringUtils.isNotBlank(conStr)&&conStr.length()>120){
					conStr = conStr.substring(0, 120)+"...";
				}
				classDesc.setContent(conStr);
			}
			return "/eclasscard/verticalshow/information/eccClassDescription.ftl";
		}else{
			return "/eclasscard/show/information/eccClassDescription.ftl";
		}
	}
	
	@RequestMapping("/showIndex/description/detail")
	@ControllerInfo("班牌班级简介详情")
	public String showClassDescriptionDetail(String id,String view,ModelMap map){
		EccClassDesc classDesc = eccClassDescService.findOne(id);
		if(classDesc==null){
			classDesc = new EccClassDesc();
		}
		map.put("classDesc",classDesc);
		return "/eclasscard/verticalshow/eccIndexClassDescDetail.ftl";
	}
	
	@RequestMapping("/showIndex/album")
	@ControllerInfo("班牌相册")
	public String showClassAlbum(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
		List<EccPhotoAlbum> albums = eccPhotoAlbumService.findByShowEccInfo(eccInfo.getId());
		map.put("albums",albums);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/verticalshow/album/eccClassAlbum.ftl";
		}else{
			map.put("infoType",eccInfo.getType());
			return "/eclasscard/show/album/eccClassAlbum.ftl";
		}
	}
	
	@RequestMapping("/showIndex/bulletin")
	@ControllerInfo("班牌通知公告")
	public String showIndexBulletin(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
		List<EccBulletin> bulletins = eccBulletinService.findListOnEcc(eccInfo.getId());
		
		map.put("bulletins",bulletins);
		map.put("infoType",eccInfo.getType());
		if(EccConstants.ECC_VIEW_2.equals(view)){
			for(EccBulletin bulletin:bulletins){
				String conStr = EccUtils.delHTMLTag(bulletin.getContent());
				if(StringUtils.isNotBlank(conStr)&&conStr.length()>45){
					conStr = conStr.substring(0, 45)+"...";
				}else{
					conStr+="...";
				}
				bulletin.setContent(conStr);
			}
			return "/eclasscard/verticalshow/bulletin/eccIndexBulletin.ftl";
		}else{
			return "/eclasscard/show/bulletin/eccIndexBulletin.ftl";
		}
	}
	
	@RequestMapping("/showIndex/bulletinDetail")
	@ControllerInfo("班牌通知公告详情")
	public String showIndexBulletinDetail(String id,String view,ModelMap map){
		EccBulletin bulletin = eccBulletinService.findOne(id);
		map.put("bulletin", bulletin);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/verticalshow/eccIndexBulletinDetail.ftl";
		}else{
			return "/eclasscard/show/eccIndexBulletinDetail.ftl";
		}
	}
	
	@RequestMapping("/showIndex/clockGrade")
	@ControllerInfo("班牌寝室打卡班级")
	public String showIndexClockGrade(String cardId,String view,String periodId,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
		List<EccDormAttence> dormAttences = eccDormAttenceService.findListByPlaceIdNotOver(eccInfo.getPlaceId(),eccInfo.getUnitId());
		Set<String> dormAttIds = EntityUtils.getSet(dormAttences, "id");
		Set<String> periodIds = EntityUtils.getSet(dormAttences, "periodId");
		if(dormAttIds.size()>0&&periodIds.size()>0){
			List<EccAttenceDormGrade> dormGrades = eccAttenceDormGradeService.findListByIn("periodId", periodIds.toArray(new String[periodIds.size()]));
			Map<String,EccAttenceDormGrade> dormGradeMap = EntityUtils.getMap(dormGrades, "grade");
			List<EccStudormAttence> studormAttences =  eccStudormAttenceService.findListByIn("dormAttId", dormAttIds.toArray(new String[dormAttIds.size()]));
			Set<String> calssIds = EntityUtils.getSet(studormAttences, "classId");
			List<Clazz> clazzs = SUtils.dt(classRemoteService.findListByIds(calssIds.toArray(new String[calssIds.size()])),new TR<List<Clazz>>() {});
			Set<String> gradeIds = EntityUtils.getSet(clazzs, "gradeId");
			List<Grade> grades = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[gradeIds.size()])),new TR<List<Grade>>() {});
			List<AttanceGradeDto> gradeDtos = Lists.newArrayList();
			for(Grade grade:grades){
				if(dormGradeMap.containsKey(grade.getGradeCode())){
					AttanceGradeDto  gradeDto = new AttanceGradeDto();
					EccAttenceDormGrade dormGrade= dormGradeMap.get(grade.getGradeCode());
					if(dormGrade!=null){
						gradeDto.setGradeName(grade.getGradeName());
						gradeDto.setAttacneTime(dormGrade.getBeginTime()+"-"+dormGrade.getEndTime());
						gradeDtos.add(gradeDto);
					}
				}
			}
			map.put("gradeDtos", gradeDtos);
		}
		if(UnitIni.ECC_USE_VERSION_STANDARD.equals(EccNeedServiceUtils.getEClassCardVerison(eccInfo.getUnitId()))){
			if(EccConstants.ECC_VIEW_2.equals(view)){
				return "/eclasscard/standard/verticalshow/clockin/eccIndexDormClockGrade.ftl";
			}else{
				return "/eclasscard/standard/show/clockin/eccIndexDormClockGrade.ftl";
			}
		} else {
			if(EccConstants.ECC_VIEW_2.equals(view)){
				return "/eclasscard/verticalshow/clockin/eccIndexDormClockGrade.ftl";
			}else{
				return "/eclasscard/show/clockin/eccIndexDormClockGrade.ftl";
			}
		}
	}
	
	@RequestMapping("/showIndex/clockIn")
	@ControllerInfo("班牌打卡记录展示")
	public String showIndexClockIn(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
		
		if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
			return "";
		}else if(EccConstants.ECC_MCODE_BPYT_2.equals(eccInfo.getType())){
			return "";
		}else if(EccConstants.ECC_MCODE_BPYT_3.equals(eccInfo.getType())){
			List<String> recentDataList = RedisUtils
					.queryDataFromList(EccConstants.DORM_RECENT_CLOCK_REDIS
							+ cardId,false);
			List<StuClockResultDto> resultDtos = Lists.newArrayList(); 
			for(String string:recentDataList){
				StuClockResultDto resultDto =JSON.parseObject(string, StuClockResultDto.class);
				resultDtos.add(resultDto);
			}
			map.put("resultDtos", resultDtos);
			
		}else{
			List<String> recentDataList = RedisUtils
					.queryDataFromList(EccConstants.GATE_RECENT_CLOCK_REDIS
							+ cardId,false);
			List<StuClockResultDto> resultDtos = Lists.newArrayList(); 
			for(String string:recentDataList){
				StuClockResultDto resultDto =JSON.parseObject(string, StuClockResultDto.class);
				resultDtos.add(resultDto);
			}
			map.put("resultDtos", resultDtos);
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/verticalshow/clockin/eccIndexClockRecord.ftl";
		}else{
			return "/eclasscard/show/clockin/eccIndexClockRecord.ftl";
		}
	}
	
	@RequestMapping("/classSpace/index")
	@ControllerInfo("班牌班级空间-首页")
	public String classSpaceIndex(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		map.put("cardId", cardId);
		map.put("view", view);
		if(StringUtils.isNotBlank(eccInfo.getClassId())){
			Clazz clazz = SUtils.dt(classRemoteService.findOneById(eccInfo.getClassId()),new TR<Clazz>() {});
			if(clazz!=null){
				List<Student> students = SUtils.dt(studentRemoteService.findByClassIds(new String[]{clazz.getId()}),new TR<List<Student>>() {});
				User user = SUtils.dt(userRemoteService.findByOwnerId(clazz.getTeacherId()),new TR<User>() {});
				Grade grade=SUtils.dt(gradeRemoteService.findOneById(clazz.getGradeId()),new TR<Grade>() {});
				if(CollectionUtils.isNotEmpty(students)){
					map.put("classStuNum", students.size());
				}
				if(grade!=null){
					map.put("className", grade.getGradeName()+clazz.getClassName());
				}
				if(user!=null){
					map.put("teacherName", user.getRealName());
					map.put("userName", user.getUsername());
				}
			}
		}
		map.put("unitId", eccInfo.getUnitId());
		map.put("classId", eccInfo.getClassId());
		map.put("view", view);
		if (EccConstants.ECC_VIEW_1.equals(view)) {
			return "/eclasscard/show/classspace/eccClassSpace.ftl";
		} else {
			return "/eclasscard/verticalshow/classspace/eccClassSpace.ftl";
		}
	}
	
	@RequestMapping("/classSpace/description")
	@ControllerInfo("班牌班级空间-班级简介")
	public String classSpaceDescription(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
		EccClassDesc classDesc = eccClassDescService.findOneBy("classId", eccInfo.getClassId());
		if(classDesc==null){
			classDesc = new EccClassDesc();
		}
		map.put("classDesc",classDesc);
		if (EccConstants.ECC_VIEW_1.equals(view)) {
			return "/eclasscard/show/classspace/classSpaceDesc.ftl";
		} else {
			return "/eclasscard/verticalshow/classspace/classSpaceDesc.ftl";
		}
	}
	
	@RequestMapping("/classSpace/album")
	@ControllerInfo("班牌班级空间-相册")
	public String classSpaceAlbum(String cardId,String view,Integer pageIndex,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
		if(pageIndex==null||pageIndex<1){
			pageIndex = 1;
		}
		Integer pageSize = 16;
		Integer pageCount = 1;
		List<EccPhotoAlbum> returnAlbums  =Lists.newArrayList();
		List<EccPhotoAlbum> eccPhotoAlbums = eccPhotoAlbumService.findByShowEccInfo(eccInfo.getId());
		for(int i=0;i<eccPhotoAlbums.size();i++){
			if(i<pageSize*pageIndex&&i>=pageSize*(pageIndex-1)){
				returnAlbums.add(eccPhotoAlbums.get(i));
			}
		}
		pageCount = (eccPhotoAlbums.size()+pageSize-1)/pageSize;
		map.put("pageIndex",pageIndex);
		map.put("pageCount",pageCount);
		map.put("albums",returnAlbums);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/verticalshow/classspace/classSpaceAlbum.ftl";
		}else{
			return "/eclasscard/show/classspace/classSpaceAlbum.ftl";
		}
	}
	
	@RequestMapping("/classSpace/album/page")
	public String classSpaceAlbumPage(String cardId,ModelMap map){
		map.put("cardId", cardId);
		map.put("pageIndex","");
		map.put("pageCount","");
		return "/eclasscard/verticalshow/classspace/classSpaceAlbum.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/classSpace/album/pageData")
	public String classSpaceAlbumData(String cardId,Integer pageIndex,ModelMap map){
		JSONArray jsonArray = new JSONArray();
		List<EccPhotoAlbum> returnAlbums  =Lists.newArrayList();
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return jsonArray.toJSONString();
		}
		if(pageIndex==null||pageIndex<1){
			pageIndex = 1;
		}
		Integer pageSize = 16;
		Integer pageCount = 1;
		List<EccPhotoAlbum> eccPhotoAlbums = eccPhotoAlbumService.findByShowEccInfo(eccInfo.getId());
		for(int i=0;i<eccPhotoAlbums.size();i++){
			if(i<pageSize*pageIndex&&i>=pageSize*(pageIndex-1)){
				returnAlbums.add(eccPhotoAlbums.get(i));
			}
		}
		pageCount = (eccPhotoAlbums.size()+pageSize-1)/pageSize;
		jsonArray.add(returnAlbums);
		jsonArray.add(pageIndex);
		jsonArray.add(pageCount);
		return jsonArray.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/get/server/usable")
	public String getServer(){
		return success("ok");
	}
}
