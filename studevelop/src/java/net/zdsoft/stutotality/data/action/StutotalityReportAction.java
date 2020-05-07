package net.zdsoft.stutotality.data.action;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.stutotality.data.dto.ClassDto;
import net.zdsoft.stutotality.data.entity.*;
import net.zdsoft.stutotality.data.service.*;
import net.zdsoft.stutotality.data.util.StutotalityConstant;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.zdsoft.stutotality.data.constant.ResultType.*;

@Controller
@RequestMapping("/stutotality/report")
public class StutotalityReportAction extends BaseAction {

    @Autowired
    private StutotalityStuResultService stutotalityStuResultService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private CustomRoleRemoteService customRoleRemoteService;
    @Autowired
    private StutotalityItemService stutotalityItemService;
    @Autowired
    private StutotalityItemOptionService stutotalityItemOptionService;
    @Autowired
    private StutotalityHealthService stutotalityHealthService;
    @Autowired
    private StutotalityStuRewardService stutotalityStuRewardService;
    @Autowired
    private StutotalityStuFinalService stutotalityStuFinalService;
    @Autowired
    private StutotalityHealthOptionService stutotalityHealthOptionService;
    @Autowired
    private StutotalitySchoolNoticeService stutotalitySchoolNoticeService;
    @Autowired
    private StutotalityTypeService stutotalityTypeService;
    @Autowired
    private StutotalityScaleService stutotalityScaleService;
    @Autowired
    private StutotalityGoodStatService stutotalityGoodStatService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private StorageDirRemoteService storageDirRemoteService;

    @RequestMapping("/personIndex/page")
    @ControllerInfo("进入个人报告单index")
    public String getHealIndex(ModelMap map){
        LoginInfo loginInfo=getLoginInfo();
        findClazzListBy(loginInfo.getUnitId(), loginInfo.getUserId(), loginInfo.getOwnerId(),map);
        return "/stutotality/report/stutotalityReportStuIndex.ftl";
    }

    @RequestMapping("/common/getStuTotalityReportPdf")
    @ControllerInfo("进入个人报告单list")
    public String getStuTotalityReportPdf(String acadyear,String semester ,String studentId,ModelMap map){
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
        if(student==null) student=new Student();
        String schoolId = student.getSchoolId();
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()),Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()),Grade.class);
        School school = SUtils.dc(schoolRemoteService.findOneById(schoolId),School.class);
        Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
        if(StringUtils.isBlank(acadyear)){
            acadyear = se.getAcadyear();
        }
        if(StringUtils.isBlank(semester)){
            semester = se.getSemester().toString();
        }
        //个人信息
        map.put("student",student);
