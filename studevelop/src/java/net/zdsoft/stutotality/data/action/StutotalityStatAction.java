package net.zdsoft.stutotality.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.stutotality.data.entity.StutotalityGoodStat;
import net.zdsoft.stutotality.data.entity.StutotalityItem;
import net.zdsoft.stutotality.data.entity.StutotalityScale;
import net.zdsoft.stutotality.data.entity.StutotalityStuResult;
import net.zdsoft.stutotality.data.service.StutotalityGoodStatService;
import net.zdsoft.stutotality.data.service.StutotalityItemService;
import net.zdsoft.stutotality.data.service.StutotalityScaleService;
import net.zdsoft.stutotality.data.service.StutotalityStuResultService;
import net.zdsoft.stutotality.data.util.StutotalityConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.zdsoft.stutotality.data.constant.ResultType.Type1;
import static net.zdsoft.stutotality.data.constant.ResultType.Type2;

@Controller
@RequestMapping("/stutotality")
public class StutotalityStatAction extends BaseAction {
    @Autowired
    SemesterRemoteService semesterRemoteService;
    @Autowired
    GradeRemoteService gradeRemoteService;
    @Autowired
    CustomRoleRemoteService customRoleRemoteService;
    @Autowired
    ClassRemoteService classRemoteService;
    @Autowired
    ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    UserRemoteService userRemoteService;
    @Autowired
    StutotalityScaleService stutotalityScaleService;
    @Autowired
    StudentRemoteService studentRemoteService;
    @Autowired
    StutotalityItemService stutotalityItemService;
    @Autowired
    StutotalityStuResultService stutotalityStuResultService;
    @Autowired
    StutotalityGoodStatService stutotalityGoodStatService;
    public final String ALL_GOOD_PERCENT="4";
    public final String All_GOOD_TYPE="8";
    public final String ITEM_SUBJECT_PERCENT="1";
    @RequestMapping("/report/statIndex/page")
    public String stutotalityStatIndex(ModelMap modelMap){
        LoginInfo info = getLoginInfo();
        String unitId = info.getUnitId();
        String userId = info.getUserId();
        //年级Code列表
        boolean isAdmin = isAdmin(unitId, userId);
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
//        List<StutotalityScale> stutotalityScales = stutotalityScaleService.findByUnitIdAndGradeIdAndtype(unitId,"00000000000000000000000000000000",ALL_GOOD_PERCENT);
        List<StutotalityScale> stutotalityScales = stutotalityScaleService.findByUnitIdAndGradeIdAndtype(unitId,"00000000000000000000000000000000",ALL_GOOD_PERCENT);
        //只取小学年级数据
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId,new Integer[]{1},semester.getAcadyear()), new TR<List<Grade>>(){});
        List<Grade> list = new ArrayList<>();
        Set<Grade> list1 = new HashSet<>();
        if (!isAdmin) {
            String ownerId=info.getOwnerId();
            Map<String, Grade> gradeMap = gradeList.stream().collect(Collectors.toMap(Grade::getId, Function.identity()));
            //获取班主任的班级
            List<Clazz> clazzsList = SUtils.dt(classRemoteService.findByTeacherId(ownerId), new TR<List<Clazz>>() {});
//            //获取任课老师的班级
//            List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingList(unitId,semester.getAcadyear(), semester.getSemester()+"", ownerId), new TR<List<ClassTeaching>>() {});
//            if(CollectionUtils.isNotEmpty(classTeachingList)) {
//                Set<String> classIds1 = classTeachingList.stream().map(ClassTeaching::getClassId).collect(Collectors.toSet());
//                List<Clazz> clazzes = SUtils.dt(classRemoteService.findListByIds(classIds1.toArray(new String[0])), new TR<List<Clazz>>() {});
//                clazzsList.addAll(clazzes);
//            }
            Set<String> gradeIds = clazzsList.stream().map(Clazz::getGradeId).collect(Collectors.toSet());
//                //加入所在班级班主任年级
            for (String s : gradeIds) {
                Grade grade = gradeMap.get(s);
                if (grade != null) {
                    list1.add(grade);
                }
            }
        } else {
            list1.addAll(gradeList);
        }
        list.addAll(list1);
        list.sort(new Comparator<Grade>() {
            @Override
            public int compare(Grade o1, Grade o2) {
                return o1.getGradeCode().compareTo(o2.getGradeCode());
            }
        });
        if(CollectionUtils.isNotEmpty(stutotalityScales)){
            modelMap.put("percent",stutotalityScales.get(0).getScale());
        }
        modelMap.put("isAdmin", isAdmin);
        modelMap.put("gradeList", list);
        return "/stutotality/stat/stutotalityStatIndex.ftl";
    }
    @RequestMapping("/stat/statList")
    public String stutotalityStatList(String classId,ModelMap map){
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        String unitId = getLoginInfo().getUnitId();
        List<StutotalityItem> itemList = stutotalityItemService.findByUnitIdAndSubjectType(unitId,new String[]{All_GOOD_TYPE});
        StutotalityItem allGoodItem = new StutotalityItem();
        if(CollectionUtils.isNotEmpty(itemList)){
            allGoodItem = itemList.get(0);
        }
        List<StutotalityScale> stutotalityScales = stutotalityScaleService.findByUnitIdAndClassIdsAndtype(unitId,semester.getAcadyear(),semester.getSemester().toString(),new String[]{classId},ALL_GOOD_PERCENT);
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}),new TR<List<Student>>(){});
        Set<String> studentIds = EntityUtils.getSet(studentList,Student::getId);
        List<StutotalityGoodStat> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(studentIds)){
            list = stutotalityGoodStatService.findListByStudentIdsWithMaster(semester.getAcadyear(),semester.getSemester().toString(),studentIds.toArray(new String[0]));
        }
        if(CollectionUtils.isNotEmpty(list)) {
            for (StutotalityGoodStat stutotalityGoodStat : list) {
                stutotalityGoodStat.setAllScore(convertFloat(stutotalityGoodStat.getAllScore(), 1));
                stutotalityGoodStat.setStandardSocre(convertFloat(stutotalityGoodStat.getStandardSocre(), 1));
            }
            list.sort(new Comparator<StutotalityGoodStat>() {
                @Override
                public int compare(StutotalityGoodStat o1, StutotalityGoodStat o2) {
                    int ret=(int)(o2.getHaveGood()-o1.getHaveGood());
                    if(ret==0){
                        if(o2.getAllScore()-o1.getAllScore()>0){
                            ret=1;
                        }else if(o2.getAllScore()-o1.getAllScore()<0){
                            ret=-1;
                        }else {
                            ret=0;
                        }
                    }
                    return ret;
                }
            });
        }

        if(CollectionUtils.isNotEmpty(stutotalityScales)){
            map.put("percent",stutotalityScales.get(0).getScale());
        }else {
            List<StutotalityScale> stutotalityScales1 = stutotalityScaleService.findByUnitIdAndGradeIdAndtype(unitId,"00000000000000000000000000000000",ALL_GOOD_PERCENT);
            if(CollectionUtils.isNotEmpty(stutotalityScales1)) {
                map.put("percent", stutotalityScales1.get(0).getScale());
            }
        }
