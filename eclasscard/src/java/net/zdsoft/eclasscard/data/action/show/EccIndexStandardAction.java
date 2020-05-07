package net.zdsoft.eclasscard.data.action.show;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.remote.utils.AttachmentUtils;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.dto.NoneMsgDto;
import net.zdsoft.eclasscard.data.dto.StuClockResultDto;
import net.zdsoft.eclasscard.data.dto.StudentWithInfo;
import net.zdsoft.eclasscard.data.dto.courseDto;
import net.zdsoft.eclasscard.data.entity.EccAttachFolder;
import net.zdsoft.eclasscard.data.entity.EccAttachFolderTo;
import net.zdsoft.eclasscard.data.entity.EccAttenceGateGrade;
import net.zdsoft.eclasscard.data.entity.EccBulletin;
import net.zdsoft.eclasscard.data.entity.EccClassAttence;
import net.zdsoft.eclasscard.data.entity.EccClassDesc;
import net.zdsoft.eclasscard.data.entity.EccExamTime;
import net.zdsoft.eclasscard.data.entity.EccFullObj;
import net.zdsoft.eclasscard.data.entity.EccFullObjAll;
import net.zdsoft.eclasscard.data.entity.EccHonor;
import net.zdsoft.eclasscard.data.entity.EccHonorTo;
import net.zdsoft.eclasscard.data.entity.EccInOutAttance;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccOtherSet;
import net.zdsoft.eclasscard.data.entity.EccSeatItem;
import net.zdsoft.eclasscard.data.entity.EccSeatSet;
import net.zdsoft.eclasscard.data.service.EccAttachFolderService;
import net.zdsoft.eclasscard.data.service.EccAttachFolderToService;
import net.zdsoft.eclasscard.data.service.EccBulletinService;
import net.zdsoft.eclasscard.data.service.EccBulletinToService;
import net.zdsoft.eclasscard.data.service.EccCacheService;
import net.zdsoft.eclasscard.data.service.EccClassAttenceService;
import net.zdsoft.eclasscard.data.service.EccClassDescService;
import net.zdsoft.eclasscard.data.service.EccExamTimeService;
import net.zdsoft.eclasscard.data.service.EccFaceActivateService;
import net.zdsoft.eclasscard.data.service.EccFullObjAllService;
import net.zdsoft.eclasscard.data.service.EccFullObjService;
import net.zdsoft.eclasscard.data.service.EccHonorService;
import net.zdsoft.eclasscard.data.service.EccHonorToService;
import net.zdsoft.eclasscard.data.service.EccInOutAttanceService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccOtherSetService;
import net.zdsoft.eclasscard.data.service.EccSeatItemService;
import net.zdsoft.eclasscard.data.service.EccSeatSetService;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.exammanage.remote.service.ExamManageRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Controller
@RequestMapping("/eccShow/eclasscard/standard")
public class EccIndexStandardAction extends BaseAction {

	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private EccHonorService eccHonorService;
	@Autowired
	private EccHonorToService eccHonorToService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private EccClassDescService eccClassDescService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private EccBulletinService eccBulletinService;
	@Autowired
	private EccBulletinToService eccBulletinToService;
	@Autowired
	private EccAttachFolderService eccAttachFolderService;
	@Autowired
	private EccAttachFolderToService eccAttachFolderToService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private ExamManageRemoteService examManageRemoteService;
	@Autowired
	private EccExamTimeService eccExamTimeService;
	@Autowired
	private EccFullObjAllService eccFullObjAllService;
	@Autowired
	private EccFullObjService eccFullObjService;
	@Autowired
	private EccOtherSetService eccOtherSetService;
	@Autowired
	private EccFaceActivateService eccFaceActivateService;
	@Autowired
	private EccCacheService eccCacheService;
	@Autowired
	private EccInOutAttanceService eccInOutAttanceService;
	@Autowired
	private EccClassAttenceService eccClassAttenceService;
	@Autowired
	private EccSeatSetService eccSeatSetService;
	@Autowired
	private EccSeatItemService eccSeatItemService;
	
	private static OpenApiOfficeService openApiOfficeService;