//        map.put("stuSex",student.getSex()==1?"男":"女");//1男  2女
        map.put("className",clazz.getClassNameDynamic());
        map.put("schoolName",school.getSchoolName());
        map.put("acadyear",acadyear);
        map.put("semester",semester);
        map.put("acadyearList",acadyearList);
        List<StutotalityItem> noStatItemList = stutotalityItemService.getItemListByParams(schoolId,acadyear,semester,clazz.getGradeId(),0);
        List<StutotalityItem> hasStatItemList = stutotalityItemService.getItemListByParams(schoolId,acadyear,semester,clazz.getGradeId(),1);
        Set<String> itemIds=EntityUtils.getSet(noStatItemList,StutotalityItem::getId);
        itemIds.addAll(EntityUtils.getSet(hasStatItemList,StutotalityItem::getId));
        //通过项目获取内容
        List<StutotalityItemOption> optionList=stutotalityItemOptionService.findByItemIds(itemIds.toArray(new String[0]));
        Map<String,List<StutotalityItemOption>> itemIdOptionListMap=null;
        if(CollectionUtils.isNotEmpty(optionList)){
            itemIdOptionListMap=optionList.stream().collect(Collectors.groupingBy(StutotalityItemOption::getItemId));
        }else{
            itemIdOptionListMap=new HashMap<>();
        }
        //获取该学生的班级所对应的科目
        List<ClassTeaching> courseList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByClassIds(acadyear, semester, new String[]{student.getClassId()}),new TR<List<ClassTeaching>>(){});
        Set<String> subjectIds = EntityUtils.getSet(courseList,ClassTeaching::getSubjectId);
        //3种项目情况的结果成绩
        List<StutotalityStuResult> resultList=stutotalityStuResultService.findListByParms
                (schoolId,acadyear,semester,studentId,itemIds.toArray(new String[0]));
        Map<String,Float> optionIdScoreMap=new HashMap<>();
        Map<String,Float> itemIdScoreMap=new HashMap<>();
        if(CollectionUtils.isNotEmpty(resultList)){
            for(StutotalityStuResult result:resultList){
                if(StringUtils.isNotBlank(result.getOptionId())){//内容成绩
                    optionIdScoreMap.put(result.getOptionId(),result.getResult());
                }else if("2".equals(result.getType())){//期末
                    itemIdScoreMap.put(result.getItemHealthId(),result.getResult());
                }
            }
        }
        Map<String,StutotalityScale> typeScaleMap=EntityUtils.getMap(stutotalityScaleService.findByUnitIdAndGradeId(schoolId,acadyear,semester,clazz.getGradeId()),StutotalityScale::getType);
        //处理学科分数以及分三种情况
        setItemResult(hasStatItemList,itemIdOptionListMap,optionIdScoreMap,itemIdScoreMap,subjectIds,typeScaleMap,map);
        map.put("noStatItemList",noStatItemList);
        map.put("hasStatItemList",hasStatItemList);
        map.put("itemIdOptionListMap",itemIdOptionListMap);
        map.put("optionIdScoreMap",optionIdScoreMap);
        map.put("itemIdScoreMap",itemIdScoreMap);
        //兴趣特长
        List<StutotalityItem> itemHobbyList = stutotalityItemService.findByUnitIdAndSubjectType(schoolId,new String[]{"9"});
        String itemHobbyName ="";
        if(CollectionUtils.isNotEmpty(itemHobbyList)) {
            int i=0;
            for (StutotalityItem stutotalityItem : itemHobbyList) {
                i=i+1;
                if(i==itemHobbyList.size()){
                    itemHobbyName = itemHobbyName+stutotalityItem.getItemName();
                }else {
                    itemHobbyName = itemHobbyName + stutotalityItem.getItemName() + ",";
                }
            }
        }
        map.put("itemHobbyName",itemHobbyName);
        //出勤记录 和 班主任寄语
        List<StutotalityStuFinal> stutotalityStuFinals = stutotalityStuFinalService.findByAcadyearAndSemesterAndUnitIdAndStudentIds
                (acadyear,semester,schoolId,new String[]{studentId});
        if(CollectionUtils.isNotEmpty(stutotalityStuFinals)){
            StutotalityStuFinal stutotalityStuFinal=stutotalityStuFinals.get(0);
            map.put("sickLeave",stutotalityStuFinal.getSickLeave());
            map.put("casualLeave",stutotalityStuFinal.getCasualLeave());
            map.put("otherLeave",stutotalityStuFinal.getOtherLeave());
            map.put("teacherContent",stutotalityStuFinal.getTeacherContent());

        }
        //身体素质发展水平
        List<StutotalityHealthOption> stutotalityHealthOptions = stutotalityHealthOptionService.findByUnitIdAndGradeCode(schoolId,grade.getGradeCode());
        if(CollectionUtils.isNotEmpty(stutotalityHealthOptions)){
            //获取身心项目ids
            Set<String> healthIds = stutotalityHealthOptions.stream().map(StutotalityHealthOption::getHealthId).collect(Collectors.toSet());
            List<StutotalityHealth> healthList = stutotalityHealthService.findListByIds(healthIds.toArray(new String[0]));
//            List<StutotalityHealth> lastList =new ArrayList<>();
            //一个身心id对应一个达标情况
            Map<String,StutotalityHealthOption> healthIdOptionMap=EntityUtils.getMap(stutotalityHealthOptions,StutotalityHealthOption::getHealthId);
            String healthId=null;
            if(CollectionUtils.isNotEmpty(healthList)){
                for(StutotalityHealth health:healthList){
                    if("视力".equals(health.getHealthName())){//视力定制
                        healthId=health.getId();
                    }
                   /* if(healthIdOptionMap.containsKey(health.getId())){
                        lastList.add(health);
                    }*/
                }
            }
            List<StutotalityStuResult> healthResults = stutotalityStuResultService.findListByParms(schoolId,acadyear, semester, studentId, healthIds.toArray(new String[0]));
            Map<String,StutotalityStuResult> healthIdResultMap=EntityUtils.getMap(healthResults,StutotalityStuResult::getItemHealthId);
            setShilResult(healthIdResultMap,healthId);
            List<StutotalityStuResult> beforeHealthResults =null;
            if(semester.equals("2")) {
                beforeHealthResults = stutotalityStuResultService.findListByParms(schoolId,acadyear, "1", studentId, healthIds.toArray(new String[0]));
            }else {
                String nowYear = acadyear.substring(0,4);
                int lastYear = Integer.parseInt(nowYear)-1;
                String lastAcadyear = lastYear+"-"+(lastYear+1);
                beforeHealthResults = stutotalityStuResultService.findListByParms(schoolId,lastAcadyear, "2", studentId, healthIds.toArray(new String[0]));
            }
            Map<String,StutotalityStuResult> beHealthIdResultMap=EntityUtils.getMap(beforeHealthResults,StutotalityStuResult::getItemHealthId);
            setShilResult(beHealthIdResultMap,healthId);
            map.put("healthList",healthList);
            map.put("healthIdOptionMap",healthIdOptionMap);
            map.put("healthIdResultMap",healthIdResultMap);
            map.put("beHealthIdResultMap",beHealthIdResultMap);
        }
        //学校公告
        StutotalitySchoolNotice notice=stutotalitySchoolNoticeService.findByUnitIdAndAcadyearAndSemesterAndGradeId(schoolId,acadyear,semester,grade.getId());
        if(notice!=null){
            map.put("notice",notice.getNotice());
        }
        //班主任
        Teacher teacher=SUtils.dc(teacherRemoteService.findOneById(clazz.getTeacherId()),Teacher.class);
        map.put("teacherName",teacher==null?"":teacher.getTeacherName());
        //教导主任
        Teacher gradeTeacher=SUtils.dc(teacherRemoteService.findOneById(grade.getTeacherId()),Teacher.class);
        map.put("gradeTeacherName",gradeTeacher==null?"":gradeTeacher.getTeacherName());
        //校长
        map.put("schoolmaster",school.getSchoolmaster());
        //获奖情况
        List<StutotalityStuReward> stutotalityStuRewards = stutotalityStuRewardService.getByAcadyearAndSemesterAndUnitIdAndStudentId(acadyear,semester,schoolId,studentId);
        if(CollectionUtils.isNotEmpty(stutotalityStuRewards)){
            int rewardLen=stutotalityStuRewards.size();
            int number1=0;
            int number2=0;
            int number3=0;
            for(StutotalityStuReward reward:stutotalityStuRewards){
                if(reward.getStarNum()==1){
                    number1+=1;
                }else if(reward.getStarNum()==2){
                    number2+=2;
                }else{
                    number3+=3;
                }
            }
            map.put("allNumber",number1+number2+number3);
            map.put("myRewardSize",rewardLen);
            map.put("number1",number1);
            map.put("number2",number2);
            map.put("number3",number3);
        }
        //全优生
        List<StutotalityGoodStat> goodStatList=stutotalityGoodStatService.findListByStudentIds(acadyear,semester,new String[]{studentId});
        StutotalityGoodStat goodStat=null;
        if(CollectionUtils.isNotEmpty(goodStatList)){
            goodStat=goodStatList.get(0);
        }else{
            goodStat=new StutotalityGoodStat();
        }
        map.put("goodStat",goodStat);


