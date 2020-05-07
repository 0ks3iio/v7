package net.zdsoft.studevelop.data.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dto.StuDevDocClassStuStatisticDto;
import net.zdsoft.studevelop.data.dto.StuDevelopDocGradeStatisDto;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItem;
import net.zdsoft.studevelop.data.entity.StudevelopActivity;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;
import net.zdsoft.studevelop.data.entity.StudevelopHonor;
import net.zdsoft.studevelop.data.entity.StudevelopMonthPerformance;
import net.zdsoft.studevelop.data.service.StudevelopActivityService;
import net.zdsoft.studevelop.data.service.StudevelopHonorService;
import net.zdsoft.studevelop.data.service.StudevelopMonthPerformanceService;
import net.zdsoft.studevelop.data.service.StudevelopPerformItemService;
import net.zdsoft.studevelop.mobile.entity.StuAttachmentDto;
import net.zdsoft.studevelop.mobile.entity.StuFamilyWishes;
import net.zdsoft.studevelop.mobile.entity.StuHonor;
import net.zdsoft.studevelop.mobile.entity.StuOutside;
import net.zdsoft.studevelop.mobile.service.StuFamilyWishesService;
import net.zdsoft.studevelop.mobile.service.StuHonorService;
import net.zdsoft.studevelop.mobile.service.StuOutsideService;

/**
 * Created by luf on 2018/10/16.
 */
