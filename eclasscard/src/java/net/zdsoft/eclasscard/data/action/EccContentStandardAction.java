package net.zdsoft.eclasscard.data.action;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.remote.utils.AttachmentUtils;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.constant.EccUsedFor;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.entity.*;
import net.zdsoft.eclasscard.data.service.*;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.eclasscard.data.utils.PushPotoUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
/**
 * 班牌标准版---我的班牌和全校班牌内容
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/eclasscard/standard")
public class EccContentStandardAction extends BaseAction{

	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
	private EccPermissionService eccPermissionService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private EccBulletinService eccBulletinService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private EccClassDescService eccClassDescService;
	@Autowired
	private EccPhotoAlbumService eccPhotoAlbumService;
	@Autowired
	private EccAttachFolderService eccAttachFolderService;
	@Autowired
	private EccBulletinToService eccBulletinToService;
	@Autowired
	private EccHonorService eccHonorService;
	@Autowired
	private EccHonorToService eccHonorToService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private EccAttachFolderToService eccAttachFolderToService;
	@Autowired
	private EccFullObjAllService eccFullObjAllService;
	@Autowired
	private EccFullObjService eccFullObjService;
	@Autowired
	private EccOtherSetService eccOtherSetService;
	
	@RequestMapping("/myclasscard/page")
	public String myClassCardPage(){
		return "/eclasscard/standard/back/myeclasscard/myEclasscradIndex.ftl";
	}
	
	@RequestMapping("/myclasscard/list")
	public String myClassCardList(ModelMap map){
		//班主任
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findByTeacherId(getLoginInfo().getOwnerId()),new TR<List<Clazz>>() {});
		Map<String,Clazz> classMap = EntityUtils.getMap(clazzs, "id");
		//设置有权限的
		List<EccPermission> permissions = eccPermissionService.findListBy("userId", getLoginInfo().getUserId());
		Map<String,EccPermission> permMap = EntityUtils.getMap(permissions, "eccName");
		List<EccInfo> eccInfos = eccInfoService.findByUnitId(getLoginInfo().getUnitId());
		List<EccInfo> needEccInfos = Lists.newArrayList();
		for(EccInfo eccInfo:eccInfos){
			if(StringUtils.isNotBlank(eccInfo.getClassId())&&classMap.containsKey(eccInfo.getClassId())){
				needEccInfos.add(eccInfo);
			}else{
				if(StringUtils.isNotBlank(eccInfo.getName())&&permMap.containsKey(eccInfo.getName())){
					needEccInfos.add(eccInfo);
				}
			}
		}
		if(CollectionUtils.isEmpty(needEccInfos)){
			
			return "/eclasscard/standard/back/myeclasscard/myEclasscrad.ftl";
		}
		fillClsAndPlaceName(needEccInfos, false);
		map.put("usedForMap", EccUsedFor.getEccUsedForMap());
		map.put("eccInfos", needEccInfos);
		return "/eclasscard/standard/back/myeclasscard/myEclasscrad.ftl";
	}
	
	@RequestMapping("/myclasscard/tab")
	public String myClassCardEdit(String id,String tabType,String subTabType,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(id);
		if(eccInfo==null){
			eccInfo = new EccInfo();
		}
		if("0".equals(tabType)){
			tabType = "3";
		}
		if(StringUtils.isEmpty(subTabType)){
			subTabType = "1";
		}
		map.put("tabType", tabType);
		map.put("subTabType", subTabType);
		map.put("eccInfo", eccInfo);
		return "/eclasscard/standard/back/myeclasscard/myEclasscradTab.ftl";
	}
	
	@RequestMapping("/description/edit")
	public String descriptionEdit(String eccInfoId,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(eccInfoId);
		EccClassDesc classDesc = new EccClassDesc();
		if(eccInfo!=null&&EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
			if(StringUtils.isNotBlank(eccInfo.getClassId())){
				classDesc = eccClassDescService.findOneBy("classId", eccInfo.getClassId());
				if(classDesc==null){
					classDesc = new EccClassDesc();
					classDesc.setId(UuidUtils.generateUuid());
					classDesc.setClassId(eccInfo.getClassId());
					eccClassDescService.save(classDesc);
				}else{
					List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(classDesc.getId()),new TR<List<Attachment>>() {});
					if(CollectionUtils.isNotEmpty(attachments)){
						classDesc.setPicUrl(attachments.get(0).getShowPicUrl());
					}
				}
			}
		}
		String photoDirId = UuidUtils.generateUuid();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		String photoPath = ymd + "\\"+File.separator+photoDirId;
		map.put("photoDirId", photoDirId);
		map.put("photoPath", photoPath);
		map.put("classDesc", classDesc);
		return "/eclasscard/standard/back/myeclasscard/myEclasscradDesc.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/description/save")
	public String descriptionSave(EccClassDesc eccClassDesc){
		try{
			if(StringUtils.isNotBlank(eccClassDesc.getId())){
				EccClassDesc classDesc = eccClassDescService.findOne(eccClassDesc.getId());
				classDesc.setPictrueId(eccClassDesc.getPictrueId());
				classDesc.setContent(EccUtils.filtrationA(eccClassDesc.getContent()));
				eccClassDescService.save(classDesc);
			}else{
				return error("保存失败！简介不存在");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@ResponseBody
	@RequestMapping("/description/deletePictrue")
	public String descriptionDelete(String id){
		try{
			if(StringUtils.isNotBlank(id)){
				EccClassDesc classDesc = eccClassDescService.findOne(id);
				List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(id),new TR<List<Attachment>>() {});
				Set<String> attachIds = EntityUtils.getSet(attachments, "id");
				if(CollectionUtils.isNotEmpty(attachIds)){
					attachmentRemoteService.deleteAttachments(attachIds.toArray(new String[attachIds.size()]), null);
					classDesc.setPictrueId("");
					eccClassDescService.save(classDesc);
					return success("保存成功");
				}
				return error("保存失败！");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@RequestMapping("/pvalbum/list")
	@ControllerInfo("视频图片文件List")
	public String photoAlbumList(String folderId,String tabType,Integer type,String changeable,ModelMap map){
		List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(folderId),new TR<List<Attachment>>() {});
		for(Attachment attachment:attachments){
			attachment.setFilename(EccUtils.getFileNameNoExt(attachment.getFilename()));
		}
		if (type == EccConstants.ECC_FOLDER_TYPE_2) {
			String furl = Evn.<SysOptionRemoteService> getBean("sysOptionRemoteService").findValue(Constant.FILE_URL);// 文件系统地址
			for(Attachment attachment:attachments){
				attachment.setFilePath(furl+"/store/"+attachment.getFilePath().replace("\\","/"));
			}
		}
		
		String fileDirId = UuidUtils.generateUuid();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		String filePath = ymd +"\\"+ File.separator+fileDirId;
		map.put("fileDirId", fileDirId);
		map.put("filePath", filePath);
		
		EccAttachFolder attachFolder = eccAttachFolderService.findOne(folderId);
		map.put("attachments", attachments);
		map.put("folderId", folderId);
		map.put("folderName", attachFolder.getTitle());
		map.put("pvNum", attachments.size());
		map.put("tabType", tabType);
		map.put("type", type);
		map.put("changeable", changeable);
		return "/eclasscard/standard/back/eclasscradAlbum.ftl";
	}
	
	@RequestMapping("/pptalbum/list")
	@ControllerInfo("PPT文件List")
	public String pptAlbumList(String folderId,ModelMap map){
		List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(folderId),new TR<List<Attachment>>() {});
		if (CollectionUtils.isNotEmpty(attachments)) {
			map.put("attachments",  attachments.stream().filter(line -> AttFileDto.PPT_STATUS_PAGE_END == line.getStatus()).collect(Collectors.toList()));
		}
		EccOtherSet otherSet = eccOtherSetService.findByUnitIdAndType(getLoginInfo().getUnitId(),EccConstants.ECC_OTHER_SET_5);
		if (otherSet != null) {
			map.put("speedValue", otherSet.getParam() + "000");
		} else {
			map.put("speedValue", "5000");
		}
		return "/eclasscard/standard/back/eclasscardPPTAlbum.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value="/photoalbum/save",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ControllerInfo("保存图片")
	public String photoAlbumSave(@RequestBody String array,String objectId){
		String showPicUrl = "";
		try{
			List<AttFileDto> fileDtos = SUtils.dt(array,new TR<List<AttFileDto>>() {});
			for(AttFileDto fileDto:fileDtos){
				fileDto.setObjectType(EccConstants.ECC_ATTACHMENT_TYPE);
				fileDto.setObjectId(objectId);
				fileDto.setObjectUnitId(getLoginInfo().getUnitId());
			}
			List<Attachment> attachments = SUtils.dt(attachmentRemoteService.saveAttachment(SUtils.s(fileDtos)),new TR<List<Attachment>>() {});
			if(CollectionUtils.isNotEmpty(attachments)){
				showPicUrl = attachments.get(0).getShowPicUrl();
			}
			EccAttachFolder attachFolder = eccAttachFolderService.findOne(objectId);
			if(attachFolder!=null&&attachFolder.isShow()){
				pushPVP(objectId,false);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return successByValue(showPicUrl);
	}
	
	@ResponseBody
	@RequestMapping(value="/video/save",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ControllerInfo("保存视频")
	public String videoSave(@RequestBody String array,String folderId){
		try{
			List<AttFileDto> fileDtos = SUtils.dt(array,new TR<List<AttFileDto>>() {});
			eccAttachFolderService.saveVideo(fileDtos,folderId,getLoginInfo().getUnitId());
			EccAttachFolder attachFolder = eccAttachFolderService.findOne(folderId);
			if(attachFolder!=null&&attachFolder.isShow()){
				pushPVP(folderId,false);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@ResponseBody
	@RequestMapping("/fileName/update")
	@ControllerInfo("修改文件名")
	public String fileNameUpdate(String id,String fileName){
		try{
			List<Attachment> attachments = SUtils.dt(attachmentRemoteService
					.findAttachmentDirPathById(id),
					new TR<List<Attachment>>() {
					});
			if(CollectionUtils.isNotEmpty(attachments)){
				Attachment attachment = attachments.get(0);
				if(attachment !=null){
					fileName=fileName+"."+attachment.getExtName();
				}
			}
			attachmentRemoteService.updateFileNameById(id, fileName);
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@RequestMapping("/eccfolder/list")
	@ControllerInfo("多媒体List")
	public String eccFolderList(String objectId,String tabType,ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		String userId = getLoginInfo().getUserId();
		List<EccAttachFolder> attachFolders = eccAttachFolderService.findListByObjId(objectId,EccConstants.ECC_FOLDER_RANGE_1);
		
		boolean noShow = attachFolders.stream().noneMatch(folder -> folder.isShow() == true );
		if (noShow) {
			List<EccAttachFolderTo> attachFolderTos = eccAttachFolderToService.findByObjIdAndRange(objectId, EccConstants.ECC_FOLDER_RANGE_2);
			if (CollectionUtils.isNotEmpty(attachFolderTos)) {
				EccAttachFolder attachFolder = eccAttachFolderService.findOne(attachFolderTos.get(0).getFolderId());
				if (attachFolder.getType() == 3) {
					List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(attachFolder.getId()),new TR<List<Attachment>>() {});
					if (CollectionUtils.isNotEmpty(attachments)) {
						Attachment attachment = attachments.get(0);
						attachFolder.setCoverUrl(attachment.getShowPicUrl());
					}
				}
				attachFolders.add(0, attachFolder);
			}
		}
		
		EccInfo eccInfo = eccInfoService.findOne(objectId);
		map.put("url",eccInfo.getUrl()==null?"":eccInfo.getUrl());
		JSONObject json = new JSONObject();
		JSONObject params = new JSONObject();
    	params.put("unitId", unitId);
    	params.put("userId", userId);
    	params.put("objectType", EccConstants.ECC_ATTACHMENT_TYPE);
    	params.put("objectId", objectId);
    	params.put("range", "1");
    	json.put(RemoteCallUtils.JSON_PARAM, params);
    	String param = RemoteCallUtils.encode(json.toJSONString());
    	String dirName = "";
    	try {
    		dirName = new BASE64Encoder().encode("我的班牌-多媒体".getBytes("UTF-8"));
    		dirName = URLEncoder.encode(dirName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		map.put("dirName", dirName);
		map.put("param", param);
		map.put("objectId", objectId);
		map.put("tabType", tabType);
		map.put("attachFolders", attachFolders);
		return "/eclasscard/standard/back/myeclasscard/myEclasscardFolder.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/eccfolder/save")
	@ControllerInfo("新增修改多媒体")
	public String eccFolderSave(String id,String title,Integer range,Integer type,String objectId){
		try{
			EccAttachFolder attachFolder = new EccAttachFolder();
			if(StringUtils.isNotBlank(id)){
				attachFolder = eccAttachFolderService.findOne(id);
				attachFolder.setTitle(title);
				eccAttachFolderService.save(attachFolder);
			}else{
				attachFolder.setId(UuidUtils.generateUuid());
				if(EccConstants.ECC_FOLDER_RANGE_2 == range){
					attachFolder.setRange(EccConstants.ECC_FOLDER_RANGE_2);
					attachFolder.setSendType(EccConstants.ECC_SENDTYPE_9);
				}else{
					attachFolder.setRange(EccConstants.ECC_FOLDER_RANGE_1);
					attachFolder.setSendType(EccConstants.ECC_SENDTYPE_0);
				}
				attachFolder.setShow(false);
				attachFolder.setTitle(title);
				attachFolder.setType(type);
				attachFolder.setUnitId(getLoginInfo().getUnitId());
				attachFolder.setCreateTime(new Date());
				eccAttachFolderService.saveNewFolder(attachFolder,objectId);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@ResponseBody
	@RequestMapping("/eccfolder/show")
	@ControllerInfo("修改展示")
	public String eccFolderShow(String id){
		try{
			eccAttachFolderService.updateIsShow(id);
			pushPVP(id, true);
		}catch (Exception e) {
			e.printStackTrace();
			return error("设置失败！"+e.getMessage());
		}
		return success("设置成功");
	}
	
	@ResponseBody
	@RequestMapping("/eccfolder/delete")
	@ControllerInfo("删除文件集")
	public String eccFolderDelete(String id,int range,boolean isShow){
		try{
			if(EccConstants.ECC_FOLDER_RANGE_2 != range){
				range = EccConstants.ECC_FOLDER_RANGE_1;
			}
			if(isShow){
				pushPVP(id, true);
			}
			eccAttachFolderService.deleteById(id,range);
		}catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！"+e.getMessage());
		}
		return success("删除成功");
	}
	
	@RequestMapping("/schclasscard/page")
	public String schClassCardPage(){
		return "/eclasscard/standard/back/scheclasscard/schEclasscradIndex.ftl";
	}
	
	@RequestMapping("/schclasscard/tab")
	@ControllerInfo("全校班牌-多媒体Tab")
	public String schClassCardTab(String tabType,ModelMap map){
		map.put("tabType", tabType);
		if(EccConstants.SCHOOL_YKJXSXZQYTXX.equals(getLoginInfo().getUnitId())) {
			map.put("showSchoolSpace", "1");
		}
		return "/eclasscard/standard/back/scheclasscard/schEclasscradTab.ftl";
	}
	
	@RequestMapping("/eccfolder/listsch")
	@ControllerInfo("全校班牌-多媒体List")
	public String eccFolderListSch(String tabType,ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		String userId = getLoginInfo().getUserId();
		List<EccAttachFolder> attachFolders = eccAttachFolderService.findListBySchIdRange2(unitId,EccConstants.ECC_FOLDER_RANGE_2);
		
		JSONObject json = new JSONObject();
		JSONObject params = new JSONObject();
    	params.put("unitId", unitId);
    	params.put("userId", userId);
    	params.put("objectType", EccConstants.ECC_ATTACHMENT_TYPE);
    	params.put("objectId", "");
    	params.put("range", "2");
    	json.put(RemoteCallUtils.JSON_PARAM, params);
    	String param = RemoteCallUtils.encode(json.toJSONString());
    	String dirName = "";
    	try {
    		dirName = new BASE64Encoder().encode("全校班牌-多媒体".getBytes("UTF-8"));
    		dirName = URLEncoder.encode(dirName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		map.put("param", param);
		map.put("dirName", dirName);
		map.put("tabType", tabType);
		map.put("attachFolders", attachFolders);
		return "/eclasscard/standard/back/scheclasscard/schEclasscardFolder.ftl";
	}
	
	@RequestMapping("/eccfolder/send")
	public String eccFolderSend(String id,ModelMap map){
		List<EccAttachFolderTo> attachFolderTos = eccAttachFolderToService.findListBy("folderId", id);
		map.put("id", id);
		if (CollectionUtils.isNotEmpty(attachFolderTos)) {
			Set<String> eccInfoIds = EntityUtils.getSet(attachFolderTos, "sendObjectId");
			List<EccInfo> eccInfos = eccInfoService.findListByIdIn(eccInfoIds.toArray(new String[eccInfoIds.size()]));
			List<EccInfo> eccInfos2 = eccInfoService.findByClassIdIn(eccInfoIds.toArray(new String[eccInfoIds.size()]));
			eccInfos.addAll(eccInfos2);
			StringBuilder builder = new StringBuilder();
			for (int i=0;i<eccInfos.size();i++) {
				builder.append(eccInfos.get(i).getId());
				if (i != (eccInfos.size() -1)) {
					builder.append(",");
				}
			}
			map.put("eccInfoIds", builder.toString());
		}
		return "/eclasscard/standard/back/scheclasscard/schFolderSend.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/send/types")
	@ControllerInfo("全校班牌-设置展示对象")
	public String eccSendTypes(String id,String eccInfoIds){
		try{
			eccAttachFolderService.updateIsShow(id,eccInfoIds);
		}catch (Exception e) {
			e.printStackTrace();
			return error("设置失败！"+e.getMessage());
		}
		return success("设置成功");
	}
	
	@ResponseBody
	@RequestMapping("/send/notshow")
	@ControllerInfo("全校班牌-对象不展示")
	public String eccSendNotShow(String id){
		try{
			eccAttachFolderService.updateNotShow(id);
		}catch (Exception e) {
			e.printStackTrace();
			return error("设置失败！"+e.getMessage());
		}
		return success("设置成功");
	}
	
	private void fillClsAndPlaceName(List<EccInfo> eccInfos,boolean merge){
		Set<String> classIdInfoSet = Sets.newHashSet();
        Set<String> palceIdInfoSet = Sets.newHashSet();
        for(EccInfo info:eccInfos){
        	if(StringUtils.isNotBlank(info.getClassId()))
        		classIdInfoSet.add(info.getClassId());
        	if(StringUtils.isNotBlank(info.getPlaceId()))
        		palceIdInfoSet.add(info.getPlaceId());
        }
        if(classIdInfoSet.size()>0){
        	List<Clazz> clazzs = SUtils.dt(classRemoteService.findClassListByIds(classIdInfoSet.toArray(new String[classIdInfoSet.size()])),new TR<List<Clazz>>() {});
        	Set<String> teachPlaceIds = (EntityUtils.getSet(clazzs, "teachPlaceId"));
        	palceIdInfoSet.addAll(teachPlaceIds);
        	Map<String,Clazz> classNameMap = EntityUtils.getMap(clazzs, "id");
        	for(EccInfo info:eccInfos){
        		if(classNameMap.containsKey(info.getClassId())){
        			Clazz clazz = classNameMap.get(info.getClassId());
        			if(clazz!=null){
        				info.setClassName(clazz.getClassNameDynamic());
        				info.setPlaceId(clazz.getTeachPlaceId());
        			}
        		}
        	}
        }
        if(palceIdInfoSet.size()>0){
        	Map<String, String> teachPlaceMap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(palceIdInfoSet.toArray(new String[0])),new TR<Map<String, String>>() {});
        	String jsonStr = stuworkRemoteService.getBuildingSbyUnitId(getLoginInfo().getUnitId());
        	List<DormBuildingDto> buildingDtos = SUtils.dt(jsonStr,new TR<List<DormBuildingDto>>() {});
    		Map<String,String> dormBuildMap = EntityUtils.getMap(buildingDtos, "buildingId","buildingName");
        	for(EccInfo info:eccInfos){
        		if(EccConstants.ECC_MCODE_BPYT_3.equals(info.getType())){
        			if(dormBuildMap.containsKey(info.getPlaceId())){
        				info.setPlaceName(dormBuildMap.get(info.getPlaceId()));
        			}else if(StringUtils.isNotBlank(info.getPlaceId())){
        				info.setPlaceName("");
        			}
        		}else{
        			if(teachPlaceMap.containsKey(info.getPlaceId())){
        				info.setPlaceName(teachPlaceMap.get(info.getPlaceId()));
        			}else if(StringUtils.isNotBlank(info.getPlaceId())){
        				info.setPlaceName("");
        			}
        			if(merge&&StringUtils.isNotBlank(info.getClassName()) && StringUtils.isNotBlank(info.getPlaceName())){
        				info.setPlaceName(info.getClassName()+"（"+info.getPlaceName()+"）");
        			}
    			}
        	}
        }
	}
	
	@RequestMapping("/bulletin/list")
	@ControllerInfo("查看班牌公告信息List")
	public String bulletinList(String bulletinLevel,String eccInfoId, ModelMap map,HttpServletRequest request){
		int row = NumberUtils.toInt(syncParameters(request).get("_pageSize"));
		Pagination page = createPagination();
		if (row <= 0) {
			page.setPageSize(20);
		}
		List<EccBulletin> eccBulletins = eccBulletinService.findStandardList(eccInfoId,getLoginInfo().getUserId(),getLoginInfo().getUnitId(),bulletinLevel,page);
		Set<String> bulletinIds = eccBulletins.stream().filter(line -> EccConstants.ECC_BULLETIN_TYPE_3.equals(line.getType())).map(EccBulletin::getId).collect(Collectors.toSet());
		if(CollectionUtils.isNotEmpty(bulletinIds)){
			List<EccFullObjAll> oldFullObjAlls = eccFullObjAllService.findByObjectId(bulletinIds.toArray(new String[bulletinIds.size()]));
			Map<String,Boolean> lockMap = EntityUtils.getMap(oldFullObjAlls, EccFullObjAll::getObjectId,EccFullObjAll::isLockScreen);
			Map<String,String> objIdMap = EntityUtils.getMap(oldFullObjAlls, EccFullObjAll::getObjectId,EccFullObjAll::getId);
			for(EccBulletin bulletin:eccBulletins){
				if(lockMap.containsKey(bulletin.getId())){
					bulletin.setLockScreen(lockMap.get(bulletin.getId()));
					bulletin.setFullObjAllId(objIdMap.get(bulletin.getId()));
				}
			}
		}
		fillUserName(eccBulletins);
		for(EccBulletin bulletin:eccBulletins){
			bulletin.setContent(EccUtils.delHTMLTag(bulletin.getContent()));
		}
		map.put("eccBulletins", eccBulletins);
		map.put("pagination", page);
        sendPagination(request, map, page);
		map.put("eccInfoId", eccInfoId);
		if (StringUtils.isNotBlank(eccInfoId)) {
			return "/eclasscard/standard/back/myeclasscard/bulletinList.ftl";
		} else {
			return "/eclasscard/standard/back/scheclasscard/bulletinList.ftl";
		}
	}
	
	/**
	 * 给公告信息添加发布人姓名和修改权限
	 * @param eccBulletins
	 */
	private void fillUserName(List<EccBulletin> eccBulletins){
		if(CollectionUtils.isEmpty(eccBulletins)){
			return;
		}
		Set<String> userIds = Sets.newHashSet();
		for(EccBulletin bulletin:eccBulletins){
			userIds.add(bulletin.getUserId());
//			bulletin.setBeginTime(bulletin.getBeginTime().substring(5));
//			bulletin.setEndTime(bulletin.getEndTime().substring(5));
		}
		List<User> users = SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[userIds.size()])),new TR<List<User>>() {});
		Map<String,String> userMap = EntityUtils.getMap(users, "id", "realName");
		for(EccBulletin bulletin:eccBulletins){
			if(userMap.containsKey(bulletin.getUserId())){
				bulletin.setUserName(userMap.get(bulletin.getUserId()));
			}
			if(getLoginInfo().getUserId().equals(bulletin.getUserId())){
				bulletin.setCanEdit(true);
			}else{
				bulletin.setCanEdit(false);
			}
		}
	}
	
	@RequestMapping("/bulletin/edit")
	@ControllerInfo("班牌公告信息详情")
	public String bulletinEdit(String id,String isEdit,String eccInfoId,ModelMap map){
		EccBulletin eccBulletin = null;
		String eccInfoIds = "";
		List<String[]> showNames = new ArrayList<String[]>();
		boolean oneOrAll = true;
		if (StringUtils.isBlank(eccInfoId)) {
			oneOrAll = false;
		}
		if(StringUtils.isNotBlank(id)){
			eccBulletin = eccBulletinService.findOne(id);
			if(StringUtils.isBlank(eccInfoId)){
				List<EccBulletinTo> bulletinTos = eccBulletinToService.findListBy("bulletinId", id);
				Set<String> infoIds = EntityUtils.getSet(bulletinTos, EccBulletinTo::getEccInfoId);
				if(CollectionUtils.isNotEmpty(infoIds)){
					List<EccInfo> eccInfos = eccInfoService.findListFillName(infoIds.toArray(new String[infoIds.size()]));
					fillClsAndPlaceName(eccInfos,false);
					for(EccInfo eccInfo:eccInfos){
						String[] names = {"",""};
						if(StringUtils.isBlank(eccInfoIds)){
							eccInfoIds = eccInfo.getId();
						}else{
							eccInfoIds = eccInfoIds+","+eccInfo.getId();
						}
						if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
							names[0] = eccInfo.getClassName();
						}else{
							names[0] = eccInfo.getPlaceName();
						}
						names[1] = eccInfo.getName();
						if(StringUtils.isNotBlank(names[0])){
							showNames.add(names);
						}
					}
				}
			}
			if(eccBulletin != null){
				List<EccFullObjAll> oldFullObjAlls = eccFullObjAllService.findByObjectId(id);
				for(EccFullObjAll fullObj:oldFullObjAlls){
					eccBulletin.setLockScreen(fullObj.isLockScreen());
					break;
				}
			}
			if(eccBulletin!=null&&EccConstants.ECC_SENDTYPE_0.equals(eccBulletin.getSendType())){
				EccBulletinTo bulletinTo = eccBulletinToService.findOneBy("bulletinId", id);
				if(bulletinTo!=null){
					eccInfoId = bulletinTo.getEccInfoId();
				}
			}
		}
		map.put("eccInfoIds", eccInfoIds);
		map.put("showNames", showNames);
		if(StringUtils.isNotBlank(eccInfoId)){
			EccInfo eccInfo = eccInfoService.findOneFillName(eccInfoId);
			map.put("eccInfo", eccInfo);
		}
		if(eccBulletin==null){
			eccBulletin = new EccBulletin();
			eccBulletin.setTempletType(1);
		}
		map.put("eccBulletin", eccBulletin);
		if("1".equals(isEdit)){
			map.put("isEdit", true);
		}else{
			map.put("isEdit", false);
		}
		if (oneOrAll) {
			return "/eclasscard/standard/back/myeclasscard/bulletinEdit.ftl";
		} else {
			return "/eclasscard/standard/back/scheclasscard/bulletinEdit.ftl";
		}
	}
	
	@ResponseBody
	@RequestMapping("/bulletin/save")
	public String bulletinSave(EccBulletin eccBulletin,String notice,String[] eccInfoIds){
		if (StringUtils.isBlank(eccBulletin.getContent())) {
			eccBulletin.setContent(notice);
		}
		if (EccConstants.ECC_BULLETIN_TYPE_1.equals(eccBulletin.getType())) {
			eccBulletin.setContent(EccUtils.filtrationA(eccBulletin.getContent()));
		}
		if(StringUtils.isBlank(eccBulletin.getId())){
			if(DateUtils.date2StringByMinute(new Date()).compareTo(eccBulletin.getEndTime())>0){
				return error("当前时间超出展示时间！");
			}
		}
		try{
			eccBulletin.setSendType(EccConstants.ECC_SENDTYPE_9);
			eccBulletinService.saveBulletin(eccBulletin,eccInfoIds,getLoginInfo().getUserId(),getLoginInfo().getUnitId());
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@RequestMapping("/honor/list")
	@ControllerInfo("全部班牌—荣誉")
	public String honorList(String eccInfoId,String subTabType,ModelMap map,HttpServletRequest request){
		if (StringUtils.isNotBlank(eccInfoId)) {
			map.put("eccInfoId", eccInfoId);
			if(StringUtils.isEmpty(subTabType)){
				subTabType = "1";
			}
			map.put("subTabType", subTabType);
			return "/eclasscard/standard/back/myeclasscard/honorListTab.ftl";
		} else {
			String unitId = getLoginInfo().getUnitId();
			int row = NumberUtils.toInt(syncParameters(request).get("_pageSize"));
			Pagination page = createPagination();
			if (row <= 0) {
				page.setPageSize(20);
			}
			List<EccHonor> eccHonors = eccHonorService.findByUnitIdAndType(unitId,EccConstants.ECC_HONOR_TYPE_1,page);
			for (EccHonor eccHonor : eccHonors) {
				if (getLoginInfo().getUserId().equals(eccHonor.getCreatorId())) {
					eccHonor.setCanEdit(true);
				} else {
					eccHonor.setCanEdit(false);
				}
			}
			map.put("eccHonors", eccHonors);
	        sendPagination(request, map, page);
			return "/eclasscard/standard/back/scheclasscard/honorList.ftl";
		}
	}
	
	@RequestMapping("/honor/classlist")
	@ControllerInfo("我的班牌—班级荣誉")
	public String honorClassList(String eccInfoId,ModelMap map,HttpServletRequest request){
		EccInfo eccInfo = eccInfoService.findOne(eccInfoId);
		int row = NumberUtils.toInt(syncParameters(request).get("_pageSize"));
		Pagination page = createPagination();
		if (row <= 0) {
			page.setPageSize(20);
		}
		List<EccHonor> eccHonors = eccHonorService.findByHonorToObjIds(new String[]{eccInfo.getClassId()},page);
		map.put("eccHonors", eccHonors);
		sendPagination(request, map, page);
		return "/eclasscard/standard/back/myeclasscard/honorClassList.ftl";
	}
	
	@RequestMapping("/honor/stulist")
	@ControllerInfo("我的班牌—个人荣誉")
	public String honorStuList( String eccInfoId,ModelMap map,HttpServletRequest request){
		int row = NumberUtils.toInt(syncParameters(request).get("_pageSize"));
		Pagination page = createPagination();
		if (row <= 0) {
			page.setPageSize(20);
		}
		EccInfo eccInfo = eccInfoService.findOne(eccInfoId);
		List<Student> students = SUtils.dt(studentRemoteService.findByClassIds(eccInfo.getClassId()),new TR<List<Student>>(){});
		Map<String,String> studentNameMap = EntityUtils.getMap(students, Student::getId, Student::getStudentName);
		Set<String> stuIdsList = EntityUtils.getSet(students, Student::getId);
		if(CollectionUtils.isNotEmpty(stuIdsList)){
			List<EccHonor> eccHonors = eccHonorService.findByHonorToObjIds(stuIdsList.toArray(new String[stuIdsList.size()]),page);
			if(CollectionUtils.isNotEmpty(eccHonors)){
				Map<String,String> honnorStuNameMap = Maps.newHashMap();
				List<EccHonorTo> eccHonorTos = eccHonorToService.findByObjectIdIn(stuIdsList.toArray(new String[0]));
				for (EccHonorTo eccHonorTo : eccHonorTos) {
					honnorStuNameMap.put(eccHonorTo.getHonorId(), studentNameMap.get(eccHonorTo.getObjectId()));
				}
				Set<String> honorids = EntityUtils.getSet(eccHonors, EccHonor::getId);
				List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(honorids.toArray(new String[0])),new TR<List<Attachment>>(){});
				Map<String,Attachment> pictureMap = EntityUtils.getMap(attachments,Attachment::getObjId);
				Attachment attachment = null;
				String smallFilePath = "";
				for (EccHonor eccHonor : eccHonors) {
					eccHonor.setStudentName(honnorStuNameMap.get(eccHonor.getId()));
					attachment = pictureMap.get(eccHonor.getId());
					eccHonor.setAttachmentId(attachment.getId());
					smallFilePath = AttachmentUtils.getAddSuffixName(attachment.getFilePath(), EccConstants.ECC_SMALL_IMG);
					attachment.setFilePath(smallFilePath);
					eccHonor.setPictureUrl(attachment.getShowPicUrl());
					if (getLoginInfo().getUserId().equals(eccHonor.getCreatorId())) {
						eccHonor.setCanEdit(true);
					} else {
						eccHonor.setCanEdit(false);
					}
				}
			}
			map.put("eccHonors", eccHonors);
		}
		map.put("eccInfoId", eccInfoId);
		sendPagination(request, map, page);
		return "/eclasscard/standard/back/myeclasscard/honorStuList.ftl";
	}
	
	@RequestMapping("/honor/show")
	@ControllerInfo("个人荣誉")
	public String honorShow(String honorId,ModelMap map){
		EccHonor eccHonor = eccHonorService.findById(honorId);
		map.put("eccHonor", eccHonor);
		if (EccConstants.ECC_HONOR_TYPE_1.equals(eccHonor.getType())) {
			return "/eclasscard/standard/back/myeclasscard/honorClassShow.ftl";
		}else{
			List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(honorId),new TR<List<Attachment>>(){});
			Attachment attachment = attachments.get(0);
			String smallFilePath = AttachmentUtils.getAddSuffixName(attachment.getFilePath(), EccConstants.ECC_SMALL_IMG);
			attachment.setFilePath(smallFilePath);
			eccHonor.setPictureUrl(attachment.getShowPicUrl());
			return "/eclasscard/standard/back/myeclasscard/honorStuShow.ftl";
		}
	}
	@RequestMapping("/honor/edit")
	@ControllerInfo("个人荣誉")
	public String honorEdit(String eccInfoId,String honorId,String attachmentId,ModelMap map){
		String photoDirId = UuidUtils.generateUuid();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		String photoPath = ymd +"\\"+ File.separator+photoDirId;
		map.put("photoDirId", photoDirId);
		map.put("photoPath", photoPath);
		String imageUrl = "";
		String smlFilePath = "";
		if (StringUtils.isNotBlank(attachmentId)) {
			List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathById(attachmentId),new TR<List<Attachment>>(){});
			Attachment attachment = attachments.get(0);
			smlFilePath = AttachmentUtils.getAddSuffixName(attachment.getFilePath(), EccConstants.ECC_SMALL_IMG);
			attachment.setFilePath(smlFilePath);
			imageUrl = attachments.get(0).getShowPicUrl();
		}
		map.put("imageUrl", imageUrl);
		EccInfo eccInfo = eccInfoService.findOne(eccInfoId);
		EccHonor eccHonor = null;
		if (StringUtils.isNotBlank(honorId)) {
			eccHonor = eccHonorService.findById(honorId);
		} else {
			eccHonor = new EccHonor();
		}
		map.put("eccHonor", eccHonor);
		map.put("eccInfoId", eccInfoId);
		map.put("classId", eccInfo.getClassId());
		return "/eclasscard/standard/back/myeclasscard/honorEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/honor/imageurl")
	@ControllerInfo("图片路径")
	public String getImageUrl(String path,ModelMap map){
		String imageUrl = "";
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		String filePath = fileSystemPath + File.separator + "upload" + File.separator + path;
		int sysPathlength = (fileSystemPath + File.separator).length();
		File file = new File(filePath);
		JSONObject jsonObject = new JSONObject();
		Boolean haveImg = false;
		long time = 0l;
		if (file.exists()) {
			File[] files = file.listFiles();
			if(files!=null){
				for(File f:files){
					if (f.isHidden()) {
						continue;
					} else {
						long ftime = f.lastModified();
						if(ftime>time){
							time = ftime;
							haveImg = true;
							imageUrl = f.getPath().substring(sysPathlength);
							jsonObject.put("filePath",imageUrl);
							jsonObject.put("fileName",f.getName());
						}else{
							f.delete();
						}
					}
				}
			}
		}
		if (!haveImg) {
			return null;
		}
		try {
			imageUrl = "/webuploader/showpicture?filePath="+URLEncoder.encode(imageUrl,"UTF-8");
			jsonObject.put("imageUrl",imageUrl);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonObject.toJSONString();
	}
	
	
	@RequestMapping("/honor/classedit")
	@ControllerInfo("班级荣誉")
	public String classEdit(String canEdit, String honorId,ModelMap map){
		EccHonor eccHonor = null; 
		if (StringUtils.isNotBlank(honorId)) {
			eccHonor = eccHonorService.findById(honorId);
		} else {
			eccHonor = new EccHonor();
		}
		if ("1".equals(canEdit)) {
			map.put("canEdit", true);
		} else {
			map.put("canEdit", false);
		}
		map.put("eccHonor", eccHonor);
		return "/eclasscard/standard/back/scheclasscard/honorEdit.ftl";
	}
	
	
	@ResponseBody
	@RequestMapping("/honor/save")
	@ControllerInfo("荣誉保存")
	public String honorSave(EccHonor eccHonor,ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		String userId = getLoginInfo().getUserId();
		eccHonor.setUnitId(unitId); 
		eccHonor.setCreatorId(userId);
		if (StringUtils.isEmpty(eccHonor.getId())) {
			if(DateUtils.date2StringByMinute(new Date()).compareTo(eccHonor.getEndTime())>0){
				return error("当前时间超出展示时间！");
			}
			eccHonor.setCreateTime(new Date());
		}
		try {
			eccHonorService.saveHonor(eccHonor);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@ResponseBody
	@RequestMapping("/honor/delete")
	@ControllerInfo("荣誉删除")
	public String honorDelete(String honorId,String attachmentId,ModelMap map){
		try {
			eccHonorService.deleteHonor(honorId,attachmentId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！"+e.getMessage());
		}
		return success("删除成功");
	}
	
	@ResponseBody
	@RequestMapping("/photo/delete/push")
	@ControllerInfo("照片删除推送页面")
	public String photoDeletePush(String folderId){
		try {
			pushPVP(folderId, false);
		} catch (Exception e) {
			return error("推送失败！"+e.getMessage());
		}
		return success("推送成功");
	}
	
	private void pushPVP(String folderId,boolean isNow){
		List<EccAttachFolderTo> attachFolderTos = eccAttachFolderToService.findListBy("folderId", folderId);
		Set<String> infoIds = EntityUtils.getSet(attachFolderTos, EccAttachFolderTo::getSendObjectId);
		if(CollectionUtils.isNotEmpty(infoIds)){
			if(isNow){
				EccNeedServiceUtils.postPhoto(infoIds, getLoginInfo().getUnitId());
			}else{
				PushPotoUtils.addPushCards(infoIds, getLoginInfo().getUnitId());
			}
		}
	}
	
	@RequestMapping("/fullscreensch/edit")
	@ControllerInfo("全校班牌全屏展示编辑")
	public String fullScreenSchEdit(String id,ModelMap map){
		if (StringUtils.isNotEmpty(id)){
			EccFullObjAll eccFullObjAll = eccFullObjAllService.findOneFromDB(id);
			List<EccFullObj> eccFullObjs = eccFullObjService.findBySourceIds(id);
			Set<String> eccInfoIds = EntityUtils.getSet(eccFullObjs, EccFullObj::getEccInfoId);
			String infoIdStr = eccInfoIds.stream().collect(Collectors.joining(","));
                    
			fillObjectMsg(eccFullObjAll.getType(), eccFullObjAll.getObjectId(), false, map);
			map.put("eccInfoIds", infoIdStr);
			
			map.put("eccFullObjAll", eccFullObjAll);
		}else{
			map.put("eccInfoIds", "");
			map.put("eccFullObjAll", new EccFullObjAll());
			
		}
		return "/eclasscard/standard/back/scheclasscard/fullobjallEdit.ftl";
	}
	
	@RequestMapping("/fullscreensch/show")
	@ControllerInfo("全校班牌全屏展示查看")
	public String fullScreenSchShow(String id,ModelMap map){
		if (StringUtils.isNotEmpty(id)){
			EccFullObjAll eccFullObjAll = eccFullObjAllService.findOne(id);
			fillObjectMsg(eccFullObjAll.getType(), eccFullObjAll.getObjectId(), true, map);
			map.put("eccFullObjAll", eccFullObjAll);
		}
		EccOtherSet otherSet = eccOtherSetService.findByUnitIdAndType(getLoginInfo().getUnitId(),EccConstants.ECC_OTHER_SET_5);
		if (otherSet != null) {
			map.put("speedValue", otherSet.getParam() + "000");
		} else {
			map.put("speedValue", "5000");
		}
		return "/eclasscard/standard/back/scheclasscard/fullobjallShow.ftl";
	}
	
	@RequestMapping("/fullscreensch/list")
	@ControllerInfo("全校班牌全屏展示列表")
	public String fullScreenSchList(ModelMap map,HttpServletRequest request){
		String showId = "";
		List<EccFullObjAll> eccFullObjAlls = eccFullObjAllService.findByUnitId(getLoginInfo().getUnitId(), null);
		if(CollectionUtils.isNotEmpty(eccFullObjAlls)){
			showId = eccFullObjAlls.get(0).getId();
		}
		map.put("eccFullObjAlls", eccFullObjAlls);
		map.put("showId", showId);
		return "/eclasscard/standard/back/scheclasscard/fullobjallList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/fullscreensch/save")
	@ControllerInfo("校级班牌发布全屏展示")
	public String fullScreenSchSave(EccFullObjAll eccFullObjAll,String[] eccInfoIds){
		boolean isEdit = false;
		String unitId = getLoginInfo().getUnitId();
		String userId = getLoginInfo().getUserId();
		eccFullObjAll.setUnitId(unitId); 
		eccFullObjAll.setUserId(userId);
		eccFullObjAll.setSendType(EccConstants.ECC_SENDTYPE_9);
		if (StringUtils.isEmpty(eccFullObjAll.getId())) {
			if(DateUtils.date2StringByMinute(new Date()).compareTo(eccFullObjAll.getEndTime())>0){
				return error("当前时间超出展示时间！");
			}
			eccFullObjAll.setCreateTime(new Date());
			eccFullObjAll.setId(UuidUtils.generateUuid());
			eccFullObjAll.setStatus(EccConstants.ECC_SHOW_STATUS_0);
		}else{
			isEdit = true;
		}
		if(isEdit){
			EccFullObjAll oldFullObjAll = eccFullObjAllService.findOne(eccFullObjAll.getId());
			if(EccConstants.ECC_SHOW_STATUS_1 == oldFullObjAll.getStatus()){
				return error("前台展示中，请勿修改！");
			}
		}
		try {
			eccFullObjAllService.saveFullObjAll(eccFullObjAll, eccInfoIds,isEdit);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@ResponseBody
	@RequestMapping("/fullscreensch/lockobj")
	@ControllerInfo("校级班牌修改全屏信息锁定状态")
	public String fullScreenSchLockobj(String id,String lock){
		boolean isLock = true;
		if(!"1".equals(lock)){
			isLock = false;
		}
		try {
			eccFullObjAllService.updateFullObjAllLock(id,isLock);
		} catch (Exception e) {
			e.printStackTrace();
			if(isLock){
				return error("锁定失败！");
			}else{
				return error("解锁失败！");
			}
		}
		if(isLock){
			return success("锁定成功");
		}else{
			return success("解锁成功");
		}
	}
	
	@ResponseBody
	@RequestMapping("/fullscreensch/delete")
	@ControllerInfo("校级班牌发布全屏展示删除")
	public String fullScreenSchDelete(String id){
		try {
			eccFullObjAllService.deleteFullObjAll(id);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！"+e.getMessage());
		}
		return success("删除成功");
	}
	
	@ResponseBody
	@RequestMapping("/fullscreen/save")
	@ControllerInfo("单个班牌发布全屏展示")
	public String fullScreenSave(EccFullObj eccFullObj){
		boolean isEdit = false;
		if (StringUtils.isEmpty(eccFullObj.getId())) {
			if(DateUtils.date2StringByMinute(new Date()).compareTo(eccFullObj.getEndTime())>0){
				return error("当前时间超出展示时间！");
			}
			eccFullObj.setCreateTime(new Date());
			eccFullObj.setId(UuidUtils.generateUuid());
			eccFullObj.setStatus(EccConstants.ECC_SHOW_STATUS_0);
		}else{
			isEdit = true;
		}
		eccFullObj.setSourceType(EccConstants.FULL_SCREEN_SOURCE_TYPE_01);
		eccFullObj.setSourceId(eccFullObj.getObjectId());
		if(isEdit){
			EccFullObj oldFullObj = eccFullObjService.findOne(eccFullObj.getId());
			if(EccConstants.ECC_SHOW_STATUS_1 == oldFullObj.getStatus()){
				return error("前台展示中，请勿修改！");
			}
		}
		try {
			eccFullObjService.saveWithTask(eccFullObj,isEdit);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@ResponseBody
	@RequestMapping("/fullscreen/lockobj")
	@ControllerInfo("我的班牌修改全屏信息锁定状态")
	public String fullscreenLockobj(String id,String lock){
		boolean isLock = true;
		if(!"1".equals(lock)){
			isLock = false;
		}
		try {
			eccFullObjService.updateFullObjAllLock(id,isLock);
		} catch (Exception e) {
			e.printStackTrace();
			if(isLock){
				return error("锁定失败！");
			}else{
				return error("解锁失败！");
			}
		}
		if(isLock){
			return success("锁定成功");
		}else{
			return success("解锁成功");
		}
	}
	
	@ResponseBody
	@RequestMapping("/fullscreen/delete")
	@ControllerInfo("单个班牌全屏展示删除")
	public String fullScreenDelete(String id){
		try {
			eccFullObjService.deleteFullObj(id);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！"+e.getMessage());
		}
		return success("删除成功");
	}
	
	@RequestMapping("/fullscreen/edit")
	@ControllerInfo("单个班牌全屏展示编辑")
	public String fullScreenEdit(String eccInfoId,String id,String tabType,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOneFillName(eccInfoId);
		if (StringUtils.isNotEmpty(id)){
			EccFullObj fullObj = eccFullObjService.findOne(id);
			fillObjectMsg(fullObj.getType(),fullObj.getObjectId(),true,map);
			map.put("fullObj", fullObj);
		}else{
			map.put("fullObj", new EccFullObj());
		}
		map.put("eccInfo", eccInfo);
		if(EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())){
			map.put("toObjectId", eccInfo.getId());
		}else{
			map.put("toObjectId", eccInfoId);
		}
		map.put("tabType", tabType);
		return "/eclasscard/standard/back/myeclasscard/fullobjEdit.ftl";
	}
	
	@RequestMapping("/fullscreen/show")
	@ControllerInfo("单个班牌全屏展示查看")
	public String fullScreenShow(String eccInfoId,String id,String tabType,ModelMap map){
		boolean canSend =  eccOtherSetService.openProperty(getLoginInfo().getUnitId(), EccConstants.ECC_OTHER_SET_1);
		EccInfo eccInfo = eccInfoService.findOne(eccInfoId);
		if (StringUtils.isNotEmpty(id)){
			EccFullObj fullObj = eccFullObjService.findOne(id);
			map.put("fullObj", fullObj);
			fillObjectMsg(fullObj.getType(), fullObj.getObjectId(), true, map);
		}
		EccOtherSet otherSet = eccOtherSetService.findByUnitIdAndType(getLoginInfo().getUnitId(),EccConstants.ECC_OTHER_SET_5);
		if (otherSet != null) {
			map.put("speedValue", otherSet.getParam() + "000");
		} else {
			map.put("speedValue", "5000");
		}
		map.put("canSend", canSend);
		map.put("eccInfo", eccInfo);
		map.put("tabType", tabType);
		return "/eclasscard/standard/back/myeclasscard/fullobjShow.ftl";
	}
	
	@RequestMapping("/fullscreen/list")
	@ControllerInfo("单个班牌全屏展示列表")
	public String fullScreenList(String eccInfoId,String tabType,ModelMap map){
		String showId = "";
		boolean canSend =  eccOtherSetService.openProperty(getLoginInfo().getUnitId(), EccConstants.ECC_OTHER_SET_1);
		List<EccFullObj> eccFullObjs = eccFullObjService.findByEccInfoId(eccInfoId, null);
		if(CollectionUtils.isNotEmpty(eccFullObjs)){
			showId = eccFullObjs.get(0).getId();
		}
		map.put("canSend", canSend);
		map.put("eccInfoId", eccInfoId);
		map.put("tabType", tabType);
		map.put("eccFullObjs", eccFullObjs);
		map.put("showId", showId);
		return "/eclasscard/standard/back/myeclasscard/fullobjList.ftl";
	}
	
	private void fillObjectMsg(String type,String objectId,boolean hasAttach,ModelMap map) {
		if(EccConstants.ECC_FULL_OBJECT_TYPE01.endsWith(type)){
			EccBulletin bulletin = eccBulletinService.findOne(objectId);
			if(EccConstants.ECC_BULLETIN_TEMPLETTYPE_3.equals(bulletin.getTempletType())){
				map.put("objectName", "欢迎致词");
			}else if(EccConstants.ECC_BULLETIN_TEMPLETTYPE_2.equals(bulletin.getTempletType())){
				map.put("objectName", "喜报");
			}else if(EccConstants.ECC_BULLETIN_TEMPLETTYPE_1.equals(bulletin.getTempletType())){
				map.put("objectName", "标准公告");
			}else{
				map.put("objectName", "自定义公告");
			}
			map.put("object", bulletin);
		}else{
			EccAttachFolder attachFolder = eccAttachFolderService.findOne(objectId);
			map.put("objectName", attachFolder.getTitle());
			if(hasAttach || attachFolder.getType() == EccConstants.ECC_FOLDER_TYPE_3){
				List<Attachment> attachments = SUtils.dt(attachmentRemoteService
						.findAttachmentDirPathByObjId(objectId),
						new TR<List<Attachment>>() {
				});
				if(attachFolder.getType() == EccConstants.ECC_FOLDER_TYPE_3 && CollectionUtils.isNotEmpty(attachments)){
					attachments = attachments.stream().filter(line -> AttFileDto.PPT_STATUS_PAGE_END == line.getStatus()).collect(Collectors.toList());
					if(CollectionUtils.isNotEmpty(attachments)){
						Attachment att = attachments.get(0);
						attachFolder.setCoverUrl(att.getShowPicUrl());
					}
				}
				for(Attachment attachment:attachments){
					attachment.setFilename(EccUtils.getFileNameNoExt(attachment.getFilename()));
				}
				if (attachFolder.getType() == EccConstants.ECC_FOLDER_TYPE_2) {
					String furl = Evn.<SysOptionRemoteService> getBean("sysOptionRemoteService").findValue(Constant.FILE_URL);// 文件系统地址
					for(Attachment attachment:attachments){
						attachment.setFilePath(furl+"/store/"+attachment.getFilePath().replace("\\","/"));
					}
				}
				attachFolder.setAttachments(attachments);
			}
			map.put("object", attachFolder);
		}
	}
	
	@RequestMapping("/schoolSpace/page")
	@ControllerInfo("校园空间")
	public String schoolSpaceShow(ModelMap map){
		EccOtherSet otherSet = eccOtherSetService.findByUnitIdAndType(getLoginInfo().getUnitId(),EccConstants.ECC_OTHER_SET_8);
		String urlParm="";
		if(otherSet!=null) {
			urlParm=otherSet.getParam();
		}
		map.put("urlParm", urlParm);
		
		return "/eclasscard/standard/back/scheclasscard/schoolSpaceSet.ftl";
	}
	@ResponseBody
	@RequestMapping("/schoolSpace/save")
	@ControllerInfo("校园空间")
	public String schoolSpaceSave(String parmUrl){
		EccOtherSet otherSet = eccOtherSetService.findByUnitIdAndType(getLoginInfo().getUnitId(),EccConstants.ECC_OTHER_SET_8);
		if(otherSet!=null) {
			otherSet.setParam(parmUrl);
		}else {
			otherSet=new EccOtherSet();
			otherSet.setId(UuidUtils.generateUuid());
			otherSet.setParam(parmUrl);
			otherSet.setType(EccConstants.ECC_OTHER_SET_8);
			otherSet.setUnitId(getLoginInfo().getUnitId());
		}
		try {
			eccOtherSetService.save(otherSet);
			RedisUtils.del(EccConstants.ECC_OTHER_SET+"_"+otherSet.getUnitId()+"_"+otherSet.getType());
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败");
		}
		return success("保存成功");
	}
}
