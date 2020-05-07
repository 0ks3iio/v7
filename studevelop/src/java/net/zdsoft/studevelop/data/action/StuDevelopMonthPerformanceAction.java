package net.zdsoft.studevelop.data.action;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItem;
import net.zdsoft.studevelop.data.entity.StudevelopMonthPerformance;
import net.zdsoft.studevelop.data.service.StudevelopMonthPerformanceService;
import net.zdsoft.studevelop.data.service.StudevelopPerformItemService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/studevelop")
public class  StuDevelopMonthPerformanceAction extends CommonAuthAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;

    @Autowired
    private StudevelopPerformItemService studevelopPerformItemService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private StudevelopMonthPerformanceService studevelopMonthPerformanceService;
    @RequestMapping("/performance/index/page")
    public String main( String acadyear , String semester , String classId , String performMonth , ModelMap modelMap){
        LoginInfo login = getLoginInfo();
        String unitId = login.getUnitId();


        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(modelMap , "学年学期不存在");
        }
        modelMap.put("acadyearList",acadyearList);
        Semester semesterObj = null;
        if(StringUtils.isEmpty(acadyear)){
            semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId),Semester.class);

        }else{
            semesterObj = SUtils.dc(semesterRemoteService.findByAcadYearAndSemester(acadyear,Integer.valueOf(semester)),Semester.class);
        }
        if(semesterObj != null){
            acadyear = semesterObj.getAcadyear();
            semester = semesterObj.getSemester() + "";

        }else{
            acadyear = acadyearList.get(0);
            semester = 1+"";
            semesterObj = SUtils.dc(semesterRemoteService.findByAcadYearAndSemester(acadyear,1),Semester.class);
        }
        if(semesterObj == null){
            return errorFtl(modelMap , "学年学期不存在");
        }

        List<Clazz> classList = null;
        if(isAdmin(StuDevelopConstant.PERMISSION_TYPE_GROWTH)){
            classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear), new TR<List<Clazz>>(){});
        }else {
            classList = headTeacherClass(acadyear);
        }
        if(CollectionUtils.isEmpty(classList)){
            return promptFlt(modelMap , "不是（副）班主任和成长手册管理员不能维护和查询每月在校表现内容");
        }
//        List<Clazz> classList = classService.findByGradeIdIn(gradeStr.toArray(new String[0]));

        modelMap.put("classList",classList);

        if(StringUtils.isEmpty(performMonth)){
            performMonth = String.valueOf(getMonth(new Date()));
        }
        int start = getMonth(semesterObj.getSemesterBegin());

        Date startDate = semesterObj.getSemesterBegin();
        Date endDate = semesterObj.getSemesterEnd();
        List<String> months = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM");
        while(startDate.before(endDate) ){
            String yearMonth = format.format(startDate);
            months.add(yearMonth);
            start++;
            String startStr = String.valueOf(start);
            String str = yearMonth.substring(0,4)+ "-" + (startStr.length()>1?startStr:"0"+startStr) +"-01";
            startDate = DateUtils.string2Date(str);

        }
        String yearMonth = format.format(endDate);
        if(!months.contains(yearMonth)) months.add(yearMonth);
        modelMap.put("months" , months);
        modelMap.put("acadyear" ,acadyear);
        modelMap.put("semester" , semester);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId) , Grade.class);
        List<String> gradeStr = new ArrayList<String>();
        for(Grade grade: gradeList){
            gradeStr.add(grade.getId());
        }


        modelMap.put("performMonth",performMonth);
        modelMap.put("classId",classId);
        return "/studevelop/stuGrow/stuDevelopMonthPerformanceMain.ftl";
    }
    private int getMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }
    @ResponseBody
    @RequestMapping("/performance/classlist/page")
    public String classList(@RequestParam String acadyear){
        String unitId =getUnitId();
//        List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear), new TR<List<Clazz>>(){});
        return classRemoteService.findByIdCurAcadyear(unitId, acadyear);
    }
    private int getAcadYearLast(String acadyear){
        String[] acadyears = acadyear.split("-");
        String acad = acadyears[1];
        return Integer.valueOf(acad);
    }
    @RequestMapping("/performance/list/page")
    public String list(StudevelopMonthPerformance studevelopMonthPerformance  , HttpServletRequest request, HttpServletResponse response, ModelMap map){
        String classId = studevelopMonthPerformance.getClassId();
        if(StringUtils.isEmpty(classId)){
            return "/studevelop/stuGrow/stuDevelopMonthPerformanceList.ftl";
        }
        Clazz cla = Clazz.dc(classRemoteService.findOneById(classId));
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(cla.getGradeId()) , Grade.class);
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()),Semester.class);
        if(semesterObj == null){
            return "/studevelop/stuGrow/stuDevelopMonthPerformanceList.ftl";
        }
        String curAcadyear = semesterObj.getAcadyear();
        String acadyear = studevelopMonthPerformance.getAcadyear();

        int length = getAcadYearLast(curAcadyear) - getAcadYearLast(acadyear);
        String gradeCode = grade.getGradeCode();
        String gradeCodeLast = gradeCode.substring(1);

        int size = Integer.valueOf(gradeCodeLast) - length;
        String newGradeCode = gradeCode.substring(0,1)+size;

//        Pagination page = createPagination();
        String unitId = getUnitId();
        List<StuDevelopPerformItem> itemList = studevelopPerformItemService.getStuDevelopPerformItemsByUnitIdAndGrade(unitId,newGradeCode);

        map.put("itemList",itemList);

        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classId) ,Student.class);
        List<String> stuIds = new ArrayList<String>();
        for(Student stu : studentList){
            stuIds.add(stu.getId());
        }
        List<StudevelopMonthPerformance> performanceList = studevelopMonthPerformanceService.getMonthPermanceListByStuIds(unitId,studevelopMonthPerformance.getAcadyear(),studevelopMonthPerformance.getSemester(),String.valueOf(studevelopMonthPerformance.getPerformMonth()),null,stuIds.toArray(new String[0]));
        Map<String,String> performanceMap = new HashMap<String, String>();
        for(StudevelopMonthPerformance performance : performanceList){
            performanceMap.put(performance.getStudentId() + "_"+performance.getItemId(),performance.getResultId());
        }
        map.put("studentList" ,studentList);
        map.put("performanceMap",performanceMap);
//        sendPagination(request,map,page);
        return "/studevelop/stuGrow/stuDevelopMonthPerformanceList.ftl";
//        return "/studevelop/stuGrow/list.ftl";
    }

    @ResponseBody
    @RequestMapping(value="/mouthPerformance/save/page" )
    public String saveItemResult(StudevelopMonthPerformance studevelopMonthPerformance ){
        try{

            studevelopMonthPerformanceService.saveMonthPermance(studevelopMonthPerformance ,studevelopMonthPerformance.getClassId(),getUnitId());
        }catch (Exception e){
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();

    }
    @ResponseBody
    @RequestMapping(value="/mouthPerformance/copyLastMouth/page" )
    public String copyLastMouth(StudevelopMonthPerformance studevelopMonthPerformance ){
        String result = returnSuccess();
        try{
           result =  studevelopMonthPerformanceService.saveCopyLastMouth(studevelopMonthPerformance,getUnitId());
        }catch (Exception e){
            e.printStackTrace();
            return returnError();
        }
        return result ;
    }
    public String getUnitId(){
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        return unitId;
    }
}
