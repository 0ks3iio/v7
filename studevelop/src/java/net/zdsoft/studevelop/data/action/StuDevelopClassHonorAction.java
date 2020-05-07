package net.zdsoft.studevelop.data.action;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;
import net.zdsoft.studevelop.data.entity.StudevelopHonor;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
import net.zdsoft.studevelop.data.service.StudevelopHonorService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/studevelop")
public class StuDevelopClassHonorAction extends CommonAuthAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private StudevelopHonorService studevelopHonorService;

    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private StudevelopAttachmentService studevelopAttachmentService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @RequestMapping("/classHonor/index/page")
    public String main(ModelMap modelMap){

        String unitId =  getUnitId();
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(modelMap , "学年学期不存在");
        }
        modelMap.put("acadyearList",acadyearList);
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId),Semester.class);
        String acadyear = "";
        String semester = "";
        if(semesterObj != null){
            acadyear = semesterObj.getAcadyear();
            semester = semesterObj.getSemester() + "";
        }else{
            acadyear = acadyearList.get(0);
            semester = 1+"";
        }
        modelMap.put("acadyear" ,acadyear);
        modelMap.put("semester" , semester);

        List<Clazz> classList = null;
        if(isAdmin(StuDevelopConstant.PERMISSION_TYPE_GROWTH)){
            classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear), new TR<List<Clazz>>(){});
        }else {
            classList = headTeacherClass(acadyear);
        }
        if(CollectionUtils.isEmpty(classList)){
            return promptFlt(modelMap , "不是（副）班主任和管理员不能维护和查询班级荣誉内容");
        }

        modelMap.put("classList",classList);

        return "/studevelop/stuGrow/stuDevelopClassHonorMain.ftl";
    }
    private String getUnitId(){
        LoginInfo login = getLoginInfo();
        return login.getUnitId();
    }
    @ResponseBody
    @RequestMapping("/classHonor/classlist/page")
    public String classList(@RequestParam String acadyear){
        String unitId =getUnitId();
//        List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear), new TR<List<Clazz>>(){});
        return classRemoteService.findByIdCurAcadyear(unitId, acadyear);
    }
    @RequestMapping("/classHonor/show/page")
    public String picShow(StudevelopHonor studevelopHonor , ModelMap map){
        String unitId =  getUnitId();
        List<StudevelopHonor> classHonorList = studevelopHonorService.getStudevelopHonorByAcadyearAndSemester(studevelopHonor.getAcadyear(),studevelopHonor.getSemester(),studevelopHonor.getClassId());
        StudevelopHonor classHonor = null;
        if(CollectionUtils.isNotEmpty(classHonorList)){
            classHonor = classHonorList.get(0);
        }
        if(classHonor == null){
            classHonor = studevelopHonor;
            classHonor.setId(UuidUtils.generateUuid());
            classHonor.setUnitId(unitId);
            classHonor.setCreationTime(new Date());
            studevelopHonorService.save(classHonor);
        }else{
            List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(classHonor.getId(), "class_honor");
            map.put("actDetails", atts);
        }

        map.put("id",classHonor.getId());
        map.put("actType","class_honor");
        return "/studevelop/stuGrow/stuDevelopClassHonorShow.ftl";
    }
}
