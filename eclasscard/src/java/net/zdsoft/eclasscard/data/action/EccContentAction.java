package net.zdsoft.eclasscard.data.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.constant.EccUsedFor;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.dto.EccInfoDto;
import net.zdsoft.eclasscard.data.dto.EccUsedForDto;
import net.zdsoft.eclasscard.data.dto.FileDto;
import net.zdsoft.eclasscard.data.entity.EccBulletin;
import net.zdsoft.eclasscard.data.entity.EccBulletinTo;
import net.zdsoft.eclasscard.data.entity.EccClassDesc;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccPermission;
import net.zdsoft.eclasscard.data.entity.EccPhotoAlbum;
import net.zdsoft.eclasscard.data.service.EccBulletinService;
import net.zdsoft.eclasscard.data.service.EccBulletinToService;
import net.zdsoft.eclasscard.data.service.EccClassDescService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccPermissionService;
import net.zdsoft.eclasscard.data.service.EccPhotoAlbumService;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.eclasscard.data.utils.PushPotoUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.entity.config.UnitIni;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
@Controller
@RequestMapping("/eclasscard")
public class EccContentAction extends BaseAction{
	
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
    private GradeRemoteService gradeRemoteService;
	@Autowired
	private EccPermissionService eccPermissionService;
	@Autowired
	private EccClassDescService eccClassDescService;
	@Autowired
	private EccPhotoAlbumService eccPhotoAlbumService;
	@Autowired
	private EccBulletinService eccBulletinService;
	@Autowired
	private EccBulletinToService eccBulletinToService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	
	@RequestMapping("/permission/page")
	public String permissionPage(ModelMap map){
		Set<String> notContains = Sets.newHashSet();
		boolean showxzb = true;
		if (!UnitIni.ECC_USE_VERSION_STANDARD.equals(EccNeedServiceUtils.getEClassCardVerison(getLoginInfo().getUnitId()))) {
			notContains.add(EccConstants.ECC_MCODE_BPYT_1);
			showxzb = false;
		}
		List<EccUsedForDto> usedForDtos = EccNeedServiceUtils.getEccUsedForList(notContains,getLoginInfo().getUnitId());
		map.put("usedForDtos", usedForDtos);
		map.put("showxzb", showxzb);
		return "/eclasscard/permission/eccPermission.ftl";
	}
	