	public OpenApiOfficeService getOpenApiOfficeService() {
		if (openApiOfficeService == null) {
			openApiOfficeService = Evn.getBean("openApiOfficeService");
			if (openApiOfficeService == null) {
				System.out.println("openApiOfficeService为null，需开启dubbo服务");
			}
		}
		return openApiOfficeService;
	}
	@RequestMapping("/showIndex")
	@ControllerInfo("进入班牌首页")
    public String showIndex(String cardId,String view,HttpServletRequest request,ModelMap map){
		NoneMsgDto msgDto = new NoneMsgDto();
		map.put("cardId", cardId);
		map.put("view", view);
		if(StringUtils.isBlank(cardId)){
			msgDto.setBtn(false);
			msgDto.setMsg("请登入设置界面，设置班牌号");
			map.put("msgDto", msgDto);
			return "/eclasscard/common/show/hintPage.ftl";
		}
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null || StringUtils.isBlank(eccInfo.getType())){
			msgDto.setBtn(true);
			msgDto.setMsg("请到后台，配置班牌信息");
			map.put("msgDto", msgDto);
			return "/eclasscard/common/show/hintPage.ftl";
		}
		if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
			Clazz clazz = SUtils.dt(classRemoteService.findOneById(eccInfo.getClassId()),new TR<Clazz>() {});
			if(clazz == null || clazz.getIsDeleted() ==1 || clazz.getIsGraduate() != 0 ){
				msgDto.setBtn(true);
				msgDto.setMsg("请到后台，配置班牌班级信息");
				map.put("msgDto", msgDto);
				return "/eclasscard/common/show/hintPage.ftl";
			}
		}
		map.put("eccInfo", eccInfo);
		String basePath = request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath();
		if("https".equals(request.getScheme())){
			map.put("webSocketUrl", "wss://" + basePath + "/eClassCard/webSocketServer?sid="+cardId);
			map.put("sockJSUrl", "https://" + basePath + "/eClassCard/sockjs/cardId?sid="+cardId);
		}else{
			map.put("webSocketUrl", "ws://" + basePath + "/eClassCard/webSocketServer?sid="+cardId);
			map.put("sockJSUrl", "http://" + basePath + "/eClassCard/sockjs/cardId?sid="+cardId);
		}
		
		String unitId = eccInfo.getUnitId();
		EccOtherSet faceSet = eccOtherSetService.findByUnitIdAndType(unitId, EccConstants.ECC_OTHER_SET_6);
		if (faceSet == null || Objects.equals(0,faceSet.getNowvalue())) {
			map.put("isActivate", false);
		} else {
			map.put("isActivate", true);
		}
		EccOtherSet promptSet = eccOtherSetService.findByUnitIdAndType(unitId, EccConstants.ECC_OTHER_SET_7);
		if (promptSet == null || Objects.equals(0,promptSet.getNowvalue())) {
			map.put("prompt", "false");
		} else {
			map.put("prompt", "true");
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/eccIndex.ftl";
		}else{
			return "/eclasscard/standard/show/eccIndex.ftl";
		}
	}
	
	@RequestMapping("/showHomePage")
	@ControllerInfo("进入班牌首页")
    public String showHomePage(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		map.put("eccInfo", eccInfo);
		if(EccNeedServiceUtils.getIsLayout()){
			map.put("layout", true);
		}else{
			map.put("layout", false);
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/eccHomePage.ftl";
		}else{
			return "/eclasscard/standard/show/eccHomePage.ftl";
		}
	}
	@RequestMapping("/showIndex/showHeader")
	@ControllerInfo("班牌头部")
    public String showHeader(String cardId,String view,boolean showBulletin,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
		if (EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())) {
			Clazz clazz = SUtils.dt(classRemoteService.findOneById(eccInfo.getClassId()),new TR<Clazz>() {});
			Grade grade = SUtils.dt(gradeRemoteService.findOneById(clazz.getGradeId()),new TR<Grade>() {});
			eccInfo.setClassName(grade.getGradeName() + clazz.getClassName());
		} else if (EccConstants.ECC_MCODE_BPYT_3.equals(eccInfo.getType())) {
        	String jsonStr = stuworkRemoteService.getBuildingSbyUnitId(eccInfo.getUnitId());
			List<DormBuildingDto> buildingDtos = SUtils.dt(jsonStr,new TR<List<DormBuildingDto>>() {});
    		Map<String,String> dormBuildMap = EntityUtils.getMap(buildingDtos, dbd -> dbd.getBuildingId(), dbd -> dbd.getBuildingName());
    		if(dormBuildMap.containsKey(eccInfo.getPlaceId())){
    			eccInfo.setPlaceName(dormBuildMap.get(eccInfo.getPlaceId()));
			}else if(StringUtils.isNotBlank(eccInfo.getPlaceId())){
				eccInfo.setPlaceName("（已删除）");
			}
		}else{
			Map<String, String> teachPlaceMap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(new String[]{eccInfo.getPlaceId()}),new TR<Map<String, String>>() {});
			if(teachPlaceMap.containsKey(eccInfo.getPlaceId())){
				eccInfo.setPlaceName(teachPlaceMap.get(eccInfo.getPlaceId()));
			}
			
		}
		map.put("eccInfo", eccInfo);
		map.put("showBulletin", showBulletin);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/eccHeader.ftl";
		}else{
			return "/eclasscard/standard/show/eccHeader.ftl";
		}
	}
	@RequestMapping("/showIndex/showFooter")
	@ControllerInfo("班牌底部")
	public String showFooter(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
		map.put("eccInfo", eccInfo);
		
		boolean isShowClassSpace=true;
        //2.给优肯环境中的单位“嘉兴市秀洲区印通小学”，定制屏蔽客户端底部的“班级空间”菜单---根据id去匹配
        if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())) {
            //会显示班级空间---根据单位id去定制匹配
            if(eccInfo.getUnitId().equals(EccConstants.SCHOOL_YKJXSXZQYTXX)) {
                isShowClassSpace=false;
            }
            //如果校园空间url改动，班牌需要刷新
            EccOtherSet otherSet = eccOtherSetService.findByUnitIdAndType(eccInfo.getUnitId(), EccConstants.ECC_OTHER_SET_8);
            if(otherSet!=null) {
            	map.put("parmUrl", otherSet.getParam());
            }
            
        }
        map.put("isShowClassSpace", isShowClassSpace);
		
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/eccFooter.ftl";
		}else{
			return "/eclasscard/standard/show/eccFooter.ftl";
		}
	}
	
	@RequestMapping("/classspace/index")
	@ControllerInfo("班牌班级空间-首页")
	public String classSpaceIndex(String cardId,String view,ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if (StringUtils.isNotBlank(eccInfo.getClassId())) {
			Clazz clazz = SUtils.dt(classRemoteService.findOneById(eccInfo.getClassId()),new TR<Clazz>() {});
			if (clazz != null) {
				User user = SUtils.dt(userRemoteService.findByOwnerId(clazz.getTeacherId()),new TR<User>() {});
				if (user != null) {
					map.put("teacherName", user.getRealName());
					map.put("userName", user.getUsername());
					map.put("sex", user.getSex());
				}
			}
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/classspace/eccClassSpace.ftl";
		} else {
			return "/eclasscard/standard/show/classspace/eccClassSpace.ftl";
		}
	}

	@RequestMapping("/classspace/students")
	@ControllerInfo("班级空间-班内学生")
	public String students(String cardId, String view,ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		List<Student> studentList = Lists.newArrayList();
		List<StudentWithInfo> studentWithInfoList = Lists.newArrayList();
		if (eccInfo != null) {
			studentList = SUtils.dt(studentRemoteService.findByClassIds(eccInfo.getClassId()),new TR<List<Student>>() {});
		}
		boolean isSeatSet=false;//是否设置座位表
		int rowNumbers=0;//行
		int colNumbers=0;//列
		String spaceNo=null;
		Map<String,EccSeatItem> seatItemMap=new HashMap<String, EccSeatItem>();
		EccSeatSet seatSet = eccSeatSetService.findOneByUnitIdAndClassId(eccInfo.getUnitId(), eccInfo.getClassId());
		if(seatSet!=null) {
			List<EccSeatItem> eccSeatItemList = eccSeatItemService.findListBySeatId(seatSet.getUnitId(), seatSet.getId());
			if(CollectionUtils.isNotEmpty(eccSeatItemList)) {
				isSeatSet=true;
				rowNumbers=seatSet.getRowNumber();
				colNumbers=seatSet.getColNumber();
				spaceNo=seatSet.getSpaceNum()==null?"":seatSet.getSpaceNum();
				seatItemMap=EntityUtils.getMap(eccSeatItemList, e->e.getStudentId());
			}
		}

		for (Student student : studentList) {
			StudentWithInfo info = new StudentWithInfo();
			info.setStudent(student);
			String time = DateUtils.date2StringByMinute(student.getModifyTime());
			if(time==null)time = DateUtils.date2StringByMinute(new Date());
			info.setShowPictrueUrl(EccUtils.showPictureUrl(student.getFilePath(), student.getSex(),time));
			if(seatItemMap.containsKey(student.getId())) {
				info.setColNo(seatItemMap.get(student.getId()).getColNum());
				info.setRowNo(seatItemMap.get(student.getId()).getRowNum());
			}
			studentWithInfoList.add(info);
		}
		map.put("studentWithInfoList", studentWithInfoList);

		String[] spaceNoArr=new String[] {};
		if(StringUtils.isNotBlank(spaceNo)) {
			spaceNoArr=spaceNo.split(",");
		}
		if(isSeatSet) {
			map.put("isSeatSet", isSeatSet);
			map.put("rowNumbers", rowNumbers);
			map.put("colNumbers", colNumbers);
			map.put("spaceNoArr", spaceNoArr);
			if(EccConstants.ECC_VIEW_2.equals(view)){
				return "/eclasscard/standard/verticalshow/classspace/eccStudentsSeat.ftl";
			}else{
				return "/eclasscard/standard/show/classspace/eccStudentsSeat.ftl";
			}
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/classspace/eccStudents.ftl";
		} else {
			return "/eclasscard/standard/show/classspace/eccStudents.ftl";
		}
	}
	
	@RequestMapping("/classspace/description")
	@ControllerInfo("班级空间-班级简介")
	public String classDescription(String cardId,String view,ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		EccClassDesc classDesc = null;
		List<EccHonor> honorList = Lists.newArrayList();
		String pictureUrl = "";
		if (eccInfo != null
				&& EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())
				&& StringUtils.isNotBlank(eccInfo.getClassId())) {
			classDesc = eccClassDescService.findOneBy("classId",
					eccInfo.getClassId());
		}
		if (classDesc==null){
			classDesc = new EccClassDesc();
		}
		if (StringUtils.isNotBlank(classDesc.getId())) {
			List<Attachment> attachments = SUtils.dt(attachmentRemoteService
					.findAttachmentDirPathByObjId(classDesc.getId()),
					new TR<List<Attachment>>() {
					});
			if (CollectionUtils.isNotEmpty(attachments)) {
				Attachment attachment = attachments.get(0);
				pictureUrl = attachment.getShowPicUrl();
			}
		}
		if (StringUtils.isNotBlank(classDesc.getClassId())) {
			List<EccHonorTo> eccHonorTos = eccHonorToService
					.findByObjectIdIn(new String[] { classDesc.getClassId() });
			Set<String> honorIds = EntityUtils.getSet(eccHonorTos, ht -> ht.getHonorId());
			if(CollectionUtils.isNotEmpty(honorIds)){
				honorList = eccHonorService.findByIdsDesc(honorIds
						.toArray(new String[0]));
			}
		}
		StringBuilder introduction = new StringBuilder();
		if (StringUtils.isNotBlank(eccInfo.getClassId())) {
			Clazz clazz = SUtils.dt(
					classRemoteService.findOneById(eccInfo.getClassId()),
					new TR<Clazz>() {
					});
			List<Student> studentList = SUtils.dt(
					studentRemoteService.findByClassIds(eccInfo.getClassId()),
					new TR<List<Student>>() {
					});
			if (clazz != null) {
				introduction.append(clazz.getClassNameDynamic());
				User user = SUtils.dt(
						userRemoteService.findByOwnerId(clazz.getTeacherId()),
						new TR<User>() {
						});
				if (user != null) {
					introduction.append("，班主任"+user.getRealName());
				}
			}
			introduction.append("，全班人数"+studentList.size()+"人");
			int boys = 0;
			int girls = 0;
			for (Student student : studentList) {
				if(student.getSex()== null){
					continue;
				}
				if(student.getSex()==1){
					boys++;
				}else if(student.getSex()==2){
					girls++;
				}
			}
			introduction.append("，其中男生"+boys+"人，女生"+girls+"人。");
		}
		map.put("introduction", introduction);
		map.put("classDesc", classDesc);
		map.put("pictureUrl", pictureUrl);
		map.put("honorList", honorList);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/classspace/eccDescription.ftl";
		} else {
			return "/eclasscard/standard/show/classspace/eccDescription.ftl";
		}
	}

	@RequestMapping("/classspace/album")
	@ControllerInfo("班牌班级空间-多媒体")
	public String classSpaceAlbum(String view,ModelMap map) {
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/classspace/eccClassAlbum.ftl";
		} else {
			return "/eclasscard/standard/show/classspace/eccClassAlbum.ftl";
		}
	}

	@RequestMapping("/classspace/pptlist")
	@ControllerInfo("班牌班级空间-多媒体PPTList详情")
	public String classSpacePPT(String folderId,String view,ModelMap map) {
		List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(folderId),new TR<List<Attachment>>() {});
		attachments = attachments.stream().filter(line -> AttFileDto.PPT_STATUS_PAGE_END == line.getStatus()).collect(Collectors.toList());
		map.put("attachments", attachments);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/classspace/eccClassPPTList.ftl";
		} else {
			return "/eclasscard/standard/show/classspace/eccClassPPTList.ftl";
		} 
	}
	
	@RequestMapping("/classspace/pptalbumlist")
	@ControllerInfo("班牌班级空间-多媒体PPTList")
	public String classSpacePPTAlbum(String cardId, String view,ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		List<EccAttachFolder> attachFolders = eccAttachFolderService.findByObjIdAndRangeAndType(eccInfo.getId(),EccConstants.ECC_FOLDER_RANGE_1,EccConstants.ECC_FOLDER_TYPE_3);
		map.put("attachFolders", attachFolders);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/classspace/eccClassPPTAlbumList.ftl";
		} else {
			return "/eclasscard/standard/show/classspace/eccClassPPTAlbumList.ftl";
		} 
	}
	
	@RequestMapping("/classspace/videolist")
	@ControllerInfo("班牌班级空间-多媒体videoList详情")
	public String classSpaceVideo(String folderId,String view,ModelMap map) {
		List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(folderId),new TR<List<Attachment>>() {});
		if (CollectionUtils.isNotEmpty(attachments)) {
			String furl = Evn.<SysOptionRemoteService> getBean("sysOptionRemoteService").findValue(Constant.FILE_URL);// 文件系统地址
			for(Attachment attachment : attachments){
				attachment.setFilename(EccUtils.getFileNameNoExt(attachment.getFilename()));
				attachment.setDirPath(furl+"/store/"+attachment.getFilePath().replace("\\","/"));
			}
		}
		map.put("attachments", attachments);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/classspace/eccClassVideoList.ftl";
		} else {
			return "/eclasscard/standard/show/classspace/eccClassVideoList.ftl";
		} 
	}
	
	@RequestMapping("/classspace/videoalbumlist")
	@ControllerInfo("班牌班级空间-多媒体VideoList")
	public String classSpaceVideoAlbum(String cardId, String view,ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		List<EccAttachFolder> attachFolders = eccAttachFolderService.findByObjIdAndRangeAndType(eccInfo.getId(),EccConstants.ECC_FOLDER_RANGE_1,EccConstants.ECC_FOLDER_TYPE_2);
		map.put("attachFolders", attachFolders);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/classspace/eccClassVideoAlbumList.ftl";
		} else {
			return "/eclasscard/standard/show/classspace/eccClassVideoAlbumList.ftl";
		} 
	}
	
	@RequestMapping("/classspace/photoalbumlist")
	@ControllerInfo("班牌班级空间-多媒体PhotoList")
	public String classSpacePhotoAlbum(String cardId, String view,ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		List<EccAttachFolder> attachFolders = eccAttachFolderService.findByObjIdAndRangeAndType(eccInfo.getId(),EccConstants.ECC_FOLDER_RANGE_1,EccConstants.ECC_FOLDER_TYPE_1);
		if (CollectionUtils.isNotEmpty(attachFolders)) {
			Set<String> folderIds = EntityUtils.getSet(attachFolders, af -> af.getId());
			List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(folderIds.toArray(new String[folderIds.size()])),new TR<List<Attachment>>() {});
			for (Attachment attachment : attachments) {
				attachment.setFilename(EccUtils.getFileNameNoExt(attachment.getFilename()));
			}
			List<Attachment> attAllList = null;
			for (EccAttachFolder folder : attachFolders) {
				attAllList = attachments.stream().filter(att -> att.getObjId().equals(folder.getId())).collect(Collectors.toList());
				if (attAllList != null) {
					folder.setNumber(attAllList.size());
					folder.setAttachments(attAllList);
				}	 	
			}
		}
		map.put("attachFolders", attachFolders);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/classspace/eccClassPhotoAlbumList.ftl";
		} else {
			return "/eclasscard/standard/show/classspace/eccClassPhotoAlbumList.ftl";
		} 
	}
	
	@RequestMapping("/classspace/honortab")
	@ControllerInfo("班级空间-荣誉")
	public String honorTab(String view) {
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/classspace/eccHonor.ftl";
		} else {
			return "/eclasscard/standard/show/classspace/eccHonor.ftl";
		}
	}
	
	@RequestMapping("/classspace/classhonor")
	@ControllerInfo("班级空间-班级荣誉")
	public String classhonorTab(String cardId,String view, ModelMap map) {
		if (StringUtils.isBlank(cardId)) {
			return "";
		}
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		List<EccHonor> eccClazzHonor = Lists.newArrayList();
		List<EccHonorTo> eccClazzHonorTos = eccHonorToService.findByObjectIdIn(new String[] { eccInfo.getClassId() });
		if (CollectionUtils.isNotEmpty(eccClazzHonorTos)) {
			Set<String> idsList = EntityUtils.getSet(eccClazzHonorTos, cht -> cht.getHonorId());
			eccClazzHonor = eccHonorService.findByIdsDesc(idsList.toArray(new String[0]));
		}
		map.put("eccClazzHonor", eccClazzHonor);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/classspace/eccClassHonor.ftl";
		} else {
			return "/eclasscard/standard/show/classspace/eccClassHonor.ftl";
		}
	}

	@RequestMapping("/classspace/stuhonor")
	@ControllerInfo("班级空间-个人荣誉")
	public String stuhonorTab(String cardId,String view, ModelMap map) {
		if (StringUtils.isBlank(cardId)) {
			return "";
		}
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		List<Student> students = SUtils.dt(studentRemoteService.findByClassIds(eccInfo.getClassId()),new TR<List<Student>>() {});
		Map<String, String> studentNameMap = EntityUtils.getMap(students, stu -> stu.getId(), stu -> stu.getStudentName());
		List<String> stuIdsList = EntityUtils.getList(students, stus -> stus.getId());
		List<EccHonor> eccStuHonor = Lists.newArrayList();
		List<EccHonorTo> eccStuHonorTos = eccHonorToService.findByObjectIdIn(stuIdsList.toArray(new String[0]));
		if (CollectionUtils.isNotEmpty(eccStuHonorTos)) {
			Map<String, String> honnorStuNameMap = Maps.newHashMap();
			for (EccHonorTo eccHonorTo : eccStuHonorTos) {
				honnorStuNameMap.put(eccHonorTo.getHonorId(),studentNameMap.get(eccHonorTo.getObjectId()));
			}
			List<String> idsList = EntityUtils.getList(eccStuHonorTos, sht -> sht.getHonorId());
			eccStuHonor = eccHonorService.findByIdsDesc(idsList.toArray(new String[0]));
			List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(idsList.toArray(new String[0])),new TR<List<Attachment>>() {});
			Map<String, Attachment> pictureMap = EntityUtils.getMap(attachments, att -> att.getObjId());
			Attachment attachment = null;
			String smallFilePath = "";
			for (EccHonor eccHonor : eccStuHonor) {
				eccHonor.setStudentName(honnorStuNameMap.get(eccHonor.getId()));
				attachment = pictureMap.get(eccHonor.getId());
				eccHonor.setAttachmentId(attachment.getId());
				smallFilePath = AttachmentUtils.getAddSuffixName(attachment.getFilePath(), EccConstants.ECC_SMALL_IMG);
				attachment.setFilePath(smallFilePath);
				eccHonor.setPictureUrl(attachment.getShowPicUrl());
			}
		}
		map.put("eccStuHonor", eccStuHonor);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/classspace/eccStuHonor.ftl";
		} else {
			return "/eclasscard/standard/show/classspace/eccStuHonor.ftl";
		}
	}
	
	@RequestMapping("/showindex/honorlist")
	@ControllerInfo("行政班首页-展示荣誉列表")
	public String showHonorList(String cardId, String view, ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if (eccInfo == null) {
			return "";
		}
		List<EccHonor> eccClassHonors = eccHonorService.findShowList(
				eccInfo.getClassId(), EccConstants.ECC_HONOR_TYPE_1);
		List<EccHonor> eccStuHonors = eccHonorService.findShowList(
				eccInfo.getClassId(), EccConstants.ECC_HONOR_TYPE_2);
		map.put("eccClassHonors", eccClassHonors);
		map.put("eccStuHonors", eccStuHonors);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/information/eccHonor.ftl";
		}else{
			return "/eclasscard/standard/show/information/eccHonor.ftl";
		}
	}

	@RequestMapping("/showindex/class/info")
	@ControllerInfo("行政班首页-班牌行政班信息")
	public String showIndexClassInfo(String cardId,String view, ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if (eccInfo == null) {
			return "";
		}
		if (StringUtils.isNotBlank(eccInfo.getClassId())) {
			Clazz clazz = SUtils.dt(
					classRemoteService.findOneById(eccInfo.getClassId()),
					new TR<Clazz>() {
					});
			if (clazz != null) {
				List<Student> students = SUtils.dt(studentRemoteService
						.findByClassIds(new String[] { clazz.getId() }),
						new TR<List<Student>>() {
						});
				User user = SUtils.dt(
						userRemoteService.findByOwnerId(clazz.getTeacherId()),
						new TR<User>() {
						});
				Grade grade = SUtils.dt(
						gradeRemoteService.findOneById(clazz.getGradeId()),
						new TR<Grade>() {
						});
				if (CollectionUtils.isNotEmpty(students)) {
					map.put("classStuNum", students.size());
				}
				if (grade != null) {
					map.put("className",
							grade.getGradeName() + clazz.getClassName());
				}
				if (user != null) {
					map.put("teacherName", user.getRealName());
					map.put("teacherUserName", user.getUsername());
				}
			}
			// 查询当前时间，该班级下的请假学生ids
			Set<String> studentIds = Sets.newHashSet();
			if (getOpenApiOfficeService() != null) {
				try {
					String jsonStr1 = getOpenApiOfficeService()
							.getHwStuLeavesByUnitId(eccInfo.getUnitId(),
									eccInfo.getClassId(), "1", null, new Date());
					String jsonStr2 = getOpenApiOfficeService()
							.getHwStuLeavesByUnitId(eccInfo.getUnitId(),
									eccInfo.getClassId(), "2", null, new Date());
					JSONArray strings1 = EccUtils.getResultArray(jsonStr1,
							"studentIds");
					JSONArray strings2 = EccUtils.getResultArray(jsonStr2,
							"studentIds");
					for (int i = 0; i < strings1.size(); i++) {
						studentIds.add(strings1.get(i).toString());
					}
					for (int i = 0; i < strings2.size(); i++) {
						studentIds.add(strings2.get(i).toString());
					}
				} catch (Exception e) {//dubbo中途中断，获取超时时，吞掉异常，数据显示为空
					e.printStackTrace();
				}
			}
			String leaveStusName = "";
			String leaveStuscut = "";
			if (CollectionUtils.isNotEmpty(studentIds)) {
				List<User> users = SUtils.dt(userRemoteService
						.findByOwnerIds(studentIds
								.toArray(new String[studentIds.size()])),
						new TR<List<User>>() {
						});
				int num = 0;
				for (User user : users) {
					if (StringUtils.isBlank(leaveStusName)) {
						leaveStusName = user.getRealName();
						leaveStuscut = user.getRealName();
						num++;
					} else {
						leaveStusName += "," + user.getRealName();
						if(num<3){
							leaveStuscut += "," + user.getRealName();
						}
						num++;
					}
				}
			}
			if (StringUtils.isBlank(leaveStuscut)) {
				leaveStuscut = "无请假人员";
			}
			map.put("leaveStusName", leaveStusName);
			map.put("leaveStuscut", leaveStuscut);
			map.put("leaveStus", studentIds.size());
			boolean showMore = false;
			if(studentIds.size()>3){
				showMore = true;
			}
			map.put("showMore", showMore);
		}
		map.put("unitId", eccInfo.getUnitId());
		map.put("classId", eccInfo.getClassId());
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/information/eccClassInfo.ftl";
		}else{
			return "/eclasscard/standard/show/information/eccClassInfo.ftl";
		}
	}

	
	@RequestMapping("/showindex/bulletin")
	@ControllerInfo("班牌普通公告")
	public String showIndexBulletin(String cardId,String view,ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if (eccInfo == null) {
			return "";
		}
		List<EccBulletin> bulletins = eccBulletinService.saveOrFindListOnEccAndType(eccInfo.getId(),EccConstants.ECC_BULLETIN_TYPE_1);
		for(EccBulletin bulletin:bulletins){
			if (bulletin.getContent().contains("img")) {
				List<String> urlList = EccUtils.getImgSrc(bulletin.getContent());
				if (CollectionUtils.isNotEmpty(urlList)) {
					bulletin.setPictureUrl(urlList.get(0));
				}
			}
			String conStr = EccUtils.delHTMLTag(bulletin.getContent());
			if(StringUtils.isNotBlank(conStr)&&conStr.length()>45){
				conStr = conStr.substring(0, 45)+"...";
			}
			bulletin.setContent(conStr);
		}
		map.put("bulletins", bulletins);
		boolean height = false;
		if(EccConstants.ECC_MCODE_BPYT_7.equals(eccInfo.getType())){
			height = true;
		}else if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())&& EccNeedServiceUtils.getIsLayout()){
			height = true;
			
		}
		map.put("height", height);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/bulletin/eccIndexBulletin.ftl";
		}else{
			return "/eclasscard/standard/show/bulletin/eccIndexBulletin.ftl";
		}
	}
	
	@RequestMapping("/showindex/topbulletin")
	@ControllerInfo("班牌顶栏公告")
	public String showIndexTopBulletin(String cardId,String view, ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if (eccInfo == null) {
			return "";
		}
		List<EccBulletin> bulletins = eccBulletinService.saveOrFindListOnEccAndType(eccInfo.getId(),EccConstants.ECC_BULLETIN_TYPE_2);
		map.put("bulletins", bulletins);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/bulletin/eccTopBulletin.ftl";
		}else{
			return "/eclasscard/standard/show/bulletin/eccTopBulletin.ftl";
		}
	}
	
	@RequestMapping("/showindex/fullbulletin")
	@ControllerInfo("班牌全屏公告")
	public String showIndexFullBulletin(String cardId,String view,String id,ModelMap map) {
		Set<String> sids = Sets.newHashSet();
		sids.add(cardId);
		try{
			EccInfo eccInfo = eccInfoService.findOne(cardId);
			
			EccBulletin bulletin = eccBulletinService.findOne(id);
			String title = "标准公告";
			if(EccConstants.ECC_BULLETIN_TEMPLETTYPE_3==bulletin.getTempletType()){
				title = "热烈欢迎";
			}else if(EccConstants.ECC_BULLETIN_TEMPLETTYPE_2==bulletin.getTempletType()){
				title = "喜报";
			}
			List<EccFullObjAll> oldFullObjAlls = eccFullObjAllService.findByObjectId(id);
			for(EccFullObjAll fullObj:oldFullObjAlls){
				bulletin.setLockScreen(fullObj.isLockScreen());
				break;
			}
			map.put("title", title);
			map.put("bulletin", bulletin);
			map.put("infoType", eccInfo.getType());
		} catch (Exception e) {
			e.printStackTrace();
			EccNeedServiceUtils.postCloseFullScreen(sids);
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/bulletin/eccFullBulletin.ftl";
		}else{
			return "/eclasscard/standard/show/bulletin/eccFullBulletin.ftl";
		}
	}
	
	@RequestMapping("/showindex/bulletindetail")
	@ControllerInfo("班牌通知公告详情")
	public String showIndexBulletinDetail(String id, String view,ModelMap map) {
		EccBulletin bulletin = eccBulletinService.findOne(id);
		map.put("bulletin", bulletin);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/eccIndexBulletinDetail.ftl";
		}else{
			return "/eclasscard/standard/show/eccIndexBulletinDetail.ftl";
		}
	}

	@RequestMapping("/showindex/stuschedule")
	@ControllerInfo("行政班牌-课表")
	public String stuCourseSchedule(String cardId,String view,Integer status,String type,String scheduleId,ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(status==null)status=0;
		boolean showHeight = false;
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(
				0, eccInfo.getUnitId()), Semester.class);
		List<courseDto> courseDtos = Lists.newArrayList();
		if (semester == null) {
			if("2".equals(view)){
		  		map.put("showTeacher", false);
		  	}else{
		  		map.put("showHeight", true);
		  	}
			map.put("courseDtos", courseDtos);
			if(EccConstants.ECC_VIEW_2.equals(view)){
				return "/eclasscard/standard/verticalshow/course/eccCourse.ftl";
			}else{
				return "/eclasscard/standard/show/course/eccCourse.ftl";
			}
		}
		if (EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())) {
			showHeight = true;
		}
		courseDtos = eccCacheService.getCardToDayCourseSchedule(eccInfo, semester, scheduleId);
	  	if(CollectionUtils.isNotEmpty(courseDtos)) {
	  		for(courseDto dto:courseDtos) {
	  			if(StringUtils.isNotBlank(scheduleId)&&scheduleId.equals(dto.getScheduleId())){
		  			if(StringUtils.isNotBlank(dto.getTeacherId())){
		  				User user = SUtils.dt(userRemoteService.findByOwnerId(dto.getTeacherId()),new TR<User>() {});
		  				if (user != null) {
		  					dto.setTeacherUserName(user.getUsername());
		  				}
		  			}
		  			dto.setAttend(true);
		  		}
	  		}
	  		
	  	}
		
		if("2".equals(type)){
	  		map.put("showTeacher", true);
	  		showHeight = true;
	  	}else{
	  		map.put("showTeacher", false);
	  	}
	  	map.put("showHeight", showHeight);
	  	map.put("status", status);
		map.put("courseDtos", courseDtos);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/course/eccCourse.ftl";
		}else{
			return "/eclasscard/standard/show/course/eccCourse.ftl";
		}
	}
	

	@RequestMapping("/showindex/album")
	@ControllerInfo("班牌-相册,视频,PPT")
	public String showIndexAlbum(String cardId,String view, ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		String objectId = eccInfo.getId();
		List<EccAttachFolderTo> attachFolderTos = eccAttachFolderToService.findByObjIdAndRange(objectId,EccConstants.ECC_FOLDER_RANGE_2);
		String showFolderId = "";
		if (CollectionUtils.isNotEmpty(attachFolderTos)) {
			showFolderId = attachFolderTos.get(0).getFolderId();
		} else {
			List<EccAttachFolderTo> attachFolderTo1s = eccAttachFolderToService.findByObjIdAndRange(objectId,EccConstants.ECC_FOLDER_RANGE_1);
			Set<String> folderIds = attachFolderTo1s.stream().map(f -> f.getFolderId()).collect(Collectors.toSet());
			if (CollectionUtils.isNotEmpty(folderIds)) {
				List<EccAttachFolder> attachFolders = eccAttachFolderService.findListByIdIn(folderIds.toArray(new String[folderIds.size()]));
				for (EccAttachFolder attachFolder : attachFolders) {
					if (attachFolder.isShow()) {
						showFolderId = attachFolder.getId();
						break;
					}
				}
			}
		}
		if (StringUtils.isNotBlank(showFolderId)) {
			EccAttachFolder attachFolder = eccAttachFolderService.findOne(showFolderId);
			List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(showFolderId),new TR<List<Attachment>>() {});
			if (EccConstants.ECC_FOLDER_TYPE_1 == attachFolder.getType()) {
				for(Attachment attachment:attachments){
					attachment.setFilename(EccUtils.getFileNameNoExt(attachment.getFilename()));
				}
			} else if (EccConstants.ECC_FOLDER_TYPE_2 == attachFolder.getType()) {
				String furl = Evn.<SysOptionRemoteService> getBean("sysOptionRemoteService").findValue(Constant.FILE_URL);// 文件系统地址
				for(Attachment attachment:attachments){
					attachment.setDirPath(furl+"/store/"+attachment.getFilePath().replace("\\","/"));
				}
			}
			
			map.put("attachFolder", attachFolder);
			map.put("attachments", attachments);
			
			if (EccConstants.ECC_FOLDER_TYPE_2 == attachFolder.getType()) {
				EccOtherSet eccOtherSet = eccOtherSetService.findByUnitIdAndType(eccInfo.getUnitId(), EccConstants.ECC_OTHER_SET_2);
				if (eccOtherSet == null) {
					map.put("mute", 0);
				} else {
					map.put("mute", eccOtherSet.getNowvalue());
				}
			}
		}

		EccOtherSet otherSet = eccOtherSetService.findByUnitIdAndType(eccInfo.getUnitId(),EccConstants.ECC_OTHER_SET_5);
		if (otherSet!=null) {
			map.put("speedValue",otherSet.getParam() + "000");
		} else {
			map.put("speedValue","5000");
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			if ("10".equals(eccInfo.getType())) {
				return "/eclasscard/standard/verticalshow/album/eccClassAlbum.ftl";
			} else {
				return "/eclasscard/standard/verticalshow/album/eccOtherClassAlbum.ftl";
			}
		}else{
			if ("10".equals(eccInfo.getType())) {
				return "/eclasscard/standard/show/album/eccClassAlbum.ftl";
			} else {
				return "/eclasscard/standard/show/album/eccOtherClassAlbum.ftl";
			}
		}
	}
	
	@RequestMapping("/showindex/fullalbum")
	@ControllerInfo("班牌-全屏:相册,视频,PPT")
	public String showfullAlbum(String cardId,String showFolderId,String view,String lockScreen, ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if (StringUtils.isNotBlank(showFolderId)) {
			EccAttachFolder attachFolder = eccAttachFolderService.findOne(showFolderId);
			List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(showFolderId),new TR<List<Attachment>>() {});
			if (EccConstants.ECC_FOLDER_TYPE_1 == attachFolder.getType()) {
				for(Attachment attachment:attachments){
					attachment.setFilename(EccUtils.getFileNameNoExt(attachment.getFilename()));
				}
			} else if (EccConstants.ECC_FOLDER_TYPE_2 == attachFolder.getType()) {
				String furl = Evn.<SysOptionRemoteService> getBean("sysOptionRemoteService").findValue(Constant.FILE_URL);// 文件系统地址
				for(Attachment attachment:attachments){
					attachment.setDirPath(furl+"/store/"+attachment.getFilePath().replace("\\","/"));
				}
			} else {
//				attachments = attachments.stream().filter(line -> AttFileDto.PPT_STATUS_PAGE_END == line.getStatus()).collect(Collectors.toList());
				for(Attachment attachment:attachments){
					attachment.setFilename(EccUtils.getFileNameNoExt(attachment.getFilename()));
				}
			}
			map.put("attachFolder", attachFolder);
			map.put("attachments", attachments);
		}
		EccOtherSet eccOtherSet = eccOtherSetService.findByUnitIdAndType(eccInfo.getUnitId(), EccConstants.ECC_OTHER_SET_2);
		if (eccOtherSet == null) {
			map.put("mute", 0);
		} else {
			map.put("mute", eccOtherSet.getNowvalue());
		}
		map.put("lockScreen", lockScreen);
		eccOtherSet = eccOtherSetService.findByUnitIdAndType(eccInfo.getUnitId(), EccConstants.ECC_OTHER_SET_5);
		if (eccOtherSet != null) {
			map.put("speedValue", eccOtherSet.getParam()+"000");
		} else {
			map.put("speedValue", "5000");
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/album/eccFullalbum.ftl";
		}else{
			return "/eclasscard/standard/show/album/eccFullalbum.ftl";
		}
	}
	
	@RequestMapping("/showindex/clockin")
	@ControllerInfo("班牌打卡记录展示")
	public String showIndexClockIn(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null || EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType()) || EccConstants.ECC_MCODE_BPYT_2.equals(eccInfo.getType())){
			return "";
		}
		String redisKey = EccConstants.GATE_RECENT_CLOCK_REDIS;
		if (EccConstants.ECC_MCODE_BPYT_3.equals(eccInfo.getType())) {
			redisKey = EccConstants.DORM_RECENT_CLOCK_REDIS;
		}
		List<String> recentDataList = RedisUtils
				.queryDataFromList(redisKey
						+ cardId,false);
		List<StuClockResultDto> resultDtos = Lists.newArrayList();
		try {
			for(String string:recentDataList){
				StuClockResultDto resultDto =JSON.parse(string, StuClockResultDto.class);
				resultDtos.add(resultDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("resultDtos", resultDtos);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/clockin/eccIndexClockRecord.ftl";
		}else{
			return "/eclasscard/standard/show/clockin/eccIndexClockRecord.ftl";
		}
	}
	
	@RequestMapping("/showindex/indexMsg")
	@ControllerInfo("班牌首页留言提示")
	public String showIndexMsg(String cardId,String view,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){
			return "";
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/eccIndexMsg.ftl";
		}else{
			return "/eclasscard/standard/show/eccIndexMsg.ftl";
		}
	}

	@ResponseBody
	@RequestMapping("/get/system/nowtime")
	public String getSystemNowtime(HttpServletResponse response){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("time",new Date().getTime());
		return jsonObject.toJSONString();
	}
	
	@RequestMapping("/exam/door/sticker")
	@ControllerInfo("获取考场门贴")
    public String examDoorSticker(String cardId,String view,String id,ModelMap map){
		Set<String> sids = Sets.newHashSet();
		sids.add(cardId);
		try{
			EccInfo eccInfo = eccInfoService.findOne(cardId);
			if(StringUtils.isNotBlank(eccInfo.getClassId())){
				Clazz clazz = SUtils.dt(classRemoteService.findOneById(eccInfo.getClassId()),new TR<Clazz>() {});
				if(clazz!=null){
					eccInfo.setPlaceId(clazz.getTeachPlaceId());
				}
			}
			EccExamTime examTime = eccExamTimeService.findOne(id);
			//获取考场门贴examId，subjectId，placeId
			if(examTime!=null&&StringUtils.isNotBlank(examTime.getExamId()) && 
					StringUtils.isNotBlank(examTime.getSubjectId()) && StringUtils.isNotBlank(eccInfo.getPlaceId())){
				JSONObject jsonObj = null;
					jsonObj = JSONObject.parseObject(examManageRemoteService.getDoorSticker(examTime.getExamId(), examTime.getSubjectId(),eccInfo.getUnitId(), eccInfo.getPlaceId(),examTime.getSubType()));
			
				if(jsonObj!=null){
					String placeName = jsonObj.getString("examPlace");
					String subjectName = jsonObj.getString("examSubject");
					String timePeriod = jsonObj.getString("examTime");
					String teacherNames = jsonObj.getString("teacherNames");
					JSONArray arr = jsonObj.getJSONArray("arr");
					map.put("placeName", placeName);
					map.put("subjectName", subjectName);
					map.put("timePeriod", timePeriod);
					map.put("teacherNames", teacherNames);
					map.put("studentInfos", arr);
				}else{
					map.put("studentInfos", new JSONArray());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			EccNeedServiceUtils.postCloseFullScreen(sids);
		}
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/exam/doorSticker.ftl";
		}else{
			return "/eclasscard/standard/show/exam/doorSticker.ftl";
		}
	}
	
	@ResponseBody
    @RequestMapping("/check/fullscreen/object")
    public String checkFullScreenObj(String cardId){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo == null || StringUtils.isBlank(eccInfo.getId())){
			return error("no");
		}
		List<EccFullObj> eccFullObjs = eccFullObjService.findByBetweenTime(eccInfo.getId());
		int index=0;
		EccFullObj fullObj = null;
		for(EccFullObj obj:eccFullObjs){
			if(index==0){
				fullObj = obj;
			}
			if(EccConstants.ECC_FULL_OBJECT_TYPE02.equals(obj.getType())){
				fullObj = obj;
				break;
			}
			index++;
		}
		if(fullObj!=null && StringUtils.isNotBlank(fullObj.getObjectId())){
			String lock = "0";
			if(fullObj.isLockScreen())lock="1";
			return successByValue(fullObj.getObjectId()+"=="+fullObj.getType()+"=="+lock+"=="+fullObj.getId());
		}
		return error("no");
	}
	
	@ResponseBody
	@RequestMapping("/check/fullscreen/lock")
	public String checkFullScreenLock(String id){
		EccFullObj eccFullObj = eccFullObjService.findOneFromDB(id);
		if(eccFullObj!=null){
			String lock = "0";
			if(eccFullObj.isLockScreen())lock="1";
			return successByValue(lock);
		}
		return error("no");
	}
	
	@ResponseBody
	@RequestMapping("/check/last/attence")
	public String checkLastAttence(String cardId,String classAttId,String periodId){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null){return error("no");}
		EccClassAttence classAttence = eccClassAttenceService.findOne(classAttId);
		List<EccAttenceGateGrade> gateGrade = eccCacheService.getInOutCacheByPeroidId(periodId, eccInfo.getUnitId());
		if(classAttence==null||CollectionUtils.isEmpty(gateGrade)||gateGrade.get(0)==null){
			return error("no");
		}else{
			if(EccUtils.addTimeStr(gateGrade.get(0).getBeginTime()).compareTo(EccUtils.addTimeStr(classAttence.getBeginTime()))<0){
				return successByValue("1");//上课考勤在后面，返回1，否则返回2
			}else{
				return successByValue("2");//上课考勤在后面，返回1，否则返回2
			}
		}
	}
	
	@ResponseBody
	@RequestMapping("/check/inout/attence")
	public String checkInOutAttence(String cardId){
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo==null || StringUtils.isEmpty(eccInfo.getClassId())||!EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
			return error("no");
		}
		Clazz clazz = SUtils.dt(classRemoteService.findOneById(eccInfo.getClassId()),new TR<Clazz>() {});
		if(clazz==null){return error("no");}
		Grade grade = SUtils.dt(gradeRemoteService.findOneById(clazz.getGradeId()),new TR<Grade>() {});
		if(grade==null){return error("no");}
		//根据年级查询考勤时段，当前时间在时段内，返回成功
		String periodId = eccCacheService.getInOutPeroidIdCache(eccInfo.getUnitId(), grade.getGradeCode());
		if(StringUtils.isNotBlank(periodId)){
			return successByValue(periodId);
		}
		return error("no");
	}
	
	@RequestMapping("/inout/students")
	@ControllerInfo("上下学考勤页面")
	public String inoutStudents(String cardId, String periodId,String view,ModelMap map) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		List<Student> studentList = Lists.newArrayList();
		Set<String> stuIds = Sets.newHashSet();
		List<StudentWithInfo> studentWithInfoList = Lists.newArrayList();
		String timeStr = "";
		String periodGrade = "";
		Integer inoutType = 0;
		boolean isSeatSet=false;//是否设置座位表
		int rowNumbers=0;//行
		int colNumbers=0;//列
		String spaceNo=null;
		Map<String,EccSeatItem> seatItemMap=new HashMap<String, EccSeatItem>();
		if (eccInfo != null) {
			String unitId = eccInfo.getUnitId();
			String classId = eccInfo.getClassId();
			studentList = SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>() {});
			//根据班级id查找已考勤学生
			List<EccInOutAttance> inouts = eccInOutAttanceService.findByPeriodIdAndClassId(unitId, periodId, classId);
			stuIds = EntityUtils.getSet(inouts, EccInOutAttance::getStudentId);
			List<EccAttenceGateGrade> gateGrade = eccCacheService.getInOutCacheByPeroidId(periodId, unitId);
			
			if(gateGrade.get(0)!=null){
				Clazz clazz = SUtils.dt(classRemoteService.findOneById(eccInfo.getClassId()),new TR<Clazz>() {});
				Grade grade = SUtils.dt(gradeRemoteService.findOneById(clazz.getGradeId()),new TR<Grade>() {});
				periodGrade +=grade.getGradeName();
				inoutType =gateGrade.get(0).getType();
				if(Objects.equals(EccConstants.GATE_ATT_IN, inoutType)){
					periodGrade +="上学考勤";
				}else{
					periodGrade +="放学考勤";
				}
				timeStr = gateGrade.get(0).getBeginTime()+"~"+gateGrade.get(0).getEndTime();
			}
			
			EccSeatSet seatSet = eccSeatSetService.findOneByUnitIdAndClassId(unitId, classId);
			if(seatSet!=null) {
				List<EccSeatItem> eccSeatItemList = eccSeatItemService.findListBySeatId(seatSet.getUnitId(), seatSet.getId());
				if(CollectionUtils.isNotEmpty(eccSeatItemList)) {
					isSeatSet=true;
					rowNumbers=seatSet.getRowNumber();
					colNumbers=seatSet.getColNumber();
					spaceNo=seatSet.getSpaceNum()==null?"":seatSet.getSpaceNum();
					seatItemMap=EntityUtils.getMap(eccSeatItemList, e->e.getStudentId());
				}
			}
		}
		for (Student student : studentList) {
			StudentWithInfo info = new StudentWithInfo();
			info.setStudent(student);
			if(stuIds.contains(student.getId())){
				info.setStatus(EccConstants.CLASS_ATTENCE_STATUS4);
			}
			String time = DateUtils.date2StringByMinute(student.getModifyTime());
			if(time==null)time = DateUtils.date2StringByMinute(new Date());
			info.setShowPictrueUrl(EccUtils.showPictureUrl(student.getFilePath(), student.getSex(),time));
			if(seatItemMap.containsKey(student.getId())) {
				info.setRowNo(seatItemMap.get(student.getId()).getRowNum());
				info.setColNo(seatItemMap.get(student.getId()).getColNum());
			}
			studentWithInfoList.add(info);
		}
		
		map.put("periodId", periodId);
		map.put("timeStr", timeStr);
		map.put("inoutType", inoutType);
		map.put("periodGrade", periodGrade);
		map.put("sumNum", studentWithInfoList.size());//应到人数
		map.put("clockNum", stuIds.size());//实到人数
		map.put("notClockNum", studentWithInfoList.size()-stuIds.size());//未到人数
		map.put("studentWithInfoList", studentWithInfoList);
		EccOtherSet faceSet = eccOtherSetService.findByUnitIdAndType(eccInfo.getUnitId(), EccConstants.ECC_OTHER_SET_6);
		if (faceSet == null || Objects.equals(0,faceSet.getNowvalue())) {
			map.put("isActivate", false);
		} else {
			map.put("isActivate", true);
		}
		String[] spaceNoArr=new String[] {};
		if(StringUtils.isNotBlank(spaceNo)) {
			spaceNoArr=spaceNo.split(",");
		}
		if(isSeatSet) {
			map.put("isSeatSet", isSeatSet);
			map.put("rowNumbers", rowNumbers);
			map.put("colNumbers", colNumbers);
			map.put("spaceNoArr", spaceNoArr);
			if(EccConstants.ECC_VIEW_2.equals(view)){
				return "/eclasscard/standard/verticalshow/inout/attenceSeat.ftl";
			}else{
				return "/eclasscard/standard/show/inout/attenceSeat.ftl";
			}
		}
		
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/inout/attence.ftl";
		}else{
			return "/eclasscard/standard/show/inout/attence.ftl";
		}
		
	}
}