//        List<StutotalityGoodStat> list = stutotalityGoodStatService.findListByClassIdAndGradeId(semester.getAcadyear(),semester.getSemester().toString(),classId);
        map.put("goodStatList",list);
        map.put("allGoodItem",allGoodItem);
        return "/stutotality/stat/stutotalityStatList.ftl";
    }

    @RequestMapping("/stat/getClassListByGradeId")
    @ResponseBody
    public String getClassListByGradeId(String gradeId) {
        String unitId = getLoginInfo().getUnitId();
        String userId = getLoginInfo().getUserId();
        User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
        boolean isAdmin = isAdmin(unitId, userId);
        List<Clazz> list = new ArrayList<>();
        if(isAdmin){
            list= SUtils.dt(classRemoteService.findByInGradeIds(new String[]{gradeId}),new TR<List<Clazz>>(){});
        }else {
            String ownId = user.getOwnerId();
            list = SUtils.dt(classRemoteService.findByGradeId(unitId,gradeId,ownId), new TR<List<Clazz>>() {
            });
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("classList",list);
        return jsonObject.toJSONString();
    }
    @ResponseBody
    @RequestMapping("/stat/statResult")
    @ControllerInfo(value = "统计")
    public String statResult(String classId,String gradeId,String percent){
        String unitId= getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        StutotalityScale scale = null;
//        StutotalityScale scale1 = new StutotalityScale();
//        scale1.setScale(Float.parseFloat(percent));
        final String key = unitId+"_"+"stutotalityStat";
        List<StutotalityItem> itemList = stutotalityItemService.findByUnitIdAndSubjectType(unitId,new String[]{All_GOOD_TYPE});
        StutotalityItem allGoodItem = null;
        if(CollectionUtils.isNotEmpty(itemList)){
            allGoodItem = itemList.get(0);
        }else {
            JSONObject jsonObject=new JSONObject();;
            jsonObject.put("type", "error");
            jsonObject.put("errorType","操作失败，请先维护达标科目");
            return JSON.toJSONString(jsonObject);
//            return error("操作失败，请先维护达标科目");
        }
        String selectGradeId = "";
        if(StringUtils.isBlank(gradeId)){
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
            selectGradeId = clazz.getGradeId();
        }else {
            selectGradeId = gradeId;
        }
        List<StutotalityItem> items = stutotalityItemService.findByParams(unitId,semester.getAcadyear(),semester.getSemester().toString(),selectGradeId,new String[]{allGoodItem.getSubjectId()});
        StutotalityItem finalAllGoodItem =null;
        if(CollectionUtils.isEmpty(items)){
            JSONObject jsonObject=new JSONObject();;
            jsonObject.put("type", "error");
            jsonObject.put("errorType","操作失败，请先维护该年级达标科目");
            return JSON.toJSONString(jsonObject);
        }else {
            finalAllGoodItem = items.get(0);
        }

        if(StringUtils.isNotBlank(classId)){
            if(StringUtils.isNotBlank(percent)) {
                List<StutotalityScale> stutotalityScales = stutotalityScaleService.findByUnitIdAndGradeIdAndtype(unitId, "00000000000000000000000000000000", ALL_GOOD_PERCENT);
                scale = new StutotalityScale();
                scale.setScale(Float.parseFloat(percent) * 100);
                scale.setClassId(classId);
                scale.setUnitId(unitId);
                scale.setId(UuidUtils.generateUuid());
                scale.setAcadyear(semester.getAcadyear());
                scale.setCreationTime(new Date());
                scale.setItemId(finalAllGoodItem.getId());
                scale.setStandardScore(stutotalityScales.get(0).getStandardScore());
                scale.setSemester(semester.getSemester().toString());
                scale.setType(ALL_GOOD_PERCENT);
            }else {
                JSONObject jsonObject=new JSONObject();;
                jsonObject.put("type", "error");
                jsonObject.put("errorType","操作失败，请先设置全优生占比");
                return JSON.toJSONString(jsonObject);
            }
        }
        if(scale!=null){
            stutotalityScaleService.deleteByUnitIdAndAcadyearAndSemesterAndClassId(unitId,semester.getAcadyear(),semester.getSemester().toString(),classId);
            stutotalityScaleService.save(scale);
        }
//
        StutotalityScale finalScale = scale;
//        stat(classId,gradeId,semester, finalAllGoodItem, finalScale);
//        RedisUtils.set(key,"success");
        StutotalityItem finalAllGoodItem1 = finalAllGoodItem;
        new Thread(new Runnable(){
            public void run(){
                try {
                    stat(unitId,classId,gradeId,semester, finalAllGoodItem1, finalScale);
                    RedisUtils.set(key,"success");
                }catch (Exception e) {
                    e.printStackTrace();
                    RedisUtils.set(key,"error");
                }
                return;
            }
        }).start();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("type", RedisUtils.get(key));
        if("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))){
            //RedisUtils.del(typekey);
            RedisUtils.del(key);
        }
        return JSON.toJSONString(jsonObject);
    }

    public void stat(String unitId,String classId,String gradeId,Semester semester,StutotalityItem allGoodItem,StutotalityScale percentScale){
        List<Student> studentList = new ArrayList<>();
        String selectGradeId = "";
        if(StringUtils.isBlank(gradeId)){
            studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}),new TR<List<Student>>(){});
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
            selectGradeId = clazz.getGradeId();
        }
        Map<String,StutotalityScale> allGoodScaleMap = new HashMap<>();
        List<StutotalityScale> defaultScales = stutotalityScaleService.findByUnitIdAndGradeIdAndtype(unitId,"00000000000000000000000000000000",ALL_GOOD_PERCENT);
        if(StringUtils.isNotBlank(gradeId)){
            List<Clazz> clazzes = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId,gradeId),new TR<List<Clazz>>(){});
            Set<String> classIds = new HashSet<>();
            if(CollectionUtils.isNotEmpty(clazzes)){
                classIds = clazzes.stream().map(Clazz::getId).collect(Collectors.toSet());
            }
            List<StutotalityScale> stutotalityScales = stutotalityScaleService.findByUnitIdAndClassIdsAndtype(unitId,semester.getAcadyear(),semester.getSemester().toString(),classIds.toArray(new String[0]),ALL_GOOD_PERCENT);
            allGoodScaleMap = EntityUtils.getMap(stutotalityScales,StutotalityScale::getClassId);
            studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])),new TR<List<Student>>(){});
            selectGradeId = gradeId;
        }
        //需达标科目日常成绩
        Map<String,List<StutotalityStuResult>> resultMap1 = new HashMap<>();
        //需达标科目期末成绩
        Map<String,List<StutotalityStuResult>> resultMap2 = new HashMap<>();
        //日常成绩
        Map<String,List<StutotalityStuResult>> resultMap3 = new HashMap<>();
        //期末成绩
        Map<String,List<StutotalityStuResult>> resultMap4 = new HashMap<>();
        Map<String,StutotalityScale> scaleMap = new HashMap<>();
        if(allGoodItem!=null) {
            //需达标科目日常成绩
            List<StutotalityStuResult> results1= stutotalityStuResultService.findListByItemIds(semester.getAcadyear(), semester.getSemester().toString(), "", new String[]{allGoodItem.getId()},Type1);
            if(CollectionUtils.isNotEmpty(results1)){
                resultMap1 = results1.stream().collect(Collectors.groupingBy(StutotalityStuResult::getStudentId));
            }
            //需达标科目期末成绩
            List<StutotalityStuResult> results2= stutotalityStuResultService.findListByItemIds(semester.getAcadyear(), semester.getSemester().toString(), "", new String[]{allGoodItem.getId()},Type2);
            if(CollectionUtils.isNotEmpty(results2)){
                resultMap2 = results2.stream().collect(Collectors.groupingBy(StutotalityStuResult::getStudentId));
            }
        }

        List<StutotalityScale> stutotalityScales = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId,semester.getAcadyear(),semester.getSemester().toString(),selectGradeId,ITEM_SUBJECT_PERCENT);
        if(CollectionUtils.isNotEmpty(stutotalityScales)){
            scaleMap = stutotalityScales.stream().collect(Collectors.toMap(StutotalityScale::getItemId,Function.identity()));
        }
        //日常成绩
        List<StutotalityStuResult> resultsDaily= stutotalityStuResultService.findByAcadyearAndSemesterAndUnitIdAndTypeWithMaster(semester.getAcadyear(), semester.getSemester().toString(), unitId,Type1,"");
        if(CollectionUtils.isNotEmpty(resultsDaily)){
            resultMap3 = resultsDaily.stream().collect(Collectors.groupingBy(StutotalityStuResult::getStudentId));
        }
        //期末成绩
        List<StutotalityStuResult> resultsFinal= stutotalityStuResultService.findByAcadyearAndSemesterAndUnitIdAndTypeWithMaster(semester.getAcadyear(), semester.getSemester().toString(), unitId,Type2,"");
        if(CollectionUtils.isNotEmpty(resultsFinal)){
            resultMap4 = resultsFinal.stream().collect(Collectors.groupingBy(StutotalityStuResult::getStudentId));
        }
        Float dailyPercent = 0f;
        Float finalPercent = 0f;
        //日常比例
        List<StutotalityScale> dailyList = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId, semester.getAcadyear(), semester.getSemester().toString(), selectGradeId,"2");
        if(CollectionUtils.isNotEmpty(dailyList)){
            StutotalityScale dailySacle = dailyList.get(0);
            if(dailySacle.getScale()!=null) {
                dailyPercent = dailySacle.getScale()/100;
            }
        }
        //期末比例
        List<StutotalityScale> termList = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId, semester.getAcadyear(), semester.getSemester().toString(), selectGradeId,"3");
        if(CollectionUtils.isNotEmpty(termList)){
            StutotalityScale termScale = termList.get(0);
            if(termScale.getScale()!=null) {
                finalPercent = termScale.getScale()/100;
            }
        }

        List<StutotalityGoodStat> list = new ArrayList<>();
        Set<String> studentIds = new HashSet<>();
        Map<String,Student> studentMap = EntityUtils.getMap(studentList,Student::getId);
        Set<String> classIds = EntityUtils.getSet(studentList,Student::getClassId);
        Map<String,List<Student>> studentListMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(studentList)){
            studentListMap=studentList.stream().collect(Collectors.groupingBy(Student::getClassId));
        }
        for(Student student:studentList){
            studentIds.add(student.getId());
            StutotalityGoodStat stutotalityGoodStat = new StutotalityGoodStat();
            stutotalityGoodStat.setAcadyear(semester.getAcadyear());
            stutotalityGoodStat.setCreationTime(new java.util.Date());
            stutotalityGoodStat.setId(UuidUtils.generateUuid());
            stutotalityGoodStat.setSemester(semester.getSemester().toString());
            stutotalityGoodStat.setStudentId(student.getId());
            stutotalityGoodStat.setStudentCode(student.getStudentCode());
            stutotalityGoodStat.setStudentName(student.getStudentName());
            stutotalityGoodStat.setUnitId(unitId);
//            if(StringUtils.isNotBlank(student.getClassId())) {
//                stutotalityGoodStat.setClassId(student.getClassId());
//            }
            //统计需达标科目分数
            List<StutotalityStuResult> results1 = resultMap1.get(student.getId());
            List<StutotalityStuResult> results2 = resultMap2.get(student.getId());
            Float dailyGoodAvg = getAvgScore(results1);
            Float finalGoodAvg = getAvgScore(results2);
            Float score = dailyGoodAvg*dailyPercent+finalGoodAvg*finalPercent;
            stutotalityGoodStat.setStandardSocre(convertFloat(score,3));
            //统计总分
            List<StutotalityStuResult> results3 = resultMap3.get(student.getId());
            List<StutotalityStuResult> results4 = resultMap4.get(student.getId());
            Map<String, List<StutotalityStuResult>> listMap1 = new HashMap<>();
            Map<String, List<StutotalityStuResult>> listMap2 = new HashMap<>();
//            for(StutotalityStuResult stutotalityStuResult:results3){
//                if(StringUtils.isBlank(stutotalityStuResult.getItemHealthId())){
//                    System.out.println(11111);
//                }
//            }
            if(CollectionUtils.isNotEmpty(results3)) {
                listMap1 = results3.stream().collect(Collectors.groupingBy(StutotalityStuResult::getItemHealthId));
            }

            if(CollectionUtils.isNotEmpty(results4)) {
                listMap2 = results4.stream().collect(Collectors.groupingBy(StutotalityStuResult::getItemHealthId));
            }
            Float allScore=0f;
            if(MapUtils.isNotEmpty(listMap1)) {
                for (String key : listMap1.keySet()) {
                    List<StutotalityStuResult> list1 = listMap1.get(key);
                    List<StutotalityStuResult> list2 = listMap2.get(key);
                    Float dailyAvg = getAvgScore(list1);
                    Float finalAvg = getAvgScore(list2);
                    Float scoreAvg = dailyAvg * dailyPercent + finalAvg*finalPercent;
                    StutotalityScale scale = scaleMap.get(key);
                    if (scale != null && scale.getScale() != null) {
                        allScore = allScore + scoreAvg * scale.getScale()/100;
                    } else {
                        allScore = allScore + scoreAvg * 0;
                    }
                }
            }
            stutotalityGoodStat.setAllScore(convertFloat(allScore,3));
            list.add(stutotalityGoodStat);
        }
        //处理全优跟排名
        if(CollectionUtils.isNotEmpty(list)) {
            list.sort(new Comparator<StutotalityGoodStat>() {
                @Override
                public int compare(StutotalityGoodStat o1, StutotalityGoodStat o2) {
                    if(o1.getAllScore()-o2.getAllScore()>0) {
                        return 1;
                    }else if(o1.getAllScore()-o2.getAllScore()<0){
                        return -1;
                    }else {
                        return 0;
                    }
                }
            });
            int rank = 1;
            //达标科目的列表
            //List<StutotalityGoodStat> passList = new ArrayList<>();
            for (StutotalityGoodStat stutotalityGoodStat : list) {
                stutotalityGoodStat.setRank(rank);
//                if(stutotalityGoodStat.getStandardSocre()>percentScale.getStandardScore()){
//                    passList.add(stutotalityGoodStat);
//                }
                rank++;
            }
            list.sort(new Comparator<StutotalityGoodStat>() {
                @Override
                public int compare(StutotalityGoodStat o1, StutotalityGoodStat o2) {
                    if(o1.getStandardSocre()-o2.getStandardSocre()>0) {
                        return -1;
                    }else if(o1.getStandardSocre()-o2.getStandardSocre()<0){
                        return 1;
                    }else {
                        return 0;
                    }
                }
            });
            for(String classId1 :classIds) {
                int passNum=0;
                for (StutotalityGoodStat stutotalityGoodStat : list) {
                    Student student = studentMap.get(stutotalityGoodStat.getStudentId());
                    if(student.getClassId().equals(classId1)) {
                        List<Student> students = new ArrayList<>();
                        if (StringUtils.isNotBlank(gradeId)) {
                            percentScale = allGoodScaleMap.get(classId1);
                            if (percentScale == null) {
                                percentScale = defaultScales.get(0);
                            }
                            if (studentListMap.containsKey(classId1)) {
                                students = studentListMap.get(classId1);
                            }
                        } else {
                            students.addAll(studentList);
                        }
                        Float allGoodNum = 0f;
                        Float passScore = 0f;
                        if (percentScale != null && percentScale.getScale() != null) {
                            allGoodNum = (Float) (students.size() * percentScale.getScale() / 100);
                            allGoodNum = convertFloat(allGoodNum, 0);
                            passScore = percentScale.getStandardScore();
                        }
                        stutotalityGoodStat.setHaveGood(0);
                        if (stutotalityGoodStat.getStandardSocre() > passScore && passNum < allGoodNum) {
                            stutotalityGoodStat.setHaveGood(1);
                            passNum++;
                        }
                    }
                }
            }
        }
        stutotalityGoodStatService.saveAll(semester.getAcadyear(),semester.getSemester().toString(),studentIds.toArray(new String[0]),list);

