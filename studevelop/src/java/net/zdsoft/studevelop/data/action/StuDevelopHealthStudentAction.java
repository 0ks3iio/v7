package net.zdsoft.studevelop.data.action;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.*;
import net.zdsoft.studevelop.data.service.*;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/4/18.
 */
@Controller

public class StuDevelopHealthStudentAction extends BaseAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private StuHealthyProjectService stuHealthyProjectService;
    @Autowired
    private StuHealthStudentService stuHealthStudentService;
    @Autowired
    private StuHealthStudentDetailService stuHealthStudentDetailService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private StudevelopTemplateService studevelopTemplateService;
    @Autowired
    private StudevelopTemplateOptionsService studevelopTemplateOptionsService;
    @Autowired
    private StudevelopTemplateItemService studevelopTemplateItemService;

    @RequestMapping("/index/page")
    public String healthStudentIndex(ModelMap map){
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"学年学期不存在");
        }
        map.put("acadyearList", acadyearList);
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()), Semester.class);
        if(semesterObj!=null){
            String acadyear=semesterObj.getAcadyear();
            String semester=semesterObj.getSemester()+"";
            map.put("acadyear", acadyear);
            map.put("semester", semester);
        }else{
            map.put("acadyear", "");
            map.put("semester", "");
        }

        return "/studevelop/record/stuHealthStudentIndex.ftl";
    }
    @ResponseBody
    @RequestMapping("/classIds")
    @ControllerInfo(value = "获得班级列表")
    public List<Clazz> acadyearUpdate(String acadyear, String semester, ModelMap map) {
        List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),acadyear), new TR<List<Clazz>>() {});
        return classList;
    }

    @ResponseBody
    @RequestMapping("/stuIds")
    @ControllerInfo(value = "获得学生列表")
    public List<Student> classUpdate(String classId, ModelMap map) {
        Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classId}),new TR<Map<String, List<Student>>>(){});
        List<Student> stuList = new ArrayList<Student>();
        if(!stuMap.isEmpty()){
            stuList = stuMap.get(classId);
        }
        return stuList;
    }

    public String studentEdit(@RequestParam String acadyear, @RequestParam String semester, @RequestParam String stuId, ModelMap map){

        Student stu = SUtils.dc(studentRemoteService.findOneById(stuId) , Student.class);
        Clazz  cla = SUtils.dc(classRemoteService.findOneById(stu.getClassId()),Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(cla.getGradeId()),Grade.class);
        int section =grade.getSection();
        String unitId=getLoginInfo().getUnitId();

        StudevelopHealthStudent healthStudent = null;
        List<StudevelopHealthStudent> healthStudentList = stuHealthStudentService.findListBy(new String[]{"acadyear","semester","studentId"},new String[]{acadyear,semester,stuId});
        if(CollectionUtils.isNotEmpty(healthStudentList)){
            healthStudent = healthStudentList.get(0);
        }
        StudevelopHealthProject project = new StudevelopHealthProject();
        project.setAcadyear(acadyear);
        project.setSemester(semester);
        project.setSchSection(String.valueOf(section));
        project.setSchoolId(unitId);

        List<StudevelopHealthProject> projectList = stuHealthyProjectService.getProjectByAcadyearSemesterSection(project);

        if(healthStudent == null){
            healthStudent = new StudevelopHealthStudent();
            healthStudent.setAcadyear(acadyear);
            healthStudent.setSemester(semester);
            healthStudent.setClassId(cla.getId());
            healthStudent.setSchoolId(unitId);
            healthStudent.setStudentId(stuId);


        }else{
            List<StudevelopHealthStudentDetail> detailList = stuHealthStudentDetailService.findListBy("healthStudentId" ,healthStudent.getId());
            Map<String,StudevelopHealthStudentDetail> detailMap = EntityUtils.getMap(detailList,"projectId");

            for(StudevelopHealthProject pro:projectList){
                StudevelopHealthStudentDetail detail = detailMap.get(pro.getId());
                if(detail!= null){
                    pro.setProjectValue(detail.getProjectValue());
                }

            }
        }
        map.put("healthStudent" ,healthStudent);
        Map<String,List<StudevelopHealthProject>> healthProjectMap =  EntityUtils.getListMap(projectList ,"projectType",null);
        map.put("healthProjectMap" ,healthProjectMap);


        List<McodeDetail> detailList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-SXXMLX") ,McodeDetail.class);

        map.put("detailList" ,detailList);
        return "/studevelop/record/StuHealthStudentEdit.ftl";
    }
    @RequestMapping("/edit")
    public String studentHealthHeartEdit(@RequestParam String acadyear, @RequestParam String semester, @RequestParam String stuId, ModelMap map){

        Student stu = SUtils.dc(studentRemoteService.findOneById(stuId) , Student.class);
        Clazz  cla = SUtils.dc(classRemoteService.findOneById(stu.getClassId()),Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(cla.getGradeId()),Grade.class);
        int section =grade.getSection();
        String unitId=getLoginInfo().getUnitId();
        List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,null,String.valueOf(section), StuDevelopConstant.TEMPLATE_CODE_HEALTH,unitId);
        StudevelopTemplate template = null;
        if(CollectionUtils.isNotEmpty(templateList)){
            template = templateList.get(0);
        }

        List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemService.findListBy("templateId",template.getId());

        List<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toList());
        List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.findListByIn("templateItemId",templagetItemIds.toArray());
        Map<String ,List<StudevelopTemplateOptions> >  optionsMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(templateOptionsList)){
           optionsMap = templateOptionsList.stream().collect(Collectors.groupingBy(e->e.getTemplateItemId()));
        }
        for (StudevelopTemplateItem item : templateItemList) {
            List<StudevelopTemplateOptions> list = optionsMap.get(item.getId());
            item.setTemplateOptions(list);
        }

        Map<String,List<StudevelopTemplateItem>> templateItemMap = templateItemList.stream().collect(Collectors.groupingBy(e->e.getObjectType()));

        map.put("templateItemMap", templateItemMap);
        List<McodeDetail> detailList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-SXXMLX") ,McodeDetail.class);

        map.put("detailList" ,detailList);
        map.put("code",StuDevelopConstant.TEMPLATE_CODE_HEALTH);
        return "/studevelop/record/stuHealthHeartEdit.ftl";
    }
    @RequestMapping("/save")
    @ResponseBody
    public String saveHealthStudent( StudevelopHealthStudent healthStudent  ){
        try{
            stuHealthStudentService.saveHealthStudent(healthStudent);
        }catch (Exception e){
            e.printStackTrace();
            return error("保存失败！");
        }
        return success("保存成功！");
    }
}