//        return "/stutotality/report/stutotalityReportStuPdf.ftl";
        return "/stutotality/report/stuTotalityReport.ftl";

    }
    public void setShilResult(Map<String,StutotalityStuResult> healthIdResultMap,String healthId){
        if(healthIdResultMap.containsKey(healthId)){
            StutotalityStuResult stutotalityStuResult=healthIdResultMap.get(healthId);
            if(StringUtils.isNotBlank(stutotalityStuResult.getHealthResult())){
                String[] ss=stutotalityStuResult.getHealthResult().split("_");
                String healthResult="左："+ss[0];
                if(ss.length>1){
                    healthResult+=" 右："+ss[1];
                }
                stutotalityStuResult.setHealthResult(healthResult);
            }
        }
    }

    @RequestMapping("/classIndex/page")
    public String getClassReportIndex(ModelMap map){
/*        String unitId=getLoginInfo().getUnitId();
        Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
        String acadyear = se.getAcadyear();
        String semester = se.getSemester().toString();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId,new Integer[]{BaseConstants.SECTION_PRIMARY},acadyear), new TR<List<Grade>>() {});
        if(CollectionUtils.isNotEmpty(gradeList)){
            Set<String> gradeIds = gradeList.stream().map(Grade::getId).collect(Collectors.toSet());
            List<Clazz> clazzs = SUtils.dt(classRemoteService.findByInGradeIds(gradeIds.toArray(new String[0])),new TR<List<Clazz>>(){});
            Map<String,List<Clazz>> gradeIdListMap= clazzs.stream().collect(Collectors.groupingBy(Clazz::getGradeId));
            map.put("gradeIdListMap",gradeIdListMap);
        }

        map.put("gradeList",gradeList);
        System.out.println(gradeList.size());*/

        String unitId = getLoginInfo().getUnitId();
        String userId = getLoginInfo().getUserId();
        User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
        boolean isAdmin = isAdmin(unitId, userId);
        List<Grade> gradeList = new ArrayList<>();
        Map<String,List<Clazz>> stringListMap = new HashMap<>();
        if(isAdmin){
            gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId),new TR<List<Grade>>(){});
        }else {
            String ownId = user.getOwnerId();
            List<Clazz> clazzsList = SUtils.dt(classRemoteService.findByTeacherId(ownId), new TR<List<Clazz>>() {
            });
            if(CollectionUtils.isNotEmpty(clazzsList)) {
                Set<String> classIds = clazzsList.stream().map(Clazz::getGradeId).collect(Collectors.toSet());
                gradeList = SUtils.dt(gradeRemoteService.findListByIds(classIds.toArray(new String[0])), new TR<List<Grade>>() {
                });
            }
        }
        gradeList = gradeList.stream().filter(grade -> grade.getSection()==1).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(gradeList)){
            Set<String> gradeIds = gradeList.stream().map(Grade::getId).collect(Collectors.toSet());
            List<Clazz> clazzs = SUtils.dt(classRemoteService.findByInGradeIds(gradeIds.toArray(new String[0])),new TR<List<Clazz>>(){});
            Map<String,List<Clazz>> gradeIdListMap= clazzs.stream().collect(Collectors.groupingBy(Clazz::getGradeId));
            map.put("gradeIdListMap",gradeIdListMap);
        }
        map.put("gradeList",gradeList);
        return "/stutotality/report/stutotalityClassIndex.ftl";
    }
    @RequestMapping("/getClassTotalityReport")
    public String getClassTotalityReport(String classId,ModelMap map){
        String unitId = getLoginInfo().getUnitId();
        Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        String acadyear = se.getAcadyear();
        String semester = se.getSemester().toString();
        if(StringUtils.isBlank(classId)){
            return "/stutotality/report/stutotalityClassReport.ftl";
        }
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()),Grade.class);
        List<StutotalityItem> itemList = stutotalityItemService.getItemListByParams(unitId,acadyear,semester,grade.getId(),null);
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}),new TR<List<Student>>(){});
        Map optionMap = new HashMap();
        if(CollectionUtils.isNotEmpty(itemList)) {
            Set<String> itemIds = itemList.stream().map(StutotalityItem::getId).collect(Collectors.toSet());
            List<StutotalityItemOption> optionList = stutotalityItemOptionService.findByItemIds(itemIds.toArray(new String[0]));
            List<StutotalityStuResult> results = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(optionList)&&CollectionUtils.isNotEmpty(studentList)) {
                Set<String> optionIds = optionList.stream().map(StutotalityItemOption::getId).collect(Collectors.toSet());
                Set<String> studentIds = studentList.stream().map(Student::getId).collect(Collectors.toSet());
                results = stutotalityStuResultService.findListByOptionIds(acadyear, semester, studentIds.toArray(new String[0]), optionIds.toArray(new String[0]), Type1);
                Map<String, List<StutotalityStuResult>> resultMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(results)) {
                    resultMap = results.stream().collect(Collectors.groupingBy(StutotalityStuResult::getStudentId));
                }
                for (StutotalityItemOption stutotalityItemOption : optionList) {
                    List<StutotalityStuResult> inResultList = null;
                    Float totalResult=0f;
                    int l=0;
                    for(Student student:studentList) {
                        if(resultMap.containsKey(student.getId())){//有学生数据存在
                            inResultList = resultMap.get(student.getId());
                            Map<String,StutotalityStuResult> stuResultMap = inResultList.stream().collect(Collectors.toMap(StutotalityStuResult::getOptionId,Function.identity()));
                            if(stuResultMap.containsKey(stutotalityItemOption.getId())){//该学生的该项目内容有结果数据 算入计分
                                StutotalityStuResult result = stuResultMap.get(stutotalityItemOption.getId());
                                if(result.getResult()!=null){
                                    totalResult = totalResult+result.getResult();
                                    l++;
                                }
                            }
                        }
                    }
                    if(l!=0){
                        stutotalityItemOption.setClassAverageResult(totalResult/l);
                    }
                }
            }
            optionMap = optionList.stream().collect(Collectors.groupingBy(StutotalityItemOption::getItemId));
        }

        map.put("optionMap",optionMap);
        map.put("className",clazz.getClassNameDynamic());
        map.put("itemList",itemList);
        return "/stutotality/report/stutotalityClassReport.ftl";
    }
    @RequestMapping("/getGradeTotalityReport")
    public String getGradeTotalityReport(String gradeId,ModelMap map){
        map.put("gradeId",gradeId);
        return "/stutotality/report/gradeTotalityReport.ftl";
    }
    @RequestMapping("/getStuReportList1")
    public String getStuReportList1(String gradeId,ModelMap map){
        List<Clazz> clazzes = SUtils.dt(classRemoteService.findByInGradeIds(new String[]{gradeId}),new TR<List<Clazz>>(){});
        List<ClassDto> classDtos = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(clazzes)) {
            for (Clazz clazz:clazzes) {
                ClassDto classDto = new ClassDto();
                classDto.setClassName(clazz.getClassNameDynamic());
                classDto.setId(clazz.getId());
                classDtos.add(classDto);
            }
        }
        map.put("classDtos",classDtos);
        return "/stutotality/report/gradeStuReport.ftl";
    }
    @RequestMapping("/getStuReportList2")
    public String getStuReportList2(){
        return "/stutotality/report/gradeStuDevReport.ftl";
    }

    @RequestMapping("/getStuReportList3")
    public String getStuReportList3(){
        return "/stutotality/report/gradeStuTotalReport.ftl";
    }

    public void setItemResult(List<StutotalityItem> hasStatItemList,Map<String,List<StutotalityItemOption>> itemIdOptionListMap,Map<String,Float> optionIdScoreMap
            ,Map<String,Float> itemIdScoreMap,Set<String> subjectIds,Map<String,StutotalityScale> typeScaleMap,ModelMap map){
        if(CollectionUtils.isNotEmpty(hasStatItemList)){
            List<StutotalityItem> hasStatMoreList=new ArrayList<>();//基础课程
            List<StutotalityItem> hasStatOneList=new ArrayList<>();//特殊课程
            for(StutotalityItem item:hasStatItemList){
                if(itemIdOptionListMap.containsKey(item.getId())){
                    if(itemIdOptionListMap.get(item.getId()).size()==1){
                        //借用内容名称
                        item.setShortName(itemIdOptionListMap.get(item.getId()).get(0).getOptionName());
                        hasStatOneList.add(item);
                    }else{
                        hasStatMoreList.add(item);
                    }
                }
            }
            float dayScale=typeScaleMap.containsKey(Type2)?typeScaleMap.get(Type2).getScale():0.0f;
            float fianlScale=typeScaleMap.containsKey(Type3)?typeScaleMap.get(Type3).getScale():0.0f;
            List<StutotalityItem> hasStatDaoList=new ArrayList<>();//阳光德育
            List<StutotalityItem> hasStatSubList=new ArrayList<>();//基础课程
            for(StutotalityItem item:hasStatMoreList){
                float dayScore=0.0f;
                int l=0;
                if(itemIdOptionListMap.containsKey(item.getId())){
                    for(StutotalityItemOption option:itemIdOptionListMap.get(item.getId())){
                        if(optionIdScoreMap.containsKey(option.getId())){
                            dayScore+=optionIdScoreMap.get(option.getId());
                            l++;
                        }
                    }
                }
                if(l!=0){
                    dayScore=dayScore/l;
                }
                float finalScore=itemIdScoreMap.containsKey(item.getId())?itemIdScoreMap.get(item.getId()):0.0f;
                item.setAvgScore((dayScale*dayScore)/100+(fianlScale*finalScore)/100);
                item.setDayScore(dayScore);
                item.setResult(finalScore);
                if(subjectIds.contains(item.getSubjectId())){
                    hasStatSubList.add(item);
                }else{
                    hasStatDaoList.add(item);
                }
            }
            map.put("hasStatMoreList",hasStatMoreList);
            map.put("hasStatOneList",hasStatOneList);
            map.put("hasStatSubList",hasStatSubList);
            map.put("hasStatDaoList",hasStatDaoList);
        }
    }
    @RequestMapping("/exportPdf")
    @ControllerInfo("生成pdf并导出")
    public void exportHtmlToPdf(String acadyear,String semester ,String studentId,HttpServletRequest request,HttpServletResponse response) throws IOException {
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
        StorageDir dir = SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
        /*MyThread myThread=new MyThread(UrlUtils.getPrefix(request),dir.getDir(),studentId,student.getSchoolId(),student.getClassId(),acadyear,semester);
        Thread thread = new Thread(myThread);
        thread.start();*/
        String uu = dir.getDir() + File.separator + "stutotality" + File.separator + "report" + File.separator + student.getSchoolId() + File.separator
                + acadyear+"_"+semester+ File.separator+student.getClassId()+File.separator+studentId+".pdf";
        File f = new File(uu);
        String urlStr = "/stutotality/report/common/getStuTotalityReportPdf?studentId=" + studentId + "&acadyear=" + acadyear + "&semester=" + semester;
        if (f.exists()) {
            f.delete();//删除
        }
        HtmlToPdf.convertFin(new String[]{UrlUtils.getPrefix(request) + urlStr}, uu, null, null, 3000, null);
        File file = new File(uu);
        //如果文件不存在
        if (!file.exists()) {
            return;
        }
        FileInputStream in =null;
        OutputStream out =null;
        try {
            //设置响应头，控制浏览器下载该文件
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(student.getStudentName()+"综合评价报告单.pdf", "UTF-8"));
            //读取要下载的文件，保存到文件输入流
            in = new FileInputStream(uu);
            //创建输出流
            out = response.getOutputStream();
            //缓存区
            byte buffer[] = new byte[1024];
            int len = 0;
            //循环将输入流中的内容读取到缓冲区中
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭
            if(in!=null){
                in.close();
            }
            if(out!=null){
                out.close();
            }
        }
    }
    /**
     * 判断是否为教务管理员
     */
    private boolean isAdmin(String unitId, String userId) {
        boolean res = customRoleRemoteService.checkUserRole(unitId, StutotalityConstant.STUTOTALITY_SUBSYSTEM, StutotalityConstant.STUTOTALITY_MANAGE_CODE, userId);
        return res;
    }
    /**
     * 获取用户的班级以及年级
     * @param unitId
     * @param userId
     * @param ownerId
     * @param map
     * @return
     */
    public List<Clazz> findClazzListBy(String unitId,String userId,String ownerId,ModelMap map){
        boolean isAdmin = isAdmin(unitId, userId);//管理员权限
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
        List<Grade> gradeList =null;
        Map<String,List<Clazz>> classListMap=null;
        List<Clazz> clazzList =null;
        if (!isAdmin) {//非管理员的权限
            //获取班主任的班级
            clazzList = SUtils.dt(classRemoteService.findByTeacherId(ownerId), new TR<List<Clazz>>() {});
            /*//对用户下所有的班级id 包括班主任的班级以及任课老师的班级
            Set<String> classIds= EntityUtils.getSet(clazzsList, Clazz::getId);
            //获取任课老师的班级
            List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingList(unitId,semester.getAcadyear(), semester.getSemester()+"", ownerId), new TR<List<ClassTeaching>>() {});
            classIds.addAll(EntityUtils.getSet(classTeachingList,ClassTeaching::getClassId));
            //所有不重复的班级
            clazzList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>() {});*/
            if(CollectionUtils.isNotEmpty(clazzList)){
                Set<String> gradeIds=EntityUtils.getSet(clazzList, Clazz::getGradeId);
                gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])), new TR<List<Grade>>(){});
                classListMap=clazzList.stream().collect(Collectors.groupingBy(Clazz::getGradeId));
            }
        }else{
            //只取小学年级数据
            gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId,new Integer[]{1},semester.getAcadyear()), new TR<List<Grade>>(){});
            clazzList = SUtils.dt(classRemoteService.findByInGradeIds(EntityUtils.getSet(gradeList,Grade::getId).toArray(new String[0])), new TR<List<Clazz>>() {});
            if(CollectionUtils.isNotEmpty(clazzList)){
                classListMap=clazzList.stream().collect(Collectors.groupingBy(Clazz::getGradeId));
            }
        }
        if(CollectionUtils.isNotEmpty(gradeList)){
            Collections.sort(gradeList, new Comparator<Grade>() {
                @Override
                public int compare(Grade o1, Grade o2) {
                    return o1.getGradeCode().compareTo(o2.getGradeCode());
                }
            });
        }
        map.put("gradeList",gradeList);
        map.put("classListMap",classListMap);
        if(clazzList==null){
            return new ArrayList<>();
        }
        return clazzList;
    }
     /* class MyThread implements Runnable {
        private String serverUrl;
        private String dirUrl;
        private String studentId;
        private String schoolId;
        private String classId;
        private String acadyear;
        private String semester;

        @Override
        public void run() {
            String uu = dirUrl + File.separator + "stutotality" + File.separator + "report" + File.separator + schoolId + File.separator
                    + acadyear+"_"+semester+ File.separator+classId+File.separator+studentId+".pdf";
            File f = new File(uu);
            String urlStr = "/stutotality/report/common/getStuTotalityReportPdf?studentId=" + studentId + "&acadyear=" + acadyear + "&semester=" + semester;
            if (f.exists()) {
                f.delete();//删除
            }
            HtmlToPdf.convertFin(new String[]{serverUrl + urlStr}, uu, null, "1300", 2000, null);
        }

        public MyThread(String serverUrl, String dirUrl, String studentId, String schoolId, String classId, String acadyear, String semester) {
            this.serverUrl = serverUrl;
            this.dirUrl = dirUrl;
            this.studentId = studentId;
            this.schoolId = schoolId;
            this.classId = classId;
            this.acadyear = acadyear;
            this.semester = semester;
        }
    }*/
}