@Controller
@RequestMapping("/studevelop/devdocStatistic")
public class StuDevelopDocStatisticAction extends CommonAuthAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudevelopPerformItemService studevelopPerformItemService;
    @Autowired
    private StudevelopMonthPerformanceService studevelopMonthPerformanceService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private StudevelopActivityService studevelopActivityService;
    @Autowired
    private StuFamilyWishesService stuFamilyWishesService;

    @Autowired
    private StuOutsideService stuOutsideService;
    @Autowired
    private StuHonorService stuHonorService;
    @Autowired
    private StudevelopHonorService studevelopHonorService;
    @RequestMapping("/index/page")
    public String main(String acadyear , String semester ,ModelMap modelMap){
        String unitId =  getLoginInfo().getUnitId();
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(modelMap , "学年学期不存在");
        }
        modelMap.put("acadyearList",acadyearList);
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId),Semester.class);
        if(StringUtils.isEmpty(acadyear) && StringUtils.isEmpty(semester)){
            if(semesterObj != null){
                acadyear = semesterObj.getAcadyear();
                semester = semesterObj.getSemester() + "";
            }else{
                acadyear = acadyearList.get(0);
                semester = 1+"";
            }
        }
        modelMap.put("acadyear" ,acadyear);
        modelMap.put("semester" , semester);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(unitId , acadyear),Grade.class);
        //去掉已毕业的
        if(CollectionUtils.isNotEmpty(gradeList)){
            Iterator<Grade> gradeIterator = gradeList.iterator();
            while (gradeIterator.hasNext()){
                Grade grade = gradeIterator.next();
                if(grade.getIsGraduate() == 1){
                    gradeIterator.remove();
                }
            }
        }
        modelMap.put("gradeList",gradeList);
        String gradeId="";
        String gradeName="";
        if(CollectionUtils.isNotEmpty(gradeList)){
            gradeId = gradeList.get(0).getId();
            gradeName = gradeList.get(0).getGradeName();
        }
        List<Clazz> classList = null;
        if(isAdmin(StuDevelopConstant.PERMISSION_TYPE_GROWTH)){
            modelMap.put("isAdmin",true);
            if(StringUtils.isEmpty(gradeId)){
                classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear), new TR<List<Clazz>>(){});
            }else{
                classList = SUtils.dt(classRemoteService.findByInGradeIds(new String[]{gradeId}), new TR<List<Clazz>>(){});
            }
            if(CollectionUtils.isNotEmpty(classList)){
                for (Clazz cla : classList) {
                    cla.setClassName(gradeName + cla.getClassName());

                }
            }
        }else {
            classList = headTeacherClass(acadyear);
            if(CollectionUtils.isEmpty(classList)){
                return promptFlt(modelMap , "不是（副）班主任和管理员不能维护和查询成长手册统计内容");
            }else{
                for (Clazz cla : classList) {
                    cla.setClassName(cla.getClassNameDynamic());

                }
            }
        }


        modelMap.put("classList",classList);
        return "/studevelop/devdocStatistic/stuDevelopDocStatisticMain.ftl";
    }
    @ResponseBody
    @RequestMapping("/changeClaListByGradeId")
    public List<Clazz> changeClassByGradeId(String acadyear ,String gradeId){
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findByGradeId(getLoginInfo().getUnitId() ,gradeId ,null) , Clazz.class);
        if(CollectionUtils.isEmpty(clazzList)){
            return new ArrayList<>();
        }
        return clazzList;
    }
    @RequestMapping("/gradeStatistic/list")
    public String gradeStatisticList(String acadyear,String semester ,String gradeId ,String gradeName, String classId ,ModelMap map){

        try{


        String unitId = getLoginInfo().getUnitId();

        if(StringUtils.isEmpty(gradeId)){
            Clazz cla = Clazz.dc(classRemoteService.findOneById(classId));
            gradeId = cla.getGradeId();
        }
        List<Clazz> classList = SUtils.dt(classRemoteService.findByGradeId(unitId , gradeId , null) , Clazz.class);
        if(CollectionUtils.isEmpty(classList)){
            return "/studevelop/devdocStatistic/gradeDevelopDocStatisticList.ftl";
        }
        Set<String> classIdSet = classList.stream().map(Clazz::getId).collect(Collectors.toSet());
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId) , Grade.class);
        Semester semesterObj = null;
        if(StringUtils.isNotEmpty(acadyear) && StringUtils.isNotEmpty(semester)){
            semesterObj = SUtils.dc(semesterRemoteService.findByAcadyearAndSemester(acadyear,Integer.valueOf(semester),unitId),Semester.class);
        }else{
            semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId),Semester.class);
        }

        int start = getMonth(semesterObj.getSemesterBegin());
        Date startDate = semesterObj.getSemesterBegin();
        Date endDate = semesterObj.getSemesterEnd();
        List<String> months = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        while(startDate.before(endDate) ){
            String yearMonth = format.format(startDate);
            months.add(yearMonth.substring(5));
            start++;
            String startStr = String.valueOf(start);
            String str = yearMonth.substring(0,4)+ "-" + (startStr.length()>1?startStr:"0"+startStr) +"-01";
            startDate = DateUtils.string2Date(str);
        }

        String yearMonth = format.format(endDate);
        if(!months.contains(yearMonth.substring(5))) months.add(yearMonth.substring(5));

        map.put("months" ,months);
        String curAcadyear = semesterObj.getAcadyear();

        int length = getAcadYearLast(curAcadyear) - getAcadYearLast(acadyear);
        String gradeCode = grade.getGradeCode();
        String gradeCodeLast = gradeCode.substring(1);

        int size = Integer.valueOf(gradeCodeLast) - length;
        String newGradeCode = gradeCode.substring(0,1)+size;

        List<StuDevelopPerformItem> itemList = studevelopPerformItemService.getStuDevelopPerformItemsByUnitIdAndGrade(unitId,newGradeCode);
        List<StudevelopMonthPerformance> performanceList = studevelopMonthPerformanceService.getMonthPermanceListByStuIds(unitId,acadyear,Integer.valueOf(semester),null,null,null);
        Map<String,List<StudevelopMonthPerformance>> monthPerformanceMap = new HashMap<>();

        for (StudevelopMonthPerformance performance : performanceList) {
            String key = performance.getStudentId()+"_"+performance.getPerformMonth();
            List<StudevelopMonthPerformance> list = monthPerformanceMap.get(key);
            if(list == null){
                list = new ArrayList<>();
                monthPerformanceMap.put(key , list);
            }
            list.add(performance);
        }

        List<Student> studentList = Student.dt(studentRemoteService.findPartStudByGradeId(unitId,gradeId,null,null));
        Map<String,Student> studentMap = EntityUtils.getMap(studentList, Student::getId);
        Map<String,List<Student>> claStuMap = studentList.stream().collect(Collectors.groupingBy(student -> student.getClassId()));