	@RequestMapping("/permission/showList")
	public String permissionPage(String bpyt,ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<EccInfo> eccInfos = Lists.newArrayList();
		if (UnitIni.ECC_USE_VERSION_STANDARD.equals(EccNeedServiceUtils.getEClassCardVerison(getLoginInfo().getUnitId()))) {
			eccInfos = eccInfoService.findByUnitId(unitId);
			fillClsAndPlaceName(eccInfos, false);
		}else{
			eccInfos = eccInfoService.findListByNotClass(unitId);
		}
		Set<String> infoNames = Sets.newHashSet();
		Iterator it = eccInfos.iterator();
		while(it.hasNext()) {
			EccInfo eccInfo = (EccInfo) it.next();
			if (StringUtils.isNotBlank(bpyt)) {
				if (eccInfo.getType().equals(bpyt)) {
					infoNames.add(eccInfo.getName());
				} else {
					it.remove();
				}
			} else {
				infoNames.add(eccInfo.getName());
			}
		}
		Map<String,List<EccPermission>> permissionsMap = eccPermissionService.findbyEccInfoMap(infoNames.toArray(new String[infoNames.size()]),unitId);
		List<EccInfoDto> dtos = Lists.newArrayList();
		for(EccInfo eccInfo:eccInfos){
			EccInfoDto infoDto = new EccInfoDto();
			infoDto.setEccInfo(eccInfo);
			if(permissionsMap.containsKey(eccInfo.getName())){
				List<EccPermission> permissions = permissionsMap.get(eccInfo.getName());
				for(EccPermission permission:permissions){
					if(StringUtils.isBlank(infoDto.getPermisionUserIds())){
						infoDto.setPermisionUserIds(permission.getUserId());
						infoDto.setPermisionUserNames(permission.getUserName());
					}else{
						infoDto.setPermisionUserIds(infoDto.getPermisionUserIds()+","+permission.getUserId());
						infoDto.setPermisionUserNames(infoDto.getPermisionUserNames()+","+permission.getUserName());
					}
				}
			}
			dtos.add(infoDto);
		}
		map.put("usedForMap", EccUsedFor.getEccUsedForMap());
		map.put("infoDtos", dtos);
		return "/eclasscard/permission/eccPermissionList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/permission/save")
	public String permissionSave(String eccName,String eccNames,String[] userIds,Boolean isAll){
		try{
			eccPermissionService.savePermission(eccName,eccNames,userIds,isAll,getLoginInfo().getUnitId());
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@RequestMapping("/myClassCard/page")
	public String myClassCardPage(){
		return "/eclasscard/myeclasscrad/myEclasscradIndex.ftl";
	}
	
	@RequestMapping("/myClassCard/list")
	public String myClassCardList(ModelMap map){
//		Set<String> classIds = Sets.newHashSet();
//		Set<String> eccNames = Sets.newHashSet();
		//班主任
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findByTeacherId(getLoginInfo().getOwnerId()),new TR<List<Clazz>>() {});
		Map<String,Clazz> classMap = EntityUtils.getMap(clazzs, "id");
//		for(Clazz clazz:clazzs){
//			classIds.add(clazz.getId());
//		}
		//设置有权限的
		List<EccPermission> permissions = eccPermissionService.findListBy("userId", getLoginInfo().getUserId());
		Map<String,EccPermission> permMap = EntityUtils.getMap(permissions, "eccName");
//		for(EccPermission permission:permissions){
//			eccNames.add(permission.getEccName());
//		}
		List<EccInfo> eccInfos = eccInfoService.findListBy("unitId",getLoginInfo().getUnitId());
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
		fillClsAndPlaceName(needEccInfos, false);
		map.put("usedForMap", EccUsedFor.getEccUsedForMap());
		map.put("eccInfos", needEccInfos);
		return "/eclasscard/myeclasscrad/myEclasscrad.ftl";
	}
	
	@RequestMapping("/myClassCard/tab")
	public String myClassCardEdit(String id,ModelMap map){
		EccInfo eccInfo = eccInfoService.findOne(id);
		if(eccInfo==null){
			eccInfo = new EccInfo();
		}
		map.put("eccInfo", eccInfo);
		return "/eclasscard/myeclasscrad/myEclasscradTab.ftl";
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
				}
			}
		}
		String photoDirId = UuidUtils.generateUuid();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		String photoPath = ymd +"\\"+ File.separator+photoDirId;
		map.put("photoDirId", photoDirId);
		map.put("photoPath", photoPath);
		map.put("classDesc", classDesc);
		return "/eclasscard/myeclasscrad/myEclasscradDesc.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/description/save")
	public String descriptionSave(EccClassDesc eccClassDesc){
		try{
			if(StringUtils.isNotBlank(eccClassDesc.getId())){
				EccClassDesc classDesc = eccClassDescService.findOne(eccClassDesc.getId());
				classDesc.setPictrueId(eccClassDesc.getPictrueId());
				classDesc.setContent(eccClassDesc.getContent());
				eccClassDescService.save(classDesc);
				Set<String> sids = Sets.newHashSet();
				if(StringUtils.isNotBlank(classDesc.getClassId())){
					List<EccInfo> infosByClz = eccInfoService.findByClassIdIn(new String[]{classDesc.getClassId()});
					sids.addAll(EntityUtils.getSet(infosByClz, EccInfo::getId));
					if(CollectionUtils.isNotEmpty(sids)){
						EccNeedServiceUtils.postDesc(sids);
					}
				}
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
	public String descriptionDeletePic(String id){
		try{
			if(StringUtils.isNotBlank(id)){
				String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
						"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
				EccClassDesc classDesc = eccClassDescService.findOne(id);
				EccPhotoAlbum album = eccPhotoAlbumService.findOne(classDesc.getPictrueId());
				if(StringUtils.isNotBlank(album.getPictrueDirpath())){
					File file = new File(fileSystemPath+ File.separator+album.getPictrueDirpath());
					if(file.delete()){
						classDesc.setPictrueId("");
						eccClassDescService.save(classDesc);
						return success("保存成功");
					}else{
						return error("保存失败！");
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@RequestMapping("/photoAlbum/list")
	public String photoAlbumEdit(String eccInfoId,ModelMap map){
		List<EccPhotoAlbum> albums = eccPhotoAlbumService.findListByObjectIdPage(eccInfoId, null);
		String photoDirId = UuidUtils.generateUuid();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		String photoPath = ymd +"\\"+ File.separator+photoDirId;
		map.put("photoDirId", photoDirId);
		map.put("photoPath", photoPath);
		try {
			for(EccPhotoAlbum album:albums){
				album.setPictrueDirpath("/webuploader/showpicture?filePath="+URLEncoder.encode(album.getPictrueDirpath(),"UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		map.put("albums", albums);
		map.put("eccInfoId", eccInfoId);
		return "/eclasscard/myeclasscrad/myEclasscradAlbum.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value="/photoAlbum/save" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String photoAlbumSave(@RequestBody String array,String objectId){
		String pictureId = "";
		try{
			List<FileDto> fileDtos = SUtils.dt(array,new TR<List<FileDto>>() {});
			List<EccPhotoAlbum> albums = Lists.newArrayList();
			for(FileDto fileDto:fileDtos){
				if(fileDto.getFileName().length()>=50){
					return error("图片名字过长，最大长度为50个字符");
				}
				EccPhotoAlbum photoAlbum = new EccPhotoAlbum();
				photoAlbum.setId(UuidUtils.generateUuid());
				photoAlbum.setObjectId(objectId);
				photoAlbum.setPictrueName(fileDto.getFileName());
				photoAlbum.setPictrueDirpath(fileDto.getFilePath());
				photoAlbum.setCreateTime(new Date());
				albums.add(photoAlbum);
			}
			EccClassDesc classDesc = eccClassDescService.findOne(objectId);
			if(albums.size()>0){
				eccPhotoAlbumService.saveAll(albums.toArray(new EccPhotoAlbum[albums.size()]));
				if(classDesc!=null){
					pictureId = albums.get(0).getId();
					classDesc.setPictrueId(pictureId);
					eccClassDescService.save(classDesc);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return successByValue(pictureId);
	}

	@RequestMapping("/allBulletin/list")
	public String bulletinList(){
		return "/eclasscard/allbulletin/bulletinIndex.ftl";
	}
	@RequestMapping("/bulletin/list")
	public String bulletinList(String eccInfoId,Date startTime,Date endTime,Integer status, ModelMap map){
		Pagination page = createPagination();
		page.setPageSize(10);
		String startTimeStr = DateUtils.date2StringByMinute(startTime);
		String endTimeStr = DateUtils.date2StringByMinute(endTime);
		List<EccBulletin> eccBulletins = eccBulletinService.findList(eccInfoId,getLoginInfo().getUserId(),getLoginInfo().getUnitId(),startTimeStr,endTimeStr,status,page);
		fillUserName(eccBulletins);
		if(StringUtils.isBlank(eccInfoId)){
			map.put("isAll", true);
		}else{
			map.put("isAll", false);
		}
		map.put("eccBulletins", eccBulletins);
		map.put("pagination", page);
		map.put("eccInfoId", eccInfoId);
		return "/eclasscard/allbulletin/bulletinList.ftl";
	}
	
	@RequestMapping("/bulletin/edit")
	public String bulletinEdit(String id,String isEdit,String eccInfoId,ModelMap map){
		EccBulletin eccBulletin = null;
		String eccInfoIds = "";
		List<String[]> showNames = new ArrayList<String[]>();
		if(StringUtils.isBlank(eccInfoId)){
			map.put("backAll", true);
		}else{
			map.put("backAll", false);
		}
		if(StringUtils.isNotBlank(id)){
			eccBulletin = eccBulletinService.findOne(id);
			if(StringUtils.isBlank(eccInfoId)){
				List<EccBulletinTo> bulletinTos = eccBulletinToService.findListBy("bulletinId", id);
				Set<String> infoIds = EntityUtils.getSet(bulletinTos, "eccInfoId");
				if(infoIds.size()>0){
					List<EccInfo> eccInfos = eccInfoService.findListFillName(infoIds.toArray(new String[infoIds.size()]));
					fillClsAndPlaceName(eccInfos,true);
					for(EccInfo eccInfo:eccInfos){
						String[] names = {"",""};
						if(StringUtils.isBlank(eccInfoIds)){
							eccInfoIds = eccInfo.getId();
						}else{
							eccInfoIds = eccInfoIds+","+eccInfo.getId();
						}
						names[0] = eccInfo.getPlaceName();
						names[1] = eccInfo.getName();
						showNames.add(names);
					}
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
		if(StringUtils.isBlank(eccInfoId)){
			map.put("isAll", true);
		}else{
			map.put("isAll", false);
		}
		if(eccBulletin==null){
			eccBulletin = new EccBulletin();
		}
		map.put("eccBulletin", eccBulletin);
		if("1".equals(isEdit)){
			map.put("isEdit", true);
		}else{
			map.put("isEdit", false);
		}
		return "/eclasscard/allbulletin/bulletinEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/bulletin/save")
	public String bulletinSave(EccBulletin eccBulletin,String[] eccInfoIds){
		try{
			eccBulletin.setSendType(EccConstants.ECC_SENDTYPE_9);
			eccBulletinService.saveBulletin(eccBulletin,eccInfoIds,getLoginInfo().getUserId(),getLoginInfo().getUnitId());
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@ResponseBody
	@RequestMapping("/bulletin/delete")
	public String bulletinDelete(String id){
		try{
			eccBulletinService.deleteBulletin(id);
		}catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！"+e.getMessage());
		}
		return success("删除成功");
	}
	private void fillUserName(List<EccBulletin> eccBulletins){
		if(CollectionUtils.isEmpty(eccBulletins)){
			return;
		}
		Set<String> userIds = Sets.newHashSet();
		for(EccBulletin bulletin:eccBulletins){
			userIds.add(bulletin.getUserId());
			bulletin.setBeginTime(bulletin.getBeginTime().substring(5));
			bulletin.setEndTime(bulletin.getEndTime().substring(5));
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
        				info.setPlaceName("（已删除）");
        			}
        		}else{
        			if(teachPlaceMap.containsKey(info.getPlaceId())){
        				info.setPlaceName(teachPlaceMap.get(info.getPlaceId()));
        			}else if(StringUtils.isNotBlank(info.getPlaceId())){
        				info.setPlaceName("（已删除）");
        			}
        			if(merge&&StringUtils.isNotBlank(info.getClassName()) && StringUtils.isNotBlank(info.getPlaceName())){
        				info.setPlaceName(info.getClassName()+"（"+info.getPlaceName()+"）");
        			}
    			}
        	}
        }
	}
	
	@RequestMapping("/photoall/edit/page")
	public String photoallEditPage(ModelMap map){
		return "/eclasscard/myeclasscrad/eclasscradPhotoIndex.ftl";
	}
	
	@RequestMapping("/photoall/edit")
	public String photoallEdit(ModelMap map){
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		fileSystemPath += File.separator;
		String photoDirId = UuidUtils.generateUuid();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		String photoPath = ymd +"\\"+ File.separator+photoDirId;
		map.put("photoDirId", photoDirId);
		map.put("photoPath", photoPath);
		try {
			fileSystemPath = URLEncoder.encode(fileSystemPath,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		map.put("sysFPath", fileSystemPath);
		return "/eclasscard/myeclasscrad/eclasscradPhotoAll.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value="/photoall/save",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String photoallSave(@RequestBody String array,String sendType,String[] eccInfoIds,ModelMap map){
		try{
			List<FileDto> fileDtos = SUtils.dt(array,new TR<List<FileDto>>() {});
			for(FileDto fileDto:fileDtos){
				if(fileDto.getFileName().length()>=50){
					return error("图片名字过长，最大长度为50个字符");
				}
			}
			eccPhotoAlbumService.saveToMore(getLoginInfo().getUnitId(),sendType,eccInfoIds,fileDtos);
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
	@ResponseBody
	@RequestMapping("/photo/modify/push")
	@ControllerInfo("照片删除推送页面")
	public String photoDeletePush(String[] objectIds){
		try {
			pushPhoto(objectIds);
		} catch (Exception e) {
			return error("推送失败！"+e.getMessage());
		}
		return success("推送成功");
	}
	
	private void pushPhoto(String[] infoIds){
		Set<String> cardNumbers = Sets.newHashSet();
		for(String cardId:infoIds){
			cardNumbers.add(cardId);
		}
		if(CollectionUtils.isNotEmpty(cardNumbers)){
			PushPotoUtils.addPushCards(cardNumbers, getLoginInfo().getUnitId());
		}
	}
}