//        stutotalityGoodStatService.delByStudentIds(semester.getAcadyear(),semester.getSemester().toString(),studentIds.toArray(new String[0]));
//        stutotalityGoodStatService.saveAll(list.toArray(new StutotalityGoodStat[0]));
    }


    @ResponseBody
    @RequestMapping("/stat/saveScale")
    @ControllerInfo(value = "保存占比")
    public String saveScale(String classId,String gradeId,String percent) {
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        StutotalityScale scale = null;
        List<StutotalityItem> itemList = stutotalityItemService.findByUnitIdAndSubjectType(unitId, new String[]{All_GOOD_TYPE});
        StutotalityItem allGoodItem = null;
        if (CollectionUtils.isNotEmpty(itemList)) {
            allGoodItem = itemList.get(0);
        } else {
            return returnSuccess("-1","操作失败，请先维护达标科目");
//            return error("操作失败，请先维护达标科目");
        }
        String selectGradeId = "";
        if (StringUtils.isBlank(gradeId)) {
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
            selectGradeId = clazz.getGradeId();
        } else {
            selectGradeId = gradeId;
        }
        List<StutotalityItem> items = stutotalityItemService.findByParams(unitId, semester.getAcadyear(), semester.getSemester().toString(), selectGradeId, new String[]{allGoodItem.getSubjectId()});
        StutotalityItem finalAllGoodItem = null;
        if (CollectionUtils.isEmpty(items)) {
            return returnSuccess("-1","操作失败，请先维护该年级达标科目");

        } else {
            finalAllGoodItem = items.get(0);
        }

        if (StringUtils.isNotBlank(classId)) {
            if (StringUtils.isNotBlank(percent)) {
                List<StutotalityScale> stutotalityScales = stutotalityScaleService.findByUnitIdAndGradeIdAndtype(unitId, "00000000000000000000000000000000", ALL_GOOD_PERCENT);
                scale = new StutotalityScale();
                scale.setScale(Float.parseFloat(percent));
                scale.setClassId(classId);
                scale.setUnitId(unitId);
                scale.setId(UuidUtils.generateUuid());
                scale.setAcadyear(semester.getAcadyear());
                scale.setCreationTime(new Date());
                scale.setItemId(finalAllGoodItem.getId());
                scale.setStandardScore(stutotalityScales.get(0).getStandardScore());
                scale.setSemester(semester.getSemester().toString());
                scale.setType(ALL_GOOD_PERCENT);
            } else {
                return returnSuccess("-1","操作失败，请先全优生占比");
            }
        }
        if (scale != null) {
            try {
                stutotalityScaleService.deleteByUnitIdAndAcadyearAndSemesterAndClassId(unitId, semester.getAcadyear(), semester.getSemester().toString(), classId);
                stutotalityScaleService.save(scale);
            } catch (Exception e) {
                returnError();
                e.printStackTrace();
            }
        }
        return returnSuccess();
    }

    public Float getAvgScore(List<StutotalityStuResult> results){
        float totalScore = 0f;
        float avgSocre = 0;
        if(CollectionUtils.isNotEmpty(results)){
            for(StutotalityStuResult stutotalityStuResult:results){
                if(stutotalityStuResult.getResult()!=0||stutotalityStuResult.getResult()!=null) {
                    totalScore = totalScore + stutotalityStuResult.getResult();
                }
            }
            avgSocre = totalScore/results.size();
        }
        return avgSocre;
    }

    public Float convertFloat(Float f,int state){
        BigDecimal b =new BigDecimal(f);
        float score =b.setScale(state,BigDecimal.ROUND_HALF_UP).floatValue();
        return score;
    }
    /**
     * 判断是否为教务管理员
     */
    private boolean isAdmin(String unitId, String userId) {
        boolean res = customRoleRemoteService.checkUserRole(unitId, StutotalityConstant.STUTOTALITY_SUBSYSTEM, StutotalityConstant.STUTOTALITY_MANAGE_CODE, userId);
        return res;
//        return true;
    }
}
