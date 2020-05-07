package net.zdsoft.familydear.action;

import net.zdsoft.familydear.entity.FamDearAttachment;
import net.zdsoft.activity.service.TestService;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.action
 * @ClassName: testWebUpload
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/22 15:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/22 15:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/test")
public class testWebUpload extends BaseAction{
    @Autowired
    private TestService testService;
    @Autowired
    private StorageDirRemoteService storageDirRemoteService;
    @RequestMapping("/testWebUpload")
    public String test(ModelMap map){
        String unitId =  getLoginInfo().getUnitId();
        map.put("id",UuidUtils.generateUuid());
        return "familydear/test/testWebUploadIndex.ftl";
    }

    @RequestMapping("/editTestWebUpload")
    public String editTest(String id,ModelMap map){
        String unitId =  getLoginInfo().getUnitId();
//        List<StudevelopHonor> classHonorList = studevelopHonorService.getStudevelopHonorByAcadyearAndSemester(studevelopHonor.getAcadyear(),studevelopHonor.getSemester(),studevelopHonor.getClassId());
////        StudevelopHonor classHonor = null;
////        if(CollectionUtils.isNotEmpty(classHonorList)){
////            classHonor = classHonorList.get(0);
////        }
////        if(classHonor == null){
////            classHonor = studevelopHonor;
////            classHonor.setId(UuidUtils.generateUuid());
////            classHonor.setUnitId(unitId);
////            classHonor.setCreationTime(new Date());
////            studevelopHonorService.save(classHonor);
////        }else{
////            List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(classHonor.getId(), "class_honor");
////            map.put("actDetails", atts);
////        }

        map.put("id",UuidUtils.generateUuid());
//        map.put("actType","class_honor");


        return "familydear/test/editTestWebUpload.ftl";
    }


    @ControllerInfo("新增附件")
    @RequestMapping("/save")
    public String saveAtt(ModelMap map, HttpServletRequest request){
        try {
            List<MultipartFile> files = StorageFileUtils.getFiles(request);
            if(CollectionUtils.isEmpty(files)){
                return "";
            }
            String objType = request.getParameter("objType");
            String objId = request.getParameter("objId");
            for (int i=0;i<files.size();i++) {
                MultipartFile file = files.get(i);
                FamDearAttachment att = new FamDearAttachment();
                att.setObjecttype("1");
                att.setObjId(objId);
                att.setUnitId(getLoginInfo().getUnitId());
                try {
                    testService.saveAttachment(att, file);
                } catch (Exception e) {
                    log.error("第"+(i+1)+"个附件保存失败："+e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    @ResponseBody
    @ControllerInfo("删除附件")
    @RequestMapping("/delete")
    public String deleteAtts(String ids, ModelMap map){
        if(StringUtils.isEmpty(ids)){
            return error("没有选择要删除的记录！");
        }
        try {
            testService.delete(StringUtils.split(ids, ","));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return error("删除失败！");
        }
        return success("删除成功！");
    }

    @ControllerInfo("显示图片附件")
    @RequestMapping("/edit")
    public String Edit(String id,ModelMap modelMap){
        List<FamDearAttachment> atts = new ArrayList<>();
        if(StringUtils.isBlank(id)) {
            atts = testService.getAttachmentByObjId("402881e86ae43355016ae43706e60009", "1");
        }else {
            atts = testService.getAttachmentByObjId(id, "1");
        }
        modelMap.put("id",id);
        modelMap.put("actDetails", atts);
        return "familydear/test/testWebUpload.ftl";
    }

    @ControllerInfo("显示图片附件")
    @RequestMapping("/showPic")
    public String showPic(String id, String showOrigin, HttpServletResponse response){
        try {
            FamDearAttachment att = testService.findOne(id);
            if(att == null){
                return null;
            }
            StorageDir sd = SUtils.dc(storageDirRemoteService.findOneById(att.getDirId()), StorageDir.class);
            if(sd == null){
                return null;
            }
            File img = new File(sd.getDir() + File.separator + att.getFilePath());
//			File img = new File("D:\\store" + File.separator + att.getFilePath());

            if(img == null || !img.exists()) {
                return null;
            }
            File pic = null;
            if(Constant.IS_TRUE_Str.equals(showOrigin)){
                String dirPath = img.getParent();
                String originFilePath = dirPath + File.separator + StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName();
                pic = new File(originFilePath);
            } else if(StuDevelopConstant.IS_MOBILE_STR.equals(showOrigin)){//当2时  显示手机端的图片(更小)
                String originFilePath = img.getParent() + File.separator + StuDevelopConstant.PIC_MOBILE_NAME+"."+att.getExtName();
                pic = new File(originFilePath);
            }else{
                pic = img;
            }
            if(pic == null || !pic.exists()) {
                pic = img;
            }
            if(pic != null && pic.exists()){
                response.getOutputStream().write(FileUtils.readFileToByteArray(pic));
				/*long time1=System.currentTimeMillis();
				BasicFileAttributes bfa=Files.readAttributes(pic.toPath(),BasicFileAttributes.class);
				System.out.println(bfa.creationTime().toString());
				System.out.println(bfa.lastModifiedTime().toString());
				long time2=System.currentTimeMillis();
				System.out.println(time2-time1);*/
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

//    public String saveFileAttach(){
//        try{
//            List<MultipartFile> files=StorageFileUtils.(new String[] {}, 0);//js已限制
//            Attachment attachment=null;
//            if(!CollectionUtils.isEmpty(files)){
//                for (UploadFile uploadFile : files) {
//                    attachment=new Attachment();
//                    attachment.setFilename(uploadFile.getFileName());
//                    attachment.setContentType(uploadFile.getContentType());
//                    attachment.setFileSize(uploadFile.getFileSize());
//                    attachment.setUnitId(getUnitId());
//                    attachment.setObjectId(leaveId);
//                    attachment.setObjectType(1);
//                    String fileExt = net.zdsoft.keel.util.FileUtils.getExtension(attachment.getFileName());
//                    if(converterFileTypeService.isVideo(fileExt)||converterFileTypeService.isDocument(fileExt)){
//                        attachment.setConStatus(BusinessTask.TASK_STATUS_NO_HAND);
//                    }
//                    if(converterFileTypeService.isPicture(fileExt)||converterFileTypeService.isAudio(fileExt)){
//                        attachment.setConStatus(BusinessTask.TASK_STATUS_SUCCESS);
//                    }
//                    attachmentService.saveAttachment(attachment, uploadFile);
//                }
//                promptMessageDto.setOperateSuccess(true);
//                promptMessageDto.setPromptMessage("文件保存成功");
//                promptMessageDto.setBusinessValue(attachment.getId()+"*"+attachment.getDownloadPath());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            promptMessageDto.setOperateSuccess(false);
//            promptMessageDto.setErrorMessage("文件保存出现问题");
//        }
//
//        return SUCCESS;
//    }
}