//        System.out.println("1111111111111111111111*************tttttttttttttttttt");
        Map<String,Integer> peopleNumMap = new HashMap<>();
        int itemSize = itemList.size();
        for(Map.Entry<String,List<StudevelopMonthPerformance>> entry : monthPerformanceMap.entrySet()){
            String entryKey = entry.getKey();
            String[] arr = entryKey.split("_");

            Student stu = studentMap.get(arr[0]);
            List<StudevelopMonthPerformance> list = entry.getValue();

            if(  stu != null  && list.size() >= itemSize){
                String key = stu.getClassId() + "_" + arr[1];
                Integer count = peopleNumMap.get(key)== null? 0:peopleNumMap.get(key) ;
                count++;
                peopleNumMap.put(key , count);

            }
        }
        map.put("peopleNumMap" ,peopleNumMap);
        //班级活动
        Map<String,String> claActivityNumMap = getActivity(acadyear,semester,StuDevelopConstant.ACTIVITY_TYPE_CLASS_ACTIVITY);
        //主题活动
        Map<String,String> themeActivityNumMap = getActivity(acadyear,semester,StuDevelopConstant.ACTIVITY_TYPE_THEME_ACTIVITY);

        List<StudevelopHonor> classHonorList  = studevelopHonorService.getStudevelopHonorByClassIds(acadyear , Integer.valueOf(semester) , classIdSet.toArray(new String[0]));
        Map<String,String> stuHonorMap = new HashMap<>();
        for (StudevelopHonor honor : classHonorList) {
            List<StudevelopAttachment> atts = honor.getAtts();
            if(CollectionUtils.isNotEmpty(atts)){
                stuHonorMap.put(honor.getClassId() , atts.size() + "张");
            }else{
                stuHonorMap.put(honor.getClassId() ,  "0张");
            }
        }
        Map<String,Integer> parentJoinNumMap = new HashMap<>();
        Map<String,Integer> parentSubMitPicNumMap = new HashMap<>();
