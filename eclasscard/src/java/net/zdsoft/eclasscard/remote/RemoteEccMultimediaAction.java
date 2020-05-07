package net.zdsoft.eclasscard.remote;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.entity.EccAttachFolder;
import net.zdsoft.eclasscard.data.entity.EccAttachFolderTo;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccPermission;
import net.zdsoft.eclasscard.data.service.EccAttachFolderService;
import net.zdsoft.eclasscard.data.service.EccAttachFolderToService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccPermissionService;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mobile/open/eclasscard")
public class RemoteEccMultimediaAction extends MobileAction {

    @Autowired
    private ModelRemoteService modelRemoteService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private EccAttachFolderService eccAttachFolderService;
    @Autowired
    private EccPermissionService eccPermissionService;
    @Autowired
    private EccInfoService eccInfoService;
    @Autowired
    private TeachPlaceRemoteService teachPlaceRemoteService;
    @Autowired
    private StuworkRemoteService stuworkRemoteService;
    @Autowired
    private EccAttachFolderToService eccAttachFolderToService;
    @Autowired
    private AttachmentRemoteService attachmentRemoteService;

    @RequestMapping("/multimediaPage")
    public String multimediaPage(String syncUserId, ModelMap map){
        User user = SUtils.dc(userRemoteService.findOneById(syncUserId), User.class);
        map.put("syncUserId",syncUserId);
        map.put("unitId",user.getUnitId());
        map.put("ownerId",user.getOwnerId());
        return "/eclasscard/mobileh5/multimediaPublishing/multimedia/multimediaPage.ftl";
    }

    @RequestMapping("/multimediaHead")
    public String multimediaHead(String unitId, String syncUserId, String ownerId, ModelMap map){
        List<Model> models = SUtils.dt(modelRemoteService.findByUserId(syncUserId), new TypeReference<List<Model>>() {});
        boolean allClass = models.parallelStream().anyMatch(model -> Objects.equals(model.getMid(),"35026"));
        map.put("allClass", allClass);
        //班主任
        List<Clazz> clazzs = SUtils.dt(classRemoteService.findByTeacherId(ownerId),new TR<List<Clazz>>() {});
        Map<String,Clazz> classMap = EntityUtils.getMap(clazzs, Clazz::getId);
        //设置有权限的
        List<EccPermission> permissions = eccPermissionService.findListBy("userId", syncUserId);
        Map<String,EccPermission> permMap = EntityUtils.getMap(permissions, EccPermission::getEccName);
        List<EccInfo> eccInfos = eccInfoService.findListBy("unitId",unitId);
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
        fillClsAndPlaceName(unitId, needEccInfos, false);
        map.put("eccInfos", needEccInfos);
        return "/eclasscard/mobileh5/multimediaPublishing/multimedia/multimediaHead.ftl";
    }

    @RequestMapping("/allMultimediaList")
    public String allMultimediaList(String unitId, ModelMap map){
        List<EccAttachFolder> attachFolders = attachFolders = eccAttachFolderService.findListBySchIdRange2(unitId,EccConstants.ECC_FOLDER_RANGE_2);
        attachFolders = attachFolders.stream().filter(att->!Objects.equals(att.getType(),EccConstants.ECC_FOLDER_TYPE_3)).collect(Collectors.toList());
        map.put("attachFolders", attachFolders);
        return "/eclasscard/mobileh5/multimediaPublishing/multimedia/multimediaList.ftl";
    }

    @RequestMapping("/myMultimediaList")
    public String myMultimediaList(String eccInfoId, ModelMap map){
        List<EccAttachFolder> attachFolders = null;
        if (StringUtils.isNotBlank(eccInfoId)) {
            attachFolders = eccAttachFolderService.findListByObjId(eccInfoId, EccConstants.ECC_FOLDER_RANGE_1);
            boolean noShow = attachFolders.stream().noneMatch(folder -> folder.isShow() == true);
            if (noShow) {
                List<EccAttachFolderTo> attachFolderTos = eccAttachFolderToService.findByObjIdAndRange(eccInfoId, EccConstants.ECC_FOLDER_RANGE_2);
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
            attachFolders = attachFolders.stream().filter(att->!Objects.equals(att.getType(),EccConstants.ECC_FOLDER_TYPE_3)).collect(Collectors.toList());
        }
        map.put("attachFolders", attachFolders);
        return "/eclasscard/mobileh5/multimediaPublishing/multimedia/multimediaList.ftl";
    }

    @RequestMapping("/showMultimedia")
    public String showMultimedia(String folderId,Integer folderType,ModelMap map){
        List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(folderId),new TR<List<Attachment>>() {});
        for(Attachment attachment:attachments){
            attachment.setFilename(EccUtils.getFileNameNoExt(attachment.getFilename()));
        }
        if (Objects.equals(folderType,EccConstants.ECC_FOLDER_TYPE_2)) {
            String furl = Evn.<SysOptionRemoteService> getBean("sysOptionRemoteService").findValue(Constant.FILE_URL);// 文件系统地址
            for(Attachment attachment:attachments){
                attachment.setFilePath(furl+"/store/"+attachment.getFilePath().replace("\\","/"));
            }
        }
        EccAttachFolder attachFolder = eccAttachFolderService.findOne(folderId);
        if (attachFolder.isShow()) {
            List<EccAttachFolderTo> attachFolderTos = eccAttachFolderToService.findListBy("folderId", folderId);
            map.put("folderToSize",attachFolderTos.size() + "");
        }
        map.put("attachments", attachments);
        map.put("attachFolder", attachFolder);
        return "/eclasscard/mobileh5/multimediaPublishing/multimedia/multimediaAlbum.ftl";
    }

    @ResponseBody
    @RequestMapping("/multimediaSave")
    @ControllerInfo("新增修改多媒体")
    public String multimediaSave(String id, String title, Integer type, Integer range, String eccInfoId, String unitId){
        EccAttachFolder attachFolder = new EccAttachFolder();
        try{
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
                attachFolder.setUnitId(unitId);
                attachFolder.setCreateTime(new Date());
                eccAttachFolderService.saveNewFolder(attachFolder,eccInfoId);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return error("保存失败！"+e.getMessage());
        }
        return success(attachFolder.getId());
    }

    private void fillClsAndPlaceName(String unitId, List<EccInfo> eccInfos, boolean merge){
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
            String jsonStr = stuworkRemoteService.getBuildingSbyUnitId(unitId);
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
}
