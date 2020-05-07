package net.zdsoft.eclasscard.data.action.show;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.FaceGroupDTO;
import net.zdsoft.eclasscard.data.entity.EccFaceActivate;
import net.zdsoft.eclasscard.data.entity.EccFaceSet;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccOtherSet;
import net.zdsoft.eclasscard.data.entity.EccUserFace;
import net.zdsoft.eclasscard.data.service.EccFaceActivateService;
import net.zdsoft.eclasscard.data.service.EccFaceSetService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccOtherSetService;
import net.zdsoft.eclasscard.data.service.EccUserFaceService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.RemoteCallUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/eccShow/eclasscard")
public class EccIOTAction extends BaseAction {
	
	@Autowired
	private EccUserFaceService eccUserFaceService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private EccFaceActivateService eccFaceActivateService;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private EccOtherSetService eccOtherSetService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private EccFaceSetService eccFaceSetService;
	@RequestMapping("/face/list")
	@ControllerInfo("人脸列表")
    public String faceList(HttpServletRequest request,ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		Pagination page = createPagination();
		List<EccUserFace> faces = eccUserFaceService.findByUnitIdPage(unitId,true,page);
		map.put("faces", faces);
		sendPagination(request, map, page);
		return "/eclasscard/attanceset/faceList.ftl";
	}

	@RequestMapping("/face/edit")
	@ControllerInfo("人脸列表")
	public String faceEdit(HttpServletRequest request,ModelMap map){

		map.put("hasPic", false);
		map.put("studentName", "");
		map.put("studentId", "");
		String photoDirId = UuidUtils.generateUuid();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		String photoPath = ymd + "\\"+File.separator+photoDirId;
		map.put("photoDirId", photoDirId);
		map.put("photoPath", photoPath);
		return "/eclasscard/attanceset/faceEdit.ftl";
	}

	
	@ResponseBody
	@RequestMapping("/face/threshold")
	public String faceThreshold(String remoteParam){
		String cardId = RemoteCallUtils.getParamValue(remoteParam, "cardId");
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		Float distinguish = 0.85F;
		Float control = 0.85F;
		JSONObject returnObject = new JSONObject();
		
		EccOtherSet faceSet = eccOtherSetService.findByUnitIdAndType(eccInfo.getUnitId(), EccConstants.ECC_OTHER_SET_6);
		if (faceSet != null && Objects.equals(1,faceSet.getNowvalue())) {
			String param = faceSet.getParam();
			if(StringUtils.isNotEmpty(param)){
				String[] pas = param.split(",");
				if(pas.length==2){
					control = Float.valueOf(pas[0]);
					distinguish = Float.valueOf(pas[1]);
				}
			}
		}
		returnObject.put("control", control);
		returnObject.put("distinguish", distinguish);
		returnObject.put("success", "获取成功");
		return RemoteCallUtils.returnResultJson(returnObject);
	}
	@ResponseBody
	@RequestMapping("/face/pictures/url")
	public String facePicturesUrl(String cardId){
		FaceGroupDTO faceGroupDTO = new  FaceGroupDTO();
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if(eccInfo!=null){
			List<EccUserFace> faces = Lists.newArrayList();
			//人脸范围
			List<EccFaceSet> faceSetList=eccFaceSetService.findEccFaceSetListByInfoId(eccInfo.getUnitId(), cardId);
			if(CollectionUtils.isNotEmpty(faceSetList)) {
				Set<String> classIds = EntityUtils.getSet(faceSetList, e->e.getSetId());
				List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])),new TR<List<Student>>(){});
				Set<String> studentIds = EntityUtils.getSet(studentList, Student::getId);
				if(CollectionUtils.isNotEmpty(studentIds)){
					faces = eccUserFaceService.findByOwnerIds(studentIds.toArray(new String[studentIds.size()]));
				}
			}
			
			List<FaceGroupDTO.FaceInfo> currentFaceInfos = Lists.newArrayList();
			
			Set<String> faceIds = EntityUtils.getSet(faces, EccUserFace::getId);
			EccFaceActivate faceLower = eccFaceActivateService.findByInfoId(eccInfo.getUnitId(),cardId);
			faceGroupDTO.setLastLowerTime(new Date().getTime());
			if(CollectionUtils.isNotEmpty(faceIds)){
				for(EccUserFace face:faces){
					String fileUrl = "/eccShow/eclasscard/loadface?loadType=1&id="+face.getId();
					FaceGroupDTO.FaceInfo faceInfo = new FaceGroupDTO().new FaceInfo();
					faceInfo.setClientTag("");
					faceInfo.setFaceId(face.getOwnerId());
					faceInfo.setFaceMd5("");
					faceInfo.setFaceUrl(fileUrl);
					faceInfo.setIndex(0);
					faceInfo.setFaceName(face.getOwnerName());
					faceInfo.setUserInfo(face.getOwnerId());
					if(faceLower.getLastClientTime()==null||faceLower.getLastClientTime().compareTo(face.getModifyTime())<0){
						faceInfo.setIsUpdate(true);
					}else{
						faceInfo.setIsUpdate(false);
					}
					currentFaceInfos.add(faceInfo);
				}
			}
			faceGroupDTO.setCurrentFaceInfos(currentFaceInfos);
			faceGroupDTO.setGroupId("");
			faceGroupDTO.setIsCleanAll(false);
		}
		return new Gson().toJson(faceGroupDTO);
	}
}