//        System.out.println("ddddddddddddd*************tttttttttttttttttt");

        claDocStatistic(unitId ,acadyear,semester , gradeId , null, parentJoinNumMap,parentSubMitPicNumMap);
        List<StuDevelopDocGradeStatisDto> gradeStatisDtos = new ArrayList<>();
        for (Clazz cla : classList) {
            StuDevelopDocGradeStatisDto dto = new StuDevelopDocGradeStatisDto();
            String id = cla.getId();
            dto.setClassId(id);
            dto.setClassName( gradeName + cla.getClassName());
            dto.setClassActivity(claActivityNumMap.get(id));
            dto.setThemeActivity(themeActivityNumMap.get(id));
            dto.setClassHonor(stuHonorMap.get(id));
            int parentJoinNum = parentJoinNumMap.get(id) == null? 0:parentJoinNumMap.get(id) ;
            int parentSubPicNum = parentSubMitPicNumMap.get(id)== null? 0:parentSubMitPicNumMap.get(id);
            List<Student> stuList = claStuMap.get(id);
            int claStuSize = 0;
            if(CollectionUtils.isNotEmpty(stuList)){
                claStuSize = stuList.size();
            }
            dto.setParentJoin(parentJoinNum + "/" + claStuSize);
            dto.setParentSubmitPic(parentSubPicNum+"");
            if(parentJoinNum != 0) {
                dto.setParentSubmitPicAve(parentSubPicNum / parentJoinNum + "");
            }
            gradeStatisDtos.add(dto);
        }
        map.put("gradeStatisDtos" , gradeStatisDtos);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "/studevelop/devdocStatistic/gradeDevelopDocStatisticList.ftl";
    }
    private int getMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }
    private Map<String,String> getActivity(String acadyear,String semester ,String actType){
        List<StudevelopActivity> acts = studevelopActivityService.findActBySemeRangeType(acadyear,Integer.valueOf(semester),actType,StuDevelopConstant.RANGETYPE_CLASS);
        Map<String,List<StudevelopActivity>> claActivityMap = acts.stream().collect(Collectors.groupingBy(StudevelopActivity::getRangeId));
        Map<String,String> claActivityNumMap = new HashMap<>();
        for(Map.Entry<String,List<StudevelopActivity>> entry : claActivityMap.entrySet()){
            String key = entry.getKey();
            List<StudevelopActivity> list = entry.getValue();
            StringBuffer buffer = new StringBuffer();
            if(CollectionUtils.isNotEmpty(list)){
                buffer.append(list.size() + "个");
                int num=0;
                for (StudevelopActivity activity : list) {
                    List<StudevelopAttachment> atts = activity.getAtts();
                    if(CollectionUtils.isNotEmpty(atts)){
                        num = num + atts.size();
                    }
                }
                buffer.append("(" + num + "张)");
            }

            claActivityNumMap.put(key , buffer.toString());
        }
        return  claActivityNumMap;
    }
    private int getAcadYearLast(String acadyear){
        String[] acadyears = acadyear.split("-");
        String acad = acadyears[1];
        return Integer.valueOf(acad);
    }
    @RequestMapping("/claStatisticList/list")
    public String getClaStatisticList(String acadyear,String semester ,String gradeId ,String classId , ModelMap map ){
        Map<String,Integer> parentJoinNumMap = new HashMap<>();
        Map<String,Integer> parentSubMitPicNumMap = new HashMap<>();
        String unitId = getLoginInfo().getUnitId();
        Map<String, List<StuDevDocClassStuStatisticDto>> claStuStatisDtoMap = claDocStatistic(unitId ,acadyear,semester,null,classId,parentJoinNumMap ,parentSubMitPicNumMap);
        List<StuDevDocClassStuStatisticDto> claStatisticDtos = claStuStatisDtoMap.get(classId);
        Collections.sort(claStatisticDtos, new Comparator<StuDevDocClassStuStatisticDto>() {
            @Override
            public int compare(StuDevDocClassStuStatisticDto o1, StuDevDocClassStuStatisticDto o2) {
                String pic1 = o1.getParentSubmitPic();
                String pic2 = o2.getParentSubmitPic();
                int num1 = NumberUtils.toInt(pic1);
                int num2 = NumberUtils.toInt(pic2);

                return num1 - num2;
            }
        });
        map.put("claStatisticDtos",claStatisticDtos);
        return "/studevelop/devdocStatistic/classDevelopDocStatisticList.ftl";
    }
    
    public Map<String, List<StuDevDocClassStuStatisticDto>> claDocStatistic(String unitId ,String acadyear,String semester ,String gradeId ,String claId ,Map<String,Integer> parentJoinNumMap , Map<String,Integer> parentSubMitPicNumMap){
        List<Student> studentList = null;
        if(claId == null){
             studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId ,gradeId,null,null), Student.class);
        }else{
            studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId ,null,new String[]{claId},null), Student.class);
        }
        if(CollectionUtils.isEmpty(studentList)){
            return new HashMap<>();
        }
        Map<String,Student> studentMap = EntityUtils.getMap(studentList, Student::getId);
        Set<String> studentIdSet = studentMap.keySet();
        String[] ids = studentIdSet.toArray(new String[0]);
//        System.out.println("333333333333333333*************tttttttttttttttttt");
        List<StuFamilyWishes> familyWishes = stuFamilyWishesService.getStuFamily(acadyear,semester,ids);
        Map<String , StuFamilyWishes> familyWishesMap = EntityUtils.getMap(familyWishes, StuFamilyWishes::getStudentId);
        List<StuOutside> stuOutsideList = stuOutsideService.findStuOutSideList(acadyear,semester,ids);
        Map<String,List<StuOutside>> stuOutSideMap = new HashMap<>();
        for (StuOutside outside : stuOutsideList) {
            String key = outside.getStudentId() + "_" + outside.getType();
            List<StuOutside> list = stuOutSideMap.get(key);
            if(list == null){
                list = new ArrayList<>();
                stuOutSideMap.put(key , list);
            }
            list.add(outside);
        }
//        Map<String,StuOutside> stuOutSideMap = stuOutsideList.stream().collect(Collectors.toMap(s-> s.getStudentId() + "_" + s.getType(),Function.identity()));

        List<StuHonor> stuHonorList = stuHonorService.getStuHonorList(acadyear,semester,ids);
        Map<String,StuHonor> stuHonorMap = EntityUtils.getMap(stuHonorList, StuHonor::getStudentId);
        Map<String, List<StuDevDocClassStuStatisticDto>> claStuStatisDtoMap = new HashMap<>();
//        System.out.println("444444444444*************tttttttttttttttttt");

        for (Student stu : studentList) {
            int num=0;
            boolean flag = false;
            String classId = stu.getClassId();
            List<StuDevDocClassStuStatisticDto> dtoList = claStuStatisDtoMap.get(classId);
            if(dtoList == null){
                dtoList = new ArrayList<>();
                claStuStatisDtoMap.put(classId, dtoList);
            }
            String stuId = stu.getId();
            StuDevDocClassStuStatisticDto dto = new StuDevDocClassStuStatisticDto();
            dto.setStudentName(stu.getStudentName());
            StuFamilyWishes wish = familyWishesMap.get(stu.getId());
            if(wish != null){
                dto.setHappyHome("完成");
                num++;
                flag = true;
            }
            String outsideKey = stuId + "_1";
            List<StuOutside> outsideList = stuOutSideMap.get(outsideKey);
            if(CollectionUtils.isNotEmpty(outsideList)){
                int m=0;
                for (StuOutside outside : outsideList) {
                    num += outside.getImageCount();
                    m += outside.getImageCount();
                    flag = true;
                }
                dto.setOutSideSchool(m + "");
            }

            String holidayKey = stuId + "_2";
            List<StuOutside> holidayList = stuOutSideMap.get(holidayKey);
            if(CollectionUtils.isNotEmpty(holidayList)){
                int m=0;
                for (StuOutside holiday : holidayList) {

                    num +=holiday.getImageCount();
                    m += holiday.getImageCount();
                    flag = true;
                }
                dto.setHoliday(m +"");
            }
            StuHonor honor = stuHonorMap.get(stuId);
            if(honor != null){
                List<StuAttachmentDto> atts = honor.getImages();
                if(CollectionUtils.isNotEmpty(atts)){
                    num += atts.size();
                    dto.setHonor(atts.size() +"");
                }
                flag = true;

            }
            Integer join = parentJoinNumMap.get(stu.getClassId());
            if(join == null){
                join = 0;
            }
            join++;
            if(flag){
                parentJoinNumMap.put(stu.getClassId(), join);
            }
            Integer picNum = parentSubMitPicNumMap.get(stu.getClassId()) == null ? 0:parentSubMitPicNumMap.get(stu.getClassId());
            parentSubMitPicNumMap.put(stu.getClassId() , picNum + num);
            dto.setParentSubmitPic(String.valueOf(num));
            dtoList.add(dto);
        }
        return claStuStatisDtoMap;
    }
}
